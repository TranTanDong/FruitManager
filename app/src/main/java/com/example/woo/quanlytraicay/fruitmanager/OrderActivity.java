package com.example.woo.quanlytraicay.fruitmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.adapter.AdapterOrder;
import com.example.woo.quanlytraicay.model.Depot;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.model.User;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderActivity extends AppCompatActivity {
    private static View tvOrderIsEmpty;
    private RecyclerView rcvOrder;
    private AdapterOrder adapterOrder;

    private TextView tvOrderBigSum;
    private Button btnOrderPay, btnOrderBuyCont;

    private FirebaseAuth mAuth;
    private DatabaseReference mData;

    private SimpleDateFormat dcf = new SimpleDateFormat("HH:mm | dd-MM-yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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

    //Ánh xạ
    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_order);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();

        tvOrderIsEmpty = findViewById(R.id.tv_orderIsEmpty);
        tvOrderBigSum = findViewById(R.id.tv_orderBigSum);
        btnOrderPay = findViewById(R.id.btn_orderPay);
        btnOrderBuyCont = findViewById(R.id.btn_orderBuyCont);
        showStatusCart();
        //Lấy dữ liệu từ FB về

        rcvOrder = findViewById(R.id.rcv_order);
        rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        adapterOrder  = new AdapterOrder(OrderActivity.this, MainActivity.orders, tvOrderBigSum);
        rcvOrder.setAdapter(adapterOrder);

        //Toast.makeText(this, MainActivity.dsDepot.size()+"", Toast.LENGTH_SHORT).show();
    }

    //Tình trạng giỏ hàng == Trống or Không trống
    public static void showStatusCart() {
        if (MainActivity.orders.size() == 0){
            tvOrderIsEmpty.setVisibility(View.VISIBLE);
        }else tvOrderIsEmpty.setVisibility(View.INVISIBLE);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapterOrder.notifyDataSetChanged();
//    }

    private void addEvents() {
        //Tiếp tục mua hàng
        btnOrderBuyCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                finish();
            }
        });

        //Thanh toán
        btnOrderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.orders.size() > 0){
                    xuLyPay();
                }else Toast.makeText(OrderActivity.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //Xử lý thanh toán
    private void xuLyPay() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pay, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final EditText edt_p_Name, edt_p_Phone, edt_p_Address;
        TextView tv_pTotal;
        edt_p_Name     = dialogView.findViewById(R.id.edt_p_Name);
        edt_p_Phone    = dialogView.findViewById(R.id.edt_p_Phone);
        edt_p_Address  = dialogView.findViewById(R.id.edt_p_Address);
        tv_pTotal      = dialogView.findViewById(R.id.tv_pTotal);

        //Set dữ liệu
        tv_pTotal.setText(tvOrderBigSum.getText().toString());
        setInfPay(edt_p_Name, edt_p_Phone, edt_p_Address);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                for (int j=0; j<MainActivity.orders.size(); j++){
//                    for (int k=0; k<MainActivity.dsDepot.size(); k++){
//                        if ((MainActivity.dsDepot.get(k).getTenTraiCay().equals(MainActivity.orders.get(j).getTen()) && (MainActivity.dsDepot.get(k).getSoLuong()>=MainActivity.orders.get(j).getSoLuong()))){
//                            MainActivity.dsDepot.get(k).setSoLuong(MainActivity.dsDepot.get(k).getSoLuong()-MainActivity.orders.get(j).getSoLuong());
//                            MainActivity.orders.get(j).setThoiGian(dcf.format(Calendar.getInstance().getTime()).toString());
//                            mData.child("HISTORY").child(mAuth.getCurrentUser().getUid().toString()).push().setValue(MainActivity.orders.get(j));
//                        }
//                    }
//                }
                //Toast.makeText(getApplicationContext(), MainActivity.dsDepot.size()+"", Toast.LENGTH_SHORT).show();
                for (Depot j : MainActivity.dsDepot){
                    for (Order i : MainActivity.orders){
                        if (i.getTen().equals(j.getTenTraiCay())){
                            j.setSoLuong(j.getSoLuong()-i.getSoLuong());
                        }
                    }
                }

                for (Order i : MainActivity.orders){
                    i.setThoiGian(dcf.format(Calendar.getInstance().getTime()).toString());
                    mData.child("HISTORY").child(mAuth.getCurrentUser().getUid().toString()).push().setValue(i);
                }
                MainActivity.orders.clear();
                adapterOrder.notifyDataSetChanged();

                DatabaseReference data = FirebaseDatabase.getInstance().getReference("DEPOT").child("NEW");
                data.removeValue();
                mData.child("DEPOT").setValue(MainActivity.dsDepot);

                MainActivity.dsDepot.clear();

                showStatusCart();
                tvOrderBigSum.setText("0đ");
                Toast.makeText(OrderActivity.this, "Đã thanh toán thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //Set thông tin thanh toán
    private void setInfPay(final EditText name, final EditText phone, final EditText address) {
        //Set thông tin Account
        mData.child("USER").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = mAuth.getCurrentUser().getEmail().toString();
                User user = dataSnapshot.getValue(User.class);
                if (email.equals(user.getEmail())){
                    name.setText(user.getName());
                    phone.setText(user.getPhone());
                    address.setText(user.getAddress());
                }
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
}
