package com.example.woo.quanlytraicay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.Adapter.AdapterProduct;
import com.example.woo.quanlytraicay.Interface.IProduct;
import com.example.woo.quanlytraicay.Model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IProduct {

    private FirebaseAuth mAuth;
    private RecyclerView rcv_productList;
    private ArrayList<Product> dsProduct = new ArrayList<>();
    private AdapterProduct adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        addControls();
        addEvents();


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

        dsProduct.add(new Product("1", "Cam", R.drawable.ic_cam, "Mô tả cam", "Xuất xứ cam", 40000, 10));
        dsProduct.add(new Product("2","Xoài", R.drawable.ic_xoai, "Mô tả Xoài", "Xuất xứ Xoài", 25000, 10));
        dsProduct.add(new Product("3", "Mận", R.drawable.ic_man, "Mô tả Mận", "Xuất xứ Mận", 15000, 10));
        dsProduct.add(new Product("4", "Dâu", R.drawable.dau, "Mô tả Dâu", "Xuất xứ Dâu", 70000, 10));
        dsProduct.add(new Product("5", "Cam", R.drawable.ic_cam, "Mô tả cam", "Xuất xứ cam", 40000, 10));
        dsProduct.add(new Product("6", "Xoài", R.drawable.ic_xoai, "Mô tả Xoài", "Xuất xứ Xoài", 25000, 10));
        dsProduct.add(new Product("7", "Mận", R.drawable.ic_man, "Mô tả Mận", "Xuất xứ Mận", 15000, 10));
        dsProduct.add(new Product("8", "Dâu", R.drawable.dau, "Mô tả Dâu", "Xuất xứ Dâu", 70000, 10));
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
            startActivityForResult(new Intent(MainActivity.this, Order.class), 2);
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
            startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 6);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
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
        mIntent.putExtra("P_ID", dsProduct.get(p).getId());
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
