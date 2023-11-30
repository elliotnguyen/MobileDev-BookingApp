package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.Model.Purchase;
import com.example.ticketbooking.Model.User;
import com.example.ticketbooking.adapters.ImageAdapter;
import com.example.ticketbooking.adapters.MovieAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView imageRecyclerView;
    RecyclerView.Adapter imageAdapter;
    ArrayList<Movie> movies;
    ArrayList<Integer> hottestMoviesIndex;
    RecyclerView movieRecyclerView;
    RecyclerView.Adapter movieAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ImageView profileImage;
    TextView viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference userRef = myRef.child("users").child(auth.getCurrentUser().getUid());
        profileImage = findViewById(R.id.navbar_profile);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubMenu(view);
            }

            private void showSubMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_purchase) {
                            Intent intent = new Intent(MainActivity.this, PurchaseHistoryActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.action_wishlist) {
                            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
            }
        });

        getMovieData();
        getUserData(userRef);

        viewAll = findViewById(R.id.activity_main_view_all);
        viewAll.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
            startActivity(intent);
        });
    }

    private void getUserData(DatabaseReference userRef) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("profilePic").getValue().toString()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getMovieData() {
        myRef.child("movies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movies = new ArrayList<>();
                hottestMoviesIndex = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    movies.add(Movie.fromFirebaseData(dataSnapshot));
                    if (dataSnapshot.hasChild("is_hot")) {
                        if (dataSnapshot.child("is_hot").getValue().toString().equals("true")) {
                            hottestMoviesIndex.add(movies.size() - 1);
                        }
                    }
                }
                //Log.v("TAG", movies.size() + "");
                handleImageRecyclerView();
                handleMovieRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleImageRecyclerView() {
        //Log.v("TAG1", movies.size() + "");
        ArrayList<Movie> hottestMovies = new ArrayList<>();
        for (int i = 0; i < hottestMoviesIndex.size(); i++) {
            hottestMovies.add(movies.get(hottestMoviesIndex.get(i)));
        }
        imageRecyclerView = findViewById(R.id.activity_main_image_movie);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(hottestMovies, new RecyclerViewClickInterface() {
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