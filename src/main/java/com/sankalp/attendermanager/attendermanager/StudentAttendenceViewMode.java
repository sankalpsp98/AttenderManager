package com.sankalp.attendermanager.attendermanager;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StudentAttendenceViewMode extends AppCompatActivity {
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    Query query;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<studentDataReciver,StudentAttendenceViewMode.StudView> firebaseRecyclerAdapter;

    long countCHILD=0;

    dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();
    private  String lecture = dataWire1.getLecture1();
    private  String CollName = dataWire1.getCollege();

    private String thisYear;
    private int thisMonth;
    private int thisDate;
    private int thisMonthVALUE;
    private int thisYearINT;
    int tempyear;
    int error123=1;
    private int positionx;
    private SwipeRefreshLayout swipeContainer;
    int i=0;
    private long COUNT=0;
    MutableLiveData<HashMap<String,String>> studentstatus= new MutableLiveData<>();
    ValueEventListener valueEventListener;
    public HashMap<String,String> studentHashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendence_view_mode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        Calendar calendar = Calendar.getInstance();
        thisYearINT = calendar.get(Calendar.YEAR);
        thisDate=calendar.get(Calendar.DATE);

        thisMonthVALUE=calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        thisMonth = thisMonthVALUE+1;
        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);
            dataWire.setAcadmicYear(thisYear);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);
            dataWire.setAcadmicYear(thisYear);

        }



        databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/"+div);

        query=databaseReference.orderByKey();

        swipeContainer.setRefreshing(true);
        final DatabaseReference a = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/subject/"+sem+"/"+sub+"/"+thisMonth+"/"+thisDate+"/l"+lecture+"/"+div);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // Stop animation (This will be after 3 seconds)
                swipeContainer.setRefreshing(false);
                error123=0;

                firebaseRecyclerAdapter.notifyDataSetChanged();
                a.addValueEventListener(valueEventListener);

            }
        }, 4000);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                MyPeriodicWork.class, 20, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        recyclerView=findViewById(R.id.recycler);
        recyclerView.hasFixedSize();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x;
        int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90,this.getResources().getDisplayMetrics());
        int columnsCount = screenWidth/photoWidth;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columnsCount));

           valueEventListener=new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              COUNT = dataSnapshot.getChildrenCount();
              a.addChildEventListener(new ChildEventListener() {
                  @Override
                  public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                      if (dataSnapshot.exists()) {
                          i += 1;

                          String a = dataSnapshot.child("status").getValue().toString();
                          String pos = dataSnapshot.child("pos").getValue().toString();
                          int a1 = Integer.parseInt(dataSnapshot.getKey());

                          //  View view =recyclerView.getLayoutManager().getChildAt(a1);
                          //  Toast.makeText(StudentAttendence.this, a1+" "+view, Toast.LENGTH_SHORT).show();
                          //TextView tv = view.findViewById(R.id.textView4);
                          if (pos != null && a != null) {
                              studentHashMap.put(pos, a);
                              if (studentHashMap != null) {
                                  studentstatus.setValue(studentHashMap);
                              }

                          }
                          if (i == COUNT) {
                              studentHashMap = studentstatus.getValue();
                              Log.e("match huwa ? ", "haan " + COUNT + "=" + i + " " + "size " + studentHashMap.size());
                              //firebaseRecyclerAdapter.notifyDataSetChanged();

                          }
                      }
                  }

                  @Override
                  public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                      String a = dataSnapshot.child("status").getValue().toString();
                      String pos = dataSnapshot.child("pos").getValue().toString();
                      if (pos != null && a != null) {
                          studentHashMap.put(pos, a);
                          studentstatus.setValue(studentHashMap);

                      }
                  }

                  @Override
                  public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                      String pos = dataSnapshot.child("pos").getValue().toString();
                      studentHashMap.put(pos, "0");
                      studentstatus.setValue(studentHashMap);
                      firebaseRecyclerAdapter.notifyItemChanged(Integer.parseInt(pos));
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

      };
        studentstatus.observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, String> stringStringHashMap) {
                if (stringStringHashMap!=null)
                {
                Log.e("hash map >>", stringStringHashMap.size() + " "+stringStringHashMap.size());
                Iterator iterator = stringStringHashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    Log.e("itorator <<", pair.getKey() + " value " + pair.getValue());
                    firebaseRecyclerAdapter.getItem(Integer.parseInt(pair.getKey().toString())).setXStatus(String.valueOf(pair.getValue()));
                    firebaseRecyclerAdapter.notifyItemChanged(Integer.parseInt(String.valueOf(pair.getKey())));
                    studentHashMap.remove(pair.getKey());
                }



                }

            }
        });

        firebaseRecyclerOptions = new  FirebaseRecyclerOptions.Builder<studentDataReciver>().setQuery(query,studentDataReciver.class).build();
        firebaseRecyclerAdapter =  new FirebaseRecyclerAdapter<studentDataReciver, StudentAttendenceViewMode.StudView>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final StudentAttendenceViewMode.StudView holder, final int position, @NonNull final studentDataReciver model) {
                holder.setRollNo(model.getRollNo());
                holder.setXStatus(model.getXStatus(),position);
                positionx =position;
                      Log.d(" A path ",a+"");



                    }



            @NonNull
            @Override
            public StudentAttendenceViewMode.StudView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_view,parent,false);

                return new StudentAttendenceViewMode.StudView(viewm);
            }
        };

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                error123=1;
                firebaseRecyclerAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        error123=0;

                        swipeContainer.setRefreshing(false);
                    }
                }, 4000);
                Log.d("IInside thread >>>","swipeeeeee in side>>>>>>>>");

            }
        });


    }
    private class StudView extends RecyclerView.ViewHolder{
        View mView;
        public StudView(View itemView)
        {
            super(itemView);
            mView = itemView;


        }

        public  void setRollNo(String rollno)
        {
            Log.d(">>>>>>>>>>>>>>>>>>>>",""+rollno);
            TextView textView = mView.findViewById(R.id.textView4);
            textView.setText(rollno);
            textView.setTextColor(Color.DKGRAY);
        }
        public  void  setXStatus(String  status,int pos){



            TextView textView =  mView.findViewById(R.id.textView4);
            if(status.equals("0"));
            {
                textView.setBackgroundColor(Color.RED);
            }
            if(status.equals("1"))
            {


                textView.setBackgroundColor(Color.GREEN);
            }
        }


    }


    public class MyPeriodicWork extends Worker {

        private static final String TAG = "MyPeriodicWork";

        public MyPeriodicWork() {
        }

        @NonNull
        @Override
        public Result doWork() {
            Log.e("ME", "doWork: Work is done.");
            return Result.SUCCESS;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        // FirebaseApp.initializeApp(this);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        finish();
        firebaseRecyclerAdapter.stopListening();
        super.onStop();

    }


}
