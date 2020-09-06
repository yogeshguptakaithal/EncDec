package com.EncDec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import se.simbio.encryption.Encryption;

public class Encrypt extends AppCompatActivity {
    String encrypted;
    TextView textView; EditText editText;  private FirebaseAuth mAuth;
    public void ccopy(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("encrypted text", encrypted);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,"encrypted text copied",Toast.LENGTH_SHORT).show();
    }
    public void doenc(View view) {
        String key = mAuth.getUid();
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.EncDec", Context.MODE_PRIVATE);

        String salt = sharedPreferences.getString("username","");
        byte[] iv = new byte[16];
        Encryption encryption = Encryption.getDefault(key, salt, iv);
        if (editText.getText().toString().length() == 0) Toast.makeText(this, "write something",
                Toast.LENGTH_SHORT).show();
        else {
            encrypted = encryption.encryptOrNull(editText.getText().toString());
            Log.i("text", encrypted);
            textView.setText(encrypted);

            InputMethodManager inputManager = (InputMethodManager)           //to close keypad
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_encrypt);
        editText=(EditText)findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView6);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navy)));




    }
}
