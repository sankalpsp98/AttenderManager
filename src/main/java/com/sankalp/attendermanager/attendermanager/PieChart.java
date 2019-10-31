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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChart extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    TextView name;
    TextView phone;
    TextView gardianPhone;
    TextView rollNo;
    EditText getUID;
    Spinner period;

    private String thisYear;

    private int thisDate;
    private int thisMonthVALUE;
    private int thisYearINT;
    private int thisMonth;
    Calendar calendar = Calendar.getInstance();
    int tempyear;

    dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();
    private  String lecture = dataWire1.getLecture1();
    private  String CollName = dataWire1.getCollege();
    private ArrayAdapter<CharSequence> periodAdapter;
    private String rollNoSTR;
    private int cout;
    private ChildEventListener childEventListener;
    private int gateKeeper=0;
    private   ArrayList<String> subjectList= new ArrayList<>();


    private HashMap<String,Integer> subStudentTotal = new HashMap<>();
    private HashMap<String,Integer> totalSubjectCout = new HashMap<>();
    private HashMap<String,Double> subjectPercentage= new HashMap<>();
    private int gateKeeper2=0;
    List<SliceValue> pieData = new ArrayList<>();
    PieChartView pieChartView;

    RandomColor randomColor = new RandomColor();
    private PieChartData pieChartData;
    private int variable;
    private String TAG="ök i gotch you > ";
    private int thisMonthTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       pieChartView = findViewById(R.id.chart);

        thisYearINT = calendar.get(Calendar.YEAR);

        thisDate=calendar.get(Calendar.DATE);

        thisMonthVALUE=calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        thisMonth = thisMonthVALUE+1;
        thisMonthTemp=thisMonth;

        Log.d("//////////////>>>>",thisMonthVALUE+" "+thisMonth);
        findViewById(R.id.viewMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),studentReportMoreInfo.class).putExtra("roll",rollNo.getText().toString()));
            }
        });





        //databaseReference.keepSynced(true);
        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }
        name =  findViewById(R.id.textView23);
        phone = findViewById(R.id.textView24);
        gardianPhone =findViewById(R.id.textView26);
        rollNo = findViewById(R.id.textView25);
        period=findViewById(R.id.spinner3);
        getUID =findViewById(R.id.editText14);
        periodAdapter= ArrayAdapter.createFromResource(this,R.array.period,android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period.setAdapter(periodAdapter);

        period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Last Month"))
                {
                    thisMonthTemp-=1;

                    Toast.makeText(getApplicationContext(), thisMonthTemp+" "+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                    setPieChart();

                } else if (adapterView.getItemAtPosition(i).equals("This Month")) {
                    thisMonthTemp=thisMonth;
                    setPieChart();

                    Toast.makeText(getApplicationContext(), thisMonth+" "+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getUID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty())
                {
                    name.setText("");
                    phone.setText("");
                    gardianPhone.setText("");
                    rollNo.setText("");
                    getUID.setError("Enter UID");
                }else {
                    setView(editable.toString());
                    getUID.setError(null);
                }

            }
        });




        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(10);

        pieChartView.setPieChartData(pieChartData);

        ImageView imageView1 = findViewById(R.id.imageView7);

        String imgurl = "https://iconfree.net/256x256/2018/11/11/profile-man-avatar-icon-0879-512x512.png";
        Glide.with(this).load(imgurl) .fitCenter().into(imageView1);






        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setView(String rollNoSTR) {
        this.rollNoSTR=rollNoSTR;
        final DatabaseReference rollno = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/"+div+ File.separator+rollNoSTR);

        rollno.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    //  Toast.makeText(PieChart.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("ye error kaha se aaya", rollno + " ");
                    Log.d("ye error kaha se aaya", dataSnapshot.child("rollNo").getValue().toString());

                    rollNo.setText(dataSnapshot.child("rollNo").getValue().toString());
                    name.setText(dataSnapshot.child("name").getValue().toString());
                    gardianPhone.setText(dataSnapshot.child("Guardian").getValue().toString());
                    phone.setText(dataSnapshot.child("phone").getValue().toString());
                    setPieChart();
                }catch (Exception e)
                {
                    Toast.makeText(PieChart.this, "Data Is Not Enough", Toast.LENGTH_SHORT).show();

                }








            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void setPieChart() {

        cout=0;
        pieData.clear();
        subStudentTotal.clear();
        totalSubjectCout.clear();
        pieChartData = new PieChartData(pieData);
        subjectList.clear();
        pieChartView.destroyDrawingCache();



        pieChartView.setPieChartData(pieChartData);
        Log.d("size kya hai", pieData.size()+" "+subStudentTotal.size()+" "+totalSubjectCout.size());




        final DatabaseReference pieChart = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + CollName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + "/stud/" + div + File.separator + rollNoSTR);
        final DatabaseReference subjectRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + CollName + "/department/" + faculty + "/" + thisYear + "/" + acdyear + File.separator + "subject/" + sem);


        if (subjectRef != null) {
            try {
                subjectRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        variable = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                        Log.d("Count kiya", variable + "  " + dataSnapshot.getKey().toString());
                        childEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                cout++;
                                //  DataSnapshot dataSnapshot1 =   dataSnapshot;
                                Log.d("chart  me aaya ", dataSnapshot.getKey().toString());
                                if (dataSnapshot.child(String.valueOf(thisMonthTemp
                                )).child("div").child(div).child("total").exists()) {
                                    subjectList.add(dataSnapshot.getKey());
                                    Log.d("ddddokman", dataSnapshot.child(String.valueOf(thisMonthTemp)).child("div").child(div).child("total").getValue().toString());
                                    int subTotal = Integer.parseInt((String) dataSnapshot.child(String.valueOf(thisMonthTemp)).child("div").child(div).child("total").getValue());
                                    totalSubjectCout.put(dataSnapshot.getKey().toString(), subTotal);
                                }

                                Log.d("Count kya hai", cout + " ==" + variable);
                                if (variable == cout) {
                                    Log.d("Undar Ja raha hai ", "Child mein aya");
                                    subjectRef.removeEventListener(childEventListener);
                                    gateKeeper = 1;
                                    cout = 0;

                                    //=====================================================================================================================

                                    pieChart.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                Log.d(TAG, pieChart + " " + dataSnapshot.getKey());
                                                for (int i = 0; i < subjectList.size(); i++)
                                                {
                                                    cout++;
                                                    String substr = subjectList.get(i);
                                                    Log.d(TAG, substr + " " + dataSnapshot.getKey()+"  size  "+subjectList.size());
                                                    if (dataSnapshot.child("dataHistory").child("subDataProfile").child(substr).child(String.valueOf(thisMonthTemp))
                                                            .child("total").exists())
                                                    {
                                                        subStudentTotal.put(substr, Integer.parseInt(dataSnapshot.child("dataHistory").child("subDataProfile")//idhr change kiya
                                                                .child(substr).child(String.valueOf(thisMonthTemp)).child("total").getValue().toString()));
                                                    }else {
                                                        subStudentTotal.put(substr,0);

                                                    }
                                                }
                                                if (subjectList.size() == cout) {
                                                    gateKeeper2 = 1;
                                                    //=============================================================inside===========================================
                                                    for (int i = 0; i < subjectList.size(); i++) {
                                                        float a = subStudentTotal.get(subjectList.get(i));
                                                        float b = totalSubjectCout.get(subjectList.get(i));
                                                        float c = (int) (((double) a * 100 / (double) b));

                                                        Log.d("Value kya hai bhai", a + " " + b);


                                                        double total = c;
                                                        subjectPercentage.put(subjectList.get(i), total);
                                                        if (c!=0) {
                                                            Log.d("Chart mein aya", "" + total + " " + subStudentTotal.get(subjectList.get(i)) + " total " + totalSubjectCout.get(subjectList.get(i)));

                                                            pieData.add(new SliceValue((float) total, randomColor.randomColor()).setLabel(subjectList.get(i) + " : " + total));


                                                            pieChartData.setHasLabels(true).setValueLabelTextSize(10);
                                                        }

                                                        if (subjectList.size() == i + 1) {

                                                            pieChartView.setPieChartData(pieChartData);
                                                        }

                                                    }


                                                }



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    //==================================================================

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
                        };
                        subjectRef.addChildEventListener(childEventListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } catch (Exception e) {
                Toast.makeText(this, "We dont have enough data to calculate this", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
