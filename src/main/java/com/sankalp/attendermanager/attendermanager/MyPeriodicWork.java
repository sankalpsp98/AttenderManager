package com.sankalp.attendermanager.attendermanager;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;

public class MyPeriodicWork extends Worker {
    @NonNull
    @Override
    public Result doWork() {

        Log.e("ME", "doWork: Work is done.");
        return Result.SUCCESS;

    }
}
