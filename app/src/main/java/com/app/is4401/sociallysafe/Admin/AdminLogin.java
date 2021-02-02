package com.app.is4401.sociallysafe.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnRegister;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


        //progress bar in video https://www.youtube.com/watch?v=KB2BIm_m1Os&list=LL&index=1

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRegister:
                startActivity(new Intent(AdminLogin.this, AdminRegister.class));
                break;
            case R.id.btnLogin:
                Login();
                break;
        }
    }

    private void Login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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
        * sign in code https://www.youtube.com/watch?v=KB2BIm_m1Os&list=LL&index=1
         **/
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(AdminLogin.this, AdminActivity.class));
                    Toast.makeText(AdminLogin.this, "Signing In", Toast.LENGTH_LONG).show();


                }else{
                    Toast.makeText(AdminLogin.this, "Login failed! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
