package com.EncDec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class allowedlist extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ArrayList<String> allusers=new ArrayList<String>();
    ArrayList<String> alwusers=new ArrayList<String>();
    String user; String str;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void remove(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
        user=autoCompleteTextView.getText().toString();
        if(isNetworkAvailable()==false)Toast.makeText(this, "internet unavailable",
                Toast.LENGTH_SHORT).show();
        else
        if (user.length() == 0) Toast.makeText(this, "write something",
                Toast.LENGTH_SHORT).show();
        else {
            InputMethodManager inputManager = (InputMethodManager)           //to close keypad
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference myRef = database.getReference().child("usernames").child(str).child("allowed users").child(user).child("key");
            if (!(alwusers.contains(user)))
                Toast.makeText(this, "User not allowed already", Toast.LENGTH_SHORT).show();
            else {
                try {
                    myRef.setValue(null);
                    alwusers.remove(user);
                    ListView ls = (ListView) findViewById(R.id.list);
                    ArrayAdapter<String> lisadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alwusers);
                    ls.setAdapter(lisadapter);
                    Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                }


            }
        }

    }

    public void adduser(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
        user=autoCompleteTextView.getText().toString();
        if(isNetworkAvailable()==false)Toast.makeText(this, "internet unavailable",
                Toast.LENGTH_SHORT).show();
        else
        if (user.length() == 0) Toast.makeText(this, "write something",
                Toast.LENGTH_SHORT).show();
        else {
            InputMethodManager inputManager = (InputMethodManager)           //to close keypad
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (!(allusers.contains(user)))
                Toast.makeText(this, "user not exist", Toast.LENGTH_SHORT).show();
            else {
                final DatabaseReference myRef = database.getReference().child("usernames").child(str).child("allowed users").child(user).child("key");
                if (alwusers.contains(user))
                    Toast.makeText(this, "User allowed already", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        myRef.setValue(mAuth.getUid());
                        alwusers.add(user);
                        ListView ls = (ListView) findViewById(R.id.list);
                        ArrayAdapter<String> lisadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alwusers);
                        ls.setAdapter(lisadapter);
                        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                    }


                }
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
                AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autoCompleteTextView);
                ArrayAdapter<String> adp =new ArrayAdapter<String>(allowedlist.this,android.R.layout.simple_list_item_1,allusers);
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
        setContentView(R.layout.activity_allowedlist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navy)));
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);
         str=sharedPreferences.getString("username","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mmyRef = database.getReference().child("usernames").child(str).child("allowed users");
        mmyRef.addListenerForSingleValueEvent(new ValueEventListener() {   //see it is triggered once only
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    //Log.i("ygukyj",name);
                    if(name!=null||name!="")
                    alwusers.add(name);
                }ListView ls=(ListView)findViewById(R.id.list);
                ArrayAdapter<String> lisadapter =new ArrayAdapter<String>(allowedlist.this,android.R.layout.simple_list_item_1,alwusers);
                ls.setAdapter(lisadapter);
                userlist();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("ee", "Failed to read value.", error.toException());
            }
        });


    }
}
