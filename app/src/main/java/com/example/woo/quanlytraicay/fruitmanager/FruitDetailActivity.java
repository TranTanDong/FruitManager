package com.example.woo.quanlytraicay.fruitmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;

public class FruitDetailActivity extends AppCompatActivity {
    private TextView tvDetailName, tvDetailPrice, tvDetailOrigin, tvDetailExpiry, tvDetailExist, tvDetailAmount, tvDetailDescribe;
    private ImageView imgDetailImage, btnDetailPlus, btnDetailMinus;

    private Intent dIntent;
    private String mName, mOrigin, mDescribe, mImage;
    private int mPrice, mExpiry;
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
        getMenuInflater().inflate(R.menu.menu_check_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Xử lý thêm vào giỏ hàng
        if (id == R.id.check_ok) {
            if (OrderActivity.orders.isEmpty() == true){//Giỏ hàng trống
                OrderActivity.orders.add(new Order(mName, Calendar.getInstance().getTime().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), mImage, Integer.parseInt(tvDetailAmount.getText().toString()), mPrice));//Thêm vào giỏ
                Toast.makeText(FruitDetailActivity.this, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
            }else {
                int tmp = 0;
                for (Order i : OrderActivity.orders){//Kiểm tra trong giỏ
                    if (i.getTen().equals(mName)){//Số lượng tồn tại >= 10
                        if (i.getSoLuong() >= 10){//Hiện thông báo đạt mức tối đa
                            Toast.makeText(FruitDetailActivity.this, R.string.toast_maximum, Toast.LENGTH_SHORT).show();
                        }else {//Tồn tại số lượng <10
                            i.setSoLuong(i.getSoLuong()+Integer.parseInt(tvDetailAmount.getText().toString()));
                            Toast.makeText(FruitDetailActivity.this, "Đã tồn tại! Số lượng tăng thêm "+tvDetailAmount.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        tmp++;//Biến kiểm tra sản phẩm đã tồn tại trong giỏ chưa
                    }
                }
                if (tmp > (OrderActivity.orders.size()-1)){//Kiểm tra sp chưa tồn tại trong giỏ thì thêm vào
                    OrderActivity.orders.add(new Order(mName, Calendar.getInstance().getTime().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), mImage, 1, mPrice));
                    Toast.makeText(FruitDetailActivity.this, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //Ánh xạ
    private void addControls() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tb_fruitDetail);
        setSupportActionBar(toolbar);


        tvDetailName = findViewById(R.id.tv_detailName);
        tvDetailPrice = findViewById(R.id.tv_detailPrice);
        tvDetailOrigin = findViewById(R.id.tv_detailOrigin);
        tvDetailExpiry = findViewById(R.id.tv_detailExpiry);
        tvDetailExist = findViewById(R.id.tv_detailExist);
        tvDetailAmount = findViewById(R.id.tv_detailAmount);
        tvDetailDescribe = findViewById(R.id.tv_detailDescribe);
        btnDetailMinus = findViewById(R.id.btn_detailMinus);
        btnDetailPlus = findViewById(R.id.btn_detailPlus);
        imgDetailImage = findViewById(R.id.img_detailImage);

        //Set dữ liệu
        dIntent = getIntent();
        if (dIntent != null){
            mName = dIntent.getStringExtra("P_TEN");
            mOrigin = dIntent.getStringExtra("P_XX");
            mDescribe = dIntent.getStringExtra("P_MOTA");
            mPrice = dIntent.getIntExtra("P_GIA", -1);
            mImage       = dIntent.getStringExtra("P_HINH");
            mExpiry = dIntent.getIntExtra("P_HSD", -1);

            tvDetailName.setText(mName);
            tvDetailPrice.setText("Giá: "+dcf.format(mPrice)+"/kg");
            tvDetailDescribe.setText(mDescribe);
            tvDetailOrigin.setText("Xuất xứ: "+ mOrigin);
            tvDetailExpiry.setText("HSD: "+ mExpiry);
            Picasso.get().load(mImage).into(imgDetailImage);
        }




    }

    private void addEvents() {
        //Giảm số lượng
        btnDetailMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(tvDetailAmount.getText().toString());
                if ( a > 1){//Số lượng > 1 thì giảm
                    a--;
                    tvDetailAmount.setText(a+"");
                }
            }
        });

        //Tăng số lượng
        btnDetailPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(tvDetailAmount.getText().toString());
                if ( a < 10){//Số lượng < 10 thig tăng
                    a++;
                    tvDetailAmount.setText(a+"");
                }else if (a == 10){//Số lượng = 10 thông báo đã đạt tối đa
                    Toast.makeText(FruitDetailActivity.this, R.string.toast_maximum, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
