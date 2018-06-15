package com.example.woo.quanlytraicay.FruitManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    private void loadDataFromFB() {
        dsFruit.clear();
        mData.child("FRUIT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product fruit = dataSnapshot.getValue(Product.class);
                dsFruit.add(new Product(fruit.getTen(), fruit.getHinh(), fruit.getMoTa(), fruit.getXuatXu(), fruit.getGia(), fruit.gethSD()));
                adapterFruit.notifyDataSetChanged();
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
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth       = FirebaseAuth.getInstance();
        mStorage    = FirebaseStorage.getInstance();

        rcv_fruitList   = findViewById(R.id.rcv_fruitList);
        rcv_fruitList.setLayoutManager(new LinearLayoutManager(this));
        adapterFruit = new AdapterFruit(FruitList.this, dsFruit, this);
        rcv_fruitList.setAdapter(adapterFruit);
    }

    private void addEvents() {

    }

    @Override
    public void ClickItemFruit(int pos) {

    }

    @Override
    public void ClickBuyFruit(int pos) {

    }
}
