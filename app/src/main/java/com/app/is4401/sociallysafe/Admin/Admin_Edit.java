package com.app.is4401.sociallysafe.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.is4401.sociallysafe.Login.FirebaseLogin;
import com.app.is4401.sociallysafe.Model.Queue;
import com.app.is4401.sociallysafe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

import static android.view.View.INVISIBLE;

public class Admin_Edit extends Fragment {

    Button btnDrop, btnSignOut, btnUpdate, btnCancel;
    Bitmap bitmap;
    Switch sw;
    EditText etName, etLocation, etDesc, etAvgTime;
    DatabaseReference queueRef;
    ImageView ivLogo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    public int len;
    Queue queueInfo;
    Boolean hasChild;

    public Admin_Edit() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_edit, container, false);

        btnUpdate = view.findViewById(R.id.btnUpdateProfile);
        btnCancel = view.findViewById(R.id.cust_profile_CancelButton);
        btnDrop = view.findViewById(R.id.btnDrop);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        etName = view.findViewById(R.id.etAdminName);
        etLocation = view.findViewById(R.id.etAddress);
        etDesc = view.findViewById(R.id.etDesc);
        etAvgTime = view.findViewById(R.id.etAveWait);
        ivLogo = view.findViewById(R.id.imageView2);


        queueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(user.getUid())) {
                    retrieveDetails();
                } else {
                    btnDrop.setVisibility(INVISIBLE);
                    etName.setText("Create a Queue!");
                    etName.setClickable(false);
                    etLocation.setText("Create a Queue!");
                    etLocation.setClickable(false);
                    etDesc.setText("Create a Queue!");
                    etDesc.setClickable(false);
                    etAvgTime.setText("Create a Queue!");
                    etAvgTime.setClickable(true);
                    Toast.makeText(getContext(), "Create a Queue", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setBtnUpdate();
        setBtnCancel();


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), FirebaseLogin.class);
                startActivity(intent);
            }
        });

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueRef.child(user.getUid()).removeValue();
                etName.setText("");
                etLocation.setText("");
                etDesc.setText("");
                etAvgTime.setText("");
                btnDrop.setVisibility(INVISIBLE);
                Toast.makeText(getContext(), "Queue dropped", Toast.LENGTH_SHORT);
            }
        });

        return view;

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
        if (isNameChanged() || isLocationChanged() || isDescriptionChanged() || isAvgWaitChanged()) {
            Toast.makeText(getContext(), "Data Updated", Toast.LENGTH_LONG).show();
            updateDetails();
        } else {
            Toast.makeText(getContext(), "Data is the same", Toast.LENGTH_LONG).show();
        }

    }

    void updateDetails() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String location = etLocation.getText().toString();
                String desc = etDesc.getText().toString();
                int aveWait = Integer.parseInt(etAvgTime.getText().toString());

                queueRef.child(user.getUid()).child("name").setValue(name);
                queueRef.child(user.getUid()).child("location").setValue(location);
                queueRef.child(user.getUid()).child("desc").setValue(desc);
                queueRef.child(user.getUid()).child("avewaiting").setValue(aveWait);



            }
        });
    }

    private boolean isAvgWaitChanged() {
        String _avgWait = queueRef.child(user.getUid()).child("avewaiting").toString();
        //if user is trying to change their details, the intent will not be equal to the updated text
        if (!_avgWait.equals(etAvgTime.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDescriptionChanged() {
        String _desc = queueRef.child(user.getUid()).child("desc").toString();
        //if user is trying to change their details, the intent will not be equal to the updated text
        if (!_desc.equals(etDesc.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLocationChanged() {
        String _location = queueRef.child(user.getUid()).child("location").toString();
        //if user is trying to change their details, the intent will not be equal to the updated text
        if (!_location.equals(etLocation.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        String _name = queueRef.child(user.getUid()).child("name").toString();
        //if user is trying to change their details, the intent will not be equal to the updated text
        if (!_name.equals(etName.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private void retrieveDetails() {
            queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Queue queueInfo = snapshot.getValue(Queue.class);

                    String name = queueInfo.getName();
                    String location = queueInfo.getLocation();
                    String desc = queueInfo.getDesc();
                    String avgWait = Integer.toString(queueInfo.getAvewaiting());
                    String imageUrl = queueInfo.getimageUrl();

                    if (imageUrl.isEmpty()) {
                        Toast.makeText(getContext(), "error loading image", Toast.LENGTH_LONG).show();
                    } else {
                        //download from firebase reference
                        new GetImageFromURL(ivLogo).execute(imageUrl);
                    }


                    if (name != null) {
                        etName.setText(name);
                    }
                    if (location != null) {
                        etLocation.setText(location);
                    }
                    if (desc != null) {
                        etDesc.setText(desc);
                    }
                    if (avgWait != null) {
                        etAvgTime.setText(avgWait);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        queueRef = FirebaseDatabase.getInstance().getReference("Queue");

    }

    /**
     * code to download image
     * code adapted https://www.youtube.com/watch?v=Il3uB5u2pSA to upload image from uri
     */

    @SuppressLint("StaticFieldLeak")
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

        ImageView imgV;

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
