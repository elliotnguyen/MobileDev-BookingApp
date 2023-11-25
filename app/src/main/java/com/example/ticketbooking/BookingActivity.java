package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.Dialog.ProgressHelper;
import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.Model.Seat;
import com.example.ticketbooking.Repository.BookingRepository;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.adapters.SeatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    DatabaseReference cinemaRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference userPurchaseRef;
    String cinemaId;
    String movieId;
    String date;
    String time;
    TextView totalTicket;
    TextView totalPrice;
    String bookedSeat;
    String CinemaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        seats = new ArrayList<>();
        for (int i = 0; i < 144; i++) {
            seats.add(new Seat(i, false, true));
        }

        cinemaId = BookingRepository.getInstance().getCurrentPurchase().getCinemaId();
        movieId = BookingRepository.getInstance().getCurrentPurchase().getMovieId();
        date = BookingRepository.getInstance().getCurrentPurchase().getDate();
        time = BookingRepository.getInstance().getCurrentPurchase().getTime();

        String purchaseId = movieId + cinemaId + date + time;
        userPurchaseRef =  myRef.child("users").child(mAuth.getCurrentUser().getUid()).child("purchases").child(purchaseId);

        cinemaRef = myRef.child("cinema").child(cinemaId);
        handlePurchaseInfo();

        totalTicket = findViewById(R.id.activity_booking_ticket);
        totalPrice = findViewById(R.id.activity_booking_total_payable);
        getSeatData();

        handlePurchaseFinish();
    }

    private void getSeatData() {
        cinemaRef = cinemaRef.child(movieId).child(date);
        cinemaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String bookedSeat = snapshot.child("booked").getValue(String.class);
                bookedSeat = snapshot.child(time).getValue(String.class);
                //Log.v("TAG", bookedSeat);
                String[] seatArray = bookedSeat.split(",");
                for (String seat : seatArray) {
                    seat = seat.replaceAll("\"","");
                    int idx = Integer.parseInt(seat);
                    seats.get(idx).setBooked(true);
                }

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
                Seat seat = seats.get(position);
                if (!seat.isBooked()) {
                    if (seat.isAvailable()) {
                        seat.setAvailable(false);
                        BookingRepository.getInstance().addSeat(seat.getIdx());
                    } else {
                        seat.setAvailable(true);
                        BookingRepository.getInstance().removeSeat(seat.getIdx());
                    }
                    int total = BookingRepository.getInstance().getCurrentPurchase().getSeat().size() + 1;
                    totalTicket.setText(String.valueOf(total));
                    totalPrice.setText(String.valueOf(total * 100));
                    seatAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        seatRecyclerView.setAdapter(seatAdapter);
    }

    private void handlePurchaseInfo() {
        TextView movieName = findViewById(R.id.card_booking_item_name);
        movieName.setText(getIntent().getStringExtra("movieName"));

        Button date = findViewById(R.id.activity_booking_booking_date);
        date.setText(BookingRepository.getInstance().getCurrentPurchase().getDate());

        Button time = findViewById(R.id.activity_booking_booking_time);
        String showTime = BookingRepository.getInstance().getCurrentPurchase().getTime();
        String realtime = String.valueOf(Integer.parseInt(showTime) + 9) + ":30";
        time.setText(realtime);

        Button cinema = findViewById(R.id.activity_booking_booking_revenue);
        cinemaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cinemaName = snapshot.child("name").getValue(String.class);
                cinema.setText(cinemaName);
                CinemaName = cinemaName;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageView movieBackdrop = findViewById(R.id.activity_booking_backdrop);
        String backdrop = getIntent().getStringExtra("movieBackdrop");
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + backdrop).placeholder(R.drawable.movie_detail_background_image).override(1250,600).fitCenter().into(movieBackdrop);
    }

    private void handlePurchaseFinish() {
        ImageView finishBtn = findViewById(R.id.activity_booking_forward_btn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPurchaseRef.child("status").setValue("booked").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(BookingActivity.this, "Error in handling your purchase", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                String newSeats = "";
                int size = BookingRepository.getInstance().getCurrentPurchase().getSeat().size();
                for (int i = 0; i <  size; i++) {
                    newSeats = newSeats + BookingRepository.getInstance().getCurrentPurchase().getSeat().get(i);
                    if (i != size - 1) {
                        newSeats += ",";
                    }
                }

                userPurchaseRef.child("seat").setValue(newSeats).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {
                            Toast.makeText(BookingActivity.this, "Server excess rating...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final String seatForQR = newSeats;
                cinemaRef.child(time).setValue(bookedSeat + "," + newSeats).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ProgressHelper.showDialog(BookingActivity.this, "Loading...");
                            Intent intent = new Intent(BookingActivity.this, PurchaseResultActivity.class);
                            intent.putExtra("movieName", getIntent().getStringExtra("movieName"));
                            intent.putExtra("cinemaName", CinemaName);
                            //intent.putExtra("cinemaName", getIntent().getStringExtra("cinemaName"));
                            intent.putExtra("date", BookingRepository.getInstance().getCurrentPurchase().getDate());
                            intent.putExtra("time", BookingRepository.getInstance().getCurrentPurchase().getTime());
                            intent.putExtra("seat", seatForQR);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(BookingActivity.this, "Error in booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressHelper.dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProgressHelper.dismissDialog();
    }
}