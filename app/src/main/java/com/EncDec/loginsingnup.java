package com.EncDec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginsingnup extends AppCompatActivity {
    String str; int sw=0;
    EditText ed1; EditText ed2;
    private FirebaseAuth mAuth; String email; String pass;
    public void forgot(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        if(mAuth.getCurrentUser()!=null)
        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
                //to clear userdata
                //Intent intent = getIntent();
                //finish();
                //startActivity(intent);
                finish();
            }
        });
       // ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
        //to clear userdata
       // Intent intent=new Intent(getApplicationContext(),loginsingnup.class);
        //intent.putExtra("KEY",str);
       // startActivity(intent);  //to restart activity
       finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void start(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        if(isNetworkAvailable()==false)Toast.makeText(this, "internet unavailable",
                Toast.LENGTH_SHORT).show();
        else {
            EditText e = (EditText) findViewById(R.id.username);
            str = e.getText().toString();
            if (str.length() == 0) Toast.makeText(this, "write something",
                    Toast.LENGTH_SHORT).show();
            else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference().child("usernames").child(str).child("userid");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {   //see it is triggered once only
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        if ( (sw == 0 && value==null)||(sw == 1 && value!=null &&value.equals(mAuth.getUid()))) {

                            SharedPreferences sharedPreferences = loginsingnup.this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("username", str).apply();

                            myRef.setValue(mAuth.getUid());
                            go();

                        } else if (sw == 0) {
                            Toast.makeText(loginsingnup.this, "username already taken", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(loginsingnup.this, "Not matches your username", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.i("ee", "Failed to read value.", error.toException());
                    }
                });
            }
        }

    }
    public void username(){
        findViewById(R.id.forgot).setVisibility(View.VISIBLE);
        //findViewById(R.id.imageView3).setVisibility(View.VISIBLE);
        findViewById(R.id.emailtext).setVisibility(View.GONE);
        findViewById(R.id.editText2).setVisibility(View.GONE);
        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        findViewById(R.id.bt).setVisibility(View.GONE);
        EditText e=(EditText)findViewById(R.id.username);
        e.setVisibility(View.VISIBLE);
        findViewById(R.id.go).setVisibility(View.VISIBLE);


    }
    public  void  signup(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //add in database
                            // Write a message to the database
// Write a message to the database
                            //String curid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            username();

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(loginsingnup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void loggin(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        if(isNetworkAvailable()==false)Toast.makeText(this, "internet unavailable",
                Toast.LENGTH_SHORT).show();
        else {
            ed1 = (EditText) findViewById(R.id.emailtext);
            email = ed1.getText().toString();
            ed2 = (EditText) findViewById(R.id.editText2);
            pass = ed2.getText().toString();
            if (email.length() == 0 || pass.length() == 0) Toast.makeText(this, "write something",
                    Toast.LENGTH_SHORT).show();
            else {
                mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i("h", "noj");
                                    sw = 1;
                                    username();

                                } else {
                                    sw = 0;
                                    signup();

                                }

                                // ...
                            }
                        });
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sw=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsingnup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.logf1);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navy)));
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            sw=1;
            username();
        }
        //pass should be long enough else it failssssssssssss;
    }
    public void go(){

        Button bt=(Button)findViewById(R.id.go);
        bt.setVisibility(View.GONE);
        //move to after login screen
        findViewById(R.id.gifImageView3).animate().translationXBy(1500).setDuration(800);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(loginsingnup.this,"logged in",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                //intent.putExtra("KEY",str);
                startActivity(intent);
                finish(); //so that on back button it not comes back here

            }
        }, 800);



    }
}
