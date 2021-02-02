package com.app.is4401.sociallysafe.model;

import java.util.ArrayList;

public class Queue{
    private String Name;
    private String imageUrl;
    private String Desc;
    private String Location;
    private int Avewaiting;
    public ArrayList<String> queue;


    public Queue(){
        this.Name = "No Queue Name";
        this.Desc="No Desc";
        this.Location = "No Location";
        this.Avewaiting=1;
        queue = new ArrayList<>();
    }

    public Queue(String name, String imageUrl, String Location, String Desc, int Avewaiting) {
        this.Name = name;
        this.imageUrl = imageUrl;
        this.Desc=Desc;
        this.Location=Location;
        this.Avewaiting=Avewaiting;
        queue = new ArrayList<>();

    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

    public int getAvewaiting() {
        return Avewaiting;
    }

    public void setAvewaiting(int w) {
        this.Avewaiting = w;
    }

    public String getLocation() {return Location;}

    public void setLocation() {this.Location = Location;}

    public int getNumPeople() {return queue.size();}

    public ArrayList<String> getQueue() {return queue;}

    public void setQueue(ArrayList<String> queue) {
        this.queue = queue;
    }
    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getimageUrl() {
        return imageUrl;
    }


}
