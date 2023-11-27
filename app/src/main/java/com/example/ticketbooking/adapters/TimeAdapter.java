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
    ArrayList<TimeModel> times;
    Context context;

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
        TimeModel time = times.get(position);
        holder.time.setText(TimeModel.showTimeWithFormat(String.valueOf(position)));
        if (time.isBlocked()) {
            holder.time.setBackgroundResource(R.drawable.bg_time_blocked);
            holder.time.setEnabled(false);
        } else {
            if (time.isSelected()) {
                holder.time.setTypeface(holder.time.getTypeface(), Typeface.BOLD);
                holder.time.setTextColor(context.getResources().getColor(R.color.blue));
            } else {
                holder.time.setTypeface(holder.time.getTypeface(), Typeface.NORMAL);
                holder.time.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
