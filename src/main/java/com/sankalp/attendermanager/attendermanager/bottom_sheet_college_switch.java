package com.sankalp.attendermanager.attendermanager;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class bottom_sheet_college_switch extends BottomSheetDialogFragment {
    private final ArrayList<String> arrayList= new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/"+user.getUid()+"/colleges");
    DatabaseReference collegeValue = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/"+user.getUid());

    private BottomSheetListner mlistner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.bottom_sheet_college_switch,container,false);
        ListView listView = v.findViewById(R.id.listview);
        Button reg = v.findViewById(R.id.button2);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),HodTeacher.class));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int  position, long l) {
                collegeValue.child("college").setValue(parent.getItemAtPosition(position));
                startActivity(new Intent(getContext(),Login.class));

                dismiss();
               /* AlertDialog.Builder alert=new AlertDialog.Builder(v.getContext());
                alert.setTitle("Alert");
                alert.setMessage("Do You Want To Continue with "+parent.getItemAtPosition(position));
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
*/

            }
        });
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String string = dataSnapshot.getKey().toString();
                arrayList.add(string);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String string = dataSnapshot.getChildren().toString();
                arrayList.add(string);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                String string = dataSnapshot.getValue().toString();
                arrayList.remove(string);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        return v;
    }


    public  interface  BottomSheetListner
    {
        void onButtonClicked(String text);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mlistner = (BottomSheetListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement BottomSheetListner");

        }
    }
}
