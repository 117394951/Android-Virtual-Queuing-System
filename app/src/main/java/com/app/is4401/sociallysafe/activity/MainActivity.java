package com.app.is4401.sociallysafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "* MainActivity";

    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Firebase connection Success", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Connection to Database Successful");

        btnJoin = findViewById(R.id.btnJoinQueue);
        setBtnJoin();

    }

    private void setBtnJoin() {
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                Log.d(TAG, "UserActivity started");

            }
        });
    }
}
