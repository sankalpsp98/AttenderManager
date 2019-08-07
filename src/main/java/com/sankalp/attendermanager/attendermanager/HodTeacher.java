package com.sankalp.attendermanager.attendermanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HodTeacher extends AppCompatActivity {

    Button teacher ;
    Button hod;

    dataWire dataWire =  new dataWire();
    EditText CollegeCode;


    DatabaseReference collegeReg=FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/CollegeNameReg");
    private  static String CollegeName;

    String gatekeeper_teacher_hod="";
    private boolean lockDown=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_teacher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        CollegeCode  =findViewById(R.id.editText9);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lockDown=false;
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d(">>>>>>",CollegeCode.getText().toString());

                if (CollegeCode.getText().toString().equals(""))
                {
                    CollegeCode.setError("Enter code");

                }else {

                    if (gatekeeper_teacher_hod.equals("a")) {

                        final DatabaseReference college1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");



                        if (collegeReg!=null) {
                            collegeReg.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String s1 = (String) dataSnapshot.child("ValueCollege").getValue();
                                    Log.d("lock>>>>>>>>>>>>>>>", lockDown + "");
                                    if (s1.equals(CollegeCode.getText().toString())) {
                                        Toast.makeText(HodTeacher.this, "<<<<<<<<<<" + s1, Toast.LENGTH_SHORT).show();
                                        Log.d("dadakjh", "code=" + s1 + "college entered  " + CollegeCode.getText() + " college name" + dataSnapshot.getKey());
                                        CollegeName = dataSnapshot.getKey();
                                        Log.e("dadakjh", "code=" + s1 + "college entered  " + CollegeCode.getText() + " college name" + dataSnapshot.getKey() + " var" + CollegeName);

                                        dataWire.setCollege(CollegeName);
                                        Log.d("dadakjh", "code=" + s1 + "college entered  " + CollegeCode.getText() + " college name" + dataSnapshot.getKey() + " var" + CollegeName + " set" + dataWire.getCollege());
                                        DatabaseReference college = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");
                                        college.child(FirebaseAuth.getInstance().getUid()).child("college").setValue(CollegeName);
                                        college1.child(FirebaseAuth.getInstance().getUid()).child("colleges").child(CollegeName).child("designation").setValue("teacher");

                                        CollegeCode.setError(null);
                                        lockDown = true;


                                    } else {
                                        Log.d("lock<<<<<<<<>>>>>>>>>>", lockDown + "");
                                        if (!lockDown) {
                                            Log.d("<<<<god>>>>>>>", lockDown + "");
                                            CollegeCode.setError("Enter Valid code");
                                        }
                                    }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String s1 = (String) dataSnapshot.child("ValueCollege").getValue();

                                    if (s1.equals(CollegeCode.getText().toString())) {
                                        Toast.makeText(HodTeacher.this, ">>>>>>>>" + s1, Toast.LENGTH_SHORT).show();
                                        CollegeName = dataSnapshot.getKey().toString();
                                        dataWire.setCollege(CollegeName);
                                    }
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
                            });


                            final Intent mIntent = new Intent(getApplicationContext(), departmentSelection.class);
                            mIntent.putExtra("From_activity", "b");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(mIntent);
                                }
                            },2000);


                        }
                    } else if (gatekeeper_teacher_hod.equals("b")){

                        if (collegeReg!=null) {
                            collegeReg.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String s1 = (String) dataSnapshot.child("ValueCollege").getValue();
                                    Log.d("lock>>>>>>>>>>>>>>>",lockDown+"");
                                        if (s1.equals(CollegeCode.getText().toString())) {
                                            Toast.makeText(HodTeacher.this, "<<<<<<<<<<" + s1, Toast.LENGTH_SHORT).show();
                                            Log.d("dadakjh", "code=" + s1 + "college entered  " + CollegeCode.getText() + " college name" + dataSnapshot.getKey());
                                            CollegeName = dataSnapshot.getKey();
                                            Log.d("dadakjh", "code=" + s1 + "college entered  " + CollegeCode.getText() + " college name" + dataSnapshot.getKey() + " var" + CollegeName);

                                            dataWire.setCollege(CollegeName);
                                            Log.d("dadakjh", "code=" + s1 + "college entered  " + CollegeCode.getText() + " college name" + dataSnapshot.getKey() + " var" + CollegeName + " set" + dataWire.getCollege());
                                            DatabaseReference college = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");
                                            college.child(FirebaseAuth.getInstance().getUid()).child("college").setValue(CollegeName);


                                            CollegeCode.setError(null);
                                            lockDown=true;
                                            dataWire.setCollegeCode(CollegeCode.getText().toString());
                                            startActivity(new Intent(getApplicationContext(), hodSelection.class));

                                        }else {
                                            Log.d("lock<<<<<<<<>>>>>>>>>>",lockDown+"");
                                            if (!lockDown) {
                                                Log.d("<<<<god>>>>>>>",lockDown+"");
                                                CollegeCode.setError("Enter Valid code");
                                            }
                                        }

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String s1 = (String) dataSnapshot.child("ValueCollege").getValue();

                                    if (s1.equals(CollegeCode.getText().toString())) {
                                        Toast.makeText(HodTeacher.this, ">>>>>>>>" + s1, Toast.LENGTH_SHORT).show();
                                        CollegeName = dataSnapshot.getKey().toString();
                                        dataWire.setCollege(CollegeName);
                                    }
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
                            });




                        }
                    }
                    else
                    {
                        Snackbar.make(view, "Are you teacher or HOD?", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();


                    }
                }
            }
        });

        teacher = findViewById(R.id.teacher);
        hod = findViewById(R.id.hod);



        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gatekeeper_teacher_hod="a";
            }

        });
        hod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gatekeeper_teacher_hod="b";

            }
        });



    }

}
