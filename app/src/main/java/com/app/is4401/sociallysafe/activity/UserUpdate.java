package com.app.is4401.sociallysafe.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserUpdate extends AppCompatActivity {

    private static final String TAG = "* UserActivity";

    String _FNAME, _LNAME, _EMAIL, _MOBILE, _PARTY;
    EditText tvFirstName, tvLastName, tvEmail, tvMobile, tvNumGuest;
    TextView tvWaitTime;
    Button btnLeave, btnUpdate;
    DatabaseReference ref, lastRef, updateRef;
    long maxId = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        btnLeave = findViewById(R.id.btnLeave);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvNumGuest = findViewById(R.id.tvNumGuests);
        tvWaitTime = findViewById(R.id.tvWaitTime);

        //Creating a variable to Connect to my Database
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        updateRef = FirebaseDatabase.getInstance().getReference("User").child(String.valueOf(maxId));

        addValueEventListener();
        setBtnLeave();
        setBtnUpdate();

// Intents, QuickLauncherApp tutorial, Michael Gleeson, IS447 module lecturer
// SCREENSHOTS OF CODE AVAILABLE UPON REQUEST

        //getting variables passed by an intent from UserActivity
        if (getIntent().hasExtra("FIRST NAME") || getIntent().hasExtra("LAST NAME") || getIntent().hasExtra("EMAIL") || getIntent().hasExtra("MOBILE") || getIntent().hasExtra("NUMGUESTS") || getIntent().hasExtra("TIME")) {

            _FNAME = getIntent().getExtras().getString("FIRST NAME");
            _LNAME = getIntent().getExtras().getString("LAST NAME");
            _EMAIL = getIntent().getExtras().getString("EMAIL");
            _MOBILE = getIntent().getExtras().getString("MOBILE");
            _PARTY = getIntent().getExtras().getString("NUMGUESTS");
            String WaitTime = getIntent().getExtras().getString("TIME");
            String presentWaitTime = WaitTime + " minutes.";
//END

            tvFirstName.setText(_FNAME);
            tvLastName.setText(_LNAME);
            tvEmail.setText(_EMAIL);
            tvMobile.setText(_MOBILE);
            tvNumGuest.setText(_PARTY);
            tvWaitTime.setText(presentWaitTime);
        }
    }


//DataSnapshot Code below Referenced from a Youtube video by Educatree, at 5:00 minutes in. link https://www.youtube.com/watch?v=r-g2R_COMqo&list=PLjMaHayx2gDG6bxZEoMuILMVv1Cv-6ua6&index=3
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
//END

        private void setBtnLeave() {
            btnLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUser();
                    Log.d(TAG, "Data deleted from the database");
                    btnLeave.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserUpdate.this, "You have been removed from the waitlist.", Toast.LENGTH_LONG).show();

                    finish();
                }
            });
        }

    // remove data from database, a built in function, Firebase RealTime Database documents, https://firebase.google.com/docs/database/web/read-and-write
    private void deleteUser() {
            lastRef = FirebaseDatabase.getInstance().getReference().child("User").child(String.valueOf(maxId)); //points to max Id key of the database (last id)
            lastRef.removeValue();
        }


        private void setBtnUpdate(){
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUser();
                }
            });
        }

        private void updateUser() {
            if(isFNameChanged() || isLNameChanged() ||isEmailChanged()|| isMobileChanged()|| isPartyChanged()){
                Toast.makeText(this, "Data Updated", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this,"Data is the same", Toast.LENGTH_LONG).show();
        }


        /**
         * Code adapted from a Youtube video https://www.youtube.com/watch?v=L0IIMlJggns
         *
         *
         * TO DO: Name is split up to first and last name on database. correct this on main actvity
         *          Party Size needs to be checked again because of spinner possibly?
         *          clean up main activity
         *          hide buttons etc....
         */
        private boolean isPartyChanged() {
            //if user is trying to change their details, the intent will not be equal to the updated text
            if(!_PARTY.equals(tvNumGuest.getText().toString())){
                ref.child(String.valueOf(maxId)).child("NumGuests").setValue(tvNumGuest.getText().toString());
                return true;
            }
            else {
                return false;
            }
        }

        private boolean isMobileChanged() {
            //if user is trying to change their details, the intent will not be equal to the updated text
            if(!_MOBILE.equals(tvMobile.getText().toString())){

                ref.child(String.valueOf(maxId)).child("Mobile").setValue(tvMobile.getText().toString());
                return true;
            }
            else {
                return false;
            }
        }

        private boolean isEmailChanged() {
            //if user is trying to change their details, the intent will not be equal to the updated text
            if(!_EMAIL.equals(tvEmail.getText().toString())){

                ref.child(String.valueOf(maxId)).child("Email").setValue(tvEmail.getText().toString());
                return true;
            }
            else {
                return false;
            }
        }

        private boolean isFNameChanged() {

            //if user is trying to change their details, the intent will not be equal to the updated text
            if(!_FNAME.equals(tvFirstName.getText().toString())){

                ref.child(String.valueOf(maxId)).child("FirstName").setValue(tvFirstName.getText().toString());
                return true;
            }
            else {
                return false;
            }

        }

        private boolean isLNameChanged() {

            //if user is trying to change their details, the intent will not be equal to the updated text
            if(!_LNAME.equals(tvLastName.getText().toString())){

                ref.child(String.valueOf(maxId)).child("LastName").setValue(tvLastName.getText().toString());
                return true;
            }
            else {
                return false;
            }
        }
    }

