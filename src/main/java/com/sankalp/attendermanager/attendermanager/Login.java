package com.sankalp.attendermanager.attendermanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 234;
    private FirebaseAuth mAuth;
    private String TAG = "login ------------------";

    dataWire dataWire = new dataWire();

    FirebaseUser user;
    private String valVerify;
    String ver="0";
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final SharedPreferences sharedPreferences1=getSharedPreferences("data2", Context.MODE_PRIVATE);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

            final DatabaseReference versionchecek =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/AlphaControleCenter");


      user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            findViewById(R.id.sign_in_button).setEnabled(false);

           valueEventListener =  new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 ver = dataSnapshot.child("version").getValue().toString();
                    Toast.makeText(Login.this, dataSnapshot.child("reson").getValue().toString(), Toast.LENGTH_LONG).show();




            if (ver.equals(BuildConfig.VERSION_CODE+"")) {
                versionchecek.removeEventListener(valueEventListener);

                DatabaseReference isVerify = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/" + user.getUid() + "/verify");
                isVerify.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(Login.this, "verify" + dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                        valVerify = dataSnapshot.getValue().toString();
                        SharedPreferences sharedPreferences1 = getSharedPreferences("data2", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.putString("Email", valVerify);
                        editor.apply();
                        editor.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (sharedPreferences1.getString("Email", "NA").equals("0")) {

                    Toast.makeText(Login.this, "" + sharedPreferences1.getString("Email", "NA"), Toast.LENGTH_LONG).show();

                    Intent i = new Intent(Login.this, CollTechRegistration.class);
                    startActivity(i);

                } else if (!dataWire.getGoogleAccToken().isEmpty()) {
                    Intent i = new Intent(Login.this, QRGen.class);
                    startActivity(i);
                    Toast.makeText(Login.this, "" + dataWire.getGoogleAccToken().isEmpty() + dataWire.getGoogleAccToken(), Toast.LENGTH_LONG).show();

                }
            }else
            {

                AlertDialog.Builder alert=new AlertDialog.Builder(Login.this);
                alert.setTitle("Alert");
                alert.setMessage(dataSnapshot.child("reson").getValue().toString());

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            versionchecek.addValueEventListener(valueEventListener);
        }

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mAuth =  FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("904112809842-drobjjmpv4e4ggp14mug1ce8k2slgnv5.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }
        else {

            connected = false;
            startActivity(new Intent(Login.this,no_internet_alert.class));
        }


    }





    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        mAuth.fetchProvidersForEmail(acct.getEmail()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                boolean check = !task.getResult().getProviders().isEmpty();

                if (!check)
                {



                    Toast.makeText(getApplicationContext()," present",Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences1=getSharedPreferences("data2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences1.edit();
                    editor.putString("Email","0");
                    editor.apply();
                    editor.commit();
                    String a  = sharedPreferences1.getString("Email","NA");

                    Toast.makeText(Login.this, ""+a,Toast.LENGTH_LONG).show();
                    dataWire.setGoogleAccToken(a);
                }


            }
        });
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference tech = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers");


                            tech.child(user.getUid()+"").child("email").setValue(user.getEmail());
                            tech.child(user.getUid()+"").child("name").setValue(user.getDisplayName());
                            tech.child(user.getUid()+"").child("phone").setValue(user.getPhoneNumber());
                            tech.child(user.getUid()+"").child("ProfileImage").setValue(user.getPhotoUrl()+"");

                            SharedPreferences sharedPreferences1=getSharedPreferences("data2", Context.MODE_PRIVATE);
                            String a  = sharedPreferences1.getString("Email","NA");

                            Toast.makeText(Login.this, ""+a,Toast.LENGTH_LONG).show();
                            if (sharedPreferences1.getString("Email","NA").equals("NA")) {
                                DatabaseReference isVerify = FirebaseDatabase.getInstance().getReferenceFromUrl("https://attender-491df.firebaseio.com/users/teachers/"+user.getUid()+"/verify");
                                isVerify.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Toast.makeText(Login.this, "verify"+dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                                        valVerify = dataSnapshot.getValue().toString();
                                        SharedPreferences sharedPreferences12=getSharedPreferences("data2", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor= sharedPreferences12.edit();
                                        editor.putString("Email",valVerify);
                                        editor.apply();
                                        editor.commit();
                                        if (valVerify.equals("1")) {
                                            Intent i = new Intent(Login.this, QRGen.class);
                                            startActivity(i);
                                            Toast.makeText(Login.this, "AAA", Toast.LENGTH_LONG).show();
                                        }else
                                        {
                                            Intent i = new Intent(Login.this, CollTechRegistration.class);
                                            startActivity(i);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }else if (dataWire.getGoogleAccToken().equals("0"))
                            {
                                Intent i = new Intent(Login.this, CollTechRegistration.class);
                                startActivity(i);
                                Toast.makeText(Login.this, "000",Toast.LENGTH_LONG).show();
                                tech.child(user.getUid()).child("verify").setValue("0");

                            }
                            else
                            {
                                Intent i = new Intent(Login.this, QRGen.class);
                                startActivity(i);
                                Toast.makeText(Login.this, "111",Toast.LENGTH_LONG).show();

                            }
                            Toast.makeText(Login.this,"Hie "+user.getDisplayName()+":D " ,Toast.LENGTH_LONG).show();

                            Toast.makeText(Login.this, "hOLA YOU GOTCH",Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "hey  error is there ",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Toast.makeText(getBaseContext(),"Hie "+":D " ,Toast.LENGTH_LONG).show();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                Toast.makeText(Login.this, "hey hey hey  ",Toast.LENGTH_LONG).show();
                break;
            // ...
        }
    }
}
