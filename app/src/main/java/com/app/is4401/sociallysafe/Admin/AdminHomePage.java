package com.app.is4401.sociallysafe.Admin;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.Admin;
import com.app.is4401.sociallysafe.model.Queue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;

public class AdminHomePage extends Fragment {

    TextView tvName, waitTime, queueSize;
    ImageView ivLogo;
    Button btnEdit;
    Bitmap bitmap;
    EditText location, description;

    private FirebaseUser user;
    private DatabaseReference reference, queueRef;
    private DatabaseReference childrenRef;


    private String userID;
    public int len;

    Queue queueInformation;
    public AdminHomePage(){

    }

@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.admin_homepage, container, false);

    tvName = view.findViewById(R.id.tvBusinessName);
    ivLogo = view.findViewById(R.id.ivLogo1);
    btnEdit = view.findViewById(R.id.btnEdit);
    waitTime = view.findViewById(R.id.tvMinutes);
    queueSize = view.findViewById(R.id.tvQueueSize);
    location  = view.findViewById(R.id.etLocation);
    description = view.findViewById(R.id.etDescription);

    user = FirebaseAuth.getInstance().getCurrentUser();
    reference = FirebaseDatabase.getInstance().getReference("Admin");
    userID = user.getUid();
    childrenRef = reference.child("imageUrl");
    queueRef = FirebaseDatabase.getInstance().getReference("Queue");

    btnEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
// https://stackoverflow.com/questions/21028786/how-do-i-open-a-new-fragment-from-another-fragment#:~:text=You%20should%20create%20a%20function,inside%20fragment%20call%20this%20function.&text=Fragment%20fr%20%3D%20new%20Fragment_class()%3B,getFragmentManager()%3B%20FragmentTransaction%20fragmentTransaction%20%3D%20fm.
            // fragement to fragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, new Admin_Edit()).commit();

        }
    });

    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Admin adminProfile = snapshot.getValue(Admin.class);

            if (adminProfile != null) {
                String businessName = adminProfile.name;
                String imageURL = adminProfile.imageUrl;
                String businessAddress = adminProfile.address;
                String businessDescription = adminProfile.desc;




                tvName.setText(businessName);
                location.setText(businessAddress);
                description.setText(businessDescription);

                if (imageURL.isEmpty()) {
                    Toast.makeText(getContext(), "error loading image", Toast.LENGTH_LONG).show();
                } else {
                    //download from firebase reference
                    new GetImageFromURL(ivLogo).execute(imageURL);


                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

        }
    });


    queueRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Queue queue = snapshot.getValue(Queue.class);

            if (queue != null){
                String time = snapshot.child("avewaiting").getValue().toString();
                waitTime.setText(time);
                len = queue.queue.size();
                queueSize.setText(Integer.toString(len));
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    return view;
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    /**
     * code to download image
     * code adapted https://www.youtube.com/watch?v=Il3uB5u2pSA to upload image from uri
     */

    @SuppressLint("StaticFieldLeak")
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap>{

        ImageView imgV;

        public GetImageFromURL(ImageView imgV){
            this.imgV = imgV;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];

            bitmap = null;
            try{
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);

            }catch (Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }
    }
}