package com.app.is4401.sociallysafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Confirmation extends AppCompatActivity {
    private static final String TAG = "***********";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String email = intent.getStringExtra("EMAIL");
        String mobile = intent.getStringExtra("MOBILE");
        Log.d(TAG, "Data Passed by Intent");

        //Initialise text view
        TextView tvFullName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvMobileNumber = findViewById(R.id.tvMobileNumber);

        //set Text
        tvFullName.setText(name);
        tvEmail.setText(email);
        tvMobileNumber.setText(mobile);
    }
}

