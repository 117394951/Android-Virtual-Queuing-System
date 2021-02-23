package com.app.is4401.sociallysafe.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.Admin.Admin_Main;
import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.User.User_CreateProfile;
import com.google.firebase.auth.FirebaseAuth;

public class SegregationActivity extends AppCompatActivity {

    ImageView user, admin;
    Button btnSignOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segregation);

        user = findViewById(R.id.ivUser);
        admin = findViewById(R.id.ivAdmin);
        btnSignOut = findViewById(R.id.btnSignOut);

        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userHome = new Intent(SegregationActivity.this, User_CreateProfile.class);
                startActivity(userHome);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminHome = new Intent(SegregationActivity.this, Admin_Main.class);
                startActivity(adminHome);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(SegregationActivity.this, FirebaseLogin.class));
            }
        });
    }
}