package com.example.ticketbooking.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ticketbooking.model.Cinema;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CinemaRepository {
    private MutableLiveData<ArrayList<Cinema>> mCinemas;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference();
    public static CinemaRepository instance;
    private CinemaRepository() {
        this.mCinemas = new MutableLiveData<>();
    }
    public static CinemaRepository getInstance() {
        if (instance == null) {
            instance = new CinemaRepository();
        }
        return instance;
    }

    public void getCinemaInDate(String date, String movieId) {
        myRef.child("cinema").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Cinema> cinemas = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChild(movieId)) {
                        Cinema cinema = Cinema.fromFirebaseData(dataSnapshot);
                        if (dataSnapshot.child(movieId).hasChild(date)) {
                            ArrayList<String> times = new ArrayList<>();
                            for (DataSnapshot time : dataSnapshot.child(movieId).child(date).getChildren()) {
                                times.add(time.getKey());
                            }
                            cinema.setTime(times);
                        }
                        cinemas.add(cinema);
                    }
                }
                if (!cinemas.isEmpty()) {
                    mCinemas.postValue(cinemas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<ArrayList<Cinema>> getCinemas() {
        return mCinemas;
    }
}
