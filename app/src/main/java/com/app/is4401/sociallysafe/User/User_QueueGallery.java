package com.app.is4401.sociallysafe.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class User_QueueGallery extends AppCompatActivity {

   private ImageView btnBack;
   private FirebaseAuth firebaseAuth;
   private FirebaseUser user;
   private String priority = "false";
   private DatabaseReference queueRef, custRef;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.user_queuegallery);

      firebaseAuth = FirebaseAuth.getInstance();
      user = firebaseAuth.getCurrentUser();

      getIncomingIntent(user);
      btnBack = findViewById(R.id.btnBack);
      btnBack.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(User_QueueGallery.this, MainActivity.class));
            finish();
         }
      });
   }


   private void getIncomingIntent(FirebaseUser user){

      if( getIntent().getExtras() != null){
         String imageUrl = getIntent().getStringExtra("image_url");
         String queueName = getIntent().getStringExtra("queue_name");
         String queueWaitingTime = getIntent().getStringExtra("queue_waiting_time");
         String queueNumPeople = String.valueOf(getIntent().getStringExtra("queue_num_people"));
         String queueLocation = getIntent().getStringExtra("location");
         String queueDesc = getIntent().getStringExtra("description");

         setGallery(user, imageUrl,queueName,queueWaitingTime,queueNumPeople,queueLocation,queueDesc);
      }
   }

   public void setGallery(final FirebaseUser user, String imageUrl, String queueName,
                           String queueTime, String queueNumPeople,
                           final String queueLocation, String queueDesc) {

      custRef = FirebaseDatabase.getInstance().getReference("Users");
      queueRef = FirebaseDatabase.getInstance().getReference("Queue");

      final TextView name = findViewById(R.id.stall_desc_name);
      name.setText(queueName);

      TextView waitingTime = findViewById(R.id.stall_desc_waiting_time);
      waitingTime.setText(queueTime);

      final TextView numPeople = findViewById(R.id.stall_desc_num_people);
      numPeople.setText(queueNumPeople);

      TextView description = findViewById(R.id.stall_desc);
      description.setText(queueDesc);

      TextView location = findViewById(R.id.stall_location);
      location.setText(queueLocation);

      ImageButton map = findViewById(R.id.mapButton);
      map.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme( "geo" ).opaquePart( "0.0" ).appendQueryParameter( "q" ,queueLocation);
            Uri geoLocation = builder.build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);
            if ( intent.resolveActivity(getPackageManager()) != null ){
               startActivity(intent);
            }
         }
      });

      ImageView image = findViewById(R.id.stall_image);
      new GetImageFromURL(image).execute(imageUrl);

      final Button joinQ = findViewById(R.id.joinQ);

      custRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.hasChild("adminId")){
               joinQ.setClickable(false);
               joinQ.setBackgroundResource(R.drawable.already_joined);
               joinQ.setVisibility(INVISIBLE);
            }else{
               joinQ.setClickable(true);
               joinQ.setBackgroundResource(R.drawable.primary_join_btn);
               joinQ.setVisibility(VISIBLE);
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
      });


      joinQ.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(final View v) {
            firebaseAuth = FirebaseAuth.getInstance();
            queueRef.orderByChild("name").equalTo(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for(DataSnapshot childSnapshot : snapshot.getChildren()){
                     Queue queue = childSnapshot.getValue(Queue.class);
                     String admin_id = childSnapshot.getKey();

                     if(queue.queue.contains(user.getUid())){
                        Toast.makeText(User_QueueGallery.this, "Already in queue", Toast.LENGTH_LONG).show();
                     }else {
                        showGuestDialog(queue, admin_id);
                     }
//                        //priority
//                        if(priority.equals("true")){
//                           queue.queue.add(0,user.getUid());
//                        }else{
//                           queue.queue.add(user.getUid());
//                        }
//                        queueRef.child(admin_id).setValue(queue);
//                        custRef.child(user.getUid()).child("adminId").setValue(admin_id);
                        numPeople.setText(String.valueOf(queue.getNumPeople()));

                     }


                  }
//               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
            });



         }
      });



   }


   private void showGuestDialog(final Queue queue, final String admin_id) {
      LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
      final View view = layoutInflater.inflate(R.layout.numguests_dialog, null);

      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(User_QueueGallery.this);
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
               queue.queue.add(0, user.getUid());

            } else {
               queue.queue.add(user.getUid());
            }

            queueRef.child(admin_id).setValue(queue);
            custRef.child(user.getUid()).child("adminId").setValue(admin_id);
            custRef.child(user.getUid()).child("numGuests").setValue(numGuests);

//            Log.d(TAG, "adding customer to queue");

            Toast.makeText(getApplicationContext(), "Joined Queue!", Toast.LENGTH_SHORT).show();

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