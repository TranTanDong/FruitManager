package com.example.woo.quanlytraicay.fruitmanager;

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

import com.example.woo.quanlytraicay.adapter.AdapterHistory;
import com.example.woo.quanlytraicay.firebase.FBDatabase;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    private TextView tvHistoryTotal;
    private static View tvHistoryIsEmpty;
    private RecyclerView rcvHistory;
    public static ArrayList<Order> dsHistory = new ArrayList<>();
    private AdapterHistory adapterHistory;
    private ProgressDialog progressDialog;

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
        progressDialog.setMessage("Đang tải");
        progressDialog.show();
        dsHistory.clear();
        mData.child("HISTORY").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order his = dataSnapshot.getValue(Order.class);
                dsHistory.add(new Order(his.getTen(), his.getThoiGian(), his.getMail(), his.getHinh(), his.getSoLuong(), his.getGia()));
                Collections.reverse(dsHistory);
                adapterHistory.notifyDataSetChanged();
            }
        });
        showStatusHistory();
        progressDialog.hide();
    }

    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_history);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        tvHistoryTotal = findViewById(R.id.tv_hisTotal);
        tvHistoryIsEmpty = findViewById(R.id.tv_hisIsEmpty);
        progressDialog = new ProgressDialog(this);

        loadDataHistory();
        //Set list History
        rcvHistory = findViewById(R.id.rcv_history);
        rcvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapterHistory  = new AdapterHistory(HistoryActivity.this, dsHistory, tvHistoryTotal);
        rcvHistory.setAdapter(adapterHistory);
    }
    public static void showStatusHistory() {
        if (dsHistory.size() == 0){
            tvHistoryIsEmpty.setVisibility(View.VISIBLE);
        }else tvHistoryIsEmpty.setVisibility(View.INVISIBLE);
    }
    private void addEvents() {

    }
}
