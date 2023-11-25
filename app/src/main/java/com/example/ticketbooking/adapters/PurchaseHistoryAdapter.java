package com.example.ticketbooking.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.Model.Purchase;
import com.example.ticketbooking.PurchaseHistoryActivity;
import com.example.ticketbooking.R;
import com.example.ticketbooking.Repository.BookingRepository;

import java.util.ArrayList;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {
    ArrayList<Purchase> purchases;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public PurchaseHistoryAdapter(ArrayList<Purchase> purchases, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.purchases = purchases;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public PurchaseHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = View.inflate(parent.getContext(), R.layout.purchase_history_viewholder, null);
        return new PurchaseHistoryAdapter.ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseHistoryAdapter.ViewHolder holder, int position) {
        Purchase purchase = purchases.get(position);
        holder.movieName.setText(purchase.getMovieName());
        holder.cinemaName.setText(purchase.getCinemaName());
        holder.time.setText(purchase.getTime());
        holder.date.setText(purchase.getDate());

        String newSeats = "";
        int size = purchase.getSeat().size();
        for (int i = 0; i <  size; i++) {
            newSeats = newSeats + purchase.getSeat().get(i);
            if (i != size - 1) {
                newSeats += ",";
            }
        }
        holder.seat.setText(newSeats);
        holder.status.setText(purchase.getStatus());

        if (purchase.getStatus().equals("booked")) {
            holder.status.setText("e-ticket has been released");
            holder.status.setBackgroundResource(R.drawable.bg_ticket_purchase_done);
        } else {
            holder.status.setBackgroundResource(R.drawable.bg_ticket_purchase_inprogress);
        }
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView cinemaName;
        TextView time;
        TextView date;
        TextView seat;
        TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.ticket_movie_name);
            cinemaName = itemView.findViewById(R.id.ticket_cinema);
            time = itemView.findViewById(R.id.ticket_time);
            date = itemView.findViewById(R.id.ticket_date);
            seat = itemView.findViewById(R.id.ticket_seat);
            status = itemView.findViewById(R.id.ticket_status);

            itemView.setOnClickListener(view -> {
                recyclerViewClickInterface.onItemClick(getAdapterPosition());
            });

            itemView.setOnLongClickListener(view -> {
                recyclerViewClickInterface.onLongItemClick(getAdapterPosition());
                return true;
            });
        }
    }
}
