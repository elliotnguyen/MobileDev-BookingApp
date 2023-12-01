package com.example.ticketbooking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ticketbooking.model.Movie;
import com.example.ticketbooking.repository.MovieRepository;

import java.util.ArrayList;

public class ViewAllViewModel extends ViewModel {
    private MovieRepository movieRepository;
    public ViewAllViewModel() {
        this.movieRepository = MovieRepository.getInstance();
    }
    public LiveData<ArrayList<Movie>> getMovies() {
        return this.movieRepository.getMovies();
    }
    public void getAllMovies() {
        this.movieRepository.getAllMovies();
    }
}
