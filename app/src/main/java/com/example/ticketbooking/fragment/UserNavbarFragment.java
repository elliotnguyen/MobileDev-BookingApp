package com.example.ticketbooking.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ticketbooking.PurchaseHistoryActivity;
import com.example.ticketbooking.R;
import com.example.ticketbooking.WishlistActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserNavbarFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageView profileImage;

    //TextView header;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userNavbarView = inflater.inflate(R.layout.user_navbar_fragment, container, false);
        profileImage = userNavbarView.findViewById(R.id.user_navbar_fragment_profile_image);

        DatabaseReference userRef = myRef.child("users").child(mAuth.getCurrentUser().getUid());
        getUserData(userRef);

        handleUserNavbar();

        return userNavbarView;
    }

    public void getUserData(DatabaseReference userRef) {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("profilePic").getValue().toString()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleUserNavbar() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubMenu(view);
            }

            private void showSubMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_purchase) {
                            Intent intent = new Intent(getActivity(), PurchaseHistoryActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.action_wishlist) {
                            Intent intent = new Intent(getActivity(), WishlistActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                popupMenu.setGravity(Gravity.END);
                popupMenu.show();
            }
        });
    }
}
