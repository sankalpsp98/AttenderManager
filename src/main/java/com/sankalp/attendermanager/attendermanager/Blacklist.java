package com.sankalp.attendermanager.attendermanager;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.andanhm.quantitypicker.QuantityPicker;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Blacklist extends AppCompatActivity {
    private int a=2;
    private int t=49;

    HashMap<String,String> subMap =new HashMap();

    ArrayList<Button> buttonsList = new ArrayList<Button>();
    int count=0;
    private String url;

    dataWire dataWire1 = new dataWire();
    private  String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub ;
    private  String div = dataWire1.getDiv1();

    private  String collName = dataWire1.getCollege();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



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
    private int monthPicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button  b1 =findViewById(R.id.b1);
        final Button b2 =findViewById(R.id.b2);
        final Button b3 =findViewById(R.id.b3);
        final Button b4 =findViewById(R.id.b4);
        final Button b5 =findViewById(R.id.b5);
        Button b6 =findViewById(R.id.b6);
        buttonsList.add(b1);
        buttonsList.add(b2);
        buttonsList.add(b3);
        buttonsList.add(b4);
        buttonsList.add(b5);
        buttonsList.add(b6);



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

        thisMonth = thisMonthVALUE+1;
        thisDate=calendar.get(Calendar.DATE);
        monthPicked =thisMonth;


        QuantityPicker quantityPicker = (QuantityPicker) findViewById(R.id.quantityPicker);

        //Returns the selected quantity
        quantityPicker.getQuantity();

        //Allows to set the minimum quantity
        quantityPicker.setMinQuantity(1);

        //Allows to set the maximum quantity
        quantityPicker.setMaxQuantity(thisMonth);

        //Enable/Disable quantity picker
        quantityPicker.setQuantityPicker(true);

        //Allows to set the text style quantity
        quantityPicker.setTextStyle(QuantityPicker.BOLD);

        //To set the quantity text color
        quantityPicker.setQuantityTextColor(R.color.colorPrimaryDark);

        //To set the quantity button color
        quantityPicker.setQuantityButtonColor(R.color.colorAccent);

        // Returns the quantity on quantity selection
        quantityPicker.setOnQuantityChangeListener(new QuantityPicker.OnQuantityChangeListener() {
            @Override
            public void onValueChanged(int quantity) {
                if (quantity!=0) {
                    monthPicked = quantity;
                    Toast.makeText(Blacklist.this, ""+monthPicked, Toast.LENGTH_SHORT).show();
                }else
                {
                    monthPicked = thisMonth;
                    Toast.makeText(Blacklist.this, ""+monthPicked, Toast.LENGTH_SHORT).show();
                }

            }
        });





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
        DatabaseReference subdb = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem);
        Log.d("Address print", String.valueOf(subdb));



        subdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(), ""+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                buttonsList.get(count).setText(dataSnapshot.getKey().toString());
                count++;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(), ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                buttonsList.get(count).setText(dataSnapshot.getKey().toString());
                count++;
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


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub=b1.getText().toString();
               // sub=sub.toLowerCase();



                percentFunction(faculty,acdyear,sem,sub,div,collName,sub,thisYear);



            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub=b2.getText().toString();
              //  sub=sub.toLowerCase();



                percentFunction(faculty,acdyear,sem,sub,div,collName,sub,thisYear);



            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub=b3.getText().toString();
                //sub=sub.toLowerCase();



                percentFunction(faculty,acdyear,sem,sub,div,collName,sub,thisYear);



            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub=b4.getText().toString();
               // sub=sub.toLowerCase();



                percentFunction(faculty,acdyear,sem,sub,div,collName,sub,thisYear);



            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub=b5.getText().toString();
                //
                //
                //
                // sub=sub.toLowerCase();



                percentFunction(faculty,acdyear,sem,sub,div,collName,sub,thisYear);



            }
        });

        final LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView.loop(true);

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                Toast.makeText(getApplicationContext(), "started",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_SHORT).show();
                lottieAnimationView.setMinAndMaxFrame(0,t);

                if (a==1) {
                    lottieAnimationView.setSpeed(-1);


                    a = 0;
                }
                else
                {
                    lottieAnimationView.setSpeed(1);
                    a=1;

                }
            }
        });

        lottieAnimationView.playAnimation();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void percentFunction(final String faculty, final String acdyear, final String sem, String sub, String div, final String collName, final String s, final String thisYear) {
        Calendar calendar = Calendar.getInstance();
        int thisYearINT = calendar.get(Calendar.YEAR);
        int thisMonthVALUE = calendar.get(Calendar.MONTH);
        final int thisMonth = thisMonthVALUE + 1;
         int tempyear;




        url ="https://us-central1-attender-491df.cloudfunctions.net/percentCal";
        OkHttpClient client1 = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("college", collName);
        urlBuilder.addQueryParameter("dept", faculty);
        urlBuilder.addQueryParameter("classStr",acdyear );
        urlBuilder.addQueryParameter("year", thisYear);
        urlBuilder.addQueryParameter("month", String.valueOf(monthPicked));
        urlBuilder.addQueryParameter("sem", sem);
        urlBuilder.addQueryParameter("sub", s);

        Toast.makeText(Blacklist.this, "got month "+monthPicked, Toast.LENGTH_SHORT).show();
        String e = urlBuilder.build().toString();
        Log.d("url222>>>>>>>>>", e);
        Request request1 = new Request.Builder()
                .url(e)
                .build();
        Call call1 = client1.newCall(request1);

        final String finalThisYear = thisYear;
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call1, IOException e) {

            }

            @Override
            public void onResponse(Call call1, Response response) throws IOException {

                if (response.isSuccessful()) {


                    DatabaseReference   databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+ "/"+acdyear+"/stud/blacklist");
                    databaseReference.removeValue();

                    String url2 ="https://us-central1-attender-491df.cloudfunctions.net/sortBlacklist";
                    OkHttpClient client1 = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(url2).newBuilder();
                    urlBuilder.addQueryParameter("college", collName);
                    urlBuilder.addQueryParameter("dept", faculty);
                    urlBuilder.addQueryParameter("classStr",acdyear );
                    urlBuilder.addQueryParameter("year", finalThisYear);
                    urlBuilder.addQueryParameter("month", String.valueOf(monthPicked));
                    urlBuilder.addQueryParameter("sem", sem);
                    urlBuilder.addQueryParameter("sub", s);

                    String e = urlBuilder.build().toString();
                    Log.d("url33>>>>>>>>>", e);
                    Request request1 = new Request.Builder()
                            .url(e)
                            .build();
                    Call call12 = client1.newCall(request1);

                    call12.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call12, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call12, Response response) throws IOException {

                            if (response.isSuccessful()) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "blacklist genrated ",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),PdfGeneration.class));

                                    }
                                });

                            } else {

                            }

                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "done<><>",Toast.LENGTH_SHORT).show();

                        }
                    });


                } else {

                }

            }
        });


    }

}
