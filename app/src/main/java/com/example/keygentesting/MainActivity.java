package com.example.keygentesting;

import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.KeyGenerator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KeyProtectionDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button generateButton = findViewById(R.id.encryptButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndProtectKey(v);
            }
        });
    }

    public void generateAndProtectKey(View view) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore"
            );

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                    "my_key_alias",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
            )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(true)  // Require user authentication
                    .setKeyValidityEnd(getNextYear())  // Key valid for a year
                    .setRandomizedEncryptionRequired(true);

            keyGenerator.init(builder.build());

            keyGenerator.generateKey();

            Log.d(TAG, "Key generation and protection successful.");

            Toast.makeText(this, "Key generation and protection successful.", Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException | NoSuchProviderException |
                 InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Key generation and protection failed: " + e.getMessage());
            Toast.makeText(this, "Key generation and protection failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Date getNextYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }
}
