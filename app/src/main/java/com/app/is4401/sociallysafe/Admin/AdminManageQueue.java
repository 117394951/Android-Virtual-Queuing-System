package com.app.is4401.sociallysafe.Admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.Model.Queue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class AdminManageQueue extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private RecyclerView recyclerView;
    private ArrayList<String> mNames, mImageUrls;

    private FirebaseAuth firebaseAuth;
    private TextView queueLength, aveWaitingTime;

    private Button refresh, operate;

    private final int REQUEST_READ_PHONE_STATE = 1;
    private final int REQUEST_CALL =2;
private ImageView ivAdd;
    private FirebaseUser user;
    private TextView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10;
    String cust_name1, cust_name2, cust_name3, cust_name4, cust_name5, cust_name6, cust_name7, cust_name8, cust_name9, cust_name10;
    String cc1, cc2, cc3, cc4, cc5, cc6, cc7, cc8, cc9, cc10;


    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10, iv11,iv12, iv13,iv14,iv15,iv16,iv17,iv18,iv19,iv20, iv21,iv22,iv23,iv24,iv25,iv26,iv27,iv28,iv29,iv30, iv31,iv32,iv33,iv34,iv35,iv36,iv37,iv38,iv39,iv40;

    TextView party1, party2, party3, party4, party5, party6 ,party7,party8,party9,party10;

    String mobile1 = "";
    String mobile2 = "";
    String mobile3 = "";
    String mobile4 = "";
    String mobile5 = "";
    String mobile6 = "";
    String mobile7 = "";
    String mobile8 = "";
    String mobile9 = "";
    String mobile10 = "";

    String id1 = "";
    String id2 = "";
    String id10 = "";
    String id3 = "";
    String id4 = "";
    String id5 = "";
    String id6 = "";
    String id7 = "";
    String id8 = "";
    String id9 = "";

    String p1 = "";
    String p2 = "";
    String p3 = "";
    String p4 = "";
    String p5 = "";
    String p6 = "";
    String p7 = "";
    String p8 = "";
    String p9 = "";
    String p10 = "";


    String message = "Good news! You are next in queue, please come to the restaurant!";

    public int len;
    Queue queueInformation;

    private DatabaseReference queueDatabaseRef, customerDatabaseReference;

    public AdminManageQueue() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNames = new ArrayList<>();
        mImageUrls = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_manage_queue, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        queueDatabaseRef = FirebaseDatabase.getInstance().getReference("Queue");
        customerDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        ivAdd = view.findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Admin_AddCust.class);
                startActivity(intent);

            }
        });

        //message visibility
        iv1 = view.findViewById(R.id.imageView21);
        iv1.setVisibility(INVISIBLE);
        iv2 = view.findViewById(R.id.imageView22);
        iv2.setVisibility(INVISIBLE);
        iv3 = view.findViewById(R.id.imageView23);
        iv3.setVisibility(INVISIBLE);
        iv4 = view.findViewById(R.id.imageView24);
        iv4.setVisibility(INVISIBLE);
        iv5 = view.findViewById(R.id.imageView25);
        iv5.setVisibility(INVISIBLE);
        iv6 = view.findViewById(R.id.imageView26);
        iv6.setVisibility(INVISIBLE);
        iv7 = view.findViewById(R.id.imageView27);
        iv7.setVisibility(INVISIBLE);
        iv8 = view.findViewById(R.id.imageView28);
        iv8.setVisibility(INVISIBLE);
        iv9 = view.findViewById(R.id.imageView29);
        iv9.setVisibility(INVISIBLE);
        iv10 = view.findViewById(R.id.imageView30);
        iv10.setVisibility(INVISIBLE);

        //phone visibility
        iv11 = view.findViewById(R.id.imageView31);
        iv11.setVisibility(INVISIBLE);
        iv12 = view.findViewById(R.id.imageView32);
        iv12.setVisibility(INVISIBLE);
        iv13 = view.findViewById(R.id.imageView33);
        iv13.setVisibility(INVISIBLE);
        iv14 = view.findViewById(R.id.imageView34);
        iv14.setVisibility(INVISIBLE);
        iv15 = view.findViewById(R.id.imageView35);
        iv15.setVisibility(INVISIBLE);
        iv16 = view.findViewById(R.id.imageView36);
        iv16.setVisibility(INVISIBLE);
        iv17 = view.findViewById(R.id.imageView37);
        iv17.setVisibility(INVISIBLE);
        iv18 = view.findViewById(R.id.imageView38);
        iv18.setVisibility(INVISIBLE);
        iv19 = view.findViewById(R.id.imageView39);
        iv19.setVisibility(INVISIBLE);
        iv20 = view.findViewById(R.id.imageView40);
        iv20.setVisibility(INVISIBLE);

        //remove user visibility
        //phone visibility
        iv21 = view.findViewById(R.id.imageView41);
        iv21.setVisibility(INVISIBLE);
        iv22 = view.findViewById(R.id.imageView42);
        iv22.setVisibility(INVISIBLE);
        iv23 = view.findViewById(R.id.imageView43);
        iv23.setVisibility(INVISIBLE);
        iv24 = view.findViewById(R.id.imageView44);
        iv24.setVisibility(INVISIBLE);
        iv25 = view.findViewById(R.id.imageView45);
        iv25.setVisibility(INVISIBLE);
        iv26 = view.findViewById(R.id.imageView46);
        iv26.setVisibility(INVISIBLE);
        iv27 = view.findViewById(R.id.imageView47);
        iv27.setVisibility(INVISIBLE);
        iv28 = view.findViewById(R.id.imageView48);
        iv28.setVisibility(INVISIBLE);
        iv29 = view.findViewById(R.id.imageView49);
        iv29.setVisibility(INVISIBLE);
        iv30 = view.findViewById(R.id.imageView50);
        iv30.setVisibility(INVISIBLE);

        party1 = view.findViewById(R.id.party1);
        iv31 = view.findViewById(R.id.imageView51);
        iv31.setVisibility(INVISIBLE);
        party2 = view.findViewById(R.id.party2);
        iv32 = view.findViewById(R.id.imageView52);
        iv32.setVisibility(INVISIBLE);
        party3 = view.findViewById(R.id.party3);
        iv33 = view.findViewById(R.id.imageView53);
        iv33.setVisibility(INVISIBLE);
        party4 = view.findViewById(R.id.party4);
        iv34 = view.findViewById(R.id.imageView54);
        iv34.setVisibility(INVISIBLE);
        party5 = view.findViewById(R.id.party5);
        iv35 = view.findViewById(R.id.imageView55);
        iv35.setVisibility(INVISIBLE);
        party6 = view.findViewById(R.id.party6);
        iv36 = view.findViewById(R.id.imageView56);
        iv36.setVisibility(INVISIBLE);
        party7 = view.findViewById(R.id.party7);
        iv37 = view.findViewById(R.id.imageView57);
        iv37.setVisibility(INVISIBLE);
        party8 = view.findViewById(R.id.party8);
        iv38 = view.findViewById(R.id.imageView58);
        iv38.setVisibility(INVISIBLE);
        party9 = view.findViewById(R.id.party9);
        iv39 = view.findViewById(R.id.imageView59);
        iv39.setVisibility(INVISIBLE);
        party10 = view.findViewById(R.id.party10);
        iv40 = view.findViewById(R.id.imageView60);
        iv40.setVisibility(INVISIBLE);




        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE},
                PackageManager.PERMISSION_GRANTED);

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }


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
                                            iv1.setVisibility(INVISIBLE);
                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name1 = dataSnapshot.child("Name").getValue().toString();
                                                mobile1 = dataSnapshot.child("Mobile").getValue().toString();

                                                p1 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv31.setVisibility(VISIBLE);
                                                party1.setText(p1);


                                                iv21.setVisibility(VISIBLE);
                                                id1 = dataSnapshot.getKey().toString();
                                                iv21.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id1);
                                                    }
                                                });

                                                cc1 = cust_name1;

                                                iv1.setVisibility(VISIBLE);
                                                iv1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile1);
                                                    }
                                                });
                                                iv11.setVisibility(VISIBLE);
                                                iv11.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile1);
                                                    }
                                                });


                                                c1.setText(cust_name1);
                                                System.out.println(cust_name1);

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



                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name2 = dataSnapshot.child("Name").getValue().toString();
                                                c2.setText(cust_name2);
                                                cc2 = cust_name2;
                                                mobile2 = dataSnapshot.child("Mobile").getValue().toString();

                                                iv22.setVisibility(VISIBLE);
                                                id2 = dataSnapshot.getKey().toString();
                                                iv22.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id2);
                                                    }
                                                });

                                                p2 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv32.setVisibility(VISIBLE);
                                                party2.setText(p2);

                                                System.out.println(cust_name2);
                                                iv2.setVisibility(VISIBLE);
                                                iv12.setVisibility(VISIBLE);
                                                iv12.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile2);
                                                    }
                                                });


                                                iv2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile2);
                                                    }
                                                });

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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name3 = dataSnapshot.child("Name").getValue().toString();
                                                mobile3 = dataSnapshot.child("Mobile").getValue().toString();
                                                c3.setText(cust_name3);
                                                cc3 = cust_name3;
                                                iv3.setVisibility(VISIBLE);
                                                iv13.setVisibility(VISIBLE);
                                                iv13.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile3);
                                                    }
                                                });
                                                iv23.setVisibility(VISIBLE);
                                                id3 = dataSnapshot.getKey().toString();
                                                iv23.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id3);
                                                    }
                                                });

                                                iv3.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile3);
                                                    }
                                                });


                                                p3 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv33.setVisibility(VISIBLE);
                                                party3.setText(p3);

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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name4 = dataSnapshot.child("Name").getValue().toString();
                                                c4.setText(cust_name4);
                                                mobile4 = dataSnapshot.child("Mobile").getValue().toString();
                                                cc4 = cust_name4;
                                                iv4.setVisibility(VISIBLE);
                                                iv4.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile4);
                                                    }
                                                });
                                                iv14.setVisibility(VISIBLE);
                                                iv14.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile4);
                                                    }
                                                });
                                                iv24.setVisibility(VISIBLE);
                                                id4= dataSnapshot.getKey().toString();
                                                iv24.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id4);
                                                    }
                                                });

                                                p4 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv34.setVisibility(VISIBLE);
                                                party4.setText(p4);

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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name5 = dataSnapshot.child("Name").getValue().toString();
                                                c5.setText(cust_name5);
                                                cc5 = cust_name5;
                                                iv5.setVisibility(VISIBLE);
                                                mobile5 = dataSnapshot.child("Mobile").getValue().toString();
                                                iv5.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile5);
                                                    }
                                                });
                                                iv15.setVisibility(VISIBLE);
                                                iv15.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile5);
                                                    }
                                                });

                                                p5 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv35.setVisibility(VISIBLE);
                                                party5.setText(p5);

                                                iv25.setVisibility(VISIBLE);
                                                id5 = dataSnapshot.getKey().toString();
                                                iv25.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id5);
                                                    }
                                                });
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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name6 = dataSnapshot.child("Name").getValue().toString();
                                                c6.setText(cust_name6);
                                                cc6 = cust_name6;
                                                iv6.setVisibility(VISIBLE);
                                                mobile6 = dataSnapshot.child("Mobile").getValue().toString();
                                                iv6.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile6);
                                                    }
                                                });

                                                p6 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv36.setVisibility(VISIBLE);
                                                party6.setText(p6);

                                                iv26.setVisibility(VISIBLE);
                                                id6 = dataSnapshot.getKey().toString();
                                                iv26.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id6);
                                                    }
                                                });

                                                iv16.setVisibility(VISIBLE);
                                                iv16.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile6);
                                                    }
                                                });
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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name7 = dataSnapshot.child("Name").getValue().toString();
                                                c7.setText(cust_name7);
                                                cc7 = cust_name7;
                                                iv7.setVisibility(VISIBLE);
                                                mobile7 = dataSnapshot.child("Mobile").getValue().toString();
                                                iv7.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile7);
                                                    }
                                                });

                                                p7 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv37.setVisibility(VISIBLE);
                                                party7.setText(p7);

                                                iv17.setVisibility(VISIBLE);
                                                iv17.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile7);
                                                    }
                                                });

                                                iv27.setVisibility(VISIBLE);
                                                id7= dataSnapshot.getKey().toString();
                                                iv27.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id7);
                                                    }
                                                });

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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name8 = dataSnapshot.child("Name").getValue().toString();
                                                c8.setText(cust_name8);
                                                cc8 = cust_name8;
                                                iv8.setVisibility(VISIBLE);
                                                mobile8 = dataSnapshot.child("Mobile").getValue().toString();
                                                iv8.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile8);
                                                    }
                                                });

                                                p8 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv38.setVisibility(VISIBLE);
                                                party8.setText(p8);


                                                iv18.setVisibility(VISIBLE);
                                                iv18.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile8);
                                                    }
                                                });

                                                iv28.setVisibility(VISIBLE);
                                                id8 = dataSnapshot.getKey().toString();
                                                iv28.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id8);
                                                    }
                                                });

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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name9 = dataSnapshot.child("Name").getValue().toString();
                                                c9.setText(cust_name9);
                                                cc9 = cust_name9;
                                                iv9.setVisibility(VISIBLE);
                                                mobile9 = dataSnapshot.child("Mobile").getValue().toString();
                                                iv9.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile9);
                                                    }
                                                });
                                                iv19.setVisibility(VISIBLE);
                                                iv19.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile9);
                                                    }
                                                });

                                                p9 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv39.setVisibility(VISIBLE);
                                                party9.setText(p9);

                                                iv29.setVisibility(VISIBLE);
                                                id9 = dataSnapshot.getKey().toString();
                                                iv29.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id9);
                                                    }
                                                });

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

                                            if (dataSnapshot.child("Name").exists()) {
                                                cust_name10 = dataSnapshot.child("Name").getValue().toString();
                                                c10.setText(cust_name10);
                                                cc10 = cust_name10;
                                                iv10.setVisibility(VISIBLE);
                                                mobile10 = dataSnapshot.child("Mobile").getValue().toString();
                                                iv10.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        SendSMS(mobile10);
                                                    }
                                                });

                                                p10 = dataSnapshot.child("NumGuests").getValue().toString();
                                                iv40.setVisibility(VISIBLE);
                                                party10.setText(p10);

                                                iv30.setVisibility(VISIBLE);
                                                id10 = dataSnapshot.getKey().toString();
                                                iv30.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        removeUser(id10);
                                                    }
                                                });
                                                iv20.setVisibility(VISIBLE);
                                                iv20.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        callMobile(mobile10);
                                                    }
                                                });
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

    private void removeUser(final String id) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Queue queueInfo = snapshot.getValue(Queue.class);
                queueInfo.queue.remove(id);
                queueDatabaseRef.child(user.getUid()).setValue(queueInfo);

                customerDatabaseReference.child(id).child("adminId").removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        queueDatabaseRef.child(user.getUid()).addListenerForSingleValueEvent(valueEventListener);


    }

    private void callMobile(String mobile) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else {
            String dial = "tel:" + mobile;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }



    }



    public void SendSMS(String mobile) {


        SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage(mobile, null, message, null, null);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
            case REQUEST_CALL:
                if( requestCode == REQUEST_CALL){
                    if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //TODO
                    }
                    break;
                }

            default:
                break;
        }
    }
}
