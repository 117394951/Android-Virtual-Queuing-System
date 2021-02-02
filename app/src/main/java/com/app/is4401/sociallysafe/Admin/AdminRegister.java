package com.app.is4401.sociallysafe.Admin;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class AdminRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private String TAG = "***";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etAdminName);
        etEmail = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
        btnRegister = findViewById(R.id.btnAdminRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }



    private void registerUser() {
        final String email = etEmail.getText().toString().trim();
        final String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "button clicked");

        if(email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide valid email!");
            etEmail.requestFocus();
            return;
        }


        if(name.isEmpty()){
            etName.setError("Name is required!");
            etName.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            etPassword.setError("Password must be longer than 6 characters");
            etPassword.requestFocus();
            return;
        }


        /**
         * Authentication code adapted from youtube video https://www.youtube.com/watch?v=Z-RE1QuUWPg
         */
        //progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Admin admin = new Admin(name, email);

                            FirebaseDatabase.getInstance().getReference("Admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminRegister.this, "Admin has been registered successfully!", Toast.LENGTH_LONG).show();
                                        //progressBar.setVisibility(View.GONE);
                                        Log.d(TAG,"successful authorisation");
                                    } else {
                                        Toast.makeText(AdminRegister.this, "failed to register admin!", Toast.LENGTH_LONG).show();
                                        //progressBar.setVisibility(View.GONE);
                                        Log.d(TAG,"unsuccessful authorisation");

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AdminRegister.this, "Failed to register, try again!", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                            Log.d(TAG,"unsuccessful authorisation 2");

                        }
                    }
                });

    }
}
