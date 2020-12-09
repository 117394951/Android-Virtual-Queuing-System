package com.app.is4401.sociallysafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.LinkedListQueue;
import com.app.is4401.sociallysafe.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "*****UserActivity";

    DatabaseReference ref, leaveRef, retrieveRef;
    EditText etFirstName2, etLastName2, etEmail2, etMobile2;
    TextView tvWaitTimeEst;
    Spinner spNumGuests2;
    Button btnInsertUser;
    User user;
    long maxId = 0;
    int Highest = 0;
    int Wait =0;


    Random rand = new Random();

    int  time = rand.nextInt(30) + 1;


    LinkedListQueue<String> list = new LinkedListQueue<>();


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
                    writeNewUser(etFName, etLName, etEmailAdd, etMobileN, spNumGuests2.getSelectedItem().toString());
                    Toast.makeText(UserActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Data Inserted into the Database");

                    //Intent to show details on previous activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("FULLNAME", etFName + " " + etLName);
                    intent.putExtra("EMAIL", etEmailAdd);
                    intent.putExtra("MOBILE", etMobileN);
                    intent.putExtra("NUMGUESTS", spNumGuests2.getSelectedItem().toString());
                    //intent.putExtra("TIME", time);
                    startActivity(intent);
                    Log.d(TAG, "sending user details by intent");


                    //Add to LinkedList Queue
                    String result = etFName + ", " + etLName + ", " + etEmailAdd + ", " + etMobileN + ", " + time;
                    btnAddAction(result);

                    //Closes UserActivity class
                    finish();
                }
            }
        });
    }

    private void btnAddAction(String result) {
        int queueSize = list.size();
        list.enqueue(result);
        Wait++;
        Log.d(TAG, "User is number " + queueSize + " in the queue. The estimated wait time is " + time);
        showList();
    }

    private void showList() {
        String result;
        int s;
        int size = list.size();
        for (s = 0; s < size; s++) {
            result = (String)list.dequeue();
            Log.d(TAG, result);
        }
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
