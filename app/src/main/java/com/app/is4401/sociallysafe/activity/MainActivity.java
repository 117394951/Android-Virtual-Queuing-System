package com.app.is4401.sociallysafe.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "***********";
    EditText etFirstName, etLastName, etEmail, etMobile;
    Spinner spNumGuests;
    TextView tvTime,tvFullName;
    Button btnJoin, btnLeave, btnRetrieve ;
    DatabaseReference ref, leaveRef, retrieveRef;
    User user;
    long maxId = 0;

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
        btnLeave = findViewById(R.id.btnLeave);
        // btnRetrieve = findViewById(R.id.btnRetrieve);

        //insert to database code reference from youtube video: https://www.youtube.com/watch?v=iy6WexahCdY
        //user = new User();
        //variable to connect to database
        ref = FirebaseDatabase.getInstance().getReference().child("User");

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

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etFName = etFirstName.getText().toString();
                String etLName = etLastName.getText().toString();
                String etEmailAdd = etEmail.getText().toString();
                String etMobileN = etMobile.getText().toString();

                if (etFName.isEmpty() || etLName.isEmpty() || etEmailAdd.isEmpty() || etMobileN.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Fill out All Details", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Invalid attempt to submit");
                } else {
                    writeNewUser(etFName, etLName, etEmailAdd, etMobileN , spNumGuests.getSelectedItem().toString());
                    Toast.makeText(MainActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Data Inserted into the Database");
                }
            }
        });

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
                Log.d(TAG, "Data deleted from the database");
            }
        });
    }

    //reference youtube video : https://www.youtube.com/watch?v=PqCpz5YtzF4
    private Long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    private void writeNewUser(String FirstName, String LastName, String Email, String Mobile, String NumGuests){
        Long time = getCurrentTimestamp();
        User user = new User(FirstName, LastName, Email, Mobile, NumGuests, time);
        ref.child(String.valueOf(maxId+1)).setValue(user);
    }

    private void deleteUser(){
        leaveRef = FirebaseDatabase.getInstance().getReference().child("User").child(String.valueOf(maxId));
        leaveRef.removeValue();
    }
}
