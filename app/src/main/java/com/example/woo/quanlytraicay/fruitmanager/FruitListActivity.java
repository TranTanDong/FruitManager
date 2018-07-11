package com.example.woo.quanlytraicay.fruitmanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.woo.quanlytraicay.adapter.AdapterFruit;
import com.example.woo.quanlytraicay.abstracts.FBDatabase;
import com.example.woo.quanlytraicay.model.Depot;
import com.example.woo.quanlytraicay.ui.IFruit;
import com.example.woo.quanlytraicay.model.Product;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FruitListActivity extends AppCompatActivity implements IFruit, SearchView.OnQueryTextListener {
    private ProgressDialog progressDialog;
    private RecyclerView rcvFruitList;
    public static ArrayList<Product> dsFruit = new ArrayList<>();
    private AdapterFruit adapterFruit;
    private SearchView searchView;

    private DatabaseReference mData;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_list);
        addControls();
        loadDataFromFB();
        addEvents();
    }

    //Cập nhật kho mỗi khi quay lại activity này
    @Override
    protected void onResume() {
        super.onResume();
        loadDataDepot();
//        loadDataFromFB();
    }

    //Tạo SearchView và Button xem giỏ hàng
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.search_view);
        searchView = (SearchView) itemSearch.getActionView();
        EditText se = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        se.setTextColor(Color.WHITE);
        se.setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(this);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Chuyển sang activity giỏ hàng
        if (id == R.id.ic_giohang) {
            startActivityForResult(new Intent(FruitListActivity.this, OrderActivity.class), 9);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterFruit.getFilter().filter(newText.trim());
        return true;
    }

    //Lấy dữ liệu từ Firebase về đưa vào list
    private void loadDataFromFB() {
//        progressDialog.setMessage("Đang tải");
//        progressDialog.show();
        dsFruit.clear();
        mData.child("FRUIT").addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product fruit = dataSnapshot.getValue(Product.class);
                dsFruit.add(new Product(fruit.getTen(), fruit.getHinh(), fruit.getMoTa(), fruit.getXuatXu(), fruit.getGia(), fruit.gethSD()));
                adapterFruit.notifyDataSetChanged();
            }
        });
        //loadDataDepot();
//        progressDialog.hide();
    }

    //Ánh xạ
    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_fruitlist);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth       = FirebaseAuth.getInstance();
        mStorage    = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        rcvFruitList = findViewById(R.id.rcv_fruitList);
        rcvFruitList.setLayoutManager(new LinearLayoutManager(this));
        adapterFruit = new AdapterFruit(FruitListActivity.this, dsFruit, this, MainActivity.dsDepot);
        rcvFruitList.setAdapter(adapterFruit);

       // Toast.makeText(this, MainActivity.dsDepot.size()+"", Toast.LENGTH_SHORT).show();
    }


    private void addEvents() {

    }
    //Load dữ liệu DEPOT(Kho) từ Firebase
    private void loadDataDepot(){
        MainActivity.dsDepot.clear();
        mData.child("DEPOT").addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Depot item = dataSnapshot.getValue(Depot.class);
                MainActivity.dsDepot.add(new Depot(item.getTenTraiCay(), item.getThoiGian(), item.getGia(), item.getSoLuong()));
                adapterFruit.notifyDataSetChanged();
            }
        });

    }

    //Xử lý click xem chi tiết sản phẩm => Gửi dữ liệu sang FruitDetailActivity
    @Override
    public void ClickItemFruit(int p) {
        Intent mIntent = new Intent(FruitListActivity.this, FruitDetailActivity.class);
        mIntent.putExtra("P_TEN", dsFruit.get(p).getTen());
        mIntent.putExtra("P_GIA", dsFruit.get(p).getGia());
        mIntent.putExtra("P_XX", dsFruit.get(p).getXuatXu());
        mIntent.putExtra("P_MOTA", dsFruit.get(p).getMoTa());
        mIntent.putExtra("P_HSD", dsFruit.get(p).gethSD());
        mIntent.putExtra("P_HINH", dsFruit.get(p).getHinh());
        startActivityForResult(mIntent, 3);
    }

    @Override
    public void ClickBuyFruit(int pos) {

    }
}
