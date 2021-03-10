package com.app.is4401.sociallysafe.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.Model.Queue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Merc_QueueDisplay
//this is behind manage queue, operate button
//here is where you can put next customer etc . . . .
public class Admin_OperateQueue extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView queuedisplayrefreshButton;
    private Button btnNext;
    private FirebaseUser user;
    private TextView c1, c2, c3, c4;
    public int len;
    Queue queueInfo;
    String nextCustEmail, subject, message;
    private DatabaseReference queueDatabaseRef, customerDatabaseReference;
    private ImageView ivAdd;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__operate_queue);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        getSupportActionBar().hide();


        btnNext = findViewById(R.id.nextCustomer);
        queuedisplayrefreshButton = findViewById(R.id.queuedisplayrefresh);

        queuedisplayrefreshButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(Admin_OperateQueue.this, Admin_OperateQueue.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        final TextView queue_length = findViewById(R.id.queue_length);
        c1 = findViewById(R.id.cust1);
        c2 = findViewById(R.id.cust2);
        c3 = findViewById(R.id.cust3);
        c4 = findViewById(R.id.cust4);
        ivAdd = findViewById(R.id.ivAdd);

        queueDatabaseRef = FirebaseDatabase.getInstance().getReference("Queue");
        customerDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");


//        ivAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Admin_OperateQueue.this, Admin_AddCust.class);
//                startActivity(intent);
//
//            }
//        });



        queueDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    queueInfo = snapshot.getValue(Queue.class);
                    len = queueInfo.queue.size();
                    queue_length.setText(Integer.toString(len));

                    String customer1 = "";
                    String customer2 = "";
                    String customer3 = "";
                    String customer4 = "";

                    if (queueInfo.queue.size() > 0) {
                        customer1 = queueInfo.queue.get(0);


                        customerDatabaseReference.child(customer1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Name").exists()) {
                                    String cust_name = dataSnapshot.child("Name").getValue().toString();
                                    c1.setText(cust_name);
                                    System.out.println("myname" + cust_name);
                                } else {
                                    c1.setText("No name entered");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    if (queueInfo.queue.size() > 1) {
                        customer2 = queueInfo.queue.get(1);


                        customerDatabaseReference.child(customer2).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Name").exists()) {
                                    String cust_name2 = dataSnapshot.child("Name").getValue().toString();
                                    c2.setText(cust_name2);
                                    System.out.println("myname" + cust_name2);
                                } else {
                                    c2.setText("No name entered");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    if (queueInfo.queue.size() > 2) {
                        customer3 = queueInfo.queue.get(2);


                        customerDatabaseReference.child(customer3).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Name").exists()) {
                                    String cust_name3 = dataSnapshot.child("Name").getValue().toString();
                                    c3.setText(cust_name3);
                                    System.out.println("myname" + cust_name3);
                                } else {
                                    c3.setText("No name entered");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    if (queueInfo.queue.size() > 3) {
                        customer4 = queueInfo.queue.get(3);


                        customerDatabaseReference.child(customer4).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Name").exists()) {
                                    String cust_name4 = dataSnapshot.child("Name").getValue().toString();
                                    c4.setText(cust_name4);
                                    System.out.println("myname" + cust_name4);
                                } else {
                                    c4.setText("No name entered");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (len >= 1) {
                    System.out.println(queueInfo.queue.size());
                    final String current_user = queueInfo.queue.get(0);
                    queueInfo.queue.remove(0);
                    System.out.println(current_user);


                    customerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {


                                if (childSnapshot.getKey().equals(current_user)) {
                                    System.out.println(customerDatabaseReference.child(childSnapshot.getKey()).child("adminId").toString());
                                    customerDatabaseReference.child(childSnapshot.getKey()).child("adminId").removeValue();

                                } else {
                                    System.out.println("Error removing user from queue");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(Admin_OperateQueue.this, "Queue updated", Toast.LENGTH_SHORT).show();
                    queueDatabaseRef.child(user.getUid()).child("queue").setValue(queueInfo.queue);
                    len--;
                    queueDatabaseRef.child(user.getUid()).child("numPeople").setValue(len);
                    queue_length.setText(Integer.toString(len));
                } else {
                    Toast.makeText(Admin_OperateQueue.this, "You have no customers right now", Toast.LENGTH_SHORT).show();

                }

                queueDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            queueInfo = snapshot.getValue(Queue.class);
                            len = queueInfo.queue.size();
                            queue_length.setText(Integer.toString(len));

                            String customer1 = "";
                            String customer2 = "";
                            String customer3 = "";
                            String customer4 = "";

                            if (queueInfo.queue.size() > 0) {
                                customer1 = queueInfo.queue.get(0);


                                customerDatabaseReference.child(customer1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child("Name").exists()) {
                                            String cust_name = dataSnapshot.child("Name").getValue().toString();
                                            c1.setText(cust_name);
                                            System.out.println("myname" + cust_name);
                                        } else {
                                            c1.setText("No name entered");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else {
                                c1.setText("-1-");
                            }

                            if (queueInfo.queue.size() > 1) {
                                customer2 = queueInfo.queue.get(1);


                                customerDatabaseReference.child(customer2).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child("Name").exists()) {
                                            String cust_name2 = dataSnapshot.child("Name").getValue().toString();
                                            c2.setText(cust_name2);
                                            System.out.println("myname" + cust_name2);
                                        } else {
                                            c2.setText("No name entered");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else {
                                c2.setText("-2-");
                            }

                            if (queueInfo.queue.size() > 2) {
                                customer3 = queueInfo.queue.get(2);


                                customerDatabaseReference.child(customer3).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child("Name").exists()) {
                                            String cust_name3 = dataSnapshot.child("Name").getValue().toString();
                                            c3.setText(cust_name3);
                                            System.out.println("myname" + cust_name3);
                                        } else {
                                            c3.setText("No name entered");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else {
                                c3.setText("-3-");
                            }

                            if (queueInfo.queue.size() > 3) {
                                customer4 = queueInfo.queue.get(3);


                                customerDatabaseReference.child(customer4).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child("Name").exists()) {
                                            String cust_name4 = dataSnapshot.child("Name").getValue().toString();
                                            c4.setText(cust_name4);
                                            System.out.println("myname" + cust_name4);
                                        } else {
                                            c4.setText("No name entered");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else {

                                c4.setText("-4-");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




    }


}