package com.app.is4401.sociallysafe.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.User.User_QueueGallery;
import com.app.is4401.sociallysafe.model.Queue;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/
     */


    private static final String TAG = "* MainActivity";

    Button btnJoin, btnLogin;
    RecyclerView list;
    DatabaseReference queueRef, baseRef;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Firebase connection Success", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Connection to Database Successful");



        list = findViewById(R.id.recycler1);
        list.setLayoutManager(new LinearLayoutManager(this));


        queueRef = FirebaseDatabase.getInstance().getReference().child("Queue");
        queueRef.keepSynced(true);


        FirebaseRecyclerOptions<Queue> options = new FirebaseRecyclerOptions.Builder<Queue>()
                .setQuery(queueRef, Queue.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter adpater = new FirebaseRecyclerAdapter<Queue, UserViewHolder>(options) {


            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cust_list_item, parent, false);
                Log.d(TAG, "layout inflated");
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Queue model) {
                int total_wait_time = model.getAvewaiting() * model.getNumPeople();

                holder.setDetails(getApplicationContext(), model.getName(), Integer.toString(total_wait_time), model.getimageUrl(),
                        Integer.toString(model.getNumPeople()),
                        model.getDesc(), model.getLocation());
                Log.d(TAG, "adapter binded");
            }
        };
        list.setAdapter(adpater);
        ArrayList<Queue> queue = new ArrayList<>();


    }




    public class UserViewHolder extends RecyclerView.ViewHolder {

        View view;

        private FirebaseAuth firebaseAuth;
        private FirebaseUser user;
        private FirebaseUser merchant;
        private String priority = "false";
        private DatabaseReference queueRef, customerRef;


        @Keep
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            customerRef = FirebaseDatabase.getInstance().getReference("Users");
            queueRef = FirebaseDatabase.getInstance().getReference("Queue");
            firebaseAuth = FirebaseAuth.getInstance();
        }


        public void setDetails(Context ctx, final String queueName, final String avgWaiting, final String imageUrl, final String numPeople, final String desc, final String location) {
            final TextView name = view.findViewById(R.id.queueName);
            TextView waitTime = view.findViewById(R.id.queueWaitTime);
            final ImageView user_image = view.findViewById(R.id.queueImage);
            final TextView qNumPeople = view.findViewById(R.id.queueNumPeople);
            Button joinQ = view.findViewById(R.id.joinQ_recycler);

            name.setText(queueName);
            qNumPeople.setText(numPeople);
            waitTime.setText(avgWaiting);
            //from AdminHomePage.class
            new GetImageFromURL(user_image).execute(imageUrl);


            //to bring details to specific queues page
            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), User_QueueGallery.class);
                    i.putExtra("image_url", imageUrl);
                    i.putExtra("location", location);
                    i.putExtra("queue_name", queueName);
                    i.putExtra("queue_waiting_time", avgWaiting);
                    i.putExtra("queue_num_people", numPeople);
                    i.putExtra("description", desc);
                    v.getContext().startActivity(i);
                }
            });


//            //User priority code
//            customerRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child("priority").getValue() != null) {
//                        priority = snapshot.child("priority").getValue().toString();
//                    }
//                    if (snapshot.hasChild("merchantID")) {
//                        joinQButton.setClickable(false);
//                        joinQButton.setBackgroundResource(R.drawable.already_join_button);
//                    }
//                    else {
//                        joinQButton.setClickable(true);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });



            //when join Q is clicked, user added to queue

            joinQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    Log.d(TAG, "Join queue button clicked");

                    queueRef.orderByChild("name").equalTo(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot childSnapshot: snapshot.getChildren()){
                                user = firebaseAuth.getCurrentUser();
                                Queue queueInfo = childSnapshot.getValue(Queue.class);
                                String adminId = childSnapshot.getKey();



                                if(queueInfo.queue.contains(user.getUid())){
                                    Log.d("Join Queue", "Already in queue.");
                                    Toast.makeText(MainActivity.this, "Already in this queue!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (priority.equals("true")) {
                                        queueInfo.queue.add(0, user.getUid());
                                    } else {
                                        queueInfo.queue.add(user.getUid());
                                    }


                                    queueRef.child(adminId).setValue(queueInfo);
                                    customerRef.child(user.getUid()).child("adminId").setValue(adminId);
                                    qNumPeople.setText(String.valueOf(queueInfo.getNumPeople()));
                                    Log.d(TAG, "adding customer to queue");

                                    Toast.makeText(MainActivity.this, "Joined Queue!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Database", "Error");
                        }
                    });






                }
            });


        }
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