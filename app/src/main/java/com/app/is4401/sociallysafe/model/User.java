package com.app.is4401.sociallysafe.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    //Alternative to getter and setter methods below
    //These will be my fields in the database, they are JSON keys
    public String Key;
    public String FirstName, LastName, Email, Mobile;
    public String NumGuests;
    public Long time;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String FirstName, String LastName, String Email, String Mobile, String NumGuests, Long time) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Mobile = Mobile;
        this.NumGuests = NumGuests;
        this.time = time;
    }
}
