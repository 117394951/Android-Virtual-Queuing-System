package com.app.is4401.sociallysafe.Model;

public class StatusItem {

    private String mStatus;
    private int mImage;

    public StatusItem(String status, int image){
        mStatus = status;
        mImage = image;
    }

    public String getStatus() {
        return mStatus;
    }

    public int getImage(){
        return mImage;
    }
}
