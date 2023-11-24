package com.example.ticketbooking.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.R;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    ArrayList<Movie> movies;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;

    public MovieAdapter(ArrayList<Movie> movies, RecyclerViewClickInterface recyclerViewClickInterface, Context context) {
        this.movies = movies;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = View.inflate(parent.getContext(), R.layout.movie_holder, null);
        return new MovieAdapter.ViewHolder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieName.setText(movie.getTitle());
        String duration = movie.getDuration();
        int totalMinutes = Integer.parseInt(duration);

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String formattedTime = hours + "h" + minutes + "m";
        holder.movieDuration.setText(formattedTime);
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).placeholder(R.drawable.bg_image_main_activity).override(400,440).fitCenter().into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView movieDuration;
        ImageView movieImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.movie_holder_name);
            movieImage = itemView.findViewById(R.id.movie_holder_image);
            movieDuration = itemView.findViewById(R.id.movie_holder_duration);

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
