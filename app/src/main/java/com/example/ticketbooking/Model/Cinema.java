package com.example.ticketbooking.Model;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Cinema {
    private String name;
    private ArrayList<String> available;

    public Cinema(String name, ArrayList<String> time) {
        this.name = name;
        this.available = time;
    }

    public String getName() {
        return name;
    }
    public ArrayList<String> getTime() {
        return available;
    }

    public Cinema() {

    }

    public static Cinema fromFirebaseData(DataSnapshot dataSnapshot) {
        String name = (String) dataSnapshot.child("name").getValue();
        String time = (String) dataSnapshot.child("available").getValue();
        String [] dataStr = time.split(" ");
        ArrayList<String> available = new ArrayList<>(Arrays.asList(dataStr));
        return new Cinema(name, available);
    }
}
