package com.app.is4401.sociallysafe.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {

    //Alternative to getter and setter methods below
    //These will be my fields in the database, they are JSON keys
    private String Name, Email, Mobile, imageUrl, NumGuests, Status;
    private Long time;
    private boolean priority = false, admin = false;
    public ArrayList<String> user;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        this.Name = "No Name";
        this.Email = "No Email";
        this.Mobile = "No mobile";
        this.imageUrl = "";
        this.NumGuests = "";
        this.priority = false;
        this.admin = false;
        this.Status = "In Queue";
    }

    public User(String Name, String Email, String Mobile, String imageUrl, String NumGuests, Boolean priority, Boolean admin, String status) {
        this.Name = Name;
        this.Email = Email;
        this.Mobile = Mobile;
        this.imageUrl = imageUrl;
        this.NumGuests = NumGuests;
//        this.time = time;
        this.priority = priority;
        this.admin = admin;
        this.Status = status;
        user = new ArrayList<>();

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNumGuests() {
        return NumGuests;
    }

    public void setNumGuests(String NumGuests) {
        this.NumGuests = NumGuests;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public void setUser(ArrayList<String> user) {
        this.user = user;
    }

    public String getMobile() {
        return Mobile;
    }

    public ArrayList<String> getUser() {return user;}

}
