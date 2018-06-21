package com.example.woo.quanlytraicay.FruitManager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

public class FruitDetail extends AppCompatActivity {
    private TextView tv_detailName, tv_detailPrice, tv_detailOrigin, tv_detailExpiry, tv_detailExist, tv_detailAmount, tv_detailDescribe;
    private ImageView img_detailImage, btn_detailPlus, btn_detailBuy, btn_detailMinus;

    private Intent dIntent;
    private String id, name, origin, describe, image;
    private int price, expiry;
    private DecimalFormat dcf = new DecimalFormat("###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_detail);
        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ic_giohang) {
            startActivityForResult(new Intent(FruitDetail.this, OrderActivity.class), 9);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addControls() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tb_fruitDetail);
        setSupportActionBar(toolbar);


        tv_detailName       = findViewById(R.id.tv_detailName);
        tv_detailPrice      = findViewById(R.id.tv_detailPrice);
        tv_detailOrigin     = findViewById(R.id.tv_detailOrigin);
        tv_detailExpiry     = findViewById(R.id.tv_detailExpiry);
        tv_detailExist      = findViewById(R.id.tv_detailExist);
        tv_detailAmount     = findViewById(R.id.tv_detailAmount);
        tv_detailDescribe   = findViewById(R.id.tv_detailDescribe);
        btn_detailMinus     = findViewById(R.id.btn_detailMinus);
        btn_detailPlus      = findViewById(R.id.btn_detailPlus);
        btn_detailBuy       = findViewById(R.id.btn_detailBuy);
        img_detailImage     = findViewById(R.id.img_detailImage);

        //Set dữ liệu
        dIntent = getIntent();
        if (dIntent != null){
            name        = dIntent.getStringExtra("P_TEN");
            origin      = dIntent.getStringExtra("P_XX");
            describe    = dIntent.getStringExtra("P_MOTA");
            price       = dIntent.getIntExtra("P_GIA", -1);
            image       = dIntent.getStringExtra("P_HINH");
            expiry      = dIntent.getIntExtra("P_HSD", -1);

            tv_detailName.setText(name);
            tv_detailPrice.setText("Giá: "+dcf.format(price)+"/kg");
            tv_detailDescribe.setText(describe);
            tv_detailOrigin.setText("Xuất xứ: "+origin);
            tv_detailExpiry.setText("HSD: "+expiry);
            Picasso.get().load(image).into(img_detailImage);
        }




    }

    private void addEvents() {
        //Giảm số lượng
        btn_detailMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(tv_detailAmount.getText().toString());
                if ( a > 0){
                    a--;
                    tv_detailAmount.setText(a+"");
                }
            }
        });

        //Tăng số lượng
        btn_detailPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(tv_detailAmount.getText().toString());
                if ( a < 10){
                    a++;
                    tv_detailAmount.setText(a+"");
                }
            }
        });

        //Thêm vào giỏ hàng
        btn_detailBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderActivity.orders.isEmpty() == true){
                    OrderActivity.orders.add(new Order(name, Calendar.getInstance().getTime().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), image, Integer.parseInt(tv_detailAmount.getText().toString()), price));
                    Toast.makeText(FruitDetail.this, "Đã thêm vào giỏ hàng!Empty"+Calendar.getInstance().getTime().toString(), Toast.LENGTH_SHORT).show();
                }else {
                    int tmp = 0;
                    for (Order i : OrderActivity.orders){
                        if (i.getTen().equals(name)){
                            if (i.getSoLuong() >= 10){
                                Toast.makeText(FruitDetail.this, "Sản phẩm đã đạt số lượng tối đa cho phép!", Toast.LENGTH_SHORT).show();
                            }else {
                                i.setSoLuong(i.getSoLuong()+Integer.parseInt(tv_detailAmount.getText().toString()));
                                Toast.makeText(FruitDetail.this, "Đã thêm vào giỏ hàng!Contained", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            tmp++;
                        }
                    }
                    if (tmp > (OrderActivity.orders.size()-1)){
                        OrderActivity.orders.add(new Order(name, Calendar.getInstance().getTime().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), image, 1, price));
                        Toast.makeText(FruitDetail.this, "Đã thêm vào giỏ hàng!Not Contain"+Calendar.getInstance().getTime().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
