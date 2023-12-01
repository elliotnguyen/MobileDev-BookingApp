package com.example.ticketbooking.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.utils.DateModel;
import com.example.ticketbooking.R;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    ArrayList<DateModel> dates;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public DateAdapter(ArrayList<DateModel> dates, RecyclerViewClickInterface recyclerViewClickInterface) {
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
        DateModel dateModel = dates.get(position);
        String date = dateModel.getDate();
        String[] parts = date.split(", ");

        if (parts.length >= 2) {
            holder.day.setText(parts[0]);

            holder.date.setText(parts[1]);
        }

        if (dateModel.isSelected()) {
            holder.day.setTypeface(holder.day.getTypeface(), Typeface.BOLD);
            holder.day.setTextColor(context.getResources().getColor(R.color.blue));

            holder.date.setTypeface(holder.date.getTypeface(), Typeface.BOLD);
            holder.date.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.day.setTypeface(holder.day.getTypeface(), Typeface.NORMAL);
            holder.date.setTypeface(holder.date.getTypeface(), Typeface.NORMAL);
            holder.day.setTextColor(context.getResources().getColor(R.color.black));
            holder.date.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView day;
        //AppCompatButton date;
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
