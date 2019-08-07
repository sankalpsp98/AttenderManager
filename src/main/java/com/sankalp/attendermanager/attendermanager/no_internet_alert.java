package com.sankalp.attendermanager.attendermanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class no_internet_alert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_alert);
        LottieAnimationView lottieAnimationView=findViewById(R.id.animation_view);
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();
    }
}
