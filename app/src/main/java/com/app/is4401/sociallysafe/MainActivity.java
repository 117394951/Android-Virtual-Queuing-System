package com.app.is4401.sociallysafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "***********";
    EditText etFirstName, etLastName, etEmail, etMobile;
    Spinner spNumGuests;
    TextView tvTime;
    Button btnJoin;
    DatabaseReference ref;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Firebase connection Success", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Connection to Database Successful");

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        spNumGuests = findViewById(R.id.spNumGuests);
        btnJoin = findViewById(R.id.btnJoinQueue);
        tvTime = findViewById(R.id.tvTime);

        //insert to database code reference from youtube video: https://www.youtube.com/watch?v=iy6WexahCdY
        user = new User();
        //variable to connect to database
        ref = FirebaseDatabase.getInstance().getReference().child("User");

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etFName = etFirstName.getText().toString();
                String etLName = etLastName.getText().toString();
                final String etEmailAdd = etEmail.getText().toString();
                String etMobileN = etMobile.getText().toString();

                if (etFName.isEmpty() || etLName.isEmpty() || etEmailAdd.isEmpty() || etMobileN.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Fill out All Details", Toast.LENGTH_SHORT).show();
                } else {
                    user.setFirstName(etFName);
                    user.setLastName(etLName);
                    user.setEmail(etEmailAdd);
                    user.setMobile(etMobileN);
                    user.setNumGuests(spNumGuests.getSelectedItem().toString());
                    user.setTime(getCurrentTimestamp());

                    ref.push().setValue(user);
                    Toast.makeText(MainActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Data Inserted into the Database");

                    //create a new intent to show confirmation activity
                    String fullName = (etFName + " " + etLName);

                    Intent startIntent = new Intent(MainActivity.this, Confirmation.class);
                    startIntent.putExtra("NAME", fullName);
                    startIntent.putExtra("EMAIL", etEmailAdd);
                    startIntent.putExtra("MOBILE", etMobileN);
                    startActivity(startIntent);
                    Log.d(TAG, "Details Sent by Intent to Confirmation Activity");
                }
            }
        });
    }

    //reference youtube video : https://www.youtube.com/watch?v=PqCpz5YtzF4
    private Long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
