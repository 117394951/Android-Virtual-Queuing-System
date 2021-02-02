package com.app.is4401.sociallysafe.Admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AdminRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etName, etEmail, etPassword, etAddress, etDesc;
    private Button btnRegister, btnLogo;
    private ImageView ivLogo;
    private ProgressBar progressBar;
    private String TAG = "*** AdminRegister";
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    String getimageURL;

    private StorageReference reference;

    // Create a storage reference from the app
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseStorage.getInstance().getReference();

        etName = findViewById(R.id.etAdminName);
        etEmail = findViewById(R.id.etAdminEmail);
        etPassword = findViewById(R.id.etAdminPassword);
        btnRegister = findViewById(R.id.btnAdminRegister);
        etAddress = findViewById(R.id.etAddress);
        etDesc = findViewById(R.id.etDesc);
        btnLogo = findViewById(R.id.btnLogo);
        ivLogo = findViewById(R.id.ivLogo);

        setLogoChooser();
        setLogoUpload();
        setbtnRegister();
    }


    private void setbtnRegister() {
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
        final String password = etPassword.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String desc = etDesc.getText().toString().trim();
        //!!!???
        final String imageUrl = ivLogo.toString();


        Log.d(TAG, "button clicked");

        if (email.isEmpty()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide valid email!");
            etEmail.requestFocus();
            return;
        }


        if (name.isEmpty()) {
            etName.setError("Name is required!");
            etName.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            etAddress.setError("Address is required!");
            etAddress.requestFocus();
            return;
        }
        if (desc.isEmpty()) {
            etDesc.setError("Business Description is required!");
            etDesc.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be longer than 6 characters");
            etPassword.requestFocus();
            return;
        }


        /**
         * Authentication code adapted from youtube video https://www.youtube.com/watch?v=Z-RE1QuUWPg
         */
        //progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Admin admin = new Admin(name, email, address, desc, getimageURL);

                            FirebaseDatabase.getInstance().getReference("Admin")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminRegister.this, "Admin has been registered successfully!", Toast.LENGTH_LONG).show();
                                        //progressBar.setVisibility(View.GONE);
                                        Log.d(TAG, "successful authorisation");
                                    } else {
                                        Toast.makeText(AdminRegister.this, "failed to register admin!", Toast.LENGTH_LONG).show();
                                        //progressBar.setVisibility(View.GONE);
                                        Log.d(TAG, "unsuccessful authorisation");

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AdminRegister.this, "Failed to register, try again!", Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "unsuccessful authorisation 2");

                        }
                    }
                });
    }

    /**
     * Code to upload image from gallery https://www.youtube.com/watch?v=b3BEa2drx4w
     */

    private void setLogoChooser() {
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(gallery, PICK_IMAGE);


                //ignore
                //startActivityForResult(gallery, 2);
                //startActivityForResult(Intent.createChooser(gallery, "Select logo from Gallery."), PICK_IMAGE);
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


        //ignore
        //            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                ivLogo.setImageBitmap(bitmap);
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//
//        }

//        if (requestCode == 2 && requestCode == RESULT_OK && data != null) {
//            imageUri = data.getData();
    }

    private void setLogoUpload() {
        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(AdminRegister.this, "Please select image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /**
     * upload image to firebase storage adapted from https://www.youtube.com/watch?v=9-oa4OS7lUQ
     *
     */

    private void uploadToFirebase(Uri uri) {

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

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
                Toast.makeText(AdminRegister.this, "Upload successful", Toast.LENGTH_LONG).show();

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
                Toast.makeText(AdminRegister.this, "Uploading Failed", Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.INVISIBLE);

            }
        });
}

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}
