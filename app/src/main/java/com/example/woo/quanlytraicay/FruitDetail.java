package com.example.woo.quanlytraicay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.Model.Product;

import java.text.DecimalFormat;
import java.util.Calendar;

public class FruitDetail extends AppCompatActivity {
    private TextView tv_detailName, tv_detailPrice, tv_detailOrigin, tv_detailExpiry, tv_detailExist, tv_detailAmount, tv_detailDescribe;
    private ImageView img_detailImage, btn_detailPlus, btn_detailBuy, btn_detailMinus;

    private Intent dIntent;
    private String id, name, origin, describe;
    private int price, image, expiry;
    private DecimalFormat dcf = new DecimalFormat("###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_detail);
        addControls();
        addEvents();
    }

    private void addControls() {
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
            id          = dIntent.getStringExtra("P_ID");
            name        = dIntent.getStringExtra("P_TEN");
            origin      = dIntent.getStringExtra("P_XX");
            describe    = dIntent.getStringExtra("P_MOTA");
            price       = dIntent.getIntExtra("P_GIA", -1);
            image       = dIntent.getIntExtra("P_HINH", -1);
            expiry      = dIntent.getIntExtra("P_HSD", -1);

            tv_detailName.setText(name);
            tv_detailPrice.setText("Giá: "+dcf.format(price)+"/kg");
            tv_detailDescribe.setText(describe);
            tv_detailOrigin.setText("Xuất xứ: "+origin);
            tv_detailExpiry.setText("HSD: "+expiry);
            img_detailImage.setImageResource(image);
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
                OrderActivity.orders.add(new Order("", id+name, Calendar.getInstance().getTime()+"", "", 1,price, image));
                Toast.makeText(FruitDetail.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
