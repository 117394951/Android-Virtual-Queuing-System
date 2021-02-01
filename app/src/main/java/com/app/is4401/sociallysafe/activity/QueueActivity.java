//not used yet, here for reference purposes

package com.app.is4401.sociallysafe.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QueueActivity extends AppCompatActivity {
    private static final String TAG = "***** Queue Activity";


    DatabaseReference userRef, lastRef, firstRef;
    DataSnapshot dataSnapshot;
    long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        lastRef = FirebaseDatabase.getInstance().getReference().child("User").child(String.valueOf(maxId));

        maxId = dataSnapshot.getChildrenCount();

        firstRef = FirebaseDatabase.getInstance().getReference().child("User").child("1");

    }
}