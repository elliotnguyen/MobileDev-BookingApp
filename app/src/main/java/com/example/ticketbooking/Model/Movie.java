package com.example.ticketbooking.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class Movie {
    public String id;
    public String name;
    public String description;
    public String poster_path;
    public String backdrop_path;
    public double vote_average;
    public long vote_count;
    public String categories;
    public long duration;
    public String releaseDate;
    public double popularity;

    public Movie() {}

    public Movie(String id, String name, String description, String poster_path, String backdrop_path, double vote_average, long vote_count, String categories, long duration, String releaseDate, double popularity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.categories = categories;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }

    public static Movie fromFirebaseData(DataSnapshot dataSnapshot) {
        long movieId = (long) dataSnapshot.child("id").getValue();
        String id = String.valueOf(movieId);
        //String id = (String) dataSnapshot.child("id").getValue();
        String name = (String) dataSnapshot.child("original_title").getValue();
        Log.v("TAG", name);
        String overview = (String) dataSnapshot.child("overview").getValue();
        String releaseDate = (String) dataSnapshot.child("release_date").getValue();
        String poster_path = (String) dataSnapshot.child("poster_path").getValue();
        String backdrop_path = (String) dataSnapshot.child("backdrop_path").getValue();
        double vote_average = (double) dataSnapshot.child("vote_average").getValue();
        long vote_count = (long) dataSnapshot.child("vote_count").getValue();
        String categories= (String) dataSnapshot.child("genres").getValue();
        long duration = (long) dataSnapshot.child("runtime").getValue();
        double popularity = (double) dataSnapshot.child("popularity").getValue();

        return new Movie(id, name, overview, poster_path, backdrop_path, vote_average, vote_count, categories, duration, releaseDate, popularity);
    }

    public String getDescription() {
        return this.description;
    }
    public String getTitle() {
        return this.name;
    }
    public String getRating() {
        return String.valueOf(this.vote_average);
    }
    public String getDuration() {
        return String.valueOf(this.duration);
    }
    public String getGenre() {
        return this.categories;
    }

    public String getPosterPath() {
        return this.poster_path;
    }

    public String getBackdropPath() {
        return this.backdrop_path;
    }

    public String getId() {
        return this.id;
    }
}
