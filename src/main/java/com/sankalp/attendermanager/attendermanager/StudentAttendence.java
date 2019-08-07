package com.sankalp.attendermanager.attendermanager;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StudentAttendence extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    Query query;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<studentDataReciver,StudentAttendence.StudView> firebaseRecyclerAdapter;



    private static String d1;
    SwipeRefreshLayout swipeContainer;
    private Thread thread;
     dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();
    private  String lecture = dataWire1.getLecture1();
    private  String CollName = dataWire1.getCollege();
    private String thisYear;
    private int thisMonth;
    private int thisDate;
    private int thisMonthVALUE;
    private int thisYearINT;
    int tempyear;
    int switchGateKeeper=0;
    int studArrayCount;
    int error123=1;
   // ArrayList<String>  student = new ArrayList<>();
    HashMap<String,String> studentHashMap = new HashMap();
    private int i=0;

    public HashMap<String,String> getStudentHashMap()
    {
     return studentHashMap;
    }
    OneTimeWorkRequest oneTimeWorkRequestA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendence);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar calendar = Calendar.getInstance();
        thisYearINT = calendar.get(Calendar.YEAR);
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<300;i++)
                {

                    Log.d("Threaddddddddddd",""+student.size());
                    student.add(" ");
                }
            }
        }).start();

*/  swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // Stop animation (This will be after 3 seconds)
                swipeContainer.setRefreshing(false);
                error123=0;
                firebaseRecyclerAdapter.notifyDataSetChanged();
                final DatabaseReference a = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem+"/"+sub+"/"+thisMonth+"/"+thisDate+"/l"+lecture+"/"+div);

                new smoothner(a).execute();
            }
        }, 4000);


       // tempyear =thisYearINT+1;
       // thisYear= String.valueOf(thisYearINT+"-"+tempyear);


        thisDate=calendar.get(Calendar.DATE);

        thisMonthVALUE=calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        thisMonth = thisMonthVALUE+1;

        Log.d("//////////////>>>>",thisMonthVALUE+" "+thisMonth);

        //databaseReference.keepSynced(true);
        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);
            dataWire.setAcadmicYear(thisYear);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);
            dataWire.setAcadmicYear(thisYear);

        }



        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/"+div);
        query=databaseReference.orderByKey();
        Log.d("College path",""+databaseReference);



        recyclerView=findViewById(R.id.recycler);
        recyclerView.hasFixedSize();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x;
        int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90,this.getResources().getDisplayMetrics());
        int columnsCount = screenWidth/photoWidth;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columnsCount));
        oneTimeWorkRequestA = new OneTimeWorkRequest.Builder(workManager.class).addTag("myWorker").build();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 error123=0;


              //  for (int i=0;i<=student.size()-1;i++)
               // {
                  //  Log.d("SIZE>>>>>>>>>>>",student.size()+"");
                  //  String studentRoll = student.get(i);
                //    if (!studentRoll.equals(" ")) {

                        DatabaseReference d = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + CollName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/subject/" + sem + "/" + sub + "/" + thisMonth + "/" + thisDate + "/l" + lecture +"/"+div);
                     //  d.child("status").setValue("1");
                        Iterator iterator = studentHashMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) iterator.next();
                            Log.d("hashmap>>><<<<>><<<>>",pair.getKey()+" value "+pair.getValue());
                            d.child(String.valueOf(pair.getKey())).child("status").setValue("1");
                            d.child(String.valueOf(pair.getKey())).child("pos").setValue(pair.getValue());


                            DatabaseReference studentDataHistory = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/"+div+"/"+ pair.getKey()+"/dataHistory");


                        }

                        //work manager -----------------------------------------------------------------------------------------------------------------


                WorkManager.getInstance().beginWith(oneTimeWorkRequestA).enqueue();




                swipeContainer.setRefreshing(true);
               /*
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);

                        startActivity(new Intent(StudentAttendence.this,QRGen.class));
                        finish();
                    }
                }, 4000);
                */


                Toast.makeText(StudentAttendence.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                //}





        });
        WorkManager.getInstance().getStatusById(oneTimeWorkRequestA.getId()).observe(this, new Observer<WorkStatus>() {
            @Override
            public void onChanged(@Nullable WorkStatus listLiveData) {
                if (listLiveData != null && listLiveData.getState().isFinished()) {
                    Log.e("works Status ", "finished");
                    swipeContainer.setRefreshing(false);
                }
            }
        });

        firebaseRecyclerOptions = new  FirebaseRecyclerOptions.Builder<studentDataReciver>().setQuery(query,studentDataReciver.class).build();
        Log.d("SSDDSDSDSD>>>>>>>>>>>>","main");
        firebaseRecyclerAdapter =  new FirebaseRecyclerAdapter<studentDataReciver,StudentAttendence.StudView>(firebaseRecyclerOptions)
        {


            @Override
            protected void onBindViewHolder(final StudentAttendence.StudView holder, final int position, @NonNull final studentDataReciver model1) {

                holder.setRollNo(model1.getRollNo());
                holder.setXStatus(model1.getXStatus(),position);

                Log.d("SSDDSDSDSD>>>>>>>>>>>>",""+model1.getRollNo());

               // Toast.makeText(getApplicationContext(), ""+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                ;
                final DatabaseReference a = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem+"/"+sub+"/"+thisMonth+"/"+thisDate+"/l"+lecture+"/"+div);
Log.d(" A path ",a+"");



           holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   Intent intent=new Intent(getApplicationContext(),studentLectureAttended.class);
                   intent.putExtra("roll",model1.getRollNo());
                   intent.putExtra("month",thisMonth+"");
                   intent.putExtra("date",thisDate+"");
                   startActivity(intent);
                   return false;
               }
           });

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference d = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem+"/"+sub+"/"+thisMonth+"/"+thisDate+"/l"+lecture+"/"+div+"/" + model1.getRollNo());
                        final DatabaseReference studentDataHistory = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/"+div+"/"+ model1.getRollNo()+"/dataHistory");

                        if (switchGateKeeper==1) {
                            if (model1.getXStatus().equals("1")) {

                                studentDataHistory.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("subDataProfile").hasChild("" + sub) && dataSnapshot.hasChild("monthData")) {


                                            String subjectMonthTotal = dataSnapshot.child("" + sub).child("" + thisMonth).child("total").getValue().toString();
                                            int studCoutMonth = Integer.parseInt(subjectMonthTotal) - 1;
                                            studentDataHistory.child("subDataProfile").child("" + sub).child("" + thisMonth).child("total").setValue("" + studCoutMonth);
                                            Toast.makeText(StudentAttendence.this, "" + lecture + " " + thisDate, Toast.LENGTH_SHORT).show();
                                            studentDataHistory.child("monthData").child("" + thisMonth).child("" + thisDate).removeValue();
                                            String a= String.valueOf(dataSnapshot.child("OverallCounter").getValue());
                                            int overall=Integer.parseInt(a)-1;
                                            studentDataHistory.child("OverallCounter").setValue(overall);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                        d.removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                                firebaseRecyclerAdapter.getItem(position).setXStatus("0");
                                                firebaseRecyclerAdapter.notifyDataSetChanged();




                                            }
                                        });




                            } else {
                                d.child("pos").setValue(position, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        d.child("status").setValue("1", new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                error123=1;
                                                new smoothner(studentDataHistory, position).execute();
                                                /*
                                                 */
                                            }

                                        });
                                    }
                                });



                            }
                        }else {

                            if (model1.getXStatus().equals("1")) {

                                firebaseRecyclerAdapter.getItem(position).setXStatus("0");
                                firebaseRecyclerAdapter.notifyItemChanged(position);
                              //  student.set(position, " ");
                                d.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        studentDataHistory.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.child("subDataProfile").hasChild("" + sub) && dataSnapshot.child("monthData").child(String.valueOf(thisMonth)).child(String.valueOf(thisDate)).hasChild("l"+lecture)) {


                                                    String subjectMonthTotal = dataSnapshot.child("subDataProfile").child("" + sub).child("" + thisMonth).child("total").getValue().toString();
                                                    int studCoutMonth = Integer.parseInt(subjectMonthTotal) - 1;
                                                    studentDataHistory.child("subDataProfile").child("" + sub).child("" + thisMonth).child("total").setValue("" + studCoutMonth);
                                                    Toast.makeText(StudentAttendence.this, "" + lecture + " " + thisDate, Toast.LENGTH_SHORT).show();
                                                    studentDataHistory.child("monthData").child("" + thisMonth).child("" + thisDate).child("l"+lecture).removeValue();
                                                    String a= String.valueOf(dataSnapshot.child("OverallCounter").getValue());
                                                    int overall=Integer.parseInt(a)-1;
                                                    studentDataHistory.child("OverallCounter").setValue(overall);
                                                }


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                                studentHashMap.remove(model1.rollNo);
                                dataWire.setStudentHashMap(studentHashMap);

                            }else {
                            //    Log.d("sdcccccccccccccccc",position+" "+student.size()+" "+model1.getRollNo()+" "+model1.getXStatus());
                                //holder.setXStatus(String.valueOf(1),position);
                                firebaseRecyclerAdapter.getItem(position).setXStatus("1");
                                firebaseRecyclerAdapter.notifyItemChanged(position);
                              //  studentHashMap.put(String.valueOf(model1.getRollNo()),new HashMap<String,String>().put("status","1"));
                                studentHashMap.put(String.valueOf(model1.getRollNo()), String.valueOf(position));
                             //   student.set(position, model1.getRollNo());
                                dataWire.setStudentHashMap(studentHashMap);
                            }

                        }

             }
                });


            }
            @Override
            public StudentAttendence.StudView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_view,parent,false);


                return new StudentAttendence.StudView(viewm);

            }

        };





        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView.setAdapter(firebaseRecyclerAdapter);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                error123=1;
                final DatabaseReference a = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem+"/"+sub+"/"+thisMonth+"/"+thisDate+"/l"+lecture+"/"+div);

                //new smoothner(a).execute();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                        error123=0;

                    }
                }, 4000);
                Log.d("IInside thread >>>","swipeeeeee in side>>>>>>>>");

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem itemSwitch=menu.findItem(R.id.mySwitch);
        MenuItem itemSwitch1 =menu.findItem(R.id.myCheckBox);
        itemSwitch.setActionView(R.layout.useswitch);
        itemSwitch1.setActionView(R.layout.user_checkbox_selectall);
        final CheckBox selectALL = menu.findItem(R.id.myCheckBox).getActionView().findViewById(R.id.checkBox);
        selectALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectALL.isChecked())
                {
                    int count = 0;
                    if (recyclerView.getAdapter() != null) {
                        count = recyclerView.getAdapter().getItemCount();
                        Log.e("number in ", count+"");
                        for (int i =0;i<count;i++)
                        {
                            firebaseRecyclerAdapter.getItem(i).setXStatus("1");
                            firebaseRecyclerAdapter.notifyItemChanged(i);
                            studentHashMap.put(String.valueOf(firebaseRecyclerAdapter.getItem(i).getRollNo()), String.valueOf(i));
                            dataWire.setStudentHashMap(studentHashMap);
                        }
                    }

                    Toast.makeText(StudentAttendence.this, "you have selected all student "+studentHashMap.size(), Toast.LENGTH_SHORT).show();
                }else
                {
                    int count = 0;
                    if (recyclerView.getAdapter() != null) {
                        count = recyclerView.getAdapter().getItemCount();
                        Log.e("number in ", count+"");
                        for (int i =0;i<count;i++)
                        {
                            firebaseRecyclerAdapter.getItem(i).setXStatus("0");
                            firebaseRecyclerAdapter.notifyItemChanged(i);
                            studentHashMap.remove(firebaseRecyclerAdapter.getItem(i).getRollNo());
                            dataWire.setStudentHashMap(studentHashMap);


                        }
                    }
                    Toast.makeText(StudentAttendence.this, "deselected all"+studentHashMap.size(), Toast.LENGTH_SHORT).show();


                }
            }
        });

        final Switch sw=menu.findItem(R.id.mySwitch).getActionView().findViewById(R.id.action_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AlertDialog.Builder alert=new AlertDialog.Builder(StudentAttendence.this,R.style.MyDialogTheme);
                    alert.setTitle("Alert");
                    alert.setMessage(" We strongly recommend to go with generic mode if network is slow." +
                            " Do You Want To Continue with Realtime Attendance ");
                    alert.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sw.setChecked(true);
                            switchGateKeeper=1;
                        }
                    });
                  alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          sw.setChecked(false);
                      }
                  });
                  alert.create().show();





                }else {
                    switchGateKeeper=0;
                    error123=0;
                }
            }
        });




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();



        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        finish();
        firebaseRecyclerAdapter.stopListening();
        finish();
        super.onStop();
    }


    public  static  class StudView extends  RecyclerView.ViewHolder
    {
        View mView;
        public StudView(View itemView)
        {
            super(itemView);
            mView = itemView;


        }

        public  void setRollNo(String rollno)
        {
            Log.d(">>>>>>>>>>>>>>>>>>>>",""+rollno);
            TextView textView = mView.findViewById(R.id.textView4);
            textView.setText(rollno);
            textView.setTextColor(Color.DKGRAY);
        }
        public  void  setXStatus(String  status,int pos){



                    TextView textView =  mView.findViewById(R.id.textView4);
            textView.setTextColor(Color.WHITE);
            if(status.equals("0"));
            {
                textView.setBackgroundColor(Color.RED);
            }
            if(status.equals("1"))
            {


                textView.setBackgroundColor(Color.GREEN);
            }
        }



        }
//=====================================================backgroud tasks>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        public  class smoothner extends AsyncTask<String,String,String>
        {
            int pos;
            int gateKeeper;
            int a= 0;
            long count =0;
            HashMap<Integer, Integer> studMapBackgroud=new HashMap<>();
            DatabaseReference  studentDataHistory;
             ChildEventListener childEventListener;
            String subjectMonthTotal;
            public smoothner(int position) {
                pos =position;
            }

            public smoothner(DatabaseReference studentDataHistory) {
                gateKeeper=0;
                this.studentDataHistory=studentDataHistory;
            }

            public smoothner(DatabaseReference studentDataHistory, int position) {
                this.studentDataHistory=studentDataHistory;
                pos=position;
                gateKeeper=1;
            }

            @Override
            protected void onPostExecute(String s) {
                firebaseRecyclerAdapter.notifyItemChanged(pos);

                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... strings) {
                childEventListener =  new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        a=a+1;

                        if (dataSnapshot.child("pos").exists()&&dataSnapshot.child("status").exists())
                        {

                            studMapBackgroud.put(Integer.parseInt(dataSnapshot.child("pos").getValue().toString()),Integer.parseInt(dataSnapshot.child("status").getValue().toString()));
                        }
                        Log.e("first >>",count+" === "+a);
                        if (count==a)
                        {


                            a=0;
                            Iterator iterator = studentHashMap.entrySet().iterator();
                            while (iterator.hasNext()) {
                                a=a+1;
                                Map.Entry pair = (Map.Entry) iterator.next();
                                Log.d("log in back async><<<>>",pair.getKey()+" value "+pair.getValue());
                                if (Integer.parseInt(pair.getValue().toString()) == 1)
                                {
                                    firebaseRecyclerAdapter.getItem((Integer) pair.getKey()).setXStatus(String.valueOf(1));
                                    firebaseRecyclerAdapter.notifyItemChanged((Integer) pair.getKey());

                                }else
                                {
                                    firebaseRecyclerAdapter.getItem((Integer) pair.getKey()).setXStatus(String.valueOf(0));
                                    firebaseRecyclerAdapter.notifyItemChanged((Integer) pair.getKey());
                                }
                                if (studMapBackgroud.size()==a)
                                {
                                    studentDataHistory.removeEventListener(childEventListener);
                                };



                            }


                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
/*
                studentDataHistory.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        count = dataSnapshot.getChildrenCount();
                      //  studentDataHistory.addChildEventListener(childEventListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
*/

                return "success";
            }
        }






}
