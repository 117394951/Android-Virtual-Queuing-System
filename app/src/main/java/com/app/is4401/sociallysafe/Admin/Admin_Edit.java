package com.app.is4401.sociallysafe.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.is4401.sociallysafe.Login.FirebaseLogin;
import com.app.is4401.sociallysafe.R;
import com.app.is4401.sociallysafe.Model.Queue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Admin_Edit extends Fragment {

    Button btnDrop, btnSignOut;
    Switch sw;
    DatabaseReference queueRef;
    FirebaseUser user;
    public int len;
    Queue queueInfo;

    public Admin_Edit() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_edit, container, false);

        btnDrop = view.findViewById(R.id.btnDrop);
        btnDrop.setVisibility(INVISIBLE);
        btnSignOut = view.findViewById(R.id.btnSignOut);
//        sw = view.findViewById(R.id.switch1);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        queueRef = FirebaseDatabase.getInstance().getReference("Queue");
        user = firebaseAuth.getInstance().getCurrentUser();

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), FirebaseLogin.class);
                startActivity(intent);
            }
        });

        queueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(user.getUid())){
                    btnDrop.setVisibility(VISIBLE);
                    btnDrop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            queueRef.child(user.getUid()).removeValue();
                            btnDrop.setVisibility(INVISIBLE);
                            Toast.makeText(getContext(),"Queue dropped", Toast.LENGTH_SHORT);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
