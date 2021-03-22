package com.app.is4401.sociallysafe.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.Admin.Admin_Main;
import com.app.is4401.sociallysafe.Model.User;
import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.User.User_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseLogin extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference custRef;
    User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, FirebaseRegister.class));

            }
        });
        //progress bar in video https://www.youtube.com/watch?v=KB2BIm_m1Os&list=LL&index=1

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
         * email verification code https://www.youtube.com/watch?v=15WRCpH-VG0&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=5
         **/
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                custRef = FirebaseDatabase.getInstance().getReference().child("Users");

                if(task.isSuccessful()){
                    //email verification and user authentication with log in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()){

                        custRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String isAdmin = snapshot.child("admin").getValue().toString();

                                if(isAdmin.equals("true")) {
                                    //redirect to profile
                                    startActivity(new Intent(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, Admin_Main.class));
                                    Toast.makeText(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, "Signing In", Toast.LENGTH_LONG).show();
                                }else{
                                    startActivity(new Intent(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, User_Main.class));
                                    Toast.makeText(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, "Signing In", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else{
                        user.sendEmailVerification();
                        Toast.makeText(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(com.app.is4401.sociallysafe.Login.FirebaseLogin.this, "Login failed! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}