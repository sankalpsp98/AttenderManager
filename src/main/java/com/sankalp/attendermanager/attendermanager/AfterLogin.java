package com.sankalp.attendermanager.attendermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AfterLogin extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    static String facCode1;
    static String colCode1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final EditText editText = findViewById(R.id.editText);
        final EditText editText1 = findViewById(R.id.editText2);
        final EditText editText2 = findViewById(R.id.editText3);
        final EditText editText3 = findViewById(R.id.editText4);
        int maxLength = 1;

        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        editText1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        editText2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        editText3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        //editText.setNextFocusDownId(editText1.getId());
        // editText1.setNextFocusDownId(editText2.getId());
        // editText2.setNextFocusDownId(editText3.getId());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c1 = String.valueOf(editText.getText());
                String c2 = String.valueOf(editText1.getText());
                String c3 = String.valueOf(editText2.getText());
                String c4 = String.valueOf(editText3.getText());
                colCode1 =c1+c2;
                Log.d("s>>>>>>","sdasa");

                facCode1 = c3+c4;
                Snackbar.make(view, ">"+colCode1+">"+editText.getText()+">"+editText1.getText(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                LinkCollege(colCode1,facCode1);

                startActivity(new Intent(AfterLogin.this,xlsUpload.class));

            }
        });
    }


    private void LinkCollege(final String colCode, final String facCode) {

        DatabaseReference college= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges");
        final DatabaseReference tech = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");


        if (college!=null) {
            college.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String c = String.valueOf(dataSnapshot.child("code").getValue());


                    if (c.equals(colCode))
                    {
                        String college = dataSnapshot.getKey().toString();
                        tech.child(""+user.getUid()).child("college").setValue(college);

                        setFaculty(college,facCode);

                        Toast.makeText(AfterLogin.this, "" +c +colCode +facCode, Toast.LENGTH_LONG).show();
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
            });
        }


    }

    private void setFaculty(final String college, final String facCode) {

        DatabaseReference fact= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges"+"/"+college);
        final DatabaseReference tech = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");


        fact.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String c = String.valueOf(dataSnapshot.child("code").getValue());

                String x =  dataSnapshot.getKey().toString();
                Toast.makeText(AfterLogin.this, x+" " +c +facCode+college, Toast.LENGTH_LONG).show();
                if (c.equals(facCode))
                {
                    String f = dataSnapshot.getKey().toString();
                    tech.child(""+user.getUid()).child("faculty").setValue(f);

                    Toast.makeText(AfterLogin.this, "" +c +facCode, Toast.LENGTH_LONG).show();

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
        });
    }

}
