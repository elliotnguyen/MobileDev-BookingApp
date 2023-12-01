package com.example.ticketbooking.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ticketbooking.model.Cinema;
import com.example.ticketbooking.repository.CinemaRepository;

import java.util.ArrayList;

public class CinemaSelectingViewModel extends ViewModel {
    private CinemaRepository cinemaRepository;
    public CinemaSelectingViewModel() {
        this.cinemaRepository = CinemaRepository.getInstance();
    }
    public LiveData<ArrayList<Cinema>> getCinemas() {
        return this.cinemaRepository.getCinemas();
    }
    public void getCinemaInDate(String date, String movieId) {
        this.cinemaRepository.getCinemaInDate(date, movieId);
    }
}
