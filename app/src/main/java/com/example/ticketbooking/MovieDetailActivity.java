package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.dialog.ProgressHelper;
import com.example.ticketbooking.dialog.WishlistHelper;
import com.example.ticketbooking.utils.DateModel;
import com.example.ticketbooking.model.Movie;
import com.example.ticketbooking.repository.BookingRepository;
import com.example.ticketbooking.fragment.CinemaSelectFragment;
import com.example.ticketbooking.fragment.ReviewFragment;
import com.example.ticketbooking.viewmodels.MovieDetailViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MovieDetailActivity extends AppCompatActivity implements WishlistHelper.HandlerDialogListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference userPurchaseRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Movie movie;
    MovieDetailViewModel movieDetailViewModel;
    //CinemaSelectFragment cinemaSelectFragment = new CinemaSelectFragment();
    //ReviewFragment reviewFragment = new ReviewFragment();
    String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieId = getIntent().getStringExtra("movieId");

        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.getMovie(movieId);
        ObserverMovieChange();

        BookingRepository.getInstance().resetCurrentPurchase();
        BookingRepository.getInstance().setMovieId(movieId);

        userPurchaseRef = myRef.child("users").child(mAuth.getCurrentUser().getUid()).child("purchases");

        handleBookingForward();
        handleBookingBackward();

        handleWishList();

        handleSessionShow();
    }

    private void handleSessionShow() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_movie_detail_frame_layout, new CinemaSelectFragment())
                .commit();
        TextView cinemaSelectShow = findViewById(R.id.activity_movie_detail_cinema_selecting);
        cinemaSelectShow.setTextColor(getResources().getColor(R.color.blue));
        TextView reviewShow = findViewById(R.id.activity_movie_detail_seat_selecting);
        reviewShow.setTextColor(getResources().getColor(R.color.black));
        cinemaSelectShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cinemaSelectShow.setTextColor(getResources().getColor(R.color.blue));
                reviewShow.setTextColor(getResources().getColor(R.color.black));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_movie_detail_frame_layout, new CinemaSelectFragment())
                        .commit();
            }
        });

        reviewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cinemaSelectShow.setTextColor(getResources().getColor(R.color.black));
                reviewShow.setTextColor(getResources().getColor(R.color.blue));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_movie_detail_frame_layout, new ReviewFragment())
                        .commit();
            }
        });
    }

    private void ObserverMovieChange() {
        movieDetailViewModel.getMovie().observe(this, movie -> {
            if (movie != null) {
                this.movie = movie;
                handleCardMovieItem();
            }
        });
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
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movie.getBackdropPath()).placeholder(R.drawable.movie_detail_background_image).override(1350,600).fitCenter().into(moviePoster);

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
                    String purchaseId = movie.getId() + cinemaId + DateModel.getDay(BookingRepository.getInstance().getDate()) + BookingRepository.getInstance().getTime();
                    BookingRepository.getInstance().setStatus("inprogress");
                    BookingRepository.getInstance().setMovieName(movie.getTitle());

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
                finish();
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
    protected void onStart() {
        super.onStart();
        BookingRepository.getInstance().resetCurrentPurchase();
        BookingRepository.getInstance().setMovieId(movieId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BookingRepository.getInstance().resetCurrentPurchase();
        BookingRepository.getInstance().setMovieId(movieId);
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