package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.Calendar.DateUtils;
import com.example.ticketbooking.Model.Cinema;
import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.adapters.CinemaAdapter;
import com.example.ticketbooking.adapters.DateAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {
    RecyclerView dateRecyclerView;
    RecyclerView.Adapter dateAdapter;
    RecyclerView cinemaRecyclerView;
    RecyclerView.Adapter cinemaAdapter;
    ArrayList<String> dates;
    ArrayList<Cinema> cinemas;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView backButton = findViewById(R.id.activity_movie_detail_backward_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this, ViewAllActivity.class);
                startActivity(intent);
            }
        });

        String id = getIntent().getStringExtra("movieId");

        getMovieData(id);

        handleDateRecylerView();

        getCinemaData();
    }

    private void getMovieData(String id) {
        myRef.child("movies").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               movie = Movie.fromFirebaseData(snapshot);
               handleCardMovieItem();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getCinemaData() {
        myRef.child("cinemas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cinemas = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cinema cinema = Cinema.fromFirebaseData(dataSnapshot);
                    cinemas.add(cinema);
                }
                handleCinemaRecylerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleCinemaRecylerView() {
        cinemaRecyclerView = findViewById(R.id.cinema_view);
        cinemaRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        cinemaAdapter = new CinemaAdapter(cinemas, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onLongItemClick(int position) {
            }
        }, MovieDetailActivity.this);
        cinemaRecyclerView.setAdapter(cinemaAdapter);
    }

    private void handleDateRecylerView() {
        dates = new ArrayList<>();
        String currentDate = DateUtils.getCurrentDate();
        for (int i = 0; i < 10; i++) {
            dates.add(currentDate);
            currentDate = DateUtils.getNextDay(currentDate);
        }

        dateRecyclerView = findViewById(R.id.calendar_view);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        dateAdapter = new DateAdapter(dates, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onLongItemClick(int position) {
            }
        });
        dateRecyclerView.setAdapter(dateAdapter);
    }

    private void handleCardMovieItem() {
        CardView cardView = findViewById(R.id.card_movie_item);
        TextView movieTitle = findViewById(R.id.card_movie_item_name);
        TextView movieDescription = findViewById(R.id.card_movie_item_description);
        TextView movieRating = findViewById(R.id.card_movie_item_rating);
        TextView movieDuration = findViewById(R.id.card_movie_item_duration);
        TextView movieGenre = findViewById(R.id.card_movie_item_genre);
        ImageView moviePoster = findViewById(R.id.activity_movie_detail_backdrop);

        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getDescription());
        movieRating.setText(movie.getRating());
        movieDuration.setText(movie.getDuration());
        movieGenre.setText(movie.getGenre());
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.getBackdropPath()).placeholder(R.drawable.movie_detail_background_image).override(1250,600).fitCenter().into(moviePoster);

        ImageButton movieExpand = findViewById(R.id.card_movie_item_expand_button);
        movieExpand.setOnClickListener(v -> {
            if (movieDescription.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                movieDescription.setVisibility(View.VISIBLE);
                movieExpand.setImageResource(R.drawable.baseline_expand_less_24);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                movieDescription.setVisibility(View.GONE);
                movieExpand.setImageResource(R.drawable.baseline_expand_more_24);
            }
        });
    }
}