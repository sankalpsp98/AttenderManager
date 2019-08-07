package com.sankalp.attendermanager.attendermanager;
import java.io.File;
import java.io.IOException;
import java.security.Signature;
import java.util.Calendar;

import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gospelware.liquidbutton.LiquidButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class PdfGeneration extends AppCompatActivity {



    private String thisYear;

    private int thisDate;
    private int thisMonthVALUE;
    private int thisYearINT;
    private int thisMonth;
    Calendar calendar = Calendar.getInstance();
    int tempyear;

    DatabaseReference pre ;
    static ArrayList<String> studRoll =  new ArrayList<>();
    dataWire dataWire1 = new dataWire();
    private String faculty = dataWire1.getFaculty1();
    private  String acdyear = dataWire1.getYear1().toLowerCase();
    private  String sem = dataWire1.getSem1();
    private  String sub = dataWire1.getSubject();
    private  String div = dataWire1.getDiv1();
    private  String lecture = dataWire1.getLecture1();
    private  String CollName = dataWire1.getCollege();

    LiquidButton liquidButton;
    private boolean gotBlaclist=true;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_generation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        thisYearINT = calendar.get(Calendar.YEAR);

        thisDate=calendar.get(Calendar.DATE);

        thisMonthVALUE=calendar.get(Calendar.MONTH);
        SimpleDateFormat month_date = new SimpleDateFormat("M");
        thisMonth = thisMonthVALUE+1;

        Log.d("//////////////>>>>",thisMonthVALUE+" "+thisMonth);


        liquidButton = findViewById(R.id.button);
        liquidButton.setAutoPlay(true);
        liquidButton.setEnabled(false);


        liquidButton.startPour();


        //databaseReference.keepSynced(true);
        if(thisMonth<=4)
        {
            tempyear =thisYearINT-1;
            thisYear= String.valueOf(tempyear +"-"+thisYearINT);

        }else if (thisMonth>4)
        {

            tempyear =thisYearINT+1;
            thisYear= String.valueOf(thisYearINT+"-"+tempyear);

        }
        pre = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/colleges/"+CollName+"/department/"+faculty+"/"+thisYear+"/"+acdyear+"/stud/blacklist");
        pre.orderByKey();
        Log.d("The Path",pre.toString());


        final Button button  =  findViewById(R.id.button5);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);

                final Document doc=new Document();
                final String outPath= Environment.getExternalStorageDirectory()+"/mypdf.pdf";
/*
                File f = new File(outPath);
                if(f.exists()){
                    f.delete();
                    Toast.makeText(PdfGeneration.this, "deleted", Toast.LENGTH_LONG).show();

                }
                */
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(outPath);
                    PdfWriter.getInstance(doc,fileOutputStream);
                    doc.setPageSize(PageSize.A4);
                    doc.open();
                    Paragraph paragraph = new Paragraph(new Chunk(dataWire.getCollege().toUpperCase()));
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    doc.add(paragraph);
                    doc.add(new Chunk("Class : "+faculty+"\n"+"Year : "+acdyear+"\n"+"Genrated by : "+user.getDisplayName()+" on "+thisDate+"/"+thisMonth+"/"+thisYear+"\n"+ "Signature :"));
                    LineSeparator lineSeparator = new LineSeparator();
                    lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
                    doc.add(new Paragraph(""));
                    doc.add(new Chunk(lineSeparator));



                    PdfPTable table = new PdfPTable(2);
                    PdfPCell cell = new PdfPCell(new Paragraph("                                                 "+"Student Blacklist"));
                    cell.setFixedHeight(30);
                    //cell.setBorder(Rectangle.ALIGN_CENTER);
                    cell.setColspan(2);
                    table.addCell(cell);


                    for(int aw = 0; aw < studRoll.size(); aw++){

                        PdfPCell cell1 = new PdfPCell(new Phrase(studRoll.get(aw)));
                        cell.setFixedHeight(30);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                        //cell.setBorder(Rectangle.);
                        table.addCell(cell1);


                        if(studRoll.size()-1==aw && aw+1%2!=0)
                        {
                            Log.d("StudRoll","Done"+studRoll.size());
                            PdfPCell cell11 = new PdfPCell(new Phrase(" "));
                            cell.setFixedHeight(30);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            //cell.setBorder(Rectangle.);
                            table.addCell(cell11);

                        }



                    }
                    doc.add(table);
                    doc.add(new Chunk(lineSeparator));
                    table.flushContent();
                    doc.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();


                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });



        pre.child(div).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String string = dataSnapshot.getKey().toString();
                studRoll.add(string);

                button.setEnabled(true);
                Toast.makeText(getApplicationContext(),""+ string,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),""+studRoll.size(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String string = dataSnapshot.getKey().toString();
                studRoll.add(string);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getKey().toString();
                studRoll.remove(string);
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



