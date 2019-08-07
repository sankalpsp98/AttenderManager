package com.sankalp.attendermanager.attendermanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class studentLectureAttended extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Query query;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<dataSubjectReciver, studentLectureAttended.subjectViewAttended> firebaseRecyclerAdapter;

    dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
    private String acdyear = dataWire1.getYear1().toLowerCase();
    private String sem = dataWire1.getSem1();
    private String sub = dataWire1.getSubject();
    private String div = dataWire1.getDiv1();

    private String collName = dataWire1.getCollege();

    private SwipeRefreshLayout swipeContainer;
    DatabaseReference databaseReference1;
    private String thisYear;
    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
    private int thisMonth;
    private int thisDate;
    private int thisMonthVALUE;

    private long Start;
    private float start;
    private float end;
    int gateKeeper = 0;
    private Date CurrentTime;
    private float currentTIME;

    private String WeekDay;

    TextView Day;
    private Date dayToday;
    private int thisYearINT;
    private int tempyear;
    private int thisYear1;
    private String intentRoll;
    private Intent mint;
    private  String CollName = dataWire1.getCollege();
    private String monthExtra=null;
    private String dateExtra=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_student_lecture_attended);
        Calendar calendar = Calendar.getInstance();
      //  Day = findViewById(R.id.textView13);

        thisYearINT = calendar.get(Calendar.YEAR);
        //thisYearINT =2019;
        CurrentTime = calendar.getTime();
        thisMonthVALUE = calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date1 = new SimpleDateFormat("EEEE");
        SimpleDateFormat month_date11 = new SimpleDateFormat("M");
        SimpleDateFormat CT = new SimpleDateFormat("HH:mm");
        currentTIME = Float.parseFloat(CT.format(CurrentTime).replace(":", "."));


        mint =  getIntent();
        intentRoll= String.valueOf(mint.getStringExtra("roll"));
        monthExtra = String.valueOf(mint.getStringExtra("month"));
        dateExtra = String.valueOf(mint.getStringExtra("date"));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .6), (int) (height * .8));
        thisMonth = thisMonthVALUE + 1;
        thisDate = calendar.get(Calendar.DATE);

       // dayToday = calendar.getTime();

        //Log.d("Date", "Today's" + thisDate + " month_date1.format(thisDate) " + month_date1.format(thisDate));
       // Day.setText(month_date1.format(dayToday));

        thisMonth= Integer.parseInt(monthExtra);
        thisDate = Integer.parseInt(dateExtra);
        if (thisMonth <= 4) {
            tempyear = thisYearINT - 1;
            thisYear = String.valueOf(tempyear + "-" + thisYearINT);

        } else if (thisMonth > 4) {

            tempyear = thisYearINT + 1;
            thisYear = String.valueOf(thisYearINT + "-" + tempyear);

        }
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/TimeTable/Ttstamp");
        query = databaseReference.orderByKey();
       // Log.d("out >><<", thisDate + "-" + thisMonth + "");

       // Log.d("CollegeName", "" + collName + " " + faculty + " " + acdyear + " " + sem + " " + sub + " " + div);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer1);

        swipeContainer.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop animation (This will be after 3 seconds)
                swipeContainer.setRefreshing(false);
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }
        }, 4000);

        recyclerView = findViewById(R.id.timeTable);


        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<dataSubjectReciver>().setQuery(query, dataSubjectReciver.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<dataSubjectReciver, subjectViewAttended>(firebaseRecyclerOptions) {


            @Override
            protected void onBindViewHolder(@NonNull final subjectViewAttended holder, int position, @NonNull final dataSubjectReciver model) {

                holder.setTtStamp(model.getTtStamp(), position);
                Log.d("status<<<<>><>",model.getSetSubjectStatus());
                holder.setSubjectStatus(model.getSetSubjectStatus(),position);
                final subjectViewAttended btnholder = (subjectViewAttended) holder;


                Toast.makeText(studentLectureAttended.this, "lokl", Toast.LENGTH_SHORT).show();


                DatabaseReference a = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/TimeTable/" + thisMonth + "-" + thisDate + "/" + div);

                DatabaseReference timetableConstant = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/TimeTable/Ttstamp");
                final DatabaseReference status = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/"+div+"/"+intentRoll+"/dataHistory/monthData/"+thisMonth+"/"+thisDate);
                Log.d("Path Kya Hai", String.valueOf(status));
                status.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                       String a = dataSnapshot.getKey().toString();
                       a= String.valueOf(a.charAt(1));
                       int index =Integer.parseInt(a);
                        Log.d(">>>JJHUIJHU",index+" "+dataSnapshot.getKey().toString());
                       firebaseRecyclerAdapter.getItem(index-1).setSubjectStatus("1");



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

                timetableConstant.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long childcCount = dataSnapshot.getChildrenCount();
                        for (int i = 1; i <= childcCount; i++) {
                            timeConvertorSanRich(dataSnapshot.child("" + i).child("ttStamp").getValue().toString());
                            boolean g = start < currentTIME && currentTIME < end;
                            //Log.d("hello>>>>>>>>>>", start + " " + currentTIME + " " + end + " " + g);
                            if (start < currentTIME && currentTIME < end) {
                              //  Log.d(">>>>>>>", "output aa gaya now fuck off");


                                // holder.setTtStamp(model.getTtStamp(),i,false);

                                // textView.setEnabled(false);
                            } else {
                                firebaseRecyclerAdapter.getItem(i - 1).setTtStamp(start + "~" + end);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                a.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()) {

                            Toast.makeText(studentLectureAttended.this, "" + dataSnapshot.child("1").child("subNameStamp").getValue() + model.getTtStamp(), Toast.LENGTH_SHORT).show();
                            String a = dataSnapshot.child("subNameStamp").getValue().toString();
                            int a1 = Integer.parseInt(dataSnapshot.getKey());

                            // View view =recyclerView.getLayoutManager().getChildAt(a1);
                            // Toast.makeText(StudentAttendence.this, a1+" "+view, Toast.LENGTH_SHORT).show();
                            //TextView tv = view.findViewById(R.id.textView4);
                            Toast.makeText(studentLectureAttended.this, "" + a1, Toast.LENGTH_SHORT).show();
                            firebaseRecyclerAdapter.getItem(a1 - 1).setTtStamp(a);
                            firebaseRecyclerAdapter.notifyItemChanged(a1-1);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(studentLectureAttended.this, "" + dataSnapshot.child("1").child("subNameStamp").getValue() + model.getTtStamp(), Toast.LENGTH_SHORT).show();

                            String a = dataSnapshot.child("subNameStamp").getValue().toString();
                            int a1 = Integer.parseInt(dataSnapshot.getKey());

                            // View view =recyclerView.getLayoutManager().getChildAt(a1);
                            // Toast.makeText(StudentAttendence.this, a1+" "+view, Toast.LENGTH_SHORT).show();
                            // TextView tv = view.findViewById(R.id.textView4);

                            firebaseRecyclerAdapter.getItem(a1 - 1).setTtStamp(a);
                            firebaseRecyclerAdapter.notifyDataSetChanged();

                            Toast.makeText(studentLectureAttended.this, "hello", Toast.LENGTH_SHORT).show();


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

              ///  Log.d("<<<<<position>>>>>", position + "");


            }

            @NonNull
            @Override
            public subjectViewAttended onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                final View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_buttons, parent, false);


                return new studentLectureAttended.subjectViewAttended(viewm);

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //recyclerView.setAdapter(firebaseRecyclerAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firebaseRecyclerAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 4000);
              //  Log.d("IInside thread >>>", "swipeeeeee in side>>>>>>>>");

            }
        });
    }

    private void timeConvertorSanRich(String str) {

        int a = str.indexOf("-");
        String starttime = str.substring(0, a - 1);
        String endtime = str.substring(a + 1);


        //Log.d(">>>>>>>>time>>>>>>>>>>", start + " " + end);

        if (starttime.contains(":")) {
            starttime = starttime.replace(":", ".");
            start = Float.parseFloat(starttime);

        } else {
            start = Float.parseFloat(starttime);
        }
        if (endtime.contains(":")) {
            endtime = endtime.replace(":", ".");
            end = Float.parseFloat(endtime);

        } else {
            end = Float.parseFloat(endtime);
        }
        if (gateKeeper == 1) {

        }
        gateKeeper = 1;
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

    public class subjectViewAttended extends RecyclerView.ViewHolder {
        View mView1;
        Button button;
        private long lastTouchTime = 0;
        private long currentTouchTime = 0;

        public subjectViewAttended(View itemView) {
            super(itemView);
            mView1 = itemView;

        }

        public void setTtStamp(final String subjectName, int pos) {
             Log.d("subjectname>>>>>>>>>>>>", "" + subjectName);
            button = mView1.findViewById(R.id.button3);
            final String a = String.valueOf(pos + 1);


            final DatabaseReference timetableConstant = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/TimeTable/Ttstamp");
            final DatabaseReference timeremove = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/TimeTable/" + thisMonth + "-" + thisDate + "/" + div + "/" + a);
            final DatabaseReference subSel = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + collName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/subject/" + sem);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Log.d(">>>>>>>>>>>>>>>>",""+textView.getText());

                    final String s = button.getText().toString();
                    final boolean isTrue = s.contains("-");
                    lastTouchTime = currentTouchTime;
                    currentTouchTime = System.currentTimeMillis();

                    if (isTrue) {
                        //  Log.d("subject slected====","yessssssssss "+a);
                       // dataWire1.setLecture1(a);
                       // Intent intent = new Intent(studentLectureAttended.this, SubjectSelection.class);
                        //intent.putExtra("From_activity", "2");
                        //startActivity(intent);
                    } else {
                        // Log.d("subject slected====","nooooooooooo");
                    }

                }
            });
            button.setText(subjectName);
            button.setTextColor(Color.DKGRAY);
            //  Log.d("FOMDSIF",""+subjectName);

        }

        public void setSubjectStatus(String status,int pos){
            Log.d(">>>>>>>>>>status",status);



            TextView textView =  mView1.findViewById(R.id.button3);
            if(status.equals("0"));
            {
                textView.setBackgroundResource(R.drawable.button_bg);
            }
            if(status.equals("1"))
            {

                textView.setBackgroundColor(Color.GREEN);
            }




        }


    }


}










