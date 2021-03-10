package com.app.is4401.sociallysafe.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {

    //Alternative to getter and setter methods below
    //These will be my fields in the database, they are JSON keys
    public String Name, Email, Mobile, imageUrl;
    public String NumGuests;
    public Long time;
    public boolean priority = false;
    public ArrayList<String> user;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String Name, String Email, String Mobile, String imageUrl, String NumGuests, Boolean priority) {
        this.Name = Name;
        this.Email = Email;
        this.Mobile = Mobile;
        this.imageUrl = imageUrl;
        this.NumGuests = NumGuests;
//        this.time = time;
        this.priority = priority;
        user = new ArrayList<>();

    }

    public String getMobile() {
        return Mobile;
    }

    public ArrayList<String> getUser() {return user;}

}
