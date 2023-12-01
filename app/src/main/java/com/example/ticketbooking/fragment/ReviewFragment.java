package com.example.ticketbooking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.model.Review;
import com.example.ticketbooking.R;
import com.example.ticketbooking.repository.BookingRepository;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.adapters.MovieReviewAdapter;
import com.example.ticketbooking.viewmodels.MovieReviewsViewModel;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    RecyclerView reviewRecyclerView;
    RecyclerView.Adapter reviewAdapter;
    MovieReviewsViewModel reviewViewModel;
    ArrayList<Review> mReviews;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View reviewView = inflater.inflate(R.layout.review_fragment, container, false);
        reviewRecyclerView = reviewView.findViewById(R.id.fragment_review_view);

        reviewViewModel = new ViewModelProvider(this).get(MovieReviewsViewModel.class);
        mReviews = new ArrayList<>();

        reviewViewModel.getMovieReviews(BookingRepository.getInstance().getMovieId());
        ObserverAnyChange();

        return reviewView;
    }

    private void ObserverAnyChange() {
        reviewViewModel.getMovieReviews().observe(getViewLifecycleOwner(), reviews -> {
            if (reviews != null) {
                mReviews = reviews;
                ConfigureReviewRecyclerView();
            }
        });
    }

    private void ConfigureReviewRecyclerView() {
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reviewAdapter = new MovieReviewAdapter(mReviews, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onLongItemClick(int position) {
            }
        }, getActivity());
        reviewRecyclerView.setAdapter(reviewAdapter);
    }
}
