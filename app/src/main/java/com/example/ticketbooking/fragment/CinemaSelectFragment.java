package com.example.ticketbooking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketbooking.utils.DateUtils;
import com.example.ticketbooking.model.Cinema;
import com.example.ticketbooking.utils.DateModel;
import com.example.ticketbooking.utils.TimeModel;
import com.example.ticketbooking.R;
import com.example.ticketbooking.repository.BookingRepository;
import com.example.ticketbooking.adapters.CinemaAdapter;
import com.example.ticketbooking.adapters.DateAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.adapters.TimeAdapter;
import com.example.ticketbooking.viewmodels.CinemaSelectingViewModel;

import java.util.ArrayList;

public class CinemaSelectFragment extends Fragment {
    RecyclerView dateRecyclerView;
    RecyclerView.Adapter dateAdapter;
    RecyclerView cinemaRecyclerView;
    RecyclerView.Adapter cinemaAdapter;
    ArrayList<DateModel> dates;
    ArrayList<Cinema> mCinemas;
    CinemaSelectingViewModel cinemaSelectingViewModel;
    int dateChosenPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cinemaSelectView = inflater.inflate(R.layout.cinema_select_fragment, container, false);
        dateRecyclerView = cinemaSelectView.findViewById(R.id.fragment_calendar_view);
        cinemaRecyclerView = cinemaSelectView.findViewById(R.id.fragment_cinema_view);

        handleDateRecylerView(cinemaSelectView);
        cinemaSelectingViewModel = new ViewModelProvider(this).get(CinemaSelectingViewModel.class);
        ObserveAnyChange();
        return cinemaSelectView;
    }
    private void handleDateRecylerView(View view) {
        dates = new ArrayList<>();
        String currentDate = DateUtils.getCurrentDate();
        for (int i = 0; i < 10; i++) {
            dates.add(new DateModel(currentDate, false));
            currentDate = DateUtils.getNextDay(currentDate);
        }

        dateRecyclerView = view.findViewById(R.id.fragment_calendar_view);
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        dateAdapter = new DateAdapter(dates, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
                if (dateChosenPosition != -1) {
                    dates.get(dateChosenPosition).setSelected(false);
                    dateAdapter.notifyItemChanged(dateChosenPosition);
                }
                dates.get(position).setSelected(true);
                dateAdapter.notifyItemChanged(position);
                dateChosenPosition = position;

                DateModel date = dates.get(position);

                BookingRepository.getInstance().setDate(date.getDate());
                cinemaSelectingViewModel.getCinemaInDate(DateModel.getDay(date.getDate()), BookingRepository.getInstance().getMovieId());
            }

            @Override
            public void onLongItemClick(int position) {
            }
        });
        dateRecyclerView.setAdapter(dateAdapter);
    }
    private void ObserveAnyChange() {
        cinemaSelectingViewModel.getCinemas().observe(getViewLifecycleOwner(), new Observer<ArrayList<Cinema>>() {
            @Override
            public void onChanged(ArrayList<Cinema> cinemas) {
                if (cinemas != null) {
                    mCinemas = cinemas;
                    ConfigureCinemaRecyclerView(getView());
                }
            }
        });
    }
    private void ConfigureCinemaRecyclerView(View view) {
        ArrayList<RecyclerView.Adapter> timeAdapters = new ArrayList<>();
        for (int i = 0; i < mCinemas.size(); i++) {
            final Cinema cinema = mCinemas.get(i);
            final int idx = i;
            timeAdapters.add(new TimeAdapter(cinema.getTime(), new RecyclerViewClickInterface() {
                @Override
                public void onItemClick(int position) {
                    TimeAdapter timeAdapter = (TimeAdapter) timeAdapters.get(idx);
                    TimeModel time = cinema.getTime().get(position);
                    if (time.isBlocked()) {
                        return;
                    }

                    if (BookingRepository.getInstance().getCinemaId().equals(String.valueOf(idx)) && BookingRepository.getInstance().getTime().equals(time.getTime())) {
                        return;
                    }

                    if (BookingRepository.getInstance().getCinemaId().equals(String.valueOf(idx))) {
                        for (int j = 0; j < mCinemas.get(idx).getTime().size(); j++) {
                            if (mCinemas.get(idx).getTime().get(j).getTime().equals(BookingRepository.getInstance().getTime())) {
                                mCinemas.get(idx).getTime().get(j).setSelected(false);
                                timeAdapter.notifyItemChanged(j);
                                break;
                            }
                        }
                    } else {
                        if (BookingRepository.getInstance().getCinemaId().isEmpty()) {
                            BookingRepository.getInstance().setCinemaId(String.valueOf(idx));
                        } else {
                            int previousCinemaId = Integer.parseInt(BookingRepository.getInstance().getCinemaId());
                            for (int j = 0; j < mCinemas.get(previousCinemaId).getTime().size(); j++) {
                                if (mCinemas.get(previousCinemaId).getTime().get(j).getTime().equals(BookingRepository.getInstance().getTime())) {
                                    mCinemas.get(previousCinemaId).getTime().get(j).setSelected(false);
                                    timeAdapters.get(previousCinemaId).notifyItemChanged(j);
                                    break;
                                }
                            }
                            BookingRepository.getInstance().setCinemaId(String.valueOf(idx));
                        }
                    }

                    mCinemas.get(idx).getTime().get(position).setSelected(true);
                    timeAdapter.notifyItemChanged(position);

                    BookingRepository.getInstance().setTime(time.getTime());
                    BookingRepository.getInstance().setCinemaName(cinema.getName());
                }

                @Override
                public void onLongItemClick(int position) {
                }
            }));
        }

        cinemaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        cinemaAdapter = new CinemaAdapter(mCinemas, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onLongItemClick(int position) {
            }
        }, getActivity(), timeAdapters);
        cinemaRecyclerView.setAdapter(cinemaAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        dateChosenPosition = -1;
    }

    @Override
    public void onResume() {
        super.onResume();
        dateChosenPosition = -1;
    }
}
