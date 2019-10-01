package com.sankalp.attendermanager.attendermanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SubjectSelection extends AppCompatActivity {


    dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
   private  String acdyear = dataWire1.getYear1();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();
    private  String lecture = dataWire1.getLecture1();
    private  String CollName = dataWire1.getCollege();
    private String thisYear;
    private int thisMonth;
    private int thisDate;
    



    private final ArrayList<String> arrayList= new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private int thisYearINT;
    private int tempyear;
    private int thisMonthVALUE;
    private ValueEventListener valueEventListener;
    private Intent mint;
    private String ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        Calendar calendar = Calendar.getInstance();

        thisYearINT = calendar.get(Calendar.YEAR);


        //tempyear =thisYearINT+1;
        //thisYear= String.valueOf(thisYearINT+"-"+tempyear);


        mint = getIntent();
        ch = String.valueOf(mint.getStringExtra("From_activity"));


        thisDate=calendar.get(Calendar.DATE);

        Log.d(">?????we are in ssssss",lecture+" "+thisMonth);

        SimpleDateFormat month_date = new SimpleDateFormat("M");
        thisMonthVALUE=calendar.get(Calendar.MONTH);
        thisMonth = thisMonthVALUE+1;
        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }

        final DatabaseReference subSel= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear.toLowerCase()+"/subject/"+sem);


        Log.d("sasasa", dataWire.getCollege() + "");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        ListView listView = findViewById(R.id.ListView3);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert=new AlertDialog.Builder(SubjectSelection.this);
                alert.setTitle("Alert");
                alert.setMessage("Do You Want To Continue with "+parent.getItemAtPosition(position)+" subject");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference timetableStore=FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear.toLowerCase()+"/TimeTable/"+thisMonth+"-"+thisDate+"/"+div+"/"+lecture);

                        dataWire1.setSubject(String.valueOf(parent.getItemAtPosition(position)));
                        timetableStore.child("subNameStamp").setValue(parent.getItemAtPosition(position));

//==================yaha pe===>>>>

              valueEventListener=   new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         int a;
                         if (dataSnapshot.hasChild(thisMonth + "")) {

                             if (dataSnapshot.child(String.valueOf(thisMonth)).child("div").hasChild(div)) {
                                 String subject = dataSnapshot.child(thisMonth + "").child("div").child(div).child("total").getValue().toString();
                                 Log.d("<<>?????666666666666666", thisMonth + " " + subject);
                                 a = Integer.parseInt(subject) + 1;
                                 subSel.child(parent.getItemAtPosition(position) + "").child(thisMonth + "").child("div").child(div).child("total").setValue("" + a);
                                 //Toast.makeText(SubjectSelection.this, ""+subject, Toast.LENGTH_SHORT).show();
                                 subSel.child(parent.getItemAtPosition(position)+"").removeEventListener(valueEventListener);
                             }else
                             {
                                 a=1;
                                 subSel.child(parent.getItemAtPosition(position) + "").child(thisMonth + "").child("div").child(div).child("total").setValue("" + a);
                                 //Toast.makeText(SubjectSelection.this, ""+subject, Toast.LENGTH_SHORT).show();
                                 subSel.child(parent.getItemAtPosition(position)+"").removeEventListener(valueEventListener);
                             }

                         } else {
                             a=1;
                             subSel.child(parent.getItemAtPosition(position) + "").child(thisMonth + "").child("div").child(div).child("total").setValue("" + a);
                             subSel.child(parent.getItemAtPosition(position) + "").child(thisMonth + "").child("total").setValue("1");
                             subSel.child(parent.getItemAtPosition(position)+"").removeEventListener(valueEventListener);
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 };
                        subSel.child(parent.getItemAtPosition(position)+"").addValueEventListener(valueEventListener);


                        if (ch.equals("qr"))
                        {
                            Intent intent = new Intent(getApplicationContext(), QR.class);
                            startActivity(intent);
                        }else if(ch.equals("simple")) {
                            Intent intent = new Intent(getApplicationContext(), StudentAttendence.class);
                            startActivity(intent);
                        }
                    }

                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SubjectSelection.this, "no", Toast.LENGTH_SHORT).show();


                    }
                });
                alert.create().show();
            }
        });

        if(subSel!=null)
        {
            subSel.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String string = dataSnapshot.getKey().toString();
                    arrayList.add(string);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String string = dataSnapshot.getChildren().toString();
                    arrayList.add(string);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    String string = dataSnapshot.getValue().toString();
                    arrayList.remove(string);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }

    }
}
