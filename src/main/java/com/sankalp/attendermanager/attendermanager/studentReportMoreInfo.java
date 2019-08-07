package com.sankalp.attendermanager.attendermanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

public class studentReportMoreInfo extends AppCompatActivity {
    CalendarView calendarView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_report_more_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendarView =  findViewById(R.id.calendarView);
       calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int date) {
               Intent intent =new Intent(getApplicationContext(),studentLectureAttended.class);
               intent.putExtra("roll",getIntent().getStringExtra("roll"));
               intent.putExtra("month",(month+1)+"");
               intent.putExtra("date",date+"");
               Toast.makeText(studentReportMoreInfo.this, ""+month+" bbb "+date, Toast.LENGTH_SHORT).show();

               startActivity(intent);

           }
       });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
