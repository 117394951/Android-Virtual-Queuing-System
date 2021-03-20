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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

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
import static android.view.View.VISIBLE;

public class Admin_QueueConfiguration extends Fragment {

    private Button btnCreateQ;
    TextView tvName, waitTime, queueSize, tv1, tv20,tv23, location, description;
    ImageView ivLogo, iv2, ivReview;
    Bitmap bitmap;
    SwitchCompat swOnline;
    private FirebaseUser user;
    private DatabaseReference imageRef, queueRef;
    private DatabaseReference childrenRef;
    private String userID;
    public int len;
    Queue queueInfo;
    Boolean online = true;

    public Admin_QueueConfiguration() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.admin__queue_configuration, container, false);

        queueRef = FirebaseDatabase.getInstance().getReference("Queue");
        imageRef = queueRef.child("imageUrl");
        user = FirebaseAuth.getInstance().getCurrentUser();

        ivReview = view.findViewById(R.id.ivReview);
        ivReview.setVisibility(INVISIBLE);
        iv2 = view.findViewById(R.id.imageView2);
        iv2.setVisibility(INVISIBLE);
        tv20 = view.findViewById(R.id.textView20);
        tv20.setVisibility(View.INVISIBLE);
        tv23 = view.findViewById(R.id.textView23);
        tv23.setVisibility(View.INVISIBLE);
        tv1 = view.findViewById(R.id.textview1);
        tv1.setVisibility(View.INVISIBLE);
        swOnline = view.findViewById(R.id.swOnline);
        swOnline.setVisibility(View.INVISIBLE);
        tvName = view.findViewById(R.id.tvBusinessName);
        tvName.setVisibility(View.INVISIBLE);
        ivLogo = view.findViewById(R.id.ivLogo1);
        ivLogo.setVisibility(View.INVISIBLE);
        waitTime = view.findViewById(R.id.tvMinutes);
        waitTime.setVisibility(View.INVISIBLE);
        queueSize = view.findViewById(R.id.tvQueueSize);
        queueSize.setVisibility(View.INVISIBLE);
        location = view.findViewById(R.id.etLocation);
        location.setVisibility(View.INVISIBLE);
        description = view.findViewById(R.id.etDescription);
        description.setVisibility(View.INVISIBLE);
        btnCreateQ = view.findViewById(R.id.btnCreateQ);


        //turn queue online or offline
        swOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    online = true;
                    Toast.makeText(getContext(), "Online", Toast.LENGTH_SHORT).show();
                    queueInfo.setOnline(true);

                } else {
                    Toast.makeText(getContext(), "Queue Offline", Toast.LENGTH_SHORT).show();
                    online = false;


                    queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Queue queueInfo = snapshot.getValue(Queue.class);
                            queueInfo.setOnline(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
                queueRef.child(user.getUid()).child("online").setValue(online);
            }
        });

//        queueRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child("Online").getValue().equals(true)) {
//                    swOnline.setChecked(true);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        //only having create queue button appear when the admin has no queue availible
        queueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user.getUid())) {
                    queueRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            tv20.setVisibility(View.VISIBLE);
                            tv23.setVisibility(View.VISIBLE);
                            tv1.setVisibility(View.VISIBLE);
                            tvName.setVisibility(View.VISIBLE);
                            waitTime.setVisibility(View.VISIBLE);
                            location.setVisibility(View.VISIBLE);
                            description.setVisibility(View.VISIBLE);
                            ivLogo.setVisibility(View.VISIBLE);
                            swOnline.setVisibility(View.VISIBLE);
                            queueSize.setVisibility(View.VISIBLE);



                            btnCreateQ.setVisibility(View.INVISIBLE);
                            queueInfo = snapshot.getValue(Queue.class);
                            String time = snapshot.child("avewaiting").getValue().toString();
                            waitTime.setText(time);
                            len = queueInfo.queue.size();
                            queueSize.setText(Integer.toString(len));

                            String name = queueInfo.getName();
                            String imageUrl = queueInfo.getimageUrl();
                            String locationQ = queueInfo.getLocation();
                            String descriptionQ = queueInfo.getDesc();

                            tvName.setText(name);
                            location.setText(locationQ);
                            description.setText(descriptionQ);

                            if (imageUrl.isEmpty()) {
                                Toast.makeText(getContext(), "error loading image", Toast.LENGTH_LONG).show();
                            } else {
                                //download from firebase reference
                                new GetImageFromURL(ivLogo).execute(imageUrl);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    btnCreateQ.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnCreateQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Configure your Queue!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), Admin_QueueActivity.class);
                startActivity(intent);
            }
        });


        return view;
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
