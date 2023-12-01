package com.example.ticketbooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.ticketbooking.model.Movie;
import com.example.ticketbooking.adapters.MovieAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.fragment.UserNavbarFragment;
import com.example.ticketbooking.viewmodels.ViewAllViewModel;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {
    RecyclerView viewAllMovieRecyclerView;
    MovieAdapter movieAdapter;
    ArrayList<Movie> movies;
    ViewAllViewModel viewAllViewModel;
    UserNavbarFragment userNavbarFragment = new UserNavbarFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        viewAllViewModel = new ViewModelProvider(this).get(ViewAllViewModel.class);
        viewAllViewModel.getAllMovies();
        ObserverAnyChange();

        getSupportFragmentManager().beginTransaction().replace(R.id.view_all_user_navbar_fragment, userNavbarFragment).commit();

        handleSearchBar();
        handleFilterGenre();
    }

    private void ObserverAnyChange() {
        viewAllViewModel.getMovies().observe(this, movies -> {
            this.movies = movies;
            handleMovieViewAllRecyclerView();
        });
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
                    public boolean onMenuItemClick(MenuItem item) {
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