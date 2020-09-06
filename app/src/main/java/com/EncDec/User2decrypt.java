package com.EncDec;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User2decrypt extends AppCompatActivity {
    public void my(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);

       final String user1 = sharedPreferences.getString("username","");

// Vibrate for 400 milliseconds
        v.vibrate(30);
        findViewById(R.id.gifImageView3).animate().translationXBy(1500).setDuration(800);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), decrypt.class);
                intent.putExtra("user2dec", user1);
                //intent.putExtra("KEY",str);
                startActivity(intent);

            }
        }, 800);


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    ArrayList<String> allusers=new ArrayList<String>();
    public void proceed(View view){
        if(isNetworkAvailable()==false)Toast.makeText(this, "internet unavailable",
                Toast.LENGTH_SHORT).show();
        else {

            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView2);
            final String st = autoCompleteTextView.getText().toString();
            if (st.length() == 0) Toast.makeText(this, "write something",
                    Toast.LENGTH_SHORT).show();
            else {
                InputMethodManager inputManager = (InputMethodManager)           //to close keypad
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                findViewById(R.id.gifImageView3).animate().translationXBy(1500).setDuration(800);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), decrypt.class);
                        intent.putExtra("user2dec", st);
                        //intent.putExtra("KEY",str);
                        startActivity(intent);

                    }
                }, 800);

            }
        }

    }
    public void userlist(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("usernames");
        myRef.addValueEventListener(new ValueEventListener() {       //first called just when this listener attached
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allusers.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    allusers.add(name);
                }
                //Log.d("", "Value is: " + value);
                AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autoCompleteTextView2);
                ArrayAdapter<String> adp =new ArrayAdapter<String>(User2decrypt.this,android.R.layout.simple_list_item_1,allusers);
                autoCompleteTextView.setAdapter(adp);
                autoCompleteTextView.setThreshold(1);
                // myRef.removeEventListener(ValueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("hh", "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2decrypt);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navy)));
        userlist();
    }
}
