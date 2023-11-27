package com.example.ticketbooking.Model;

import com.example.ticketbooking.Repository.BookingRepository;

import java.util.ArrayList;

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

    public static ArrayList<Integer> convertStringSeatToInteger(String bookedSeat) {
        ArrayList<Integer> result = new ArrayList<>();
        if (bookedSeat == null) return result;
        String[] seatArray = bookedSeat.split(",");
        for (String seat : seatArray) {
            seat = seat.replaceAll("\"","");
            try {
                int idx = Integer.parseInt(seat);
                result.add(idx);
            } catch (NumberFormatException e) {
                continue;
            }
            //int idx = Integer.parseInt(seat);
            //result.add(idx);
        }
        return result;
    }

    public static String convertIntegerSeatToString(ArrayList<Integer> yourBookedSeat) {
        String newSeats = "";
        if (yourBookedSeat == null || yourBookedSeat.isEmpty()) return newSeats;
        int size = yourBookedSeat.size();
        for (int i = 0; i <  size; i++) {
            newSeats = newSeats + yourBookedSeat.get(i);
            if (i != size - 1) {
                newSeats += ",";
            }
        }
        return newSeats;
    }
}
