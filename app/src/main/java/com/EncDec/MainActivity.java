package com.EncDec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public void decry(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        Intent intent=new Intent(getApplicationContext(),User2decrypt.class);
        //intent.putExtra("KEY",str);
        startActivity(intent);
    }

    public void enc(View view){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(30);
        Intent intent=new Intent(getApplicationContext(),Encrypt.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.alwusers:
                Intent intent=new Intent(getApplicationContext(),allowedlist.class);

                startActivity(intent);
                return true;
            case R.id.signout:mAuth.signOut();
                Intent intentt=new Intent(getApplicationContext(),loginsingnup.class);

                startActivity(intentt);
                finish();  //so that on back it doesnt come here again

            default:
                return false;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navy)));

       // Bundle extras = getIntent().getExtras();
        String tt="";
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);
        tt=sharedPreferences.getString("username","");

        setTitle(tt);


    }
}
