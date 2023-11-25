package com.example.ticketbooking.Model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Purchase {
    private String id;
    private String movieId;
    private String movieName;
    private String cinemaId;
    private String cinemaName;
    private String date;
    private String date_long;
    private String time;
    private ArrayList<Integer> seat;
    private String status;

    public Purchase(String id, String movieId, String cinemaId, String date, String time, ArrayList<Integer> seat, String status) {
        this.id = id;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.status = status;
    }

    public Purchase() {}

    public Purchase(String movieId, String cinemaId, String date, String time, ArrayList<Integer> seat, String status) {
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.status = status;
    }

    public Purchase(String movieId, String movieName, String cinemaId, String cinemaName, String date, String time, ArrayList<Integer> seat, String status) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<Integer> getSeat() {
        return seat;
    }

    public void setSeat(ArrayList<Integer> seat) {
        this.seat = seat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addSeat(int seat) {
        if (this.seat == null) {
            this.seat = new ArrayList<>();
        } else {
            this.seat.add(seat);
        }
    }

    public void removeSeat(int seat) {
        for (int i = 0; i < this.seat.size(); i++) {
            if (this.seat.get(i) == seat) {
                this.seat.remove(i);
                break;
            }
        }
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getDate_long() {
        return date_long;
    }

    public void setDate_long(String date_long) {
        this.date_long = date_long;
    }

    public static Purchase fromFirebaseData(DataSnapshot dataSnapshot) {
        String movieName = (String) dataSnapshot.child("movieName").getValue();
        String cinemaName = (String) dataSnapshot.child("cinemaName").getValue();
        String movieId = (String) dataSnapshot.child("movieId").getValue();
        String cinemaId = (String) dataSnapshot.child("cinemaId").getValue();
        String date = (String) dataSnapshot.child("date").getValue();
        String time = (String) dataSnapshot.child("time").getValue();
        String seatStr = (String) dataSnapshot.child("seat").getValue();
        ArrayList<Integer> seat = new ArrayList<>();
        if (seatStr != null) {
            String seatStrArr[] = seatStr.split(",");
            for (int i = 0; i < seatStrArr.length; i++) {
                seatStrArr[i] = seatStrArr[i].replaceAll("\"","");
                int idx = Integer.parseInt(seatStrArr[i]);
                seat.add(idx);
            }
        } else seat.add(1);
        /*String seatStrArr[] = seatStr.split(",");
        for (int i = 0; i < seatStrArr.length; i++) {
            seatStrArr[i] = seatStrArr[i].replaceAll("\"","");
            int idx = Integer.parseInt(seatStrArr[i]);
            seat.add(idx);
        }*/
        String status = (String) dataSnapshot.child("status").getValue();

        return new Purchase(movieId, movieName, cinemaId, cinemaName, date, time, seat, status);
    }
}
