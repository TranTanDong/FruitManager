package com.example.woo.quanlytraicay.FruitManager;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import java.util.Collections;
import java.util.Comparator;

public class HistoryActivity extends AppCompatActivity {
    private TextView tv_hisTotal;
    private static View tv_hisIsEmpty;
    private RecyclerView rcv_history;
    public static ArrayList<Order> dsHistory = new ArrayList<>();
    private AdapterHistory adapterHistory;
    private ProgressDialog pr_his;

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
        pr_his.setMessage("Đang tải");
        pr_his.show();
        dsHistory.clear();
        mData.child("HISTORY").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order his = dataSnapshot.getValue(Order.class);
                dsHistory.add(new Order(his.getTen(), his.getThoiGian(), his.getMail(), his.getHinh(), his.getSoLuong(), his.getGia()));
                Collections.reverse(dsHistory);
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
        showStatusHistory();
        pr_his.hide();
    }

    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_history);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        tv_hisTotal = findViewById(R.id.tv_hisTotal);
        tv_hisIsEmpty = findViewById(R.id.tv_hisIsEmpty);
        pr_his  = new ProgressDialog(this);

        loadDataHistory();
        //Set list History
        rcv_history = findViewById(R.id.rcv_history);
        rcv_history.setLayoutManager(new LinearLayoutManager(this));
        adapterHistory  = new AdapterHistory(HistoryActivity.this, dsHistory, tv_hisTotal);
        rcv_history.setAdapter(adapterHistory);
    }
    public static void showStatusHistory() {
        if (dsHistory.size() == 0){
            tv_hisIsEmpty.setVisibility(View.VISIBLE);
        }else tv_hisIsEmpty.setVisibility(View.INVISIBLE);
    }
    private void addEvents() {

    }
}
