package com.app.is4401.sociallysafe.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.model.Queue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminManageQueue extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String> mNames;

    private FirebaseAuth firebaseAuth;
    private TextView queueLength, aveWaitingTime;

    private Button refresh, operate;

    private FirebaseUser user;
    private TextView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
    String cust_name1, cust_name2, cust_name3, cust_name4, cust_name5, cust_name6, cust_name7, cust_name8, cust_name9, cust_name10;
    String cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8, cc9, cc10;

    public int len;
    Queue queueInformation;

    private DatabaseReference queueDatabaseRef, customerDatabaseReference;

    public AdminManageQueue() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNames = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_manage_queue, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        queueDatabaseRef = FirebaseDatabase.getInstance().getReference("Queue");
        c1 = view.findViewById(R.id.c1);
        c2 = view.findViewById(R.id.c2);
        c3 = view.findViewById(R.id.c3);
        c4 = view.findViewById(R.id.c4);
        c5 = view.findViewById(R.id.c5);
        c6 = view.findViewById(R.id.c6);
        c7 = view.findViewById(R.id.c7);
        c8 = view.findViewById(R.id.c8);
        c9 = view.findViewById(R.id.c9);
        c10 = view.findViewById(R.id.c10);

        operate = view.findViewById(R.id.btnOperate);
        operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Admin_OperateQueue.class);
                startActivity(intent);
            }
        });

        refresh = view.findViewById(R.id.btnRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Refreshed!", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.detach(AdminManageQueue.this).attach(AdminManageQueue.this).commit();
            }
        });

        queueLength = view.findViewById(R.id.tvQueueLength);
        aveWaitingTime = view.findViewById(R.id.tvAveWait);
        queueLength.setText("-");
        aveWaitingTime.setText("-");

        queueDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    queueDatabaseRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String time = dataSnapshot.child("avewaiting").getValue().toString();
                            aveWaitingTime.setText(time);
                            queueInformation = dataSnapshot.getValue(Queue.class);
                            len = queueInformation.queue.size();
                            queueLength.setText(Integer.toString(len));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    queueDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                queueInformation = dataSnapshot.getValue(Queue.class);
                                len = queueInformation.queue.size();
                                queueLength.setText(Integer.toString(len));
                                System.out.println(len);

                                String customer1 = "";
                                String customer2 = "";
                                String customer3 = "";
                                String customer4 = "";
                                String customer5 = "";
                                String customer6 = "";
                                String customer7 = "";
                                String customer8 = "";
                                String customer9 = "";
                                String customer10 = "";

                                if (queueInformation.queue.size() > 0) {
                                    customer1 = queueInformation.queue.get(0);


                                    customerDatabaseReference.child(customer1).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name1 = dataSnapshot.child("name").getValue().toString();
                                                cc1 = cust_name1;
                                                c1.setText(cust_name1);
                                                //final String[] cust = new String[(cust_name1)];
                                            } else {
                                                c1.setText("No name entered");
                                                cc1 = "-1-";
                                            }

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                if (queueInformation.queue.size() > 1) {
                                    customer2 = queueInformation.queue.get(1);


                                    customerDatabaseReference.child(customer2).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name2 = dataSnapshot.child("name").getValue().toString();
                                                c2.setText(cust_name2);
                                                cc2 = cust_name2;

                                                System.out.println(cust_name2);

                                                //final String[] cust = new String[Integer.parseInt(cust_name2)];
                                            } else {
                                                c2.setText("No name entered");
                                                cc2 = "-2-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 2) {
                                    customer3 = queueInformation.queue.get(2);


                                    customerDatabaseReference.child(customer3).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name3 = dataSnapshot.child("name").getValue().toString();
                                                c3.setText(cust_name3);
                                                cc3 = cust_name3;

                                                System.out.println("myname" + cust_name3);

                                            } else {
                                                c3.setText("No name entered");
                                                cc3 = "-3-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 3) {
                                    customer4 = queueInformation.queue.get(3);


                                    customerDatabaseReference.child(customer4).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name4 = dataSnapshot.child("name").getValue().toString();
                                                c4.setText(cust_name4);
                                                cc4 = cust_name4;

                                                System.out.println("myname" + cust_name4);
                                            } else {
                                                c4.setText("No name entered");
                                                cc4 = "-4-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 4) {
                                    customer5 = queueInformation.queue.get(4);


                                    customerDatabaseReference.child(customer5).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name5 = dataSnapshot.child("name").getValue().toString();
                                                c5.setText(cust_name5);
                                                cc5 = cust_name5;

                                                System.out.println("myname" + cust_name5);
                                            } else {
                                                c5.setText("No name entered");
                                                cc5 = "-5-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 5) {
                                    customer6 = queueInformation.queue.get(5);


                                    customerDatabaseReference.child(customer6).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name6 = dataSnapshot.child("name").getValue().toString();
                                                c6.setText(cust_name6);
                                                cc6 = cust_name6;

                                                System.out.println("myname" + cust_name6);
                                            } else {
                                                c6.setText("No name entered");
                                                cc6 = "-6-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 6) {
                                    customer7 = queueInformation.queue.get(6);


                                    customerDatabaseReference.child(customer7).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name7 = dataSnapshot.child("name").getValue().toString();
                                                c7.setText(cust_name7);
                                                cc7 = cust_name7;

                                                System.out.println("myname" + cust_name7);
                                            } else {
                                                c7.setText("No name entered");
                                                cc7 = "-7-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 7) {
                                    customer8 = queueInformation.queue.get(7);


                                    customerDatabaseReference.child(customer8).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name8 = dataSnapshot.child("name").getValue().toString();
                                                c8.setText(cust_name8);
                                                cc8 = cust_name8;

                                                System.out.println("myname" + cust_name8);
                                            } else {
                                                c8.setText("No name entered");
                                                cc8 = "-8-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 8) {
                                    customer9 = queueInformation.queue.get(8);


                                    customerDatabaseReference.child(customer9).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name9 = dataSnapshot.child("name").getValue().toString();
                                                c9.setText(cust_name9);
                                                cc9 = cust_name9;

                                                System.out.println("myname" + cust_name9);
                                            } else {
                                                c9.setText("No name entered");
                                                cc9 = "-9-";
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                if (queueInformation.queue.size() > 9) {
                                    customer10 = queueInformation.queue.get(9);


                                    customerDatabaseReference.child(customer10).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.child("name").exists()) {
                                                cust_name10 = dataSnapshot.child("name").getValue().toString();
                                                c10.setText(cust_name10);
                                                cc10 = cust_name10;

                                                System.out.println("myname" + cust_name10);
                                            } else {
                                                c10.setText("No name entered");
                                                cc10 = "-10-";
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
