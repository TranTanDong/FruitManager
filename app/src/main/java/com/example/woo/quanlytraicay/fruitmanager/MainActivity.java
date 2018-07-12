package com.example.woo.quanlytraicay.fruitmanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.woo.quanlytraicay.adapter.AdapterProduct;
import com.example.woo.quanlytraicay.abstracts.FBDatabase;
import com.example.woo.quanlytraicay.model.Depot;
import com.example.woo.quanlytraicay.ui.IProduct;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.model.Product;
import com.example.woo.quanlytraicay.model.User;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IProduct {

    private FirebaseAuth mAuth;
    private RecyclerView rcvProductList;
    private ArrayList<Product> dsProduct = new ArrayList<Product>();
    public static ArrayList<Depot> dsDepot = new ArrayList<Depot>();
    private AdapterProduct adapterProduct;
    private ProgressBar progressBar;
    public static TextView tvHiUser;
    public static TextView tvShowMail;
    private ViewFlipper vfMain;

    public static final int REQUEST_CODE_MAIN_ORDER = 3;
    public static final int RESULT_CODE_MAIN_ORDER = 4;
    public static final int REQUEST_CODE_MAIN_HISTORY = 5;
    public static final int RESULT_CODE_MAIN_HISTORY = 6;
    public static final int REQUEST_CODE_MAIN_FRUIT = 7;
    public static final int RESULT_CODE_MAIN_FRUIT = 8;
    public static final int REQUEST_CODE_MAIN_ACCOUNT = 9;
    public static final int RESULT_CODE_MAIN_ACCOUNT = 10;
    public static final int REQUEST_CODE_MAIN_DETAIL = 11;
    public static final int RESULT_CODE_MAIN_DETAIL = 12;

    private DatabaseReference mData;
   // private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        checkNetWork();
        loadDataProduct();
        loadDataOrder();
        proViewFlipper();
        hiUser();
        addEvents();
    }

    //Load dữ liệu ORDER(Đặt hàng) từ Firebase và đưa vào list
    private void loadDataOrder() {
        OrderActivity.orders.clear();
        mData.child("ORDER").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order order = dataSnapshot.getValue(Order.class);
                OrderActivity.orders.add(new Order(order.getTen(), order.getThoiGian(), order.getMail(), order.getHinh(), order.getSoLuong(), order.getGia()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataDepot();//Load lại dữ liệu khi quay lại
    }

    //Đưa dữ liệu ORDER lên Firebase
    @Override
    protected void onDestroy() {
        super.onDestroy();
       if (mAuth.getCurrentUser() != null){
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ORDER").child(mAuth.getCurrentUser().getUid());
           databaseReference.removeValue();
           for (Order i : OrderActivity.orders){
               mData.child("ORDER").child(mAuth.getCurrentUser().getUid()).push().setValue(i);
           }
           OrderActivity.orders.clear();
       }

    }

    //Kiểm tra mạng
    private void checkNetWork() {
        if (!isNetWorkConnected()){
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.network_error);
        builder.setMessage(R.string.check_network);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isNetWorkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    //Chạy quảng cáo
    private void proViewFlipper() {
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_cam);
        images.add(R.drawable.ic_dau);
        images.add(R.drawable.img_traicay);
        images.add(R.drawable.ic_dau);
        images.add(R.drawable.ic_cam);
        images.add(R.drawable.img_traicay);
       for (int i=0; i < images.size(); i++){
           ImageView imageView = new ImageView(this);
           Picasso.get().load(images.get(i)).into(imageView);
           imageView.setScaleType(ImageView.ScaleType.FIT_XY);
           vfMain.addView(imageView);
           vfMain.setFlipInterval(6000);
           vfMain.setAutoStart(true);

           Animation slide_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
           Animation slide_out = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

           vfMain.setInAnimation(slide_in);
           vfMain.setOutAnimation(slide_out);
       }

    }

    //Load dữ liệu USER từ Firebase về và hiển thị
    private void hiUser() {
        mData.child("USER").addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = mAuth.getCurrentUser().getEmail().toString();
                User user = dataSnapshot.getValue(User.class);
                if (email.equals(user.getEmail())){
                    tvHiUser.setText(user.getName().toString());
                    tvHiUser.setVisibility(View.VISIBLE);
                    tvShowMail.setText(user.getEmail().toString());
                    tvShowMail.setVisibility(View.VISIBLE);
                }
            }
        });}

    //Load dữ liệu FRUIT từ Firebase về và đưa vào list
    private void loadDataProduct() {
//        progressDialog.setMessage("Đang tải");
//        progressDialog.show();
        dsProduct.clear();
        mData.child("FRUIT").limitToLast(6).addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product prod = dataSnapshot.getValue(Product.class);
                dsProduct.add(new Product(prod.getTen(), prod.getHinh(), prod.getMoTa(), prod.getXuatXu(), prod.getGia(), prod.gethSD()));
                adapterProduct.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        //progressBar.setVisibility(View.INVISIBLE);
        //loadDataDepot();
//        progressDialog.hide();
    }

    //Load dữ liệu DEPOT(Kho) từ Firebase về và đưa vào list
    private void loadDataDepot(){
        dsDepot.clear();
        mData.child("DEPOT").addChildEventListener(new FBDatabase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Depot item = dataSnapshot.getValue(Depot.class);
                dsDepot.add(new Depot(item.getTenTraiCay(), item.getThoiGian(), item.getGia(), item.getSoLuong()));
                adapterProduct.notifyDataSetChanged();
            }
        });
        //Toast.makeText(this, MainActivity.dsDepot.size()+"", Toast.LENGTH_SHORT).show();

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
        View headView = navigationView.getHeaderView(0);
        tvHiUser = headView.findViewById(R.id.tv_HiUser);
        tvShowMail = headView.findViewById(R.id.tv_showMail);
        navigationView.setNavigationItemSelectedListener(this);

        //======================================================================================//
        mAuth       = FirebaseAuth.getInstance();
        //mStorage    = FirebaseStorage.getInstance();
        mData       = FirebaseDatabase.getInstance().getReference();
        progressBar = findViewById(R.id.pb_main);

        vfMain = findViewById(R.id.vf_main);

        //Setup RecyclerView
        rcvProductList = findViewById(R.id.rcv_productList);
        rcvProductList.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        adapterProduct  = new AdapterProduct(dsProduct, MainActivity.this, this, dsDepot);
        rcvProductList.setAdapter(adapterProduct);
    }

    private void addEvents() {
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    //Xử lý thoát App
    private void closeApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn thoát không?");
        builder.setCancelable(false);
        builder.setNegativeButton("CÓ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("KHÔNG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_main) {
            startActivityForResult(new Intent(MainActivity.this, OrderActivity.class), REQUEST_CODE_MAIN_ORDER);
        }else if (id == R.id.close_main){
            closeApp();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle the camera action
        if (id == R.id.nav_history) {
            startActivityForResult(new Intent(MainActivity.this, HistoryActivity.class), REQUEST_CODE_MAIN_HISTORY);
        } else if (id == R.id.nav_fruitList) {
            startActivityForResult(new Intent(MainActivity.this, FruitListActivity.class), REQUEST_CODE_MAIN_FRUIT);
        } else if (id == R.id.nav_info_account) {
            startActivityForResult(new Intent(MainActivity.this, AccountActivity.class), REQUEST_CODE_MAIN_ACCOUNT);
        } else if (id == R.id.nav_logout) {
            showSigout();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Xử lý đăng xuất
    private void showSigout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.log_out);
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setNegativeButton("CÓ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                HistoryActivity.dsHistory.clear();
                OrderActivity.orders.clear();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("KHÔNG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Xử lý click sản phẩm ==> Gửi dữ liệu qua FruitDetail
    @Override
    public void ClickItemProduct(int p) {
        Intent mIntent = new Intent(MainActivity.this, FruitDetailActivity.class);
        mIntent.putExtra("P_TEN", dsProduct.get(p).getTen());
        mIntent.putExtra("P_GIA", dsProduct.get(p).getGia());
        mIntent.putExtra("P_XX", dsProduct.get(p).getXuatXu());
        mIntent.putExtra("P_MOTA", dsProduct.get(p).getMoTa());
        mIntent.putExtra("P_HSD", dsProduct.get(p).gethSD());
        mIntent.putExtra("P_HINH", dsProduct.get(p).getHinh());
        startActivityForResult(mIntent, REQUEST_CODE_MAIN_DETAIL);
    }



}
