package com.example.woo.quanlytraicay.FruitManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.woo.quanlytraicay.Adapter.AdapterFruit;
import com.example.woo.quanlytraicay.Interface.IFruit;
import com.example.woo.quanlytraicay.Model.Product;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FruitList extends AppCompatActivity implements IFruit {
    private ProgressDialog pr_list;
    private RecyclerView rcv_fruitList;
    private ArrayList<Product> dsFruit = new ArrayList<>();
    private AdapterFruit adapterFruit;

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
            startActivityForResult(new Intent(FruitList.this, OrderActivity.class), 9);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadDataFromFB() {
        pr_list.setMessage("Đang tải");
        pr_list.show();
        dsFruit.clear();
        mData.child("FRUIT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product fruit = dataSnapshot.getValue(Product.class);
                dsFruit.add(new Product(fruit.getTen(), fruit.getHinh(), fruit.getMoTa(), fruit.getXuatXu(), fruit.getGia(), fruit.gethSD()));
                adapterFruit.notifyDataSetChanged();
                pr_list.hide();
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

    private void addControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_fruitlist);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth       = FirebaseAuth.getInstance();
        mStorage    = FirebaseStorage.getInstance();
        pr_list     = new ProgressDialog(this);

        rcv_fruitList   = findViewById(R.id.rcv_fruitList);
        rcv_fruitList.setLayoutManager(new LinearLayoutManager(this));
        adapterFruit = new AdapterFruit(FruitList.this, dsFruit, this);
        rcv_fruitList.setAdapter(adapterFruit);
    }

    private void addEvents() {

    }

    @Override
    public void ClickItemFruit(int p) {
        Intent mIntent = new Intent(FruitList.this, FruitDetail.class);
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
