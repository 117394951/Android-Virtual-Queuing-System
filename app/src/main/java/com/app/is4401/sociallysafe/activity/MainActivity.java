package com.app.is4401.sociallysafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "***********";

    TextView tvFullName, tvEmail, tvMobile, tvNumGuest;
    Button btnJoin, btnLeave;
    DatabaseReference ref, leaveRef;
    long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Firebase connection Success", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Connection to Database Successful");


        btnJoin = findViewById(R.id.btnJoinQueue);
        btnLeave = findViewById(R.id.btnLeave);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvNumGuest = findViewById(R.id.tvNumGuests);

        //variable to connect to database
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        addValueEventListener();
        setBtnJoin();
        setBtnLeave();

        if(getIntent().hasExtra("FULLNAME") || getIntent().hasExtra("EMAIL") || getIntent().hasExtra("MOBILE") || getIntent().hasExtra("NUMGUESTS") || getIntent().hasExtra("TIME")) {

            String FullName = getIntent().getExtras().getString("FULLNAME");
            String Email = getIntent().getExtras().getString("EMAIL");
            String Mobile = getIntent().getExtras().getString("MOBILE");
            String NumGuest = getIntent().getExtras().getString("NUMGUESTS");

            tvFullName.setText(FullName);
            tvEmail.setText(Email);
            tvMobile.setText(Mobile);
            tvNumGuest.setText(NumGuest);
        }
    }

    private void deleteUser(){
        leaveRef = FirebaseDatabase.getInstance().getReference().child("User").child(String.valueOf(maxId));
        leaveRef.removeValue();
    }

    private void addValueEventListener(){
        //https://www.youtube.com/watch?v=r-g2R_COMqo&list=PLjMaHayx2gDG6bxZEoMuILMVv1Cv-6ua6&index=3 reference to database snapshot and maxId code
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxId = (dataSnapshot.getChildrenCount());
                Log.d(TAG, "msg:" + dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "msg: " + error);
            }
        });
    }

    private void setBtnJoin(){
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                Log.d(TAG, "UserActivity started");
            }
        });
    }

    private void setBtnLeave(){
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
                Log.d(TAG, "Data deleted from the database");
            }
        });
    }
}
