package com.example.ticketbooking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ticketbooking.model.Review;
import com.example.ticketbooking.repository.ReviewRepository;

import java.util.ArrayList;

public class MovieReviewsViewModel extends ViewModel {
    private ReviewRepository reviewRepository;
    public MovieReviewsViewModel() {
        this.reviewRepository = ReviewRepository.getInstance();
    }
    public LiveData<ArrayList<Review>> getMovieReviews() {
        return this.reviewRepository.getMovieReviews();
    }
    public void getMovieReviews(String movie_id) {
        this.reviewRepository.getMovieReviews(movie_id);
    }
}
