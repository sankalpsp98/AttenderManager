package com.sankalp.attendermanager.attendermanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
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

import java.util.ArrayList;


public class departmentSelection extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Query query;
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<dataDepartmentReceiver, departmentSelection.deptView> firebaseRecyclerAdapter;

    RecyclerView recyclerView1;
    DatabaseReference databaseReference1;
    Query query1;
    FirebaseRecyclerOptions firebaseRecyclerOptions1;
    FirebaseRecyclerAdapter<dataDepartmentReceiver, selectedView> firebaseRecyclerAdapter1;
     String triggerkeyA = "a";
     String triggerkeyB ="b";



     Intent mint ;
    String ch ;

    dataWire dataWire = new dataWire();

    String svText;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<String> checklist = new ArrayList<String>(20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mint =  getIntent();
        ch= String.valueOf(mint.getStringExtra("From_activity"));






        dataWire.setIntentExtra(ch);
        final EditText code = findViewById(R.id.editText8);

        Toast.makeText(this, ""+mint.getStringExtra("From_activity"), Toast.LENGTH_SHORT).show();
        final SearchView searchView = findViewById(R.id.sv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(departmentSelection.this, "hey", Toast.LENGTH_SHORT).show();

                deptSearch(newText);

                firebaseRecyclerAdapter.startListening();
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("dept");//main list

        recyclerView = findViewById(R.id.courses);
        recyclerView1 = findViewById(R.id.selectedDept);


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));





        Toast.makeText(this, "chhhhhhhhhhhhh "+ch, Toast.LENGTH_SHORT).show();
        if (ch.equals(triggerkeyB))
        {
            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child("teachers").child("" + user.getUid()).child("colleges").child(dataWire.getCollege()).child("faculty");
            databaseReference1.child("sdd").child("selectedDept").setValue("plz select dept");

            Toast.makeText(this, "heyyyyyyyyyyyyyyyyyyy", Toast.LENGTH_SHORT).show();

        }else if (ch.equals(triggerkeyA))
        {
            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("colleges").child(dataWire.getCollege()).child("department");
            databaseReference1.child("sdd").child("selectedDept").setValue("plz select dept");

        }


        // databaseReference1.child("casacsda").child("selectedDept").setValue("san");

        deptSearch("/");
        firebaseRecyclerAdapter.startListening();


        setSelected();
        firebaseRecyclerAdapter1.startListening();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (!code.getText().toString().isEmpty() && !checklist.isEmpty()) {

                    Log.d("Code",code.getText().toString()+" "+dataWire.getCollegeCode());



                        if (ch.equals(triggerkeyB)) {
                            Intent upload = new Intent(getApplicationContext(), QRGen.class);
                            DatabaseReference college = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");
                            college.child(FirebaseAuth.getInstance().getUid()).child("verify").setValue("1");


                            //upload.putExtra("option","3");
                            startActivity(upload);

                        } else if (ch.equals(triggerkeyA)) {
                            Intent upload = new Intent(getApplicationContext(), uploadDetails.class);
                            upload.putExtra("option", "4");
                            startActivity(upload);

                        }

                } else {
                    code.setError("cannot be empty");

                }
            }
        });
    }




    private void setSelected() {
        query1 = databaseReference1.orderByChild("selectedDept");
        Log.d("daaataaaaaa>>>>>", String.valueOf(query1));
        firebaseRecyclerOptions1 = new FirebaseRecyclerOptions.Builder<dataDepartmentReceiver>().setQuery(query1, dataDepartmentReceiver.class).build();

        setFRV1(firebaseRecyclerOptions1);

    }

    private void setFRV1(FirebaseRecyclerOptions firebaseRecyclerOptions) {
        firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<dataDepartmentReceiver, departmentSelection.selectedView>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(final departmentSelection.selectedView holder, int position, @NonNull final dataDepartmentReceiver model) {
                holder.setSelected(model.getSelectedDept(), position);
                holder.mVIEW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(">>>>>>>DSDASDSD", "SSSSS");
                    }
                });

                Log.d(">>>>>>>ddddddddd", "" + query + " " + query);
            }


            @Override
            public departmentSelection.selectedView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_dept_view, parent, false);
                return new departmentSelection.selectedView(viewm);
            }
        };
        recyclerView1.setAdapter(firebaseRecyclerAdapter1);


    }

    private void getCollegeDecoder(String text) {


    }


    private void deptSearch(String query1) {
        if (query1.equals("/")) {
            query = databaseReference.orderByChild("deptName");
            Log.d(">>>>>>>>s", query + " " + query1);
        } else {
            query = databaseReference.orderByChild("deptName").startAt(query1);
            Log.d(">>>>>>>", "" + query + " " + query);
        }
        Log.d(">>>>>>>zzz", "" + query + " " + query);

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<dataDepartmentReceiver>().setQuery(query, dataDepartmentReceiver.class).build();

        setFRV(firebaseRecyclerOptions);


    }


    private void setFRV(FirebaseRecyclerOptions firebaseRecyclerOptions) {

        Log.d(">>>>>>>zzz>>>>>>>>>>>>>", "" + query + " " + query);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<dataDepartmentReceiver, departmentSelection.deptView>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(final departmentSelection.deptView holder, int position, @NonNull final dataDepartmentReceiver model) {
                holder.setDept(model.getDeptName(), position);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(">>>>>>>DSDASDSD", "SSSSS");
                    }
                });

                Log.d(">>>>>>>ddddddddd", "" + query + " " + query);
            }


            @Override
            public departmentSelection.deptView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewm = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_view, parent, false);
                return new departmentSelection.deptView(viewm);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

//====================================================<<<<<<view>>>>>>>>>>>>>>>>>>===================================================================
    public class selectedView extends RecyclerView.ViewHolder {
        View mVIEW;

        public selectedView(View itemView) {
            super(itemView);
            mVIEW = itemView;
        }

        public void setSelected(String selectedDept, int position) {
            TextView textView = mVIEW.findViewById(R.id.textView17);
            textView.setText(selectedDept);
            Log.d("FOMDSIF", "" + selectedDept + " pos " + position + " SIZE " + checklist.size());

        }


    }

    public class deptView extends RecyclerView.ViewHolder {




        DatabaseReference faculty;

        View mView;
        private int count;
        private int gatekeeper;
        private long childcount=0;
        private int c=0;
        public  String ch = "";
        private ChildEventListener childEventListener;

        public deptView(View itemView) {
            super(itemView);
            mView = itemView;
            ch = mint.getStringExtra("From_activity");

            if(ch.equals(triggerkeyB))
            {
                faculty= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid() + "/colleges/"+dataWire.getCollege()+"/faculty");

            }else
            {
                faculty= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + dataWire.getCollege()+"/department");

            }

        }

        public void setDept(final String deptName, final int position) {
            Log.d(">>>>>>>>>>>>>>>>>>>>", "" + deptName);
            final CheckBox checkBox1 = mView.findViewById(R.id.checkBox6);
            checkBox1.setText(deptName);
            checkBox1.setTextColor(Color.DKGRAY);
            checkBox1.setEnabled(false);


            faculty.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    childcount = dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            if (faculty!=null) {
                checkBox1.setEnabled(true);
                checkBox1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Log.d(">>>>>>>>", String.valueOf(checkBox1.isChecked()));

                        if (checkBox1.isChecked()) {
                            checklist.add(String.valueOf(checkBox1.getText()));
                            checkBox1.setChecked(true);

                            count = 0;
                            gatekeeper = 0;

                            Log.d("FOMDSIF", "" + deptName + " pos " + position + " size " + checklist.size() + "count" + count + "gatekeeper" + gatekeeper);

                                connectFacu(checkBox1.getText());




                        } else {
                            checkBox1.setChecked(false);
                            checklist.remove(checkBox1.getText());

                            if (checklist.size()==0)
                            {
                                faculty.child("sdd").child("selectedDept").setValue("plz select option given below");
                            }
                             childEventListener = new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    Log.d("insideChildRemove",">>>>>>>> childcount:== "+childcount+" c:== " +c+" <<<<<<<<<");

                                    if(dataSnapshot.child("selectedDept").getValue().equals(checkBox1.getText()))
                                    {
                                        Log.d("insideChildRemoveIFF",">>>>>>>> "+childcount+" c:== " +c+" <<<<<<<<<");


                                        removeChildListener(childEventListener);
                                        Toast.makeText(departmentSelection.this, ""+ dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                        faculty.child(dataSnapshot.getKey()).removeValue();
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
                            faculty.addChildEventListener(childEventListener);
                             count = 0;
                            gatekeeper = 0;

                            Log.d("else", "" + deptName + " pos " + position + " SIZE " + checklist.size());
                        }




                    }


                });
            }
        }

        private void removeChildListener(ChildEventListener childEventListener) {
            faculty.removeEventListener(childEventListener);
        }

        private void update(CharSequence text) {
            Toast.makeText(departmentSelection.this, "out side", Toast.LENGTH_SHORT).show();
            Log.d("update",">>>>>>>>gate  "+gatekeeper+" count "+count);
            if (count == 0 && gatekeeper == 1) {
                faculty.child(text+"").child("selectedDept").setValue(text);
                Toast.makeText(departmentSelection.this, "hey", Toast.LENGTH_SHORT).show();
                faculty.child("sdd").child("selectedDept").removeValue();
            }
        }

        private void connectFacu(final CharSequence text) {
c=0;



                    faculty.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Log.d("insideFacULTY",">>>>>>>>");

                            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                            Toast.makeText(departmentSelection.this, "mmmmmammamama    "+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                            //childcount=  dataSnapshot.getChildrenCount();
                            gatekeeper = 1;
                            if (dataSnapshot.child("selectedDept").getValue().equals(text)) {
                                count = 1;
                            }

                            Toast.makeText(departmentSelection.this, "child "+childcount+" count "+ c, Toast.LENGTH_SHORT).show();
                            c++;
                            Log.d("data44",">>>>>>>> childcount:== "+childcount+" c:== " +c);
                            if (childcount==c)
                            {
                                Log.d("insideIfff",">>>>>>>> childcount:== "+childcount+" c:== " +c+" <<<<<<<<<");
                                update(text);
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

    @Override
    public void onBackPressed() {
        ch = mint.getStringExtra("From_activity");

        if(ch.equals(triggerkeyB))
        {
           DatabaseReference faculty= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid() +"/colleges/"+dataWire.getCollege()+ "/faculty");
            faculty.child("sdd").removeValue();
        }else
        {
          DatabaseReference  faculty= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/" + dataWire.getCollege()+"/department");
            faculty.child("sdd").removeValue();
        }
        super.onBackPressed();
    }

}
