package com.sankalp.attendermanager.attendermanager;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicMarkableReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class FileUploadDownload extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    RecyclerView recyclerView;
    DatabaseReference  databaseReference;
    Query query ;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<dataFileUpDown,FileUploadDownload.fileView> firebaseRecyclerAdapter;
    dataWire dataWire1 = new dataWire();
    private  String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();

    private  String collName = dataWire1.getCollege();



    private int thisMonth;
    private int thisDate;
    private int thisMonthVALUE;
    private String thisYear;
    private long Start;
    private float start;
    private float end;
    int gateKeeper=0;
    private Date CurrentTime;
    private float currentTIME;

    private String WeekDay;

    TextView Day;
    private Date dayToday;
    private int thisYearINT;
    private int tempyear;
    private int thisYear1;


    private StorageReference mStorageRef;
    private boolean firstTime=true;

    String[] descriptionData = {"File", "in\nprocess","in\ncloud"};
    StateProgressBar stateProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AtomicMarkableReference storage;

        mStorageRef = FirebaseStorage.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        Day=findViewById(R.id.textView13);

        thisYearINT = calendar.get(Calendar.YEAR);
        //thisYearINT =2019;
        CurrentTime=calendar.getTime();
        thisMonthVALUE=calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date1 = new SimpleDateFormat("EEEE");
        SimpleDateFormat month_date11 = new SimpleDateFormat("M");
        SimpleDateFormat CT = new SimpleDateFormat("HH:mm");
        currentTIME = Float.parseFloat(CT.format(CurrentTime).replace(":","."));



   stateProgressBar  = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);

        stateProgressBar.enableAnimationToCurrentState(true);


        thisMonth = thisMonthVALUE+1;
        thisDate=calendar.get(Calendar.DATE);


        Log.d("Date","Today's"+thisDate+" month_date1.format(thisDate) "+month_date1.format(thisDate));



        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }







        recyclerView = findViewById(R.id.timeTable);


        recyclerView.hasFixedSize();
        recyclerView.setFocusable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,true));
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+ "/"+acdyear+"/fileData/file");
        query = databaseReference.orderByKey();

        Log.d("filee<><><><>M<><",databaseReference+"");
        firebaseRecyclerOptions = new  FirebaseRecyclerOptions.Builder<dataFileUpDown>().setQuery(query,dataFileUpDown.class).build();


        firebaseRecyclerAdapter =  new FirebaseRecyclerAdapter<dataFileUpDown, FileUploadDownload.fileView>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public fileView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_fille_ui,parent,false);


                return new FileUploadDownload.fileView(viewm);     }

            @Override
            protected void onBindViewHolder(@NonNull final fileView holder, final int position, @NonNull final dataFileUpDown model) {
             holder.setFileName(model.getFileName());

             holder.setFileMB(model.getFileMB());
             holder.setUploaderName(model.getUploaderName());

                holder.mView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mStorageRef.child("/College/" + collName + "/" + faculty + "/"+acdyear+"/"+model.getFileName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadFiles(FileUploadDownload.this,firebaseRecyclerAdapter.getItem(position).getFileName(),".pdf",DIRECTORY_DOWNLOADS, uri);



                            }
                        });



                    }
                });
                Log.d("filee<><><><>H<><",""+model.getFileName());

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        if (recyclerView.getChildCount() > 0 && FileUploadDownload.this.firstTime){
                            FileUploadDownload.this.firstTime = false;

                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                            firebaseRecyclerAdapter.notifyDataSetChanged();
                            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }



                    }
                });
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (checkPermission()) {

                    new MaterialFilePicker()
                            .withActivity(FileUploadDownload.this)
                            .withRequestCode(1)
                            //.withFilter(Pattern.compile(".*\\.xls$")) // Filtering files and directories by file name using regexp
                            .withFilterDirectories(false) // Set directories filterable (false by default)
                            .withHiddenFiles(true) // Show hidden files and folders
                            .start();
                }

            }
        });

    }

    public void download() {




    }



    public void downloadFiles(Context context,String fileName,String fileExtension,String destinationDirectory,Uri url){
        File file=new File("/storage/emulated/0/Attender",fileName);

        DownloadManager downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=url;
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setShowRunningNotification(true);
       request.setDestinationUri(Uri.fromFile(file));
       request.setVisibleInDownloadsUi(true);
        request.setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
        .setTitle(fileName)// Title of the Download Notification
                .setDescription("Downloading");// Description of the Download Notification
       // request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);
       long a= downloadManager.enqueue(request);

        Log.d("yesme kya hai bruh??",""+a+"  " +file);
        checkPermission();

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {
            return  true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();



        firebaseRecyclerAdapter.startListening();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            //Toast.makeText(this, "" + filePath, Toast.LENGTH_LONG).show();
            DatabaseReference teacher = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid());

            if (teacher != null) {
                /*
                teacher.child("college").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        college = dataSnapshot.getValue().toString();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                teacher.child("faculty").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        faculty = dataSnapshot.getValue().toString();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
*/
                try {
                    //AssetManager ar=getAssets();
                    final File file = new File(filePath);
                    final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    final StorageReference storageRef = firebaseStorage.getReference();
                    final StorageReference uploadRef = storageRef.child("/College/" + collName + "/" + faculty + "/"+acdyear+"/"+file.getName());

                    uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //FILE FOUND
                            Toast.makeText(FileUploadDownload.this, "File with same name already exist consider renaming file", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

                            //FILE NOT FOUND

                            uploadRef.putFile(Uri.fromFile(file)).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e(">>>>>", "Failed to upload file to cloud storage");

                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    mStorageRef.child("/College/" + collName + "/" + faculty + "/"+acdyear+"/"+file.getName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            DatabaseReference ReferenceForStorage = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/fileData/file");
                                            DatabaseReference uriD= ReferenceForStorage.push();
                                            uriD.child("fileName").setValue(file.getName());
                                            uriD.child("fileURI").setValue(uri+"");
                                            uriD.child("uploaderName").setValue(user.getDisplayName());
                                            if ((file.length()/1048576)==0) {
                                                uriD.child("fileMB").setValue((file.length()/1024)+"KB");
                                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                                                firebaseRecyclerAdapter.notifyDataSetChanged();
                                                firstTime=true;
                                                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);

                                                recyclerView.getViewTreeObserver()
                                                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                            @Override
                                                            public void onGlobalLayout() {
                                                                //At this point the layout is complete and the
                                                                //dimensions of recyclerView and any child views are known.
                                                                //Remove listener after changed RecyclerView's height to prevent infinite loop
                                                                if (recyclerView.getChildCount() > 0 && FileUploadDownload.this.firstTime){
                                                                    FileUploadDownload.this.firstTime = false;

                                                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                                                                    firebaseRecyclerAdapter.notifyDataSetChanged();
                                                                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                                }



                                                            }
                                                        });
                                            }
                                            else {
                                                uriD.child("fileMB").setValue((file.length()/1048576)+"MB");
                                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                                                firebaseRecyclerAdapter.notifyDataSetChanged();
                                                firstTime =true;
                                                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                                                recyclerView.getViewTreeObserver()
                                                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                            @Override
                                                            public void onGlobalLayout() {
                                                                //At this point the layout is complete and the
                                                                //dimensions of recyclerView and any child views are known.
                                                                //Remove listener after changed RecyclerView's height to prevent infinite loop
                                                                if (recyclerView.getChildCount() > 0 && FileUploadDownload.this.firstTime){
                                                                    FileUploadDownload.this.firstTime = false;

                                                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                                                                    firebaseRecyclerAdapter.notifyDataSetChanged();

                                                                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                                                }



                                                            }
                                                        });
                                            }

                                            Log.d("yesme kya hai bruh??",file.length()+"  .."+(file.length()/1048576+"  >>"+recyclerView.getChildCount()));
                                        }
                                    });


                                    Log.d("yesme kya hai bruh??",""+taskSnapshot.getUploadSessionUri());
                                    Toast.makeText(getApplicationContext(),
                                            "File has been uploaded to cloud storage"+ taskSnapshot.getUploadSessionUri(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });



                } catch (Exception e) {
                    Toast.makeText(FileUploadDownload.this, "" + e, Toast.LENGTH_SHORT).show();
                }
                // Do anything with file
            }
        }
    }






    public class fileView extends RecyclerView.ViewHolder{
        View mView1;
        TextView name;
        TextView mb;
        TextView fileUploader;

        public fileView(View itemView) {
            super(itemView);
            mView1=itemView;
        }
        public  void setFileName(String fileName)
        {
            name = mView1.findViewById(R.id.textView19);
            name.setText(fileName);
        }
        public void setFileMB(String fileMB)
        {
            mb= mView1.findViewById(R.id.textView20);
            mb.setText(fileMB);

        }
        public  void setUploaderName(String uploaderName)
        {
            fileUploader = mView1.findViewById(R.id.textView21);
            fileUploader.setText(uploaderName);
        }

    }

}
