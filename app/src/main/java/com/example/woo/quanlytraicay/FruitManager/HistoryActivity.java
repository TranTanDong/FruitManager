package com.example.woo.quanlytraicay.FruitManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.woo.quanlytraicay.Adapter.AdapterHistory;
import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private TextView tv_hisTotal;
    private RecyclerView rcv_history;
    public static ArrayList<Order> dsHistory = new ArrayList<>();
    private AdapterHistory adapterHistory;

    private FirebaseAuth mAuth;
    private DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        addControls();
        addEvents();
    }

    private void loadDataHistory() {
        dsHistory.clear();
        mData.child("HISTORY").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order his = dataSnapshot.getValue(Order.class);
                dsHistory.add(new Order(his.getTen(), his.getThoiGian(), his.getMail(), his.getHinh(), his.getSoLuong(), his.getGia()));
                adapterHistory.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        tv_hisTotal = findViewById(R.id.tv_hisTotal);

        loadDataHistory();
        //Set list History
        rcv_history = findViewById(R.id.rcv_history);
        rcv_history.setLayoutManager(new LinearLayoutManager(this));
        adapterHistory  = new AdapterHistory(HistoryActivity.this, dsHistory, tv_hisTotal);
        rcv_history.setAdapter(adapterHistory);
    }

    private void addEvents() {

    }
}
