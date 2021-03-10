package com.app.is4401.sociallysafe.Admin;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.Model.Queue;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Admin_QueueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private FirebaseAuth firebaseAuth;
    private EditText etName, etEmail, etPassword, etAddress, etDesc, etAveWait;
    private Button btnRegister, btnLogo;
    private ImageView ivLogo;
    private ProgressBar progressBar;
    private String TAG = "*** Admin Queue Configuration";
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    String getimageURL;
    private StorageReference mStorageRef;
    private DatabaseReference queue_databaseReference;
    private FirebaseUser user;
    private StorageTask mUploadTask;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG,"onCreate");


        //Firebase Instances
        firebaseAuth = FirebaseAuth.getInstance();
        queue_databaseReference = FirebaseDatabase.getInstance().getReference("Queue");
        mStorageRef = FirebaseStorage.getInstance().getReference("Admin");
        user = firebaseAuth.getCurrentUser();

        //UI Variables
        etAveWait = findViewById(R.id.etAveWait);
        etName = findViewById(R.id.etAdminName);
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

    private void registerUser(){
        final String name = etName.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();
        final String desc = etDesc.getText().toString().trim();
        final int aveWait =Integer.parseInt(etAveWait.getText().toString().trim());
        final Boolean online = true;



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




        //upload business details to queue to database
        Queue upload = new Queue(name, getimageURL, address, desc, aveWait, online );
        ArrayList<String> testqueue = upload.getQueue();
        if(testqueue != null){
            for(String s:testqueue){
                System.out.println("extra consumers: " +s);
            }
        }

        queue_databaseReference.child(user.getUid()).setValue(upload);
        queue_databaseReference.child(user.getUid()).child("Online").setValue("true");
        Log.d(TAG, "Merchant info saved");
        Toast.makeText(Admin_QueueActivity.this, "Merchant info saved in the database", Toast.LENGTH_SHORT).show();

        Intent intent= new Intent(Admin_QueueActivity.this, Admin_QueueConfigurated.class);
        startActivity(intent);
        finish();
    }

    private void setLogoChooser() {
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(gallery, PICK_IMAGE);
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

    private void setLogoUpload() {
        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(Admin_QueueActivity.this, "Please select image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /**
     * upload image to firebase storage adapted from https://www.youtube.com/watch?v=9-oa4OS7lUQ
     *
     */

    private void uploadToFirebase(Uri uri) {

        final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));

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
                Toast.makeText(Admin_QueueActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

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
                Toast.makeText(Admin_QueueActivity.this, "Uploading Failed", Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
