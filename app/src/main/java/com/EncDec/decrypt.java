package com.EncDec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.simbio.encryption.Encryption;

public class decrypt extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String decrypted; String todec; String user2=""; String user1;
    TextView textView; EditText editText;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void pastee(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
            editText.setText(textToPaste.toString());
           // todec=textToPaste.toString();
            Toast.makeText(this,"encrypted text pasted",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            return;
        }
    }
    public void dodec(View view){
        if(!(user1.equals(user2))) {
            if(isNetworkAvailable()==false)Toast.makeText(this, "internet unavailable",
                    Toast.LENGTH_SHORT).show();
            else {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference().child("usernames").child(user2).child("allowed users").child(user1).child("key");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {   //see it is triggered once only
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        if (value == null)
                            Toast.makeText(decrypt.this, "Not allowed", Toast.LENGTH_SHORT).show();
                        else {
                            try {
                                String key = value;
                                String salt = user2;
                                byte[] iv = new byte[16];


                                todec = editText.getText().toString();
                                if (editText.getText().toString().length() == 0)
                                    Toast.makeText(decrypt.this, "write something",
                                            Toast.LENGTH_SHORT).show();


                                Encryption encryption = Encryption.getDefault(key, salt, iv);

                                String decrypted = encryption.decryptOrNull(todec);
                                Log.i("txttt", decrypted);
                                textView.setText(decrypted);


                            } catch (Exception e) {
                                Log.i("exetion", e.toString());
                                if (editText.getText().toString().length() == 0)
                                    Toast.makeText(decrypt.this, "write something",
                                            Toast.LENGTH_SHORT).show();
                                else

                                    Toast.makeText(decrypt.this, "Cannot Decrypt", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.i("ee", "Failed to read value.", error.toException());
                    }
                });
            }
        }
        else{
            try {

                mAuth = FirebaseAuth.getInstance();

                String key = mAuth.getUid();
                String salt = user2;
                byte[] iv = new byte[16];


                todec = editText.getText().toString();
                if (editText.getText().toString().length() == 0)
                    Toast.makeText(decrypt.this, "write something",
                            Toast.LENGTH_SHORT).show();


                Encryption encryption = Encryption.getDefault(key, salt, iv);

                String decrypted = encryption.decryptOrNull(todec);
                Log.i("txttt", decrypted);
                textView.setText(decrypted);


            } catch (Exception e) {
                Log.i("exetion", e.toString());
                if (editText.getText().toString().length() == 0)
                    Toast.makeText(decrypt.this, "write something",
                            Toast.LENGTH_SHORT).show();
                else

                    Toast.makeText(decrypt.this, "Cannot Decrypt", Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);

        user1 = sharedPreferences.getString("username","");
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
             user2 = extras.getString("user2dec");
        }
        editText=(EditText)findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView6);
        editText.setLongClickable(false); //to disable auto paste option on long click
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navy)));
    }
}
