package com.sankalp.attendermanager.attendermanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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

public class timeTableUI extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference  databaseReference;
    Query query ;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    dataWire dataWire1 = new dataWire();
    FirebaseRecyclerAdapter<dataSubjectReciver,timeTableUI.subjectView> firebaseRecyclerAdapter;

    private  String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase()
            ;
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();

    private  String collName = dataWire1.getCollege();

    private SwipeRefreshLayout swipeContainer;
    DatabaseReference databaseReference1;

    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
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
    private int switchGateKeeper=0;
    private Intent mint;
    private String ch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_ui);
        Calendar calendar = Calendar.getInstance();
        Day=findViewById(R.id.textView13);

        mint = getIntent();
        ch = String.valueOf(mint.getStringExtra("From_activity"));



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

        dayToday = calendar.getTime();

        Log.d("Date","Today's"+thisDate+" month_date1.format(thisDate) "+month_date1.format(thisDate));
        Day.setText(month_date1.format(dayToday));


        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+ "/"+acdyear+"/TimeTable/Ttstamp");
        query = databaseReference.orderByKey();
        Log.d("out >><<",thisDate+"-"+thisMonth+"");

        Log.d("CollegeName",""+collName+" "+faculty+" "+acdyear+" "+sem+" "+sub+" "+div);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer1);

        swipeContainer.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // Stop animation (This will be after 3 seconds)
                swipeContainer.setRefreshing(false);
                firebaseRecyclerAdapter.notifyDataSetChanged();

            }
        }, 4000);

        recyclerView = findViewById(R.id.timeTable);


        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));




        firebaseRecyclerOptions = new  FirebaseRecyclerOptions.Builder<dataSubjectReciver>().setQuery(query,dataSubjectReciver.class).build();


        firebaseRecyclerAdapter =  new FirebaseRecyclerAdapter<dataSubjectReciver, timeTableUI.subjectView>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final timeTableUI.subjectView holder, final int position, @NonNull final dataSubjectReciver model) {

                holder.setTtStamp(model.getTtStamp(),position);
                final subjectView btnholder= (subjectView) holder;


                Toast.makeText(timeTableUI.this, "lokl", Toast.LENGTH_SHORT).show();


                DatabaseReference a= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/TimeTable/"+thisMonth+"-"+thisDate+"/"+div);

                DatabaseReference timetableConstant = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/TimeTable/Ttstamp");

                timetableConstant.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long childcCount = dataSnapshot.getChildrenCount();
                        for (int i=1;i<=childcCount;i++)
                        {
                            timeConvertorSanRich(dataSnapshot.child(""+i).child("ttStamp").getValue().toString());
                            boolean g = start<currentTIME&&currentTIME<end;
                            Log.d("hello>>>>>>>>>>",start+" "+currentTIME+" "+end +" "+g);
                            if (start<currentTIME&&currentTIME<end)
                            {
                                Log.d(">>>>>>>","output aa gaya now fuck off");





                                // holder.setTtStamp(model.getTtStamp(),i,false);

                                // textView.setEnabled(false);
                            }else {
                                firebaseRecyclerAdapter.getItem(i-1).setTtStamp(start+"~"+end);
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
                        if(dataSnapshot.exists())
                        {

                            Toast.makeText(timeTableUI.this, ""+dataSnapshot.child("1").child("subNameStamp").getValue()+model.getTtStamp(), Toast.LENGTH_SHORT).show();
                            String a =  dataSnapshot.child("subNameStamp").getValue().toString();
                            int a1= Integer.parseInt(dataSnapshot.getKey());

                            // View view =recyclerView.getLayoutManager().getChildAt(a1);
                            // Toast.makeText(StudentAttendence.this, a1+" "+view, Toast.LENGTH_SHORT).show();
                            //TextView tv = view.findViewById(R.id.textView4);
                            Toast.makeText(timeTableUI.this,""+a1,Toast.LENGTH_SHORT).show();
                            firebaseRecyclerAdapter.getItem(a1-1).setTtStamp(a);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.exists())
                        {
                            Toast.makeText(timeTableUI.this, ""+dataSnapshot.child("1").child("subNameStamp").getValue()+model.getTtStamp(), Toast.LENGTH_SHORT).show();

                            String a =  dataSnapshot.child("subNameStamp").getValue().toString();
                            int a1= Integer.parseInt(dataSnapshot.getKey());

                            // View view =recyclerView.getLayoutManager().getChildAt(a1);
                            // Toast.makeText(StudentAttendence.this, a1+" "+view, Toast.LENGTH_SHORT).show();
                            // TextView tv = view.findViewById(R.id.textView4);

                            firebaseRecyclerAdapter.getItem(a1-1).setTtStamp(a);
                            firebaseRecyclerAdapter.notifyDataSetChanged();

                            Toast.makeText(timeTableUI.this, "hello", Toast.LENGTH_SHORT).show();






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

                Log.d("<<<<<position>>>>>",position+"");



            }

            @NonNull
            @Override
            public subjectView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                final View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_buttons,parent,false);


                return new timeTableUI.subjectView(viewm);

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
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 4000);
                Log.d("IInside thread >>>","swipeeeeee in side>>>>>>>>");

            }
        });
    }

    private void timeConvertorSanRich(String str) {

        int a= str.indexOf("-");
        String starttime = str.substring(0,a-1);
        String endtime = str.substring(a+1);


        Log.d(">>>>>>>>time>>>>>>>>>>",start+" "+end);

        if(starttime.contains(":"))
        {
            starttime= starttime.replace(":",".");
            start = Float.parseFloat(starttime) ;

        }
        else
        {
            start = Float.parseFloat(starttime) ;
        }
        if(endtime.contains(":"))
        {
            endtime= endtime.replace(":",".");
            end = Float.parseFloat(endtime) ;

        }
        else
        {
            end = Float.parseFloat(endtime) ;
        }
        if (gateKeeper==1)
        {

        }
        gateKeeper=1;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem itemSwitch=menu.findItem(R.id.mySwitch);
        itemSwitch.setActionView(R.layout.useswitch);
        final Switch sw=menu.findItem(R.id.mySwitch).getActionView().findViewById(R.id.action_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(timeTableUI.this, R.style.MyDialogTheme);
                    alert.setTitle("Alert");
                    alert.setMessage("Do you want to enable View Only mode ");
                    alert.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sw.setChecked(true);
                            switchGateKeeper = 1;
                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sw.setChecked(false);
                        }
                    });
                    alert.create().show();


                } else {
                    switchGateKeeper = 0;

                }

        }
    });
        return super.onCreateOptionsMenu(menu);

    }

    public class subjectView extends  RecyclerView.ViewHolder {
        View mView1;
        Button textView;
        private long lastTouchTime = 0;
        private long currentTouchTime = 0;
        public subjectView(View itemView) {
            super(itemView);
            mView1 = itemView;

        }

        public void setTtStamp(final String subjectName, int pos) {
            // Log.d("subjectname>>>>>>>>>>>>", "" + subjectName);
            textView = mView1.findViewById(R.id.button3);
            textView.setEnabled(false);
            final String a= String.valueOf(pos+1);



            final DatabaseReference timetableConstant = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/TimeTable/Ttstamp");
            final DatabaseReference timeremove =FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/TimeTable/"+thisMonth+"-"+thisDate+"/"+div+"/"+a);
            final DatabaseReference subSel= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+collName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem);


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Log.d(">>>>>>>>>>>>>>>>",""+textView.getText());

                    final String s =  textView.getText().toString();
                    final boolean isTrue =s.contains("-");
                    lastTouchTime = currentTouchTime;
                    currentTouchTime = System.currentTimeMillis();

                    if (currentTouchTime - lastTouchTime < 250) {
                        Log.d("Duble","Click");
                        lastTouchTime = 0;
                        currentTouchTime = 0;
                        AlertDialog.Builder alert=new AlertDialog.Builder(timeTableUI.this);
                        alert.setTitle("Alert");
                        alert.setMessage("Are you sure you want to remove subject this action will cause deletion of DATA");

                        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.d("Path",subSel.toString());
                                Log.d("Just A Check",div+sub+subjectName);
                                subSel.child(subjectName).child(thisMonth+"").child("div").child(div).child("total").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String a= dataSnapshot.getValue().toString();
                                        Log.d("YAHA KYA HAI", a);
                                        int b= Integer.parseInt(a)-1;
                                        Log.d("YAHA KYA HAI", String.valueOf(b));
                                        subSel.child(subjectName).child(thisMonth+"").child("div").child(div).child("total").setValue(b+"");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                subSel.child(textView.getText().toString()).child(thisMonth+"").child(""+thisDate).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        timeremove.removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                swipeContainer.setRefreshing(true);

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override public void run() {
                                                        // Stop animation (This will be after 3 seconds)

                                                        firebaseRecyclerAdapter.notifyDataSetChanged();
                                                        textView.setText("");



                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override public void run() {
                                                                // Stop animation (This will be after 3 seconds)
                                                                swipeContainer.setRefreshing(false);
                                                                firebaseRecyclerAdapter.notifyDataSetChanged();
                                                            }
                                                        }, 4000);

                                                    }
                                                }, 4000);

                                            }
                                        });
                                    }
                                });
                            }
                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();

                    }
                    if (!s.contains("-")&&!s.contains("~"))
                    {
                        if (switchGateKeeper==1)
                        {
                            dataWire1.setLecture1(a);
                            dataWire.setSubject(textView.getText().toString());
                            Intent intent=new Intent(timeTableUI.this,StudentAttendenceViewMode.class);
                            startActivity(intent);
                        }
                    }
                    if (isTrue)
                    {

                        //  Log.d("subject slected====","yessssssssss "+a);
                        dataWire1.setLecture1(a);
                        Intent intent=new Intent(timeTableUI.this,SubjectSelection.class);
                        if (ch.equals("simple")) {
                            intent.putExtra("From_activity", "simple");
                        }else if(ch.equals("qr")){

                            intent.putExtra("From_activity", "qr");

                        }
                        startActivity(intent);
                    }else {
                        // Log.d("subject slected====","nooooooooooo");
                    }

                }
            });
            textView.setText(subjectName);
            textView.setTextColor(Color.DKGRAY);
            textView.setEnabled(true);
            //  Log.d("FOMDSIF",""+subjectName);

        }



    }







}
