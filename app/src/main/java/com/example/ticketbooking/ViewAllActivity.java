package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.adapters.MovieAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {
    RecyclerView viewAllMovieRecyclerView;
    RecyclerView.Adapter movieAdapter;
    ArrayList<Movie> movies;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

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
                handleMovieViewAllRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleMovieViewAllRecyclerView() {
        viewAllMovieRecyclerView = findViewById(R.id.view_all_content_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        viewAllMovieRecyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter = new MovieAdapter(movies, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ViewAllActivity.this, MovieDetailActivity.class);
                intent.putExtra("movieId", movies.get(position).getId());
                startActivity(intent);
            }
            @Override
            public void onLongItemClick(int position) {

            }
        }, this);
        viewAllMovieRecyclerView.setAdapter(movieAdapter);
    }
}