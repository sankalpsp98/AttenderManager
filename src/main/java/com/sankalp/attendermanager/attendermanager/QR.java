package com.sankalp.attendermanager.attendermanager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.MessageDigest;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class QR extends Activity {

    static dataWire dataWire1 = new dataWire();
    private static String faculty = dataWire1.getFaculty1();
    private static String year = dataWire1.getYear1();
    private static String sem = dataWire1.getSem1();
    private static String sub = dataWire1.getSubject();
    private static String div = dataWire1.getDiv1();
    private  static String lecture = dataWire1.getLecture1();
    private String thisYear;
    private int month;
    private int date;
    private String code;
    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference teacher  = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/"+user1.getUid()+"/college");
    private  static  String college ;
    private int thisYearINT;
    private int tempyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Calendar calendar = Calendar.getInstance();

        month =  calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        month = month+1;
        thisYearINT = calendar.get(Calendar.YEAR);
        if(month<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (month>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }

        DisplayMetrics displayMetrics  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width =  displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*.9),(int) (height*.6));

        teacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                college =  dataSnapshot.getValue().toString();
                Toast.makeText(QR.this, ""+college, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageView  imageView =findViewById(R.id.imageView2);
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        if (teacher!=null) {
            try {
                Toast.makeText(QR.this, ">>>>>>>>>>>>"+college, Toast.LENGTH_SHORT).show();
                code = encrypt("https://attender-491df.firebaseio.com/colleges/" + college + "/department/" + faculty + "/"+thisYear+"/"+ year + "/subject/" + sem + "/" + sub + "/" + month + "/" + date + "/" + lecture + "/" + div, "sankalpparab");
            } catch (Exception e) {

                e.printStackTrace();

            }

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE, 800, 800);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }


    private String encrypt(String s, String sankalpparab)  throws Exception {
        SecretKeySpec key =  genkey(sankalpparab);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] enc = cipher.doFinal(s.getBytes());
        String encrypt = Base64.encodeToString(enc,Base64.DEFAULT);
        return encrypt;

    }

    private SecretKeySpec genkey(String sankalpparab)  throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = sankalpparab.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKey= new SecretKeySpec(key ,"AES");
        return  secretKey;

    }



}
