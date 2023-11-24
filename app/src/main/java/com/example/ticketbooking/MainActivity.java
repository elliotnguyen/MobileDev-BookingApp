package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.adapters.ImageAdapter;
import com.example.ticketbooking.adapters.MovieAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView imageRecyclerView;
    RecyclerView.Adapter imageAdapter;
    ArrayList<Movie> movies;
    RecyclerView movieRecyclerView;
    RecyclerView.Adapter movieAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMovieData();
    }
    private void getMovieData() {
        myRef.child("movies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movies = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    movies.add(Movie.fromFirebaseData(dataSnapshot));
                }
                Log.v("TAG", movies.size() + "");
                handleImageRecyclerView();
                handleMovieRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleImageRecyclerView() {
        Log.v("TAG1", movies.size() + "");
        imageRecyclerView = findViewById(R.id.activity_main_image_movie);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(movies, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onLongItemClick(int position) {
            }
        }, this);
        imageRecyclerView.setAdapter(imageAdapter);
    }

    private void handleMovieRecyclerView() {
        movieRecyclerView = findViewById(R.id.activity_main_list_all_movies);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieAdapter = new MovieAdapter(movies, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("movieId", movies.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(int position) {
            }
        }, this);
        movieRecyclerView.setAdapter(movieAdapter);
    }
}