package com.example.ticketbooking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ticketbooking.model.Movie;
import com.example.ticketbooking.repository.MovieRepository;

public class MovieDetailViewModel extends ViewModel {
    private MovieRepository movieRepository;
    public MovieDetailViewModel() {
        this.movieRepository = MovieRepository.getInstance();
    }
    public LiveData<Movie> getMovie() {
        return this.movieRepository.getMovie();
    }
    public void getMovie(String movieId) {
        this.movieRepository.getMovie(movieId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
