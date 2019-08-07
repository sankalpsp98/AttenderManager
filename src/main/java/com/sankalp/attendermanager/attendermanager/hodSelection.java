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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class hodSelection extends AppCompatActivity {


        private final ArrayList<String> arrayList= new ArrayList<>();
        private  ArrayAdapter<String> arrayAdapter;

        dataWire dataWire =new dataWire();
        FirebaseUser firebaseUser ;


        DatabaseReference hodDept=FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+dataWire.getCollege()+"/department");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_hod_selection);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            Log.d("sasasa", dataWire.getCollege() + "");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            getWindow().setLayout((int) (width * .8), (int) (height * .6));

            ListView listView = findViewById(R.id.ListView3);


            arrayAdapter = new ArrayAdapter<String>(this, R.layout.textcview_style_colorlayout, R.id.textView2, arrayList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(hodSelection.this);
                    alert.setTitle("Alert");
                    alert.setMessage("Do You Want To Continue As HOD of "+parent.getItemAtPosition(position)+" Department");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(hodSelection.this, "yes", Toast.LENGTH_SHORT).show();
                            String google = "https://us-central1-attender-491df.cloudfunctions.net/HodOtp";
                            OkHttpClient client1 = new OkHttpClient();

                            HttpUrl.Builder urlBuilder = HttpUrl.parse(google).newBuilder();
                            urlBuilder.addQueryParameter("HodEmail", String.valueOf(firebaseUser.getEmail()));
                            urlBuilder.addQueryParameter("college", String.valueOf(dataWire.getCollege()));

                            urlBuilder.addQueryParameter("Department",String.valueOf(parent.getItemAtPosition(position)));
                            urlBuilder.addQueryParameter("Name",String.valueOf(firebaseUser.getDisplayName()));


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
                                                    Intent mIntent = new Intent(getApplicationContext(), HodOtp.class);
                                                mIntent.putExtra("hod",""+parent.getItemAtPosition(position));
                                                startActivity(mIntent);


                                            }
                                        });

                                    } else {

                                    }

                                }
                            });






                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(hodSelection.this, "no", Toast.LENGTH_SHORT).show();


                        }
                    });
                    alert.create().show();

                }
            });

            if (hodDept!=null) {

                hodDept.addChildEventListener(new ChildEventListener() {
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


