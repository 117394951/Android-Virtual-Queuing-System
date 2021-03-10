package com.app.is4401.sociallysafe.Admin;

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
import com.app.is4401.sociallysafe.Model.Queue;
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

public class VerifyLogin extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
   String custId;
    private DatabaseReference queueRef, custRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setVisibility(View.INVISIBLE);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        mAuth = FirebaseAuth.getInstance();
        queueRef = FirebaseDatabase.getInstance().getReference("Queue");
        custRef=FirebaseDatabase.getInstance().getReference("Users");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

    }

    public void Login(){
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


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (getIntent().hasExtra("CUSTID")) {
                        custId = getIntent().getExtras().getString("CUSTID");
                    }


                    ValueEventListener valueEventListener = new ValueEventListener(){

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Queue queueInfo = snapshot.getValue(Queue.class);
                            queueInfo.queue.add(custId);
                            queueRef.child(user.getUid()).setValue(queueInfo);

                            DatabaseReference newCustRef = custRef.child(custId);
                            newCustRef.child("adminId").setValue(user.getUid());
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    queueRef.child(user.getUid()).addListenerForSingleValueEvent(valueEventListener);




                } else {
                    Toast.makeText(VerifyLogin.this, "Login failed! Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
