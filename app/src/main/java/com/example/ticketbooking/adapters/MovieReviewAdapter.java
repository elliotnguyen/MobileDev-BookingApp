package com.example.ticketbooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.model.Review;
import com.example.ticketbooking.R;

import java.util.ArrayList;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {
    ArrayList<Review> reviews;
    RecyclerViewClickInterface recyclerViewClickInterface;
    Context context;
    public MovieReviewAdapter(ArrayList<Review> reviews, RecyclerViewClickInterface recyclerViewClickInterface, Context context) {
        this.reviews = reviews;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
        this.context = context;
    }
    @NonNull
    @Override
    public MovieReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_holder, parent, false);
        return new MovieReviewAdapter.ReviewViewHolder(inflator);
    }
    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.review_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
