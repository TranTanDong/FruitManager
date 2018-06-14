package com.example.woo.quanlytraicay.FruitManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.woo.quanlytraicay.Adapter.AdapterOrder;
import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rcv_order;
    public static ArrayList<Order> orders = new ArrayList<>();
    private AdapterOrder adapterOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addControls();
        addEvents();
    }

    private void addControls() {


        rcv_order   = findViewById(R.id.rcv_order);
        rcv_order.setLayoutManager(new LinearLayoutManager(this));
        adapterOrder  = new AdapterOrder(OrderActivity.this, orders);
        rcv_order.setAdapter(adapterOrder);
    }

    private void addEvents() {

    }

}
