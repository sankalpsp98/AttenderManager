package com.sankalp.attendermanager.attendermanager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class QRGen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , bottom_sheet_college_switch.BottomSheetListner {

    dataWire dataWire = new dataWire();
    private ArrayList<String> yearList= new ArrayList<>();
    private ArrayAdapter<CharSequence> yearAdapter;
    private ArrayList<String> semList= new ArrayList<>();
    private ArrayAdapter<CharSequence> semAdapter;
    private ArrayList<String> divList= new ArrayList<>();
    private ArrayAdapter<CharSequence> divAdapter;
    private ArrayList<String> factList= new ArrayList<>();
    private ArrayAdapter<String> factAdapter;
    private int thisYear;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String collName;
    private String facuName;
    private String selectedYear;
    private String Faculty;
    DatabaseReference semData;
    DatabaseReference  subData;
    DatabaseReference divData;
    private String selectedSem;
    private static String selectedDiv;
    private String selectedLecture;
    DatabaseReference collDir = null;
    private Spinner sem;
    Spinner year;
    Spinner div;
    Spinner faculty;
    private String selectedSub;
    String hod=" ";
    Button HOD;


    Intent mint ;
    String ch=" " ;
    TextView divText ;
    TextView semText;
    TextView classText;
    TextView facultyText;
    private Button collegeNameSwitch;
    private String college;
    DatabaseReference facList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Details");



        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            mint = getIntent();
            ch = String.valueOf(mint.getStringExtra("From_activity"));

            Log.d("Intent <><><><><><>", ch);


            final DatabaseReference user1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid());
            DatabaseReference teacherColl = user1.child("college");


            user1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String collegeName = dataSnapshot.child("college").getValue().toString();
                    dataWire.setCollege(collegeName);
                    facList   = user1.child("colleges").child(dataWire.getCollege()).child("faculty");
                    collegeNameSwitch.setText(collegeName);
                    if (facList != null) {
                /*teacherColl.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        collName = dataSnapshot.getValue().toString();
                        dataWire.setCollege(collName);
                        Log.d("Collegename.....", "done " + collName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
                        factAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, factList);
                        factAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        final DatabaseReference finalCollDir = collDir;
                        facList.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                String factList1 = dataSnapshot.getKey().toString();

                                factList.add(factList1);
                                factAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                String factList1 = dataSnapshot.getKey().toString();

                                factList.add(factList1);
                                factAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                String factList1 = dataSnapshot.getKey().toString();
                                factList.remove(factList1);
                                factAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        faculty.setAdapter(factAdapter);

                    }
                    if (dataSnapshot.child("colleges").child(collegeName).child("hod").exists()) {
                        hod = dataSnapshot.child("colleges").child(collegeName).child("hod").getValue().toString();
                        college = dataSnapshot.child("college").getValue().toString();
                        Log.d(">>>>HOD>>>>>>>", "" + hod);
                        if (!hod.equals(" ")) {
                            HOD.setText("HOD" + " " + hod);
                            HOD.setVisibility(View.VISIBLE);

                        }
                    } else {
                        HOD.setText(" ");
                        HOD.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            year = findViewById(R.id.spinner);
            sem = findViewById(R.id.spinner2);
            div = findViewById(R.id.spinner4);
            faculty = findViewById(R.id.spinner5);
            divText = findViewById(R.id.textView5);
            semText = findViewById(R.id.textView3);
            classText = findViewById(R.id.textView2);
            facultyText = findViewById(R.id.textView8);


            if (ch.equals("2")) {

                sem.setVisibility(View.INVISIBLE);
                semText.setVisibility(View.GONE);
                div.setVisibility(View.GONE);
                divText.setVisibility(View.GONE);
            } else if (ch.equals("1")) {
                faculty.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_bg));
                sem
                        .setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_bg));

               // div.setVisibility(View.GONE);
                //divText.setVisibility(View.GONE);
            }


            Calendar calendar = Calendar.getInstance();
            thisYear = calendar.get(Calendar.YEAR);




            Toast.makeText(QRGen.this, "" + " sdds" + facuName, Toast.LENGTH_LONG).show();

            yearAdapter = ArrayAdapter.createFromResource(this, R.array.Class, android.R.layout.simple_spinner_item);
            yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            year.setAdapter(yearAdapter);
            // semAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, semList);
            semAdapter = ArrayAdapter.createFromResource(this, R.array.sem, android.R.layout.simple_spinner_item);
            semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sem.setAdapter(semAdapter);


            divAdapter = ArrayAdapter.createFromResource(this, R.array.div, android.R.layout.simple_spinner_item);
            divAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            div.setAdapter(divAdapter);


            faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Faculty = (String) parent.getItemAtPosition(position);
                    dataWire.setFaculty1(Faculty);


                    //semData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/list/"+Faculty);
                    // setSem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedYear = (String) parent.getItemAtPosition(position);

                    dataWire.setYear1(selectedYear);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSem = (String) parent.getItemAtPosition(position);
                    //subData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/list/"+Faculty+"/"+selectedSem);
                    dataWire.setSem1(selectedSem);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            div.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDiv = parent.getItemAtPosition(position).toString();
                    Toast.makeText(QRGen.this, "" + selectedDiv, Toast.LENGTH_SHORT).show();

                    dataWire.setDiv1(selectedDiv);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Toast.makeText(QRGen.this, "" + "hey ", Toast.LENGTH_LONG).show();


//============================================================================================================
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Log.d("path<<<>>>>", " datachecker " + Faculty + " " + selectedYear + " " + selectedSem + " " + selectedDiv);
                    Toast.makeText(QRGen.this, "" + ch, Toast.LENGTH_SHORT).show();
                    if (ch.equals("null")) {
                        Intent intent =new Intent(getApplicationContext(), timeTableUI.class);
                        intent.putExtra("From_activity","simple");
                        startActivity(intent);
                        Toast.makeText(QRGen.this, ">>>>>" + ch.isEmpty(), Toast.LENGTH_SHORT).show();

                    } else if (ch.equals("2")) {
                        startActivity(new Intent(getApplicationContext(), FileUploadDownload.class));
                    } else if (ch.equals("3")) {
                        Intent intent = new Intent(QRGen.this, PieChart.class);
                        View view1 = faculty;
                        View view2 = year;
                        View view3 = facultyText;
                        View view4 = classText;
                        View view5 = semText;
                        View view6 = divText;
                        ActivityOptions activityOptions = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                            activityOptions = ActivityOptions.makeSceneTransitionAnimation(QRGen.this,
                                    Pair.create(view2, "e1"), Pair.create(view1, "spin1"), Pair.create(view3, "t1"), Pair.create(view4, "t2"), Pair.create(view5, "t3"), Pair.create(view6, "t4"));

                            startActivity(intent, activityOptions.toBundle());
                        } else {
                            startActivity(intent);
                        }


                    } else if (ch.equals("4")) {

                        Intent intent =new Intent(getApplicationContext(), timeTableUI.class);
                        intent.putExtra("From_activity","qr");
                        startActivity(intent);



                    }else
                    {
                        Intent intent = new Intent(QRGen.this, Blacklist.class);
                        intent.putExtra("From_activity", "1");
                        //intent.putExtra("month",selectedMonth);
                        //intent.putExtra("blacklist",selectedYear);
                        startActivity(intent);
                        //chooseMonthOnly();

                    }


                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);
            TextView tvHeaderName = headerView.findViewById(R.id.email);
            tvHeaderName.setText(user.getEmail());

            View headerView1 = navigationView.getHeaderView(0);
            TextView tvHeaderName1 = headerView1.findViewById(R.id.name);
            tvHeaderName1.setText(user.getDisplayName());
            ImageView imageView1 = headerView.findViewById(R.id.imageView);
            String imgurl = user.getPhotoUrl().toString();
            Glide.with(this).load(imgurl).fitCenter().into(imageView1);


            HOD = headerView.findViewById(R.id.hod);
            collegeNameSwitch= headerView.findViewById(R.id.college);
            collegeNameSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottom_sheet_college_switch switchCollege =  new bottom_sheet_college_switch();
                    switchCollege.show(getSupportFragmentManager()," bottom_sheet_college_switch");

                }
            });

            HOD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent=new Intent(getApplicationContext(),hod_Management.class);
                    startActivity(new Intent(getApplicationContext(), hod_Management.class));

                }
            });


        }  else {

            connected = false;
            startActivity(new Intent(QRGen.this,no_internet_alert.class));
        }


    }


/*

    private void setSem() {
        semList.clear();
        semData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String studSem = dataSnapshot.getKey().toString();
                semList.add(studSem);
                semAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String studSem = dataSnapshot.getKey().toString();
                semList.add(studSem);
                semAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String studSem = dataSnapshot.getKey().toString();
                semList.remove(studSem);
                semAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sem.setAdapter(semAdapter);
    }
*/
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qrgen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void chooseMonthOnly() {
        //setContentView(R.layout.activity_choose_month);


                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getApplicationContext(), new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {


                    }
                }, /* activated number in year */ 3, 5);

                builder.showMonthOnly()
                        .build()
                        .show();

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent=new Intent(QRGen.this,QRGen.class);
            intent.putExtra("From_activity","1");
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(),QRGen.class);
            intent.putExtra("From_activity","4");
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(QRGen.this,QRGen.class);
            intent.putExtra("From_activity","3");
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(QRGen.this,PdfGeneration.class));


        } else if (id == R.id.nav_share) {
            Intent intent=new Intent(QRGen.this,QRGen.class);
            intent.putExtra("From_activity","2");
            startActivity(intent);




        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(QRGen.this, Login.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onButtonClicked(String text) {
        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
    }
}
