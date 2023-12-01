package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.example.ticketbooking.model.Movie;
import com.example.ticketbooking.adapters.ImageAdapter;
import com.example.ticketbooking.adapters.MovieAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.fragment.UserNavbarFragment;
import com.example.ticketbooking.viewmodels.MainViewModel;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {
    ViewPager2 mImageRecyclerView;
    CircleIndicator3 mIndicator;
    RecyclerView.Adapter imageAdapter;
    ArrayList<Movie> movies;
    ArrayList<Movie> mHottestMovies;
    RecyclerView movieRecyclerView;
    RecyclerView.Adapter movieAdapter;
    TextView viewAll;
    UserNavbarFragment userNavbarFragment = new UserNavbarFragment();
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main_user_navbar_fragment, userNavbarFragment).commit();

        movies = new ArrayList<>();
        mainViewModel.getAllMovies();
        ObserverAllMovies();

        mHottestMovies = new ArrayList<>();
        mainViewModel.getHottestMovie();
        ObserverHottestMovie();

        handleViewAll();
    }

    private void ObserverAllMovies() {
        mainViewModel.getMovies().observe(this, movies -> {
            this.movies = movies;
            handleMovieRecyclerView();
        });
    }

    private void ObserverHottestMovie() {
        mainViewModel.getHottestMovies().observe(this, movies -> {
            this.mHottestMovies = movies;
            handleImageRecyclerView();
        });
    }

    private void handleViewAll() {
        viewAll = findViewById(R.id.activity_main_view_all);
        viewAll.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewAllActivity.class);
            startActivity(intent);
        });
    }
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mImageRecyclerView.getAdapter() == null || mImageRecyclerView.getAdapter().getItemCount() == 0) {
                return;
            }
            int size = mImageRecyclerView.getAdapter().getItemCount();
            mImageRecyclerView.setCurrentItem((mImageRecyclerView.getCurrentItem() + 1) % size);
        }
    };

    private void handleImageRecyclerView() {
        mImageRecyclerView = findViewById(R.id.activity_main_image_movie);
        mIndicator = findViewById(R.id.activity_main_indicator);
        mImageRecyclerView.setOffscreenPageLimit(3);
        mImageRecyclerView.setClipToPadding(false);
        mImageRecyclerView.setClipChildren(false);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        mImageRecyclerView.setPageTransformer(compositePageTransformer);
        mImageRecyclerView.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        mImageRecyclerView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageScrollStateChanged(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }
        });

        imageAdapter = new ImageAdapter(mHottestMovies, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("movieId", mHottestMovies.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(int position) {
            }
        }, this);

        mImageRecyclerView.setAdapter(imageAdapter);
        mIndicator.setViewPager(mImageRecyclerView);
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

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 3000);
    }
}