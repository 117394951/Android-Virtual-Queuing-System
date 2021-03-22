package com.app.is4401.sociallysafe.User;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.Admin.Admin_Main;
import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class User_CreateProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask;
    private Context mContext;
User userInfo;
    private TextView textViewUserEmail;
    private CheckBox prioritycheckbox;
    private Uri imageUri;
    private String imageURL = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    String getimageURL;
    private static final int PICK_IMAGE = 1;
    private static final String TAG = "User_ProfileInit";

    private ImageView btnBack;
    private EditText name, mobile;
    private ImageButton ivLogo;
    private Button save,  btnMyQueue;
    private CheckBox cbAdmin;
    protected static Boolean priority=false, admin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__create_profile);
        mContext = this;

        cbAdmin = findViewById(R.id.checkBoxAdmin);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        name= findViewById(R.id.nickname);
        mobile = findViewById(R.id.mobile);
        ivLogo = findViewById(R.id.init_profile_image);
        setLogoChooser();
        save=findViewById(R.id.btnsaveinfo);
        prioritycheckbox=findViewById(R.id.priorityCheckbox);
        textViewUserEmail= findViewById(R.id.textviewemail);
        textViewUserEmail.setText("Welcome "+user.getEmail());
        save = findViewById(R.id.btnsaveinfo);

        cbAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cbAdmin.setChecked(true);
                admin=true;
            }
        });

        prioritycheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prioritycheckbox.setChecked(true);
                priority=true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo() {



        if (imageUri != null && imageUri != user.getPhotoUrl()) {
            Log.d(TAG, "Processing mImageURI into storage.");
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri, mContext));

            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        final String getName = name.getText().toString().trim();
                        final String getMobile = mobile.getText().toString().trim();
                        String numGuests = "";
                        Long time = null;
                        final String getEmail = user.getEmail();


                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "Upload Customer Profile Image successful");

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri imageUri = urlTask.getResult();
                            imageURL = String.valueOf(imageUri);

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(getName)
                                    .setPhotoUri(Uri.parse(imageURL))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            User userInfo = new User(getName, getEmail, getMobile, getimageURL, numGuests, priority, admin);
                            databaseReference.child(user.getUid()).setValue(userInfo);
                            Toast.makeText(User_CreateProfile.this, "Information saved! Welcome to SociallySafe", Toast.LENGTH_LONG).show();


                            if (userInfo.isAdmin()) {
                                startActivity(new Intent(User_CreateProfile.this, Admin_Main.class));
                            } else {
                            Intent i = new Intent(User_CreateProfile.this, User_Main.class);
                            startActivity(i);
                        }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "Image Upload in progress");
                        }
                    });

        }
    }

    private void setLogoChooser() {
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(gallery, PICK_IMAGE);

                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(User_CreateProfile.this, "Please select image", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivLogo.setImageURI(imageUri);
        }else{
            Log.d(TAG, "ERROR LOADING");
        }
    }


    /**
     * upload image to firebase storage adapted from https://www.youtube.com/watch?v=9-oa4OS7lUQ
     *
     */

    private void uploadToFirebase(Uri uri) {

        final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri, mContext));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //progress bar bits
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProgressBar.setProgress(0);
//                    }
//                }, 500);

                Log.d(TAG, "Upload successful");
                Toast.makeText(User_CreateProfile.this, "Upload successful", Toast.LENGTH_LONG).show();

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                Uri downloadUrl = urlTask.getResult();
                getimageURL = String.valueOf(downloadUrl);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //progressBar.setVisibility(View.VISIBLE);
//                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                mProgressBar.setProgress((int) progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(User_CreateProfile.this, "Uploading Failed", Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    private String getFileExtension(Uri mUri, Context mContext) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}