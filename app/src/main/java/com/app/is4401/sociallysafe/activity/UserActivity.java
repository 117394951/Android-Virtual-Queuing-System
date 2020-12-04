package com.app.is4401.sociallysafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "*****UserActivity";

    DatabaseReference ref, leaveRef, retrieveRef;
    EditText etFirstName2, etLastName2, etEmail2, etMobile2;
    Spinner spNumGuests2;
    Button btnInsertUser;
    User user;
    long maxId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_user);

        etFirstName2 = findViewById(R.id.etFirstName2);
        etLastName2 = findViewById(R.id.etLastName2);
        etEmail2 = findViewById(R.id.etEmail2);
        etMobile2 = findViewById(R.id.etMobile2);
        spNumGuests2 = findViewById(R.id.spNumGuests2);
        btnInsertUser = findViewById(R.id.btnSave);

        ref = FirebaseDatabase.getInstance().getReference().child("User");
        addValueEventListener();

        btnInsertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etFName = etFirstName2.getText().toString();
                String etLName = etLastName2.getText().toString();
                String etEmailAdd = etEmail2.getText().toString();
                String etMobileN = etMobile2.getText().toString();

                if (etFName.isEmpty() || etLName.isEmpty() || etEmailAdd.isEmpty() || etMobileN.isEmpty()) {
                    Toast.makeText(UserActivity.this, "Please Fill out All Details", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Invalid attempt to submit");
                } else {
                    writeNewUser(etFName, etLName, etEmailAdd, etMobileN , spNumGuests2.getSelectedItem().toString());
                    Toast.makeText(UserActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Data Inserted into the Database");

                    //Intent to show details on previous activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("FULLNAME", etFName + " " + etLName);
                    intent.putExtra("EMAIL", etEmailAdd);
                    intent.putExtra("MOBILE", etMobileN);
                    intent.putExtra("NUMGUESTS", spNumGuests2.getSelectedItem().toString());
                    startActivity(intent);
                    Log.d(TAG,"sending user details by intent");

                    //Closes UserActivity class
                    finish();
                }
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
}
