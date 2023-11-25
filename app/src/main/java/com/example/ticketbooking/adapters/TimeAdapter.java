package com.example.ticketbooking.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.Model.Cinema;
import com.example.ticketbooking.Model.TimeModel;
import com.example.ticketbooking.R;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    RecyclerViewClickInterface recyclerViewClickInterface;
    //Cinema cinema;
    ArrayList<TimeModel> times;
    Context context;

    /*public TimeAdapter(Cinema cinema, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.cinema = cinema;
    }*/
    public TimeAdapter(ArrayList<TimeModel> times, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.times = times;
    }

    @NonNull
    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_viewholder, parent, false);
        return new TimeAdapter.ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.ViewHolder holder, int position) {
        //TimeModel time = cinema.getTime().get(position);
        TimeModel time = times.get(position);
        if (time.getTime().equals("")) {
            holder.time.setBackgroundResource(R.drawable.bg_time_blocked);
            String realtime = String.valueOf(position + 9) + ":30";
            holder.time.setText(realtime);
            holder.time.setEnabled(false);
        } else {
            //holder.time.setBackgroundResource(R.drawable.bg_time_available);
            String realtime = String.valueOf(Integer.parseInt(time.getTime()) + 9) + ":30";
            holder.time.setText(realtime);
        }

        if (time.isSelected()) {
            holder.time.setTypeface(holder.time.getTypeface(), Typeface.BOLD);
            holder.time.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.time.setTextColor(context.getResources().getColor(R.color.black));
            //holder.time.setBackgroundResource(R.drawable.bg_time_available);
        }
    }

    @Override
    public int getItemCount() {
        //return cinema.getTime().size();
        return times.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Button time;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.viewholder_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewClickInterface.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
