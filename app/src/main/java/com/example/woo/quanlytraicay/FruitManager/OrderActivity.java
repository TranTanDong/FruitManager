package com.example.woo.quanlytraicay.FruitManager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.Adapter.AdapterOrder;
import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rcv_order;
    public static ArrayList<Order> orders = new ArrayList<>();
    private AdapterOrder adapterOrder;

    private TextView tv_orderBigSum;
    private Button btn_orderPay, btn_orderBuyCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addControls();
        addEvents();
    }

    private void addControls() {
        tv_orderBigSum      = findViewById(R.id.tv_orderBigSum);
        btn_orderPay        = findViewById(R.id.btn_orderPay);
        btn_orderBuyCont    = findViewById(R.id.btn_orderBuyCont);

        rcv_order   = findViewById(R.id.rcv_order);
        rcv_order.setLayoutManager(new LinearLayoutManager(this));
        adapterOrder  = new AdapterOrder(OrderActivity.this, orders, tv_orderBigSum);
        rcv_order.setAdapter(adapterOrder);
    }

    private void addEvents() {
        //Tiếp tục mua hàng
        btn_orderBuyCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                finish();
            }
        });

        //Thanh toán
        btn_orderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderActivity.this, "Đợi xíu anh 2 à", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
