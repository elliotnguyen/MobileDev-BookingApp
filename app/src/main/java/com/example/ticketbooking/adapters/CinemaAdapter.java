package com.example.ticketbooking.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.Model.Cinema;
import com.example.ticketbooking.Model.TimeModel;
import com.example.ticketbooking.R;
import com.example.ticketbooking.Repository.BookingRepository;

import java.util.ArrayList;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder> {
    ArrayList<Cinema> cinemas;
    RecyclerViewClickInterface recyclerViewClickInterface;
    ArrayList<RecyclerView.Adapter> TimeAdapter;
    Context context;
    int previousTimePosition = -1;
    //String currentCinemaPosition = "";
    int previousCinemaPosition = -1;
    public CinemaAdapter(ArrayList<Cinema> cinemas, RecyclerViewClickInterface recyclerViewClickInterface, Context context) {
        this.cinemas = cinemas;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
        TimeAdapter = new ArrayList<>();
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
        Cinema cinema = cinemas.get(position);
        //holder.cinemaName.setText(cinemas.get(position).getName());
        holder.cinemaName.setText(cinema.getName());
        holder.timeRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        final int pos = position;
        //Cinema cinema = cinemas.get(position);
        ArrayList<TimeModel> times = cinema.getTime();
        RecyclerView.Adapter timeAdapter = new TimeAdapter(times, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                //TimeModel time = cinema.getTime().get(position);
                TimeModel time = times.get(position);
                if (time.getTime().equals("")) {
                    return;
                }

                if (previousTimePosition != -1 && previousCinemaPosition != -1) {
                    //cinemas.get(pos).getTime().get(previousTimePosition).setSelected(false);
                    ArrayList<TimeModel> previousTimes = cinemas.get(previousCinemaPosition).getTime();
                    previousTimes.get(previousTimePosition).setSelected(false);
                    TimeAdapter.get(previousCinemaPosition).notifyItemChanged(previousTimePosition);
                }
                //cinemas.get(pos).getTime().get(position).setSelected(true);
                times.get(position).setSelected(true);
                TimeAdapter.get(pos).notifyItemChanged(position);
                previousTimePosition = position;
                previousCinemaPosition = pos;
                //currentCinemaPosition = cinema.getId();
                BookingRepository.getInstance().setCinemaId(cinema.getId());
                BookingRepository.getInstance().setTime(time.getTime());
            }

            @Override
            public void onLongItemClick(int position) {
            }
        });
        holder.timeRecyclerView.setAdapter(timeAdapter);
        if (!this.TimeAdapter.isEmpty() && TimeAdapter.size() > position) {
            this.TimeAdapter.set(position, timeAdapter);
        } else {
            this.TimeAdapter.add(timeAdapter);
        }

        /*if (this.TimeAdapter.size() == 4) {
            if (currentCinemaPosition.equals(cinemas.get(position).getId())) {
                cinemas.get(position).getTime().get(previousTimePosition).setSelected(false);
                TimeAdapter.get(pos).notifyItemChanged(previousTimePosition);
                previousTimePosition = -1;
                currentCinemaPosition = "";
            }
        }*/
        /*if (currentCinemaPosition.equals(cinemas.get(position).getId())) {
            cinemas.get(position).getTime().get(previousTimePosition).setSelected(false);
            timeAdapter.notifyItemChanged(previousTimePosition);
        }*/
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
