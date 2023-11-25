package com.example.ticketbooking.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.Model.Seat;
import com.example.ticketbooking.R;

import java.util.ArrayList;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.ViewHolder> {
    ArrayList<Seat> seats;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public SeatAdapter(ArrayList<Seat> seats, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.seats = seats;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public SeatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = View.inflate(parent.getContext(), R.layout.seat_viewholder, null);
        return new SeatAdapter.ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatAdapter.ViewHolder holder, int position) {
        Log.v("TAG", String.valueOf(position));
        Seat seat = seats.get(position);
        if (seat.isBooked()) {
            holder.seatButton.setBackgroundResource(R.drawable.bg_seat_booked);
        } else if (seat.isAvailable()) {
            holder.seatButton.setBackgroundResource(R.drawable.bg_seat_available);
        } else if (!seat.isAvailable()) {
            holder.seatButton.setBackgroundResource(R.drawable.bg_seat_your_selection);
        }
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView seatButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            seatButton = itemView.findViewById(R.id.seat_viewholder_seat_button);

            seatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

            seatButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewClickInterface.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
