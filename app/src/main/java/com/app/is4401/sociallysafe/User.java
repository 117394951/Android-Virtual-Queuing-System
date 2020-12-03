package com.app.is4401.sociallysafe;

public class User {
    private String FirstName, LastName, Email, Mobile;
    private String NumGuests;
    private Long time;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getNumGuests() {
        return NumGuests;
    }

    public void setNumGuests(String numGuests) {
        NumGuests = numGuests;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
