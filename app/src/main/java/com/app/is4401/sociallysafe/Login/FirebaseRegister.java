package com.app.is4401.sociallysafe.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRegister extends AppCompatActivity {

    private Button btnRegister;
    private EditText etUsername, etPassword;
    private FirebaseDatabase mRef;
    private FirebaseAuth firebaseAuth;
    private CheckBox cbAdmin, cbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_register);

        btnRegister = findViewById(R.id.btnRegister);
        etUsername = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        firebaseAuth=FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUsers();
            }
        });
    }

    private void addUsers() {
        String email=etUsername.getText().toString().trim();
        String pass=etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email ID", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();

                    Intent segregate= new Intent(FirebaseRegister.this, SegregationPostRegistration.class);
                    startActivity(segregate);


                }
                else {

                    if (etPassword.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Make sure password is at least 6 characters long", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Account already exists \n / Email ID invalid", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}