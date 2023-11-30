package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.Calendar.DateUtils;
import com.example.ticketbooking.Dialog.ProgressHelper;
import com.example.ticketbooking.Dialog.VerifyPurchaseHelper;
import com.example.ticketbooking.Dialog.WishlistHelper;
import com.example.ticketbooking.Model.Cinema;
import com.example.ticketbooking.Model.DateModel;
import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.Model.User;
import com.example.ticketbooking.Repository.BookingRepository;
import com.example.ticketbooking.adapters.CinemaAdapter;
import com.example.ticketbooking.adapters.DateAdapter;
import com.example.ticketbooking.adapters.MovieAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.util.ArrayList;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity implements WishlistHelper.HandlerDialogListener {
    RecyclerView dateRecyclerView;
    RecyclerView.Adapter dateAdapter;
    RecyclerView cinemaRecyclerView;
    RecyclerView.Adapter cinemaAdapter;
    ArrayList<DateModel> dates;
    ArrayList<Cinema> cinemas;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference userPurchaseRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Movie movie;
    int dateChosenPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        String movieId = getIntent().getStringExtra("movieId");

        getMovieData(movieId);
        BookingRepository.getInstance().setMovieId(movieId);

        handleDateRecylerView();

        userPurchaseRef = myRef.child("users").child(mAuth.getCurrentUser().getUid()).child("purchases");

        handleBookingForward();

        handleBookingBackward();

        handleWishList();
    }

    private void handleWishList() {
        ImageView wishlistButton = findViewById(R.id.activity_movie_detail_wishlist_btn);
        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistHelper wishlistHelper = new WishlistHelper(MovieDetailActivity.this);
                wishlistHelper.show(getSupportFragmentManager(), "wishlist");
            }
        });
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

    private void getCinemaData(String date) {
        myRef.child("cinema").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cinemas = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.hasChild(movie.getId())) {
                        Cinema cinema = Cinema.fromFirebaseData(dataSnapshot);
                        if (dataSnapshot.child(movie.getId()).hasChild(date)) {
                            ArrayList<String> times = new ArrayList<>();
                            for (DataSnapshot time : dataSnapshot.child(movie.getId()).child(date).getChildren()) {
                                times.add(time.getKey());
                            }
                            cinema.setTime(times);
                            cinemas.add(cinema);
                        }
                    }
                }
                if (!cinemas.isEmpty()) {
                    handleCinemaRecylerView();
                }
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
                //cinemaAdapter.notifyItemChanged(position);
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
            dates.add(new DateModel(currentDate, false));
            currentDate = DateUtils.getNextDay(currentDate);
        }

        dateRecyclerView = findViewById(R.id.calendar_view);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        dateAdapter = new DateAdapter(dates, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                if (dateChosenPosition != -1) {
                    dates.get(dateChosenPosition).setSelected(false);
                    dateAdapter.notifyItemChanged(dateChosenPosition);
                }
                dates.get(position).setSelected(true);
                dateAdapter.notifyItemChanged(position);
                dateChosenPosition = position;

                DateModel date = dates.get(position);

                BookingRepository.getInstance().setDate(date.getDate());
                getCinemaData(DateModel.getDay(date.getDate()));
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

    private void handleBookingForward() {
        ImageView bookButton = findViewById(R.id.activity_movie_detail_forward_btn);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BookingRepository.getInstance().getDate().isEmpty()) {
                    ProgressHelper.dismissDialog();
                    Toast.makeText(MovieDetailActivity.this, "Please choose the date", Toast.LENGTH_SHORT).show();
                } else if (BookingRepository.getInstance().getCinemaId().isEmpty() || BookingRepository.getInstance().getTime().isEmpty()) {
                    ProgressHelper.dismissDialog();
                    Toast.makeText(MovieDetailActivity.this, "Please choose the cinema and time", Toast.LENGTH_SHORT).show();
                } else {
                    String cinemaId = BookingRepository.getInstance().getCinemaId();
                    String purchaseId = movie.getId() + BookingRepository.getInstance().getCinemaId() + DateModel.getDay(BookingRepository.getInstance().getDate()) + BookingRepository.getInstance().getTime();
                    BookingRepository.getInstance().setStatus("inprogress");
                    BookingRepository.getInstance().setMovieName(movie.getTitle());
                    //String cinemaId = BookingRepository.getInstance().getCurrentPurchase().getCinemaId();
                    BookingRepository.getInstance().setCinemaName(cinemas.get(Integer.parseInt(cinemaId)).getName());
                    userPurchaseRef.child(purchaseId).setValue(BookingRepository.getInstance().getCurrentPurchase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressHelper.dismissDialog();
                            if (task.isSuccessful()) {
                                ProgressHelper.showDialog(MovieDetailActivity.this, "Continue...");
                                Intent intent = new Intent(MovieDetailActivity.this, BookingActivity.class);
                                intent.putExtra("movieName", movie.getTitle());
                                intent.putExtra("movieBackdrop", movie.getBackdropPath());
                                intent.putExtra("continuePurchase", true);
                                startActivity(intent);
                                //finish();
                            } else {
                                Toast.makeText(MovieDetailActivity.this, "Error in booking", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void handleBookingBackward() {
        ImageView backButton = findViewById(R.id.activity_movie_detail_backward_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this, ViewAllActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressHelper.dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProgressHelper.dismissDialog();
    }

    @Override
    public void handle() {
        String movieId = BookingRepository.getInstance().getMovieId();
        String userId = mAuth.getCurrentUser().getUid();
        myRef.child("users").child(userId).child("wishlist").child(movieId).setValue(movieId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MovieDetailActivity.this, "Added " + movie.getTitle() + " to wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MovieDetailActivity.this, "Server excess rating...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}