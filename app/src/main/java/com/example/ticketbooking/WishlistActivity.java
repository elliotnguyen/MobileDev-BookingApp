package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ticketbooking.Model.Movie;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.example.ticketbooking.adapters.WishlistAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class WishlistActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    RecyclerView wishlistRecyclerView;
    RecyclerView.Adapter wishlistAdapter;
    DatabaseReference userRef;
    ArrayList<Movie> wishList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        mAuth = FirebaseAuth.getInstance();
        userRef = myRef.child("users").child(mAuth.getCurrentUser().getUid());

        getWishlistData(userRef);

        ImageView backBtn = findViewById(R.id.activity_wishlist_backward_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getWishlistData(DatabaseReference userRef) {
        userRef.child("wishlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> wishlist = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    wishlist.add(dataSnapshot.getValue(String.class));
                }
                wishList = new ArrayList<>();
                setWishlistRecyclerView(wishlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void setWishlistRecyclerView(ArrayList<String> wishlistString) {
        final int size = wishlistString.size();
        for (String movie : wishlistString) {
            myRef.child("movies").child(movie).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Movie wishlistMovie = Movie.fromFirebaseData(snapshot);
                    wishList.add(wishlistMovie);

                    if (wishList.size() == size) {
                        handleWishlistRecyclerView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void handleWishlistRecyclerView() {
        wishlistRecyclerView = findViewById(R.id.wishlist_recycler_view);
        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wishlistAdapter = new WishlistAdapter(wishList, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongItemClick(int position) {
                removeMovieFromWishlist(wishList.get(position), position);
                wishlistAdapter.notifyItemRemoved(position);
            }
        }, this);
        wishlistRecyclerView.setAdapter(wishlistAdapter);
    }

    private void removeMovieFromWishlist(Movie movie, int position) {
        userRef.child("wishlist").child(movie.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    wishList.remove(position);
                    Toast.makeText(WishlistActivity.this, "Removed " + movie.getTitle() + " from wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WishlistActivity.this, "Server excess rating...", Toast.LENGTH_SHORT).show();
                }
            }
        });;
    }
}