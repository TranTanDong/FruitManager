package com.example.woo.quanlytraicay.FruitManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.woo.quanlytraicay.Adapter.AdapterProduct;
import com.example.woo.quanlytraicay.Interface.IProduct;
import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.Model.Product;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IProduct {

    private FirebaseAuth mAuth;
    private RecyclerView rcv_productList;
    public static ArrayList<Product> dsProduct = new ArrayList<>();
    private AdapterProduct adapterProduct;
    private ProgressDialog pr_main;

    private DatabaseReference mData;
   // private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        loadDataOrder();
        loadDataProduct();
        addEvents();


    }

    private void loadDataOrder() {
        if (OrderActivity.orders.size() == 0)
            mData.child("ORDER").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (OrderActivity.orders.isEmpty() == true){
                        OrderActivity.orders.add(new Order(order.getTen(), order.getThoiGian(), order.getMail(), order.getHinh(), order.getSoLuong(), order.getGia()));
                    }else {
                        int tmp = 0;
                        for (Order i : OrderActivity.orders){
                            if (i.getTen().equals(order.getTen())){
                                i.setSoLuong(i.getSoLuong()+order.getSoLuong());
                            }else {
                                tmp++;
                            }
                        }
                        if (tmp > (OrderActivity.orders.size()-1)){
                            OrderActivity.orders.add(new Order(order.getTen(), order.getThoiGian(), order.getMail(), order.getHinh(), order.getSoLuong(), order.getGia()));
                        }
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

    private void loadDataProduct() {
        pr_main.setMessage("Loading");
        pr_main.show();
        dsProduct.clear();
        mData.child("FRUIT").limitToLast(6).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product prod = dataSnapshot.getValue(Product.class);
                dsProduct.add(new Product(prod.getTen(), prod.getHinh(), prod.getMoTa(), prod.getXuatXu(), prod.getGia(), prod.gethSD()));
                adapterProduct.notifyDataSetChanged();
                pr_main.hide();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //======================================================================================//
        mAuth       = FirebaseAuth.getInstance();
        //mStorage    = FirebaseStorage.getInstance();
        mData       = FirebaseDatabase.getInstance().getReference();
        pr_main = new ProgressDialog(this);

//        dsProduct.add(new Product("1", "Cam", R.drawable.ic_cam, "Mô tả cam", "Xuất xứ cam", 40000, 10));
//        dsProduct.add(new Product("2","Xoài", R.drawable.ic_xoai, "Mô tả Xoài", "Xuất xứ Xoài", 25000, 9));
//        dsProduct.add(new Product("3", "Mận", R.drawable.ic_man, "Mô tả Mận", "Xuất xứ Mận", 15000, 8));
//        dsProduct.add(new Product("4", "Dâu", R.drawable.dau, "Mô tả Dâu", "Xuất xứ Dâu", 70000, 11));
//        dsProduct.add(new Product("5", "Cam", R.drawable.ic_cam, "Mô tả cam", "Xuất xứ cam", 40000, 15));
//        dsProduct.add(new Product("6", "Xoài", R.drawable.ic_xoai, "Mô tả Xoài", "Xuất xứ Xoài", 25000, 22));
//        dsProduct.add(new Product("7", "Mận", R.drawable.ic_man, "Mô tả Mận", "Xuất xứ Mận", 15000, 14));
//        dsProduct.add(new Product("8", "Dâu", R.drawable.dau, "Mô tả Dâu", "Xuất xứ Dâu", 70000, 3));


        //Setup RecyclerView
        rcv_productList  = findViewById(R.id.rcv_productList);
        rcv_productList.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        adapterProduct  = new AdapterProduct(dsProduct, MainActivity.this, this);
        rcv_productList.setAdapter(adapterProduct);
    }

    private void addEvents() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            startActivityForResult(new Intent(MainActivity.this, OrderActivity.class), 2);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // Handle the camera action
            startActivityForResult(new Intent(MainActivity.this, HistoryActivity.class), 4);
        } else if (id == R.id.nav_fruitList) {
            startActivityForResult(new Intent(MainActivity.this, FruitList.class), 5);
        } else if (id == R.id.nav_home) {
            startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), 6);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            OrderActivity.orders.clear();
            HistoryActivity.dsHistory.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void ClickItemProduct(int p) {
        Intent mIntent = new Intent(MainActivity.this, FruitDetail.class);
        mIntent.putExtra("P_TEN", dsProduct.get(p).getTen());
        mIntent.putExtra("P_GIA", dsProduct.get(p).getGia());
        mIntent.putExtra("P_XX", dsProduct.get(p).getXuatXu());
        mIntent.putExtra("P_MOTA", dsProduct.get(p).getMoTa());
        mIntent.putExtra("P_HSD", dsProduct.get(p).gethSD());
        mIntent.putExtra("P_HINH", dsProduct.get(p).getHinh());
        startActivityForResult(mIntent, 3);
        //Toast.makeText(MainActivity.this, "Chi tiết sp"+dsProduct.get(p).getTen(), Toast.LENGTH_SHORT).show();
    }

}
