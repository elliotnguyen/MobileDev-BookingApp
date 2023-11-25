package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ticketbooking.Model.Purchase;
import com.example.ticketbooking.adapters.PurchaseHistoryAdapter;
import com.example.ticketbooking.adapters.RecyclerViewClickInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PurchaseHistoryActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    RecyclerView purchaseHistoryRecyclerView;
    RecyclerView.Adapter purchaseHistoryAdapter;
    DatabaseReference userRef;

    ArrayList<Purchase> purchases;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        mAuth = FirebaseAuth.getInstance();
        userRef = myRef.child("users").child(mAuth.getCurrentUser().getUid());

        getPurchasesData(userRef);
    }

    private void getPurchasesData(DatabaseReference userRef) {
        userRef.child("purchases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchases = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Purchase purchase = Purchase.fromFirebaseData(dataSnapshot);
                    purchases.add(purchase);
                }
                handlePurchaseRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handlePurchaseRecyclerView() {
        purchaseHistoryRecyclerView = findViewById(R.id.purchase_history_recycler_view);
        purchaseHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        purchaseHistoryAdapter = new PurchaseHistoryAdapter(purchases, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongItemClick(int position) {

            }
        });
        purchaseHistoryRecyclerView.setAdapter(purchaseHistoryAdapter);
    }
}