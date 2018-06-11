package com.example.woo.quanlytraicay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.woo.quanlytraicay.Adapter.AdapterOrder;
import com.example.woo.quanlytraicay.Model.Product;

import java.util.ArrayList;

public class Order extends AppCompatActivity {
    private RecyclerView rcv_order;
    private ArrayList<Product> products = new ArrayList<>();
    private AdapterOrder adapterOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addControls();
        addEvents();
    }

    private void addControls() {



        products.add(new Product("1", "Cam", R.drawable.ic_cam, "Mô tả cam", "Xuất xứ cam", 40000, 10));
        products.add(new Product("2","Xoài", R.drawable.ic_xoai, "Mô tả Xoài", "Xuất xứ Xoài", 25000, 10));
        products.add(new Product("3", "Mận", R.drawable.ic_man, "Mô tả Mận", "Xuất xứ Mận", 15000, 10));
        products.add(new Product("4", "Dâu", R.drawable.dau, "Mô tả Dâu", "Xuất xứ Dâu", 70000, 10));
        products.add(new Product("5", "Cam", R.drawable.ic_cam, "Mô tả cam", "Xuất xứ cam", 40000, 10));
        products.add(new Product("6", "Xoài", R.drawable.ic_xoai, "Mô tả Xoài", "Xuất xứ Xoài", 25000, 10));
        products.add(new Product("7", "Mận", R.drawable.ic_man, "Mô tả Mận", "Xuất xứ Mận", 15000, 10));
        products.add(new Product("8", "Dâu", R.drawable.dau, "Mô tả Dâu", "Xuất xứ Dâu", 70000, 10));

        rcv_order   = findViewById(R.id.rcv_order);
        rcv_order.setLayoutManager(new LinearLayoutManager(this));
        adapterOrder  = new AdapterOrder(this, products);
        rcv_order.setAdapter(adapterOrder);
    }

    private void addEvents() {

    }

}
