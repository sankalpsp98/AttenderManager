package com.sankalp.attendermanager.attendermanager;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.sankalp.attendermanager.attendermanager.dataModel.AcadYear;
import com.sankalp.attendermanager.attendermanager.dataModel.Div;
import com.sankalp.attendermanager.attendermanager.dataModel.Faculty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class uploadDetails extends Activity {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String college;
    private String faculty;
    DatabaseReference data;
    ArrayList<String> fact = new ArrayList<>();
    ArrayAdapter<String> factAdapter;
    ArrayList<String> sem = new ArrayList<>();
    ArrayAdapter<CharSequence> semAdapter;
    private String collegeS;
    Spinner dept;
    Spinner semester;
    CheckBox fy;
    CheckBox sy;
    CheckBox ty;
    private String yearfst;
    Intent mint ;
    String ch ;

    DatabaseReference techerData;
    private int thisYearINT;
    private int tempyear;
    private String thisYear;
    private int checkme=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_details);

           mint =  getIntent();
        ch= String.valueOf(mint.getStringExtra("option"));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .4));
        dept = findViewById(R.id.spinner7);
        semester = findViewById(R.id.spinner9);

        // semAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sem);

        fy = findViewById(R.id.checkBox3);
        sy = findViewById(R.id.checkBox4);
        ty = findViewById(R.id.checkBox5);




        DatabaseReference college = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid());
        college.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                //collegeS = dataSnapshot.child("college").getValue().toString();
                collegeS = dataWire.getCollege();
                if (ch.equals("4"))
                {
                    techerData =FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collegeS+"/department");
                }else
                {
                    techerData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid()+"/faculty");
                }
                techerData.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        fact.add(dataSnapshot.getKey().toString());
                        factAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        fact.add(dataSnapshot.getKey().toString());
                        factAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        String factList1 = dataSnapshot.getKey().toString();
                        fact.remove(factList1);
                        factAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        semAdapter = ArrayAdapter.createFromResource(this, R.array.sem, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(semAdapter);
        factAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fact);

        factAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(factAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (checkPermission()) {
                    if (fy.isChecked() && !sy.isChecked() && !ty.isChecked()) {
                        yearfst = "fy";
                        callFile(ch);
                    } else if (sy.isChecked() && !fy.isChecked() && !ty.isChecked()) {
                        yearfst = "sy";
                        callFile(ch);
                    } else if (ty.isChecked() && !sy.isChecked() && !fy.isChecked()) {
                        yearfst = "ty";
                        callFile(ch);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(uploadDetails.this);
                        alert.setTitle("Alert");
                        alert.setMessage("Please select only 1 checkbox");

                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alert.create().show();
                    }
                }


            }
        });


    }

    public void callFile(String ch) {


            new MaterialFilePicker()
                    .withActivity(uploadDetails.this)
                    .withRequestCode(Integer.parseInt(ch))
                    .withFilter(Pattern.compile(".*\\.xls$"))// Filtering files and directories by file name using regexp
                    .withFilterDirectories(true) // Set directories filterable (false by default)
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start();


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Calendar calendar = Calendar.getInstance();
        int thisYear1 = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        thisYearINT = calendar.get(Calendar.YEAR);
        month = month + 1;

        if(month<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (month>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(this, "" + filePath, Toast.LENGTH_LONG).show();


            //tempyear =thisYearINT+1;
           // thisYear= String.valueOf(thisYearINT+"-"+tempyear);



            DatabaseReference sub = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collegeS + "/department/" + dept.getSelectedItem() + "/" + thisYear + "/" + yearfst + "/subject/" + semester.getSelectedItem());

            try {
                File file = new File(filePath);
                Workbook wb = Workbook.getWorkbook(file);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

                int indexc = 0;
                int subjectc = 0;

                for (int i = 0; i < col; i++) {
                    switch (String.valueOf(s.getCell(i, 0).getContents())) {
                        case "index":
                            indexc = i;
                            break;
                        case "subject":
                            subjectc = i;
                            break;
                        default:
                            break;
                    }
                }
                sub.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    }
                });
                for (int i = 1; i < row; i++) {


                    String index = s.getCell(indexc, i).getContents().toString();
                    String subject = s.getCell(subjectc, i).getContents().toString();
                    sub.child(subject).child("total").setValue("0");

                }
            } catch (Exception e) {
                Toast.makeText(uploadDetails.this, "" + e, Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(this, "" + filePath, Toast.LENGTH_LONG).show();

            DatabaseReference timestamppy = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collegeS + "/department/" + dept.getSelectedItem() + "/" + thisYear+ "/" + yearfst );

            try {
                File file = new File(filePath);
                Workbook wb = Workbook.getWorkbook(file);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

                int timestampc = 0;


                for (int i = 0; i < col; i++) {
                    switch (String.valueOf(s.getCell(i, 0).getContents())) {
                        case "timestamp":
                            timestampc = i;
                            break;
                        default:
                            break;

                    }

                }
                for (int i = 1; i < row; i++) {

                    String time1 = s.getCell(timestampc, i).getContents().toString();
                    timestamppy.child("TimeTable").child("Ttstamp").child(i+"").child("ttStamp").setValue(time1);



                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }finally {

            }
        }else if (requestCode == 3 ||requestCode==4&& resultCode == RESULT_OK)
        {

            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(this,""+filePath,Toast.LENGTH_LONG).show();
            DatabaseReference stud = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collegeS + "/department/" + dept.getSelectedItem() + "/" + thisYear +"/" + yearfst +"/stud");

            try {
                //AssetManager ar=getAssets();
                File file = new File(filePath);
                Workbook wb = Workbook.getWorkbook(file);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

                int uidc = 0;
                int namec = 0;
                int Yearc = 0;
                int phonec = 0;
                int divisionc = 0;
                int GuardianNumc = 0;
                int Categoryc = 0;
                int Classc = 0;
                int Sexc = 0;
                int Emailc=0;
                for (int i = 0; i < col; i++) {
                    switch (String.valueOf(s.getCell(i, 0).getContents())) {
                        case "Roll No":
                            uidc = i;
                            break;
                        case "Combined name":
                            namec = i;
                            break;
                        case "Year":
                            Yearc = i;
                            break;
                        case "Mobile No":
                            phonec = i;
                            break;
                        case "Div":
                            divisionc = i;
                            break;
                        case "Guardian Mobile No":
                            GuardianNumc = i;
                            break;
                        case "Category":
                            Categoryc = i;
                            break;
                        case "Class":


                            Classc = i;
                            break;
                        case "Sex":
                            Sexc = i;
                            break;
                        case "E-Mail ID":
                            Emailc=i;
                            break;

                        default:
                            break;
                    }
                }


                for (int i = 1; i < row; i++) {

                    String division = s.getCell(divisionc, i).getContents().toString();
                    if (!division.equals("")) {




                        String uid = s.getCell(uidc, i).getContents().toString();
                        String name = s.getCell(namec, i).getContents().toString();
                        String Year = s.getCell(Yearc, i).getContents().toString();
                        String phone = s.getCell(phonec, i).getContents().toString();
                        //division=s.getCell(divisionc,i).getContents().toString();
                        String Guardian = s.getCell(GuardianNumc, i).getContents().toString();
                        String Category = s.getCell(Categoryc, i).getContents().toString();
                        String Classs = s.getCell(Classc, i).getContents().toString();
                        String Sex = s.getCell(Sexc, i).getContents().toString();
                        Log.e("email",i+"");
                        String email=s.getCell(Emailc,i).getContents().toString();


                        Classs = Classs.replace(".", "").toLowerCase();

                        Log.d("classsssssss>>>>>>>>>>", Classs);


                        Toast.makeText(uploadDetails.this, "hey5" + uidc + " " + namec + " " + Yearc + " " + phonec + " " + divisionc + " " + GuardianNumc + " " + Categoryc + " " + Classc + " " + Sexc + " ", Toast.LENGTH_SHORT).show();


                        Log.d("Div>>>>>>>","hey5" + uidc + " " + namec + " " + Yearc + " " + phonec + " " + divisionc + " " + GuardianNumc + " " + Categoryc + " " + Classc + " " + Sexc + " "+Emailc);





                        Toast.makeText(uploadDetails.this, "hey5" + uid + name + Year + phone + division + Guardian + Category + Classs + Sex+email, Toast.LENGTH_SHORT).show();
                        stud.child(division).child(uid).child("name").setValue(name);
                        stud.child(division).child(uid).child("Year").setValue(Year);
                        stud.child(division).child(uid).child("phone").setValue(phone);
                        stud.child(division).child(uid).child("division").setValue(division);
                        stud.child(division).child(uid).child("Guardian").setValue(Guardian);
                        stud.child(division).child(uid).child("Category").setValue(Category);
                        stud.child(division).child(uid).child("Classs").setValue(Classs);
                        stud.child(division).child(uid).child("Sex").setValue(Sex);
                        stud.child(division).child(uid).child("rollNo").setValue(uid);
                        stud.child(division).child(uid).child("email").setValue(email);

                    }
                }

            }catch(Exception e)
            {
                Toast.makeText(uploadDetails.this, "" + e, Toast.LENGTH_SHORT).show();
            }
        }

    }
}