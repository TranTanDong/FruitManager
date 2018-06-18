package com.example.woo.quanlytraicay.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.woo.quanlytraicay.Interface.IFruit;
import com.example.woo.quanlytraicay.Model.Product;
import com.example.woo.quanlytraicay.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterFruit extends RecyclerView.Adapter<AdapterFruit.FruitViewHolder> {

    private Context context;
    private ArrayList<Product> dsFruit;
    private IFruit iFruit;

    private DecimalFormat dcf = new DecimalFormat("###,###,###");


    public AdapterFruit(Context context, ArrayList<Product> dsFruit, IFruit iFruit) {
        this.context = context;
        this.dsFruit = dsFruit;
        this.iFruit = iFruit;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewFruit = LayoutInflater.from(context).inflate(R.layout.item_fruit, parent, false);
        return new FruitViewHolder(viewFruit);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        holder.tv_fName.setText(dsFruit.get(position).getTen());
        holder.tv_fPrice.setText(dcf.format(dsFruit.get(position).getGia()));
        holder.tv_fOrigin.setText(dsFruit.get(position).getXuatXu());
        holder.tv_fExpiry.setText(dsFruit.get(position).gethSD()+"");
        holder.tv_fExist.setText("Còn hàng");
        Picasso.get().load(dsFruit.get(position).getHinh()).into(holder.img_fImage);

        holder.btn_fBuy.setTag(position);
        holder.itemView.setTag(position);

        holder.btn_fBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFruit.ClickBuyFruit(Integer.parseInt(v.getTag().toString()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iFruit.ClickItemFruit(Integer.parseInt(v.getTag().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsFruit.size();
    }

    public class FruitViewHolder extends RecyclerView.ViewHolder{
        private ImageView btn_fBuy, img_fImage;
        private TextView tv_fName, tv_fPrice, tv_fOrigin, tv_fExpiry, tv_fExist;

        public FruitViewHolder(View itemView) {
            super(itemView);
            tv_fName    = itemView.findViewById(R.id.tv_fName);
            tv_fPrice   = itemView.findViewById(R.id.tv_fPrice);
            tv_fOrigin  = itemView.findViewById(R.id.tv_fOrigin);
            tv_fExpiry  = itemView.findViewById(R.id.tv_fExpiry);
            tv_fExist   = itemView.findViewById(R.id.tv_fExist);
            btn_fBuy    = itemView.findViewById(R.id.btn_fBuy);
            img_fImage  = itemView.findViewById(R.id.img_fImage);
        }
    }
}
