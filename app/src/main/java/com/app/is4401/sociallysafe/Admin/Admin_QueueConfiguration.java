package com.app.is4401.sociallysafe.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.is4401.sociallysafe.R;

public class Admin_QueueConfiguration extends Fragment {

    private Button btnCreateQ;

    public Admin_QueueConfiguration() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin__queue_configuration, container, false);
        btnCreateQ = view.findViewById(R.id.btnCreateQ);

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
}