package com.example.woo.quanlytraicay.FruitManager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.Adapter.AdapterOrder;
import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rcv_order;
    public static ArrayList<Order> orders = new ArrayList<>();
    private AdapterOrder adapterOrder;

    private TextView tv_orderBigSum;
    private Button btn_orderPay, btn_orderBuyCont;

    private FirebaseAuth mAuth;
    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addControls();
        addEvents();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ORDER");
//        databaseReference.removeValue();
//        for (Order i : OrderActivity.orders){
//            mData.child("ORDER").push().setValue(i);
//        }
//        //OrderActivity.orders.clear();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.history) {
            startActivityForResult(new Intent(OrderActivity.this, HistoryActivity.class), 10);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();

        tv_orderBigSum      = findViewById(R.id.tv_orderBigSum);
        btn_orderPay        = findViewById(R.id.btn_orderPay);
        btn_orderBuyCont    = findViewById(R.id.btn_orderBuyCont);

        //Lấy dữ liệu từ FB về

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
