package com.app.is4401.sociallysafe.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.is4401.sociallysafe.Model.Queue;
import com.app.is4401.sociallysafe.R;
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

public class MainActivity extends Fragment
        //implements ActivityCompat.OnRequestPermissionsResultCallback
{

    private static final String TAG = "* MainActivity";

    Button btnSearch;
    RecyclerView list;
    DatabaseReference queueRef, custRef;
    private FirebaseAuth mAuth;
    private  FirebaseUser user;


    public MainActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main, container, false);

        ArrayList<Queue> queue = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        custRef = FirebaseDatabase.getInstance().getReference().child("Users");

        btnSearch = view.findViewById(R.id.ivSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), Cust_Search.class));

            }
        });

//How to populate RecyclerViewAdpater with FirebaseDatabase
// https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/

        list = view.findViewById(R.id.recycler1);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        queueRef = FirebaseDatabase.getInstance().getReference().child("Queue");
        queueRef.keepSynced(true);
        FirebaseRecyclerOptions<Queue> options = new FirebaseRecyclerOptions.Builder<Queue>()
                .setQuery(queueRef.orderByChild("numPeople"), Queue.class)
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

                if (model.getOnline().equals(true)) {
                    holder.setDetails(getContext(), model.getName(), Integer.toString(total_wait_time), model.getimageUrl(),
                            Integer.toString(model.getNumPeople()),
                            model.getDesc(), model.getLocation());
                } else {

//                    https://stackoverflow.com/questions/41223413/how-to-hide-an-item-from-recycler-view-on-a-particular-condition
                    holder.view.setVisibility(View.GONE);
                    holder.view.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }


                Log.d(TAG, "adapter binded");
            }
        };

        list.setAdapter(adpater);


        return view;
//END
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
            user = firebaseAuth.getCurrentUser();
        }


        public void setDetails(Context ctx, final String queueName, final String avgWaiting, final String imageUrl, final String numPeople, final String desc, final String location) {
            final TextView name = view.findViewById(R.id.queueName);
            TextView waitTime = view.findViewById(R.id.queueWaitTime);
            final ImageView user_image = view.findViewById(R.id.queueImage);
            final TextView qNumPeople = view.findViewById(R.id.queueNumPeople);
            final Button joinQ = view.findViewById(R.id.joinQ_recycler);
            final TextView tvAlready = view.findViewById(R.id.tvAlready);

            name.setText(queueName);
            qNumPeople.setText(numPeople);
            waitTime.setText(avgWaiting);
            //from AdminHomePage.class
            new GetImageFromURL(user_image).execute(imageUrl);


// Intents, QuickLauncherApp tutorial, Michael Gleeson, IS447 module lecturer
// SCREENSHOTS OF CODE AVAILABLE UPON REQUEST
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
// END

            customerRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("adminId")) {
//                        joinQ.setClickable(false);
//                        joinQ.setBackgroundResource(R.drawable.already_joined);
//                        joinQ.setText("Already Q-ing!");
                        joinQ.setVisibility(View.INVISIBLE);
                    } else {
//                        joinQ.setClickable(true);
//                        joinQ.setBackgroundResource(R.drawable.primary_join_btn);
//                        joinQ.setText("Join Q!");
                        joinQ.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            //User priority code didnt use in the end
//            customerRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.child("priority").getValue() != null) {
//                        priority = snapshot.child("priority").getValue().toString();
//                    }
//                    if (snapshot.hasChild("adminId")) {
//                        joinQ.setClickable(false);
//                        joinQ.setBackgroundResource(R.drawable.already_joined);
//                    }
//                    else {
//                        joinQ.setClickable(true);
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

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                user = firebaseAuth.getCurrentUser();
                                Queue queueInfo = childSnapshot.getValue(Queue.class);

                                String adminId = childSnapshot.getKey();

                                if (queueInfo.queue.contains(user.getUid())) {
                                    Log.d("Join Queue", "Already in queue.");
                                    Toast.makeText(getContext(), "Already in this queue!", Toast.LENGTH_SHORT).show();
                                } else {
                                    showGuestDialog(queueInfo, adminId);
                                }
                                qNumPeople.setText(String.valueOf(queueInfo.getNumPeople()));
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

        private void showGuestDialog(final Queue queueInfo, final String adminId) {

// Alert Dialog pop-up
// Code adpater from Michael Gleeson code from Lecture in IS4447SQLNotesAdpater
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View view = layoutInflater.inflate(R.layout.numguests_dialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setView(view);

            final Spinner spinner = view.findViewById(R.id.spinner);


            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String numGuests = spinner.getSelectedItem().toString();


                    if (priority.equals("true")) {
                        queueInfo.queue.add(0, user.getUid());

                    } else {
                        queueInfo.queue.add(user.getUid());
                    }

                    queueRef.child(adminId).setValue(queueInfo);
                    customerRef.child(user.getUid()).child("adminId").setValue(adminId);
                    customerRef.child(user.getUid()).child("numGuests").setValue(numGuests);

                    Log.d(TAG, "adding customer to queue");

                    Toast.makeText(getContext(), "Joined Queue!", Toast.LENGTH_SHORT).show();

                    alertDialog.cancel();
                    alertDialog.hide();
                }
            });

            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                    alertDialog.hide();

                }
            });
        }
    }
//END

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
