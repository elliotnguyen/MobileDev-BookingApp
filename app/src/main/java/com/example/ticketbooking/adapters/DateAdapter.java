package com.example.ticketbooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    ArrayList<String> dates;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public DateAdapter(ArrayList<String> dates, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.dates = dates;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View inflator = LayoutInflater.from(this.context).inflate(R.layout.viewholder_calendar, parent, false);
        return new DateAdapter.ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.ViewHolder holder, int position) {
        String date = dates.get(position);
        String[] parts = date.split(", ");

        if (parts.length >= 2) {
            holder.day.setText(parts[0]);
            holder.date.setText(parts[1]);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.viewholder_calendar_date);
            day = itemView.findViewById(R.id.viewholder_calendar_day);

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
