package com.app.is4401.sociallysafe.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.is4401.sociallysafe.R;

import java.util.ArrayList;


public class StatusAdapter extends ArrayAdapter<StatusItem>{
// YouTube video by Coding In Flow 16th December 2017
// https://www.youtube.com/watch?v=GeO5F0nnzAw
    public StatusAdapter(Context context , ArrayList<StatusItem> statusList){
        super(context, 0 , statusList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View converstView, ViewGroup parent){
        if ( converstView == null ){
            converstView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_status_content,parent,false
            );
        }

        ImageView imageView = converstView.findViewById(R.id.spinner_image);
        TextView textView = converstView.findViewById(R.id.spinner_text);

        StatusItem currentItem = getItem(position);

        if(currentItem != null) {
            imageView.setImageResource(currentItem.getImage());
            textView.setText(currentItem.getStatus());
        }
        return converstView;
    }
}
