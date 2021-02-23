//package com.app.is4401.sociallysafe.User;
//
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.View;
//import android.webkit.MimeTypeMap;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.app.is4401.sociallysafe.R;
//import com.app.is4401.sociallysafe.model.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.util.ArrayList;
//
//public class User_Register extends AppCompatActivity {
//
//    private EditText etFName, etLName, etEmail, etMobile, etPassword;
//    ImageView ivLogo;
//    Button btnRegister, btnUpload;
//    private FirebaseAuth mAuth;
//    private static final int PICK_IMAGE = 1;
//    private Uri imageUri;
//    String getimageURL;
//    private StorageReference reference;
//    private DatabaseReference user_databaseReference;
//    private FirebaseUser user;
//    private String TAG = "* User Register";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_register);
//
//        etFName = findViewById(R.id.etUserFName);
//        etLName = findViewById(R.id.etUserLName);
//        etEmail = findViewById(R.id.etUserEmail);
//        etMobile = findViewById(R.id.etUserMobile);
//        etPassword = findViewById(R.id.etUserPassword);
//        btnRegister = findViewById(R.id.btnUserRegister);
//        ivLogo = findViewById(R.id.ivLogo);
//        btnUpload = findViewById(R.id.btnUploadLogo);
//
//        mAuth = FirebaseAuth.getInstance();
//        reference = FirebaseStorage.getInstance().getReference();
//        user_databaseReference = FirebaseDatabase.getInstance().getReference("User");
//        user = mAuth.getCurrentUser();
//
//
//        setLogoChooser();
//        setLogoUpload();
//        setbtnRegister();
//
//    }
//
//    private void setbtnRegister() {
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registerUser();
//            }
//        });
//    }
//
//    private void registerUser() {
//        final String email = etEmail.getText().toString().trim();
//        final String fName = etFName.getText().toString().trim();
//        final String lName = etLName.getText().toString().trim();
//        final String password = etPassword.getText().toString().trim();
//        final String mobile = etMobile.getText().toString().trim();
//
//
//        Log.d(TAG, "button clicked");
//
//        if (email.isEmpty()) {
//            etEmail.setError("Email is required!");
//            etEmail.requestFocus();
//            return;
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            etEmail.setError("Please provide valid email!");
//            etEmail.requestFocus();
//            return;
//        }
//
//        if (fName.isEmpty()) {
//            etFName.setError("First Name is required!");
//            etFName.requestFocus();
//            return;
//        }
//        if (lName.isEmpty()) {
//            etLName.setError("Last name is required!");
//            etLName.requestFocus();
//            return;
//        }
//
//
//        if (mobile.isEmpty()) {
//            etMobile.setError("Mobile number is required!");
//            etMobile.requestFocus();
//            return;
//        }
//
//        if (password.isEmpty()) {
//            etPassword.setError("Password is required!");
//            etPassword.requestFocus();
//            return;
//        }
//
//        if (password.length() < 6) {
//            etPassword.setError("Password must be longer than 6 characters");
//            etPassword.requestFocus();
//            return;
//        }
//
//
//        /**
//         * Authentication code adapted from youtube video https://www.youtube.com/watch?v=Z-RE1QuUWPg
//         */
//        //progressBar.setVisibility(View.VISIBLE);
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            String numGuests = "";
//                            Long time = null;
//
//                            User upload = new User(lName, email, mobile, getimageURL, numGuests, time);
//                            Toast.makeText(User_Register.this, "data inserted successfully", Toast.LENGTH_LONG).show();
//                            Log.d(TAG, "Data Inserted into the Database");
//
//                            ArrayList<String> testuser = upload.getUser();
//                            if (testuser != null) {
//                                for (String s : testuser) {
//                                    System.out.println("extra consumers: " + s);
//                                }
//
//                            }
//
//                            user_databaseReference.child(user.getUid()).setValue(upload);
//
//                            Toast.makeText(User_Register.this, "User info saved in database", Toast.LENGTH_LONG).show();
//
//
//                            FirebaseDatabase.getInstance().getReference("User")
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(User_Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
//                                        //progressBar.setVisibility(View.GONE);
//                                        Log.d(TAG, "successful authorisation");
//                                        finish();
//                                    } else {
//                                        Toast.makeText(User_Register.this, "failed to register user!", Toast.LENGTH_LONG).show();
//                                        //progressBar.setVisibility(View.GONE);
//                                        Log.d(TAG, "unsuccessful authorisation");
//
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(User_Register.this, "Failed to register, try again!", Toast.LENGTH_LONG).show();
//                            //progressBar.setVisibility(View.GONE);
//                            Log.d(TAG, "unsuccessful authorisation 2");
//
//                        }
//                    }
//                });
//    }
//    /**
//     * Code to upload image from gallery https://www.youtube.com/watch?v=b3BEa2drx4w
//     */
//
//    private void setLogoChooser() {
//        ivLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = new Intent();
//                gallery.setType("image/*");
//                gallery.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(gallery, PICK_IMAGE);
//            }
//
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            ivLogo.setImageURI(imageUri);
//        }else{
//            Log.d(TAG, "ERROR LOADING");
//        }
//    }
//
//    private void setLogoUpload() {
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (imageUri != null) {
//                    uploadToFirebase(imageUri);
//                } else {
//                    Toast.makeText(User_Register.this, "Please select image", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//    /**
//     * upload image to firebase storage adapted from https://www.youtube.com/watch?v=9-oa4OS7lUQ
//     *
//     */
//
//    private void uploadToFirebase(Uri uri) {
//
//        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
//
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                //progress bar bits
////                Handler handler = new Handler();
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        mProgressBar.setProgress(0);
////                    }
////                }, 500);
//
//                Log.d(TAG, "Upload successful");
//                Toast.makeText(User_Register.this, "Upload successful", Toast.LENGTH_LONG).show();
//
//                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!urlTask.isSuccessful()) ;
//                Uri downloadUrl = urlTask.getResult();
//                getimageURL = String.valueOf(downloadUrl);
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                //progressBar.setVisibility(View.VISIBLE);
////                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
////                mProgressBar.setProgress((int) progress);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(User_Register.this, "Uploading Failed", Toast.LENGTH_LONG).show();
//                //progressBar.setVisibility(View.INVISIBLE);
//
//            }
//        });
//    }
//
//    private String getFileExtension(Uri mUri) {
//
//        ContentResolver cr = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cr.getType(mUri));
//
//    }
//}