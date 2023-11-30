package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.ticketbooking.Model.Movie;
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

public class ViewAllActivity extends AppCompatActivity {
    RecyclerView viewAllMovieRecyclerView;
    //RecyclerView.Adapter movieAdapter;
    MovieAdapter movieAdapter;
    ArrayList<Movie> movies;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        DatabaseReference userRef = myRef.child("users").child(mAuth.getCurrentUser().getUid());
        handleUserNavabr();

        getMovieData();
        getUserData(userRef);

        handleSearchBar();
        handleFilterGenre();
    }

    private void handleFilterGenre() {
        ImageView filterGenre = findViewById(R.id.view_all_content_filter);
        filterGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterMenu(view);
            }

            private void showFilterMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(ViewAllActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_genre, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) { // when user press search button
                        if (item.getItemId() == R.id.genre_action) {
                            movieAdapter.setMovies(movies);
                            movieAdapter.getFilter().filter("filter Action");
                        } else if (item.getItemId() == R.id.genre_adventure) {
                            movieAdapter.setMovies(movies);
                            movieAdapter.getFilter().filter("filter Adventure");
                        } else if (item.getItemId() == R.id.genre_animation) {
                            movieAdapter.setMovies(movies);
                            movieAdapter.getFilter().filter("filter Animation");
                        } else if (item.getItemId() == R.id.genre_comedy) {
                            movieAdapter.setMovies(movies);
                            movieAdapter.getFilter().filter("filter Comedy");
                        } else if (item.getItemId() == R.id.genre_crime) {
                            movieAdapter.setMovies(movies);
                            movieAdapter.getFilter().filter("filter Crime");
                        } else if (item.getItemId() == R.id.genre_drama) {
                            movieAdapter.setMovies(movies);
                            movieAdapter.getFilter().filter("filter Drama");
                        } else if (item.getItemId() == R.id.genre_all) {
                            movieAdapter.setMovies(movies);
                        }
                        return false;
                    }
                });

                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
            }
        });
    }

    private void handleUserNavabr() {
        profileImage = findViewById(R.id.navbar_profile_view_all);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubMenu(view);
            }

            private void showSubMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(ViewAllActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_purchase) {
                            Intent intent = new Intent(ViewAllActivity.this, PurchaseHistoryActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.action_wishlist) {
                            Intent intent = new Intent(ViewAllActivity.this, WishlistActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
            }
        });
    }

    private void handleSearchBar() {
        SearchView searchView = findViewById(R.id.search_view_all);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // when user press search button
                movieAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { // when user type
                if (TextUtils.isEmpty(newText)) {
                    movieAdapter.setMovies(movies);
                }
                return true;
            }
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