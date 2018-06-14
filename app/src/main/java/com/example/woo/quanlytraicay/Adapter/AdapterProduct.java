package com.example.woo.quanlytraicay.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.Interface.IProduct;
import com.example.woo.quanlytraicay.Model.Product;
import com.example.woo.quanlytraicay.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ProductViewHolder> {

    private ArrayList<Product> dsProduct;
    private Context context;
    private IProduct iProduct;

    private DecimalFormat dcf = new DecimalFormat("###,###");

    public AdapterProduct(ArrayList<Product> dsProduct, Context context, IProduct iProduct) {
        this.dsProduct = dsProduct;
        this.context = context;
        this.iProduct = iProduct;
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
        holder.btn_productBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ". Chưa có setup giỏ hàng anh 2 àk", Toast.LENGTH_SHORT).show();
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
