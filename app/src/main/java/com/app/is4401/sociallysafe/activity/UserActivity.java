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

import java.util.Random;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "* UserActivity";

    DatabaseReference ref;
    EditText etFirstName2, etLastName2, etEmail2, etMobile2;
    Spinner spNumGuests2;
    Button btnInsertUser, btnUpdateUser;
    long maxId = 0;

    // Implementing a random wait time
    Random rand = new Random();
    int time = rand.nextInt(30) + 1;

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
        btnUpdateUser = findViewById(R.id.btnUpdate);

        ref = FirebaseDatabase.getInstance().getReference().child("User");
        addValueEventListener();

        setBtnInsert();
        //setBtnUpdate();
        
        
    }

//    private void setBtnUpdate() {
//        String etFName = etFirstName2.getText().toString();
//        String etLName = etLastName2.getText().toString();
//        String etEmailAdd = etEmail2.getText().toString();
//        String etMobileN = etMobile2.getText().toString();
//
//        if (etFName.isEmpty() || etLName.isEmpty() || etEmailAdd.isEmpty() || etMobileN.isEmpty()) { //error handling
//            Toast.makeText(UserActivity.this, "Please Fill out All Details", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Invalid attempt to submit");
//        } else {
//            updateUser(etFName, etLName, etEmailAdd, etMobileN, spNumGuests2.getSelectedItem().toString());
//            Toast.makeText(UserActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "Data Inserted into the Database");
//        }
//    }


    private void setBtnInsert(){
        btnInsertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etFName = etFirstName2.getText().toString();
                String etLName = etLastName2.getText().toString();
                String etEmailAdd = etEmail2.getText().toString();
                String etMobileN = etMobile2.getText().toString();

                if (etFName.isEmpty() || etLName.isEmpty() || etEmailAdd.isEmpty() || etMobileN.isEmpty()) { //error handling
                    Toast.makeText(UserActivity.this, "Please Fill out All Details", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Invalid attempt to submit");
                } else {
                    writeNewUser(etFName, etLName, etEmailAdd, etMobileN, spNumGuests2.getSelectedItem().toString());
                    Toast.makeText(UserActivity.this, "data inserted successfully", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Data Inserted into the Database");

                    // Intent to send details to previous activity (MainActivity)
                    Intent intent = new Intent(getApplicationContext(), UserUpdate.class);
                    intent.putExtra("FIRST NAME", etFName);
                    intent.putExtra("LAST NAME", etLName);
                    intent.putExtra("EMAIL", etEmailAdd);
                    intent.putExtra("MOBILE", etMobileN);
                    intent.putExtra("NUMGUESTS", spNumGuests2.getSelectedItem().toString());
                    intent.putExtra("TIME", String.valueOf(time));
                    startActivity(intent);
                    Log.d(TAG, "sending user details by intent");


                    Intent intent2 = new Intent(getApplicationContext(), UserUpdate.class);
                    startActivity(intent2);

                    finish();

                }
            }
        });
    }


//     old Linked List code, saving for a rainy day if needed

//    LinkedListQueue<String> list = new LinkedListQueue<>();

//    private void btnAddAction(String result) {
//        int queueSize = list.size();
//        list.enqueue(result);
//        Wait++;
//        Log.d(TAG, "User is number + queueSize + in the queue. The estimated wait time is " + time);
//        showList();
//    }
//
//    private void showList() {
//        String result;
//        int s;
//        int size = list.size();
//        for (s = 0; s < size; s++) {
//            result = (String) list.dequeue();
//            Log.d(TAG, result);
//        }
//    }

    // reference youtube video to get timestamp: https://www.youtube.com/watch?v=PqCpz5YtzF4
    private Long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    private void writeNewUser(String FirstName, String LastName, String Email, String Mobile, String NumGuests) {
        Long time = getCurrentTimestamp();
        User user = new User(FirstName, LastName, Email, Mobile, NumGuests, time);
        ref.child(String.valueOf(maxId + 1)).setValue(user);
    }

//    private void updateUser(String FirstName, String LastName, String Email, String Mobile, String NumGuests) {
//        Long time = getCurrentTimestamp();
//
//    }

    // code referenced in Main Activity
    private void addValueEventListener() {
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
