package com.sankalp.attendermanager.attendermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.functions.FirebaseFunctions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class collegeVerification extends AppCompatActivity {
    private FirebaseFunctions mFunction= FirebaseFunctions.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    dataWire dataWire = new dataWire();

    DatabaseReference otpcode;
    DatabaseReference otpdir;
    FloatingActionButton fab;
    EditText otp ;
    EditText domainEmail;
    String s12;
    String email;
    EditText collegeName;

    TextInputLayout inputLayout1 ;
    TextInputLayout inputLayout2;

    String gateKeeper="b";

    private DatabaseReference collegeNameFromDatabase;
    private DatabaseReference CollegeNameReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CardView cardView =  findViewById(R.id.CardView);
        otp =  findViewById(R.id.editText6);
        domainEmail = findViewById(R.id.editText7);
        collegeName = findViewById(R.id.editText5);

        inputLayout1 = findViewById(R.id.editD1);
        inputLayout2 = findViewById(R.id.editD2);
        final DatabaseReference college = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid());




        otp.setVisibility(View.INVISIBLE);
        otp.setFilters(new InputFilter[] {new InputFilter.LengthFilter(4)});

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent mint =  getIntent();
        Toast.makeText(this, ""+mint.getStringExtra("From_activity"), Toast.LENGTH_SHORT).show();


        collegeNameFromDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges");
        CollegeNameReg=FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/CollegeNameReg");


        collegeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s2) {


                collegeNameFromDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("pagalError",""+dataSnapshot.hasChild(s2.toString()));


                        boolean value = dataSnapshot.hasChild(s2.toString());
                        if (value)
                        {

                            inputLayout2.setHint("Enter code");
                          gateKeeper="a";


                        }else
                        {

                            inputLayout2.setHint("Enter College email");
                            gateKeeper="b";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






            }
        });




        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s1) {
                if (s12.equals(s1.toString())) {

                    otpdir = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/wichtigData/Otp/"+email+"/otpCode");
                    otpdir.removeValue();
                    dataWire.setCollege(collegeName.getText().toString().toLowerCase());


                    String google = "https://us-central1-attender-491df.cloudfunctions.net/CollegeVerify";
                    OkHttpClient client1 = new OkHttpClient();
                    HttpUrl.Builder urlBuilder = HttpUrl.parse(google).newBuilder();
                    urlBuilder.addQueryParameter("email", String.valueOf(domainEmail.getText()));
                    urlBuilder.addQueryParameter("collegeName", String.valueOf(collegeName.getText().toString().toLowerCase()));

                    String e = urlBuilder.build().toString();
                    Log.d("url222>>>>>>>>>", e);
                    Request request1 = new Request.Builder()
                            .url(e)
                            .build();
                    Call call1 = client1.newCall(request1);
                    call1.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call1, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call1, Response response) throws IOException {

                            if (response.isSuccessful()) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        DatabaseReference officials = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collegeName.getText().toString().toLowerCase()+"/officials/email");
                                        college.child("college").setValue(collegeName.getText().toString().toLowerCase());
                                        college.child("designation").setValue("official");
                                        officials.setValue(domainEmail.getText().toString());
                                        Intent mIntent = new Intent(getApplicationContext(), departmentSelection.class);
                                        mIntent.putExtra("From_activity","a");
                                        startActivity(mIntent);

                                    }
                                });

                            } else {

                            }

                        }
                    });


                }else {
                    if (otp.length()==4)
                    {
                        otp.setError("invalid otp");
                    }
                }
            }
        });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    if (gateKeeper.equals("b")) {



                        if (domainEmail.getText().toString().isEmpty()) {
                            domainEmail.setError("Enter an Email");
                        } else

                        {

                            email = String.valueOf(domainEmail.getText());
                            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(email);
                            if (matcher.matches()) {


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


                                String google = "https://us-central1-attender-491df.cloudfunctions.net/OtpGEN";
                                OkHttpClient client1 = new OkHttpClient();
                                HttpUrl.Builder urlBuilder = HttpUrl.parse(google).newBuilder();
                                urlBuilder.addQueryParameter("emailUid", String.valueOf(domainEmail.getText()));
                                String e = urlBuilder.build().toString();
                                Log.d("url>>>>>>>>>", e);
                                Request request1 = new Request.Builder()
                                        .url(e)
                                        .build();


                                Call call1 = client1.newCall(request1);
                                call1.enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call1, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call1, Response response) throws IOException {

                                        if (response.isSuccessful()) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    cardView.setVisibility(View.VISIBLE);
                                                    otp.setVisibility(View.VISIBLE);


                                                }
                                            });

                                        } else {

                                        }

                                    }
                                });
                            } else {
                                domainEmail.setError("Enter valid Email");
                            }
                        }
                    }else
                    {
                        CollegeNameReg.child(collegeName.getText().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               String  codeV = dataSnapshot.child("ValueCollege").getValue().toString();

                                if (codeV.equals(domainEmail.getText().toString()))
                               {
                                   Intent mIntent = new Intent(getApplicationContext(), departmentSelection.class);
                                   mIntent.putExtra("From_activity","a");
                                   startActivity(mIntent);
                                   dataWire.setCollege(collegeName.getText().toString().toLowerCase());
                                   dataWire.setCollegeCode(domainEmail.getText().toString());

                               }
                               else
                                {
                                    domainEmail.setError("Invalid Code");
                                }


                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    }

            });

    }

}
