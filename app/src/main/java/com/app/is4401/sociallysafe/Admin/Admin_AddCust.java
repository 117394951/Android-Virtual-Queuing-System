package com.app.is4401.sociallysafe.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.Model.User;
import com.app.is4401.sociallysafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_AddCust extends AppCompatActivity {

    EditText etName, etEmail, etMobile, etCode;
    Spinner spNumGuestCust;
    TextView tvKey;
    Button btnAdd;
    private DatabaseReference queueRef, custRef;
    private FirebaseAuth firebaseAuth, newCustAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_user);

        etName = findViewById(R.id.etNameCust);
        etEmail = findViewById(R.id.etEmailCust);
        etMobile = findViewById(R.id.etMobileCust);
        spNumGuestCust = findViewById(R.id.spNumGuestsCust);
        etCode = findViewById(R.id.etCode);
        btnAdd = findViewById(R.id.btnAdd);
        tvKey = findViewById(R.id.tvKey);

        setBtnAdd();


        firebaseAuth = FirebaseAuth.getInstance();


        //Linking my database inorder to create a new authorised user in-app, as another logged in user
        //https://stackoverflow.com/questions/37094631/get-the-pushed-id-for-specific-value-in-firebase-android#:~:text=In%20Java%20%2D%20Android%20Studio%2C%20you,written%20to%20the%20db...&text=use%20push()%20to%20create,the%20Key%20for%20that%20record
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://console.firebase.google.com/u/3/project/sociallysafe-f43b8/database/sociallysafe-f43b8/data")
                .setApiKey("AIzaSyAnWJr2g4bDKrJYRqb7hBfNnwtanS7n8XY")
                .setApplicationId("sociallysafe-f43b8").build();

        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "SociallySafe");
            //This gives new created user access to database
            newCustAuth = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            newCustAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance("SociallySafe"));

        }

        queueRef = FirebaseDatabase.getInstance().getReference("Queue");
        custRef = FirebaseDatabase.getInstance().getReference("Users");

    }

    private void setBtnAdd() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });
    }

    private void addCustomer() {
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String mobile = etMobile.getText().toString().trim();
        final String numGuests = spNumGuestCust.getSelectedItem().toString().trim();
        final String code = etCode.getText().toString().trim();


        if (name.isEmpty()) {
            etName.setError("Name is required!");
            etName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if (mobile.isEmpty()) {
            etMobile.setError("Mobile is required!");
            etMobile.requestFocus();
            return;
        }

        if (code.isEmpty()) {
            etCode.setError("Code is required!");
            etCode.requestFocus();
            return;
        }
        final String imageUrl = "";
        final Boolean priority = false;

        newCustAuth.createUserWithEmailAndPassword(email, code)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            String ex = task.getException().toString();
                            Toast.makeText(Admin_AddCust.this, "Registration Failed" + ex,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Admin_AddCust.this, "Registration Success",
                                    Toast.LENGTH_LONG).show();

                            //Technically logging in as a new user
                            FirebaseUser newCust;
                            newCust = newCustAuth.getCurrentUser();
                            final String newCustId = newCust.getUid();
                            tvKey.setText(newCustId);
                            Boolean admin = false;
                            String status = "In queue";
                            User upload = new User(name, email, mobile, imageUrl, numGuests, priority, admin, status);
                            custRef.child(newCustId).setValue(upload);
                            Toast.makeText(Admin_AddCust.this, "New Customer Joined Queue ", Toast.LENGTH_SHORT).show();

                            etCode.setText(newCustId);

                            //signing out as new user, to re-sign in the admin
                            newCustAuth.signOut();

                            final String newCustKey = tvKey.getText().toString();
                            Toast.makeText(Admin_AddCust.this, "Need to sign in to Verify Admin Id", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), VerifyLogin.class);
                            intent.putExtra("CUSTID", newCustKey);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
