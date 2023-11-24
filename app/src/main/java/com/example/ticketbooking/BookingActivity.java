package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.Model.Seat;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.adapters.SeatAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {
    RecyclerView seatRecyclerView;
    RecyclerView.Adapter seatAdapter;
    ArrayList<Seat> seats;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        seats = new ArrayList<>();
        for (int i = 0; i < 144; i++) {
            seats.add(new Seat(i, false, true));
        }

        getSeatData("0");
    }

    private void getSeatData(String cinemaId) {
        myRef.child("booking").child(cinemaId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bookedSeat = snapshot.child("booked").getValue(String.class);
                Log.v("TAG", bookedSeat);
                String[] seatArray = bookedSeat.split(",");
                for (String seat : seatArray) {
                    int idx = Integer.parseInt(seat);
                    seats.get(idx).setBooked(true);
                    seats.get(idx).setAvailable(false);
                }

                /*String discardSeat = snapshot.child("discard").getValue(String.class);
                Log.v("TAG", discardSeat);
                String[] discardArray = discardSeat.split(",");
                for (String seat : discardArray) {
                    int idx = Integer.parseInt(seat);
                    for (int i = 0; i <= 11; i++) {
                        seats.get(idx + 12*i).setAvailable(false);
                    }
                }*/
                handleSeatRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleSeatRecyclerView() {
        seatRecyclerView = findViewById(R.id.activity_booking_seat_recycler_view);
        seatRecyclerView.setLayoutManager(new GridLayoutManager(this, 12));
        seatAdapter = new SeatAdapter(seats, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                Log.v("TAG", String.valueOf(position));
                Seat seat = seats.get(position);
                if (seat.isAvailable()) {
                    if (seat.isBooked()) {
                        seat.setBooked(false);
                    } else {
                        seat.setBooked(true);
                    }
                    seatAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        seatRecyclerView.setAdapter(seatAdapter);
    }
}