package com.example.ticketbooking.Repository;

import com.example.ticketbooking.Model.Purchase;

public class BookingRepository {
    private Purchase currentPurchase;
    public static BookingRepository instance;
    private BookingRepository() {
        this.currentPurchase = new Purchase();
    }
    public static BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
        }
        return instance;
    }
    public void setCurrentPurchase(Purchase purchase) {
        this.currentPurchase = purchase;
    }
    public void addSeat(int seat) {
        this.currentPurchase.addSeat(seat);
    }
    public void removeSeat(int seat) {
        this.currentPurchase.removeSeat(seat);
    }
    public void setTime(String time) {
        this.currentPurchase.setTime(time);
    }
    public void setDate(String date) {
        this.currentPurchase.setDate(date);
    }
    public void setCinemaId(String cinemaId) {
        this.currentPurchase.setCinemaId(cinemaId);
    }
    public void setMovieId(String movieId) {
        this.currentPurchase.setMovieId(movieId);
    }
    public void setStatus(String status) {
        this.currentPurchase.setStatus(status);
    }
    public void resetCurrentPurchase() {
        this.currentPurchase = new Purchase();
    }
    public Purchase getCurrentPurchase() {
        return this.currentPurchase;
    }
}
