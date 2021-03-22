package com.app.is4401.sociallysafe.User;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class User_MyQueues extends Fragment {


    private FirebaseAuth firebaseAuth;
    private TextView queue_name, est_wait, num_people, position;
    private Button btnDropQ, btnRefresh;
    private FirebaseUser user;
    private ImageView ivLogo, ivProfile, btnBack;
    public int len;
    Queue queue;
    private DatabaseReference queueRef, custRef;
    private Switch notificationEnabled;
    private TextView notificationEnabledText;

    public User_MyQueues() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_myqueue, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        ivLogo = view.findViewById(R.id.imageView62);
        queue_name = view.findViewById(R.id.queue_name);
        btnDropQ = view.findViewById(R.id.btnDropQueue);
        btnRefresh = view.findViewById(R.id.buttonCurrentRefresh);
        est_wait = view.findViewById(R.id.waiting_time_data);
        num_people = view.findViewById(R.id.num_people_data);

        custRef = FirebaseDatabase.getInstance().getReference("Users");
        queueRef = FirebaseDatabase.getInstance().getReference("Queue");


        custRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("adminId")) {

                    queueRef.child(snapshot.child("adminId").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String admin_name = snapshot.child("name").getValue().toString();
                            queue_name.setText(admin_name);

                            queue = snapshot.getValue(Queue.class);
                            int queueSize = queue.getNumPeople();
                            int index = queue.queue.indexOf(user.getUid());
                            int av_wait_time = queue.getAvewaiting();
                            int waitTime = (index + 1) * av_wait_time;
                            String imageurl = queue.getimageUrl();
                            new GetImageFromURL(ivLogo).execute(imageurl);


                            est_wait.setText(Integer.toString(waitTime) + " mins");
                            num_people.setText(String.valueOf(queueSize));


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    System.out.println(snapshot.child("adminId").getValue());
                } else {
                    System.out.println("Hard Luck");
                }
                System.out.println(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(User_MyQueues.this).attach(User_MyQueues.this).commit();
            }
        });

        btnDropQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                custRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child("adminId").exists()) {
                            final String admin_id = snapshot.child("adminId").getValue().toString();
                            custRef.child(user.getUid()).child("adminId").removeValue();

                            queueRef.child(admin_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int index = queue.queue.indexOf(user.getUid());
                                    queue.queue.remove(index);
                                    System.out.println("index" + index);
                                    queueRef.child(admin_id).setValue(queue);

                                    queue_name.setText("Please join a Queue");
                                    est_wait.setText("No Queue Yet");
                                    num_people.setText("No Queue Yet");
                                    ivLogo.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                            Toast.makeText(getContext(), "You are not in any queue at the moment", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;

    }

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

