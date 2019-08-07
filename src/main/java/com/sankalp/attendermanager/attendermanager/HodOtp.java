package com.sankalp.attendermanager.attendermanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HodOtp extends AppCompatActivity {


    private EditText firstDigitOtpEdt, secondDigitOtpEdt, thirdDigitOtpEdt, fourthDigitOtpEdt;
    private String Concat;

    FirebaseUser firebaseUser;
    private String email;
    private DatabaseReference otpcode;
    private String s12;



    DatabaseReference otp ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_otp);



        firstDigitOtpEdt = findViewById(R.id.editText10);
        secondDigitOtpEdt=findViewById(R.id.editText11);
        thirdDigitOtpEdt=findViewById(R.id.editText12);

        fourthDigitOtpEdt=findViewById(R.id.editText13);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        email = firebaseUser.getEmail();

        int a = email.indexOf("@");
        email = email.substring(0, a).replaceAll("[*.@]", "");
        otpcode = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/wichtigData/Otp/");

        otpcode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(email)) {

                    s12 = dataSnapshot.child("otpCode").getValue().toString();
                    Log.d("s12>>>>>>>>>>>>>>", "" + s12);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(email)) {
                    s12 = dataSnapshot.child("otpCode").getValue().toString();
                    Log.d("s12>>>>>>>>>>>>>>s12", "" + s12);
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


        firstDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (firstDigitOtpEdt.getText().toString().length() == 1) {
                    secondDigitOtpEdt.requestFocus();
                }
            }
        });

        secondDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (secondDigitOtpEdt.getText().toString().length() == 0) {
                    firstDigitOtpEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (secondDigitOtpEdt.getText().toString().length() == 1) {
                    thirdDigitOtpEdt.requestFocus();
                }
            }
        });

        thirdDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (thirdDigitOtpEdt.getText().toString().length() == 0) {
                    secondDigitOtpEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (thirdDigitOtpEdt.getText().toString().length() == 1) {
                    fourthDigitOtpEdt.requestFocus();
                }
            }
        });

        fourthDigitOtpEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (fourthDigitOtpEdt.getText().toString().length() == 0) {
                    thirdDigitOtpEdt.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // We can call api to verify the OTP here or on an explicit button click
                Concat=firstDigitOtpEdt.getText().toString()+secondDigitOtpEdt.getText().toString()+thirdDigitOtpEdt.getText().toString()+fourthDigitOtpEdt.getText().toString();
                Toast.makeText(HodOtp.this, ""+Concat, Toast.LENGTH_SHORT).show();

                if (Concat.equals(s12))
                {
                    DatabaseReference college = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");

                    DatabaseReference isVerify = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/"+firebaseUser.getUid()+"/verify");

                    isVerify.setValue("1");
                    SharedPreferences sharedPreferences12=getSharedPreferences("data2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences12.edit();
                    editor.putString("Email","1");
                    editor.apply();
                    editor.commit();
                    Intent mint = getIntent();
                    String ch = String.valueOf(mint.getStringExtra("hod"));
                    college.child(FirebaseAuth.getInstance().getUid()).child("colleges").child(dataWire.getCollege()).child("designation").setValue("hod");
                    college.child(FirebaseAuth.getInstance().getUid()).child("colleges").child(dataWire.getCollege()).child("hod").setValue(ch);
                    college.child(FirebaseAuth.getInstance().getUid()).child("colleges").child(dataWire.getCollege()).child("faculty").child(ch).child("selectedDept").setValue(ch);

                    Intent mIntent = new Intent(getApplicationContext(),  QRGen.class);
                    //mIntent.putExtra("From_activity","b");
                    startActivity(mIntent);
                }





            }
        });


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
