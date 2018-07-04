package com.example.woo.quanlytraicay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.fruitmanager.MainActivity;
import com.example.woo.quanlytraicay.fruitmanager.OrderActivity;
import com.example.woo.quanlytraicay.model.Depot;
import com.example.woo.quanlytraicay.ui.IProduct;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.model.Product;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ProductViewHolder> {

    private ArrayList<Product> dsProduct;
    private ArrayList<Depot> dsDepot;
    private Context context;
    private IProduct iProduct;

    private DecimalFormat dcf = new DecimalFormat("###,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm | dd-MM-yyyy");

    public AdapterProduct(ArrayList<Product> dsProduct, Context context, IProduct iProduct, ArrayList<Depot> dsDepot) {
        this.dsProduct = dsProduct;
        this.context = context;
        this.iProduct = iProduct;
        this.dsDepot = dsDepot;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewProduct = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(viewProduct);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        holder.tv_productName.setText(dsProduct.get(position).getTen());
        holder.tv_productPrice.setText(dcf.format(dsProduct.get(position).getGia())+"/kg");
        Picasso.get().load(dsProduct.get(position).getHinh()).into(holder.img_Product);
        holder.tv_productSource.setText(dsProduct.get(position).getXuatXu());
        //Kiểm tra Kho
        for (Depot i : dsDepot){
            if((i.getSoLuong() > 0) && (i.getTenTraiCay().equals(dsProduct.get(position).getTen()))){
                holder.btn_productBuy.setVisibility(View.VISIBLE);
            }

            if ((i.getSoLuong() == 0) && (i.getTenTraiCay().equals(dsProduct.get(position).getTen()))){
                holder.btn_productBuy.setVisibility(View.INVISIBLE);
            }
        }
        holder.btn_productBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderActivity.orders.isEmpty() == true){
                    OrderActivity.orders.add(new Order(dsProduct.get(position).getTen(), sdf.format(Calendar.getInstance().getTime().toString()), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), dsProduct.get(position).getHinh(), 1, dsProduct.get(position).getGia()));
                    Toast.makeText(context, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
                }else {
                    int tmp = 0;
                    for (Order i : OrderActivity.orders){
                        int n = 0; //Số lượng còn lại trong kho
                        for (Depot j : MainActivity.dsDepot){
                            if (j.getTenTraiCay().equals(holder.tv_productName.getText())){
                                n = j.getSoLuong();
                            }
                        }
                        if (i.getTen().equals(dsProduct.get(position).getTen())){
                            if (i.getSoLuong() >= 10 || i.getSoLuong() >= n){
                                Toast.makeText(context, R.string.toast_maximum, Toast.LENGTH_SHORT).show();
                            }else {
                                i.setSoLuong(i.getSoLuong()+1);
                                Toast.makeText(context, R.string.toast_exist_add, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            tmp++;
                        }
                    }
                    if (tmp > (OrderActivity.orders.size()-1)){
                        OrderActivity.orders.add(new Order(dsProduct.get(position).getTen(), sdf.format(Calendar.getInstance().getTime().toString()), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), dsProduct.get(position).getHinh(), 1, dsProduct.get(position).getGia()));
                        Toast.makeText(context, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(v.getTag().toString());
                iProduct.ClickItemProduct(i);
            }
        });

    }



    @Override
    public int getItemCount() {
        return dsProduct.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_Product;
        private TextView tv_productName, tv_productPrice, tv_productSource;
        private Button btn_productBuy;

        public ProductViewHolder(View itemView) {
            super(itemView);

            img_Product         = itemView.findViewById(R.id.img_product);
            tv_productName      = itemView.findViewById(R.id.tv_productName);
            tv_productPrice     = itemView.findViewById(R.id.tv_productPrice);
            tv_productSource    = itemView.findViewById(R.id.tv_productSource);
            btn_productBuy      = itemView.findViewById(R.id.btn_productBuy);
        }
    }
}
