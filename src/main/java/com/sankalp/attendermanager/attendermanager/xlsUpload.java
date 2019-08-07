package com.sankalp.attendermanager.attendermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.security.Guard;
import java.time.Year;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;

public class xlsUpload extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button button;
    private String thisYear;
    private int thisYearINT;
    private int tempyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xls_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();

        thisYearINT = calendar.get(Calendar.YEAR);

        tempyear =thisYearINT+1;
        thisYear= String.valueOf(thisYearINT+"-"+tempyear);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {new MaterialFilePicker()
                    .withActivity(xlsUpload.this)
                    .withRequestCode(1)
                    .withFilter(Pattern.compile(".*\\.xls$"))// Filtering files and directories by file name using regexp
                    .withFilterDirectories(true) // Set directories filterable (false by default)
                    .withHiddenFiles(true) // Show hidden files and folders
                    .start();

            }
        });




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Toast.makeText(this,""+filePath,Toast.LENGTH_LONG).show();

            DatabaseReference stud  = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/sathaye/department/bmm/"+thisYear+"/fy/stud");

            try {
                //AssetManager ar=getAssets();
                File file = new File(filePath);
                Workbook wb = Workbook.getWorkbook(file);
                Sheet s = wb.getSheet(0);
                int row = s.getRows();
                int col = s.getColumns();

                int uidc = 0;
                int namec = 0;
                int Yearc = 0;
                int phonec = 0;
                int divisionc = 0;
                int GuardianNumc = 0;
                int Categoryc = 0;
                int Classc = 0;
                int Sexc = 0;
                for (int i = 0; i < col; i++) {
                    switch (String.valueOf(s.getCell(i, 0).getContents())) {
                        case "Roll No":
                            uidc = i;
                            break;
                        case "Combined name":
                            namec = i;
                            break;
                        case "Year":
                            Yearc = i;
                            break;
                        case "Mobile No":
                            phonec = i;
                            break;
                        case "Div":
                            divisionc = i;
                            break;
                        case "Guardian Mobile No":
                            GuardianNumc = i;
                            break;
                        case "Category":
                            Categoryc = i;
                            break;
                        case "Class":


                            Classc = i;
                            break;
                        case "Sex":
                            Sexc = i;
                            break;

                        default:
                            break;
                    }
                }


                for (int i = 1; i < row; i++) {

                    String division = s.getCell(divisionc, i).getContents().toString();
                    if (!division.equals("")) {




                    String uid = s.getCell(uidc, i).getContents().toString();
                    String name = s.getCell(namec, i).getContents().toString();
                    String Year = s.getCell(Yearc, i).getContents().toString();
                    String phone = s.getCell(phonec, i).getContents().toString();
                    //division=s.getCell(divisionc,i).getContents().toString();
                    String Guardian = s.getCell(GuardianNumc, i).getContents().toString();
                    String Category = s.getCell(Categoryc, i).getContents().toString();
                    String Classs = s.getCell(Classc, i).getContents().toString();
                    String Sex = s.getCell(Sexc, i).getContents().toString();


                    Classs = Classs.replace(".", "").toLowerCase();

                    Log.d("classsssssss>>>>>>>>>>", Classs);


                    Toast.makeText(xlsUpload.this, "hey5" + uidc + " " + namec + " " + Yearc + " " + phonec + " " + divisionc + " " + GuardianNumc + " " + Categoryc + " " + Classc + " " + Sexc + " ", Toast.LENGTH_SHORT).show();


                    Log.d("Div>>>>>>>","hey5" + uidc + " " + namec + " " + Yearc + " " + phonec + " " + divisionc + " " + GuardianNumc + " " + Categoryc + " " + Classc + " " + Sexc + " ");




                    Toast.makeText(xlsUpload.this, "hey5" + uid + name + Year + phone + division + Guardian + Category + Classs + Sex, Toast.LENGTH_SHORT).show();
                    stud.child(division).child(uid).child("name").setValue(name);
                    stud.child(division).child(uid).child("Year").setValue(Year);
                    stud.child(division).child(uid).child("phone").setValue(phone);
                    stud.child(division).child(uid).child("division").setValue(division);
                    stud.child(division).child(uid).child("Guardian").setValue(Guardian);
                    stud.child(division).child(uid).child("Category").setValue(Category);
                    stud.child(division).child(uid).child("Classs").setValue(Classs);
                    stud.child(division).child(uid).child("Sex").setValue(Sex);
                    stud.child(division).child(uid).child("rollNo").setValue(uid);

                }
                    }

                }catch(Exception e)
                {
                    Toast.makeText(xlsUpload.this, "" + e, Toast.LENGTH_SHORT).show();
                }

            // Do anything with file
        }
    }

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
        getMenuInflater().inflate(R.menu.xls_upload, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(xlsUpload.this,departmentSelection.class));
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(xlsUpload.this, QRGen.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(xlsUpload.this,uploadDetails.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(xlsUpload.this,timeTableUI.class));


        } else if (id == R.id.nav_share) {
            startActivity(new Intent(xlsUpload.this,FileUploadDownload.class));
        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(xlsUpload.this, Login.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
