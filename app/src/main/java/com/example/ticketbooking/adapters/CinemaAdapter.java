package com.example.ticketbooking.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.Model.Cinema;
import com.example.ticketbooking.R;

import java.util.ArrayList;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder> {
    ArrayList<Cinema> cinemas;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;
    public CinemaAdapter(ArrayList<Cinema> cinemas, RecyclerViewClickInterface recyclerViewClickInterface, Context context) {
        this.cinemas = cinemas;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public CinemaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this.context = parent.getContext();
        View inflator = View.inflate(parent.getContext(), R.layout.cinema_time_viewholder, null);
        return new CinemaAdapter.ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaAdapter.ViewHolder holder, int position) {
        holder.cinemaName.setText(cinemas.get(position).getName());
        holder.timeRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        TimeAdapter timeAdapter = new TimeAdapter(cinemas.get(position), new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onLongItemClick(int position) {
            }
        });
        holder.timeRecyclerView.setAdapter(timeAdapter);
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaName;
        RecyclerView timeRecyclerView;
        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            cinemaName = itemView.findViewById(R.id.cinema_name);
            timeRecyclerView = itemView.findViewById(R.id.cinema_time_recyclerview);
        }
    }
}
