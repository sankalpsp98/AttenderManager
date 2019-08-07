package com.sankalp.attendermanager.attendermanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.regex.Pattern;

public class hod_Management extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod__management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
        TextView Subject=findViewById(R.id.textView15);
        TextView StudentAttendance=findViewById(R.id.textView16);
        TextView Timetable=findViewById(R.id.textView17);
        Subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert=new AlertDialog.Builder(hod_Management.this);
                alert.setTitle("Alert");
                alert.setMessage("This action will cause deletion of DATA of all  subject wise attendance  ");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent upload = new Intent(getApplicationContext(),uploadDetails.class);
                        upload.putExtra("option","1");
                        startActivity(upload);
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.create().show();


            }
        });

        StudentAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(hod_Management.this);
                alert.setTitle("Alert");
                alert.setMessage("This action will cause deletion of DATA TimeStamp ");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //startActivity(new Intent(getApplicationContext(),uploadDetails.class));
                        Intent upload = new Intent(getApplicationContext(),uploadDetails.class);
                        upload.putExtra("option","3");
                        startActivity(upload);
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.create().show();


            }
        });

        Timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(hod_Management.this);
                alert.setTitle("Alert");
                alert.setMessage("This action will cause deletion of DATA TimeStamp ");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //startActivity(new Intent(getApplicationContext(),uploadDetails.class));
                        Intent upload = new Intent(getApplicationContext(),uploadDetails.class);
                        upload.putExtra("option","2");
                        startActivity(upload);
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alert.create().show();

            }
        });
    }


}
