package com.sankalp.attendermanager.attendermanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import androidx.work.Worker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class workManager extends Worker {
  dataWire dataReciver = new dataWire();
    dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();
    private  String lecture = dataWire1.getLecture1();
    private  String CollName = dataWire1.getCollege();
    private  String acadmmicYear = dataWire1.getAcadmicYear();

    Calendar calendar = Calendar.getInstance();
    private int thisMonthVALUE;
    private int thisMonth;
    private int thisDate;




    HashMap<String,String> dataStud = dataReciver.getStudentHashMap();
     ValueEventListener valueEventListener;
    private static final String TAG = "MyPeriodicWork";
    DatabaseReference proDATA;
    DatabaseReference profiles= FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+acadmmicYear+"/"+acdyear+"/stud/"+div);
    @NonNull
    @Override
    public Result doWork() {
        thisMonthVALUE=calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        thisMonth = thisMonthVALUE+1;
        thisDate=calendar.get(Calendar.DATE);




        Iterator iterator = dataStud.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry pair = (Map.Entry) iterator.next();

            proDATA =   profiles.child(pair.getKey()+"/dataHistory/");
            Log.e("path >>>>>",proDATA+"");
            Log.e("hashmap>>><<<<>><<<>>",pair.getKey()+" value "+pair.getValue());

           // proDATA.child("sankalp").setValue("12");
            proDATA.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("subDataProfile").hasChild(sub))
                    {
                        Log.e("first   ","i am in");

                       String subjectTotal =   dataSnapshot.child("subDataProfile").child(sub).child(String.valueOf(thisMonth)).child("total").getValue().toString();
                       int a=  Integer.parseInt(subjectTotal);
                       a=a+1;
                        proDATA.child("subDataProfile").child(sub).child(String.valueOf(thisMonth)).child("total").setValue(a+"", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                proDATA.child("monthData").child(String.valueOf(thisMonth)).child(String.valueOf(thisDate)).child("l"+lecture).setValue("1", new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (dataSnapshot.hasChild("OverallCounter"))
                                        {
                                            String a= dataSnapshot.child("OverallCounter").getValue().toString();
                                            int b=  Integer.parseInt(a);
                                            b=b+1;
                                            proDATA.child("OverallCounter").setValue(b+"");
                                        }else
                                        {
                                            proDATA.child("OverallCounter").setValue("1");

                                        }
                                    }
                                });
                            }
                        });
                    }
                    else
                    {

                        Log.e("second  ","i am in");
                        proDATA.child("subDataProfile").child(sub).child(String.valueOf(thisMonth)).child("total").setValue("1", new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                proDATA.child("monthData").child(String.valueOf(thisMonth)).child(String.valueOf(thisDate)).child("l"+lecture).setValue("1", new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                        if (dataSnapshot.hasChild("OverallCounter"))
                                        {
                                            String a= dataSnapshot.child("OverallCounter").getValue().toString();
                                            int b=  Integer.parseInt(a);
                                            b=b+1;
                                            proDATA.child("OverallCounter").setValue(b+"");
                                        }else
                                        {
                                            proDATA.child("OverallCounter").setValue("1");

                                        }

                                    }
                                });
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            try {
                Thread.sleep(730);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        Log.e(TAG, "doWork: Work is done.  "+dataStud.size());
        return sendNotification("Done","we have uploded data");
    }


    public Result sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
        return Result.SUCCESS;
    }
}
