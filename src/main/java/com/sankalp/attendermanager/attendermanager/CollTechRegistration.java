package com.sankalp.attendermanager.attendermanager;

import android.app.ActivityOptions;
import android.app.usage.NetworkStats;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CollTechRegistration extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coll_tech_registration);
        final Button collCreate=findViewById(R.id.button4);
        final Button teacherReg=findViewById(R.id.button5);
        final View b1 = collCreate;
        View b2 = teacherReg;
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            collCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getApplicationContext(), collegeVerification.class);
                    ActivityOptions activityOptions = null;


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(CollTechRegistration.this,
                                Pair.create(b1, "b1"));

                        startActivity(mIntent, activityOptions.toBundle());
                    } else {
                        startActivity(mIntent);
                    }


                }
            });

            teacherReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getApplicationContext(), HodTeacher.class);
                    mIntent.putExtra("From_activity", "b");
                    ActivityOptions activityOptions = null;


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(CollTechRegistration.this,
                                Pair.create(b1, "b2"));

                        startActivity(mIntent, activityOptions.toBundle());
                    } else {
                        startActivity(mIntent);
                    }

                }
            });
        }  else {

            connected = false;
            startActivity(new Intent(CollTechRegistration.this,no_internet_alert.class));
        }
    }

}
