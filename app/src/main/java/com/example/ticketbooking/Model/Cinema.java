package com.example.ticketbooking.Model;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Cinema {
    private String id;
    private String name;

    private String date;
    private ArrayList<TimeModel> time;

    public Cinema(String id, String name) {
        this.id = id;
        this.name = name;
        this.time = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Cinema() {}

    public static Cinema fromFirebaseData(DataSnapshot dataSnapshot) {
        String id = (String) dataSnapshot.child("id").getValue();
        String name = (String) dataSnapshot.child("name").getValue();
        return new Cinema(id,name);
    }

    public void setTime(ArrayList<String> time) {
        //this.time = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            this.time.add(new TimeModel("", false));
        }
        for (String t : time) {
            this.time.set(Integer.parseInt(t), new TimeModel(t, false));
        }
    }

    public ArrayList<TimeModel> getTime() {
        return this.time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
