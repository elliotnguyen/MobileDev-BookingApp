package com.example.ticketbooking.Model;

public class Seat {
    private int idx;
    private boolean isBooked;
    private boolean isAvailable;

    public Seat(int idx, boolean isBooked, boolean isAvailable) {
        this.idx = idx;
        this.isBooked = isBooked;
        this.isAvailable = isAvailable;
    }

   public int getIdx() {
        return idx;
    }
    public void setIdx(int idx) {
        this.idx = idx;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
