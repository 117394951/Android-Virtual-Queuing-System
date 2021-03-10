package com.app.is4401.sociallysafe.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.Login.FirebaseLogin;
import com.app.is4401.sociallysafe.Model.User;
import com.app.is4401.sociallysafe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.InputStream;

public class User_Profile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
     DatabaseReference custRef;

    private Button btnUpdate;
    private Button btnSignOut;
    private Button btnMyQueue;
    private Button btnCancel;
    private EditText email;
    private EditText password;
    private EditText mobile;
    private TextView name;

private ImageView btnBack;
    private ImageButton profilePic;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private String imageURL;

    private final String TAG = "CustSettings";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("Customers");
        custRef = FirebaseDatabase.getInstance().getReference("Users");

        btnBack = findViewById(R.id.btnBack);
        mobile = findViewById(R.id.cust_profile_mobile);
        btnMyQueue = findViewById(R.id.btnMyQueue);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        btnCancel = findViewById(R.id.cust_profile_CancelButton);
        btnSignOut = findViewById(R.id.cust_profile_switchModeButton);
        email = findViewById(R.id.cust_profile_email_editText);
        name = findViewById(R.id.cust_profile_nameOld);
        profilePic = findViewById(R.id.cust_profile_image_new);



        setBtnUpdate();
        setBtnCancel();
        retrieveDetails();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(User_Profile.this, FirebaseLogin.class));
                finish();
            }
        });

        btnMyQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile.this, User_MyQueues.class));
            }
        });




    }

    private void setBtnCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveDetails();
            }
        });
    }

    private void setBtnUpdate() {
        if(isEmailChanged() || isMobileChanged() ){
            Toast.makeText(this, "Data Updated", Toast.LENGTH_LONG).show();
            updateDetails();
        }
        else Toast.makeText(this,"Data is the same", Toast.LENGTH_LONG).show();
    }




    /**
     * Code adapted from a Youtube video https://www.youtube.com/watch?v=L0IIMlJggns
     */

    private boolean isMobileChanged() {

        String _MOBILE = custRef.child(user.getUid()).child("Mobile").toString();


        //if user is trying to change their details, the intent will not be equal to the updated text
        if(!_MOBILE.equals(mobile.getText().toString())){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEmailChanged() {

        String _EMAIL = custRef.child(user.getUid()).child("Email").toString();
        String accEmail = user.getEmail();

        //if user is trying to change their details, the intent will not be equal to the updated text
        if(!accEmail.equals(email.getText().toString())){
            return true;
        }
        else {
            return false;
        }
    }

    //retrieing, checking, uploading account details
    void retrieveDetails() {

        if (user.getEmail() != null) {
            email.setText(user.getEmail());
        }
        if (user.getDisplayName() != null) {
            name.setText(user.getDisplayName());
        }

        if (user.getPhotoUrl() != null) {
            String mImageUri = user.getPhotoUrl().toString();
            new GetImageFromURL(profilePic).execute(mImageUri);
        }

        custRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userInfo = snapshot.getValue(User.class);
                String mobileNumber = userInfo.getMobile();

                if(mobileNumber != null ){
                    mobile.setText(mobileNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    void updateDetails() {
       btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               user.updateEmail(email.getText().toString())
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Log.d(TAG, "User email address updated.");
                                   custRef.child(user.getUid()).child("Email").setValue(email.getText().toString());
                               }
                           }
                       });

               custRef.child(user.getUid()).child("Mobile").setValue(mobile.getText().toString());
           }
       });

    }

    ///**
// * code to download image
// * code adapted https://www.youtube.com/watch?v=Il3uB5u2pSA to upload image from uri
// */
//
    @SuppressLint("StaticFieldLeak")
    class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

        ImageView imgV;
        private Bitmap bitmap;

        public GetImageFromURL(ImageView imgV) {
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];

            bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }

    }
}