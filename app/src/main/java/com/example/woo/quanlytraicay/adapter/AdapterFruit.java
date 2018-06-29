package com.example.woo.quanlytraicay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woo.quanlytraicay.fruitmanager.MainActivity;
import com.example.woo.quanlytraicay.fruitmanager.OrderActivity;
import com.example.woo.quanlytraicay.model.Depot;
import com.example.woo.quanlytraicay.ui.IFruit;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.model.Product;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterFruit extends RecyclerView.Adapter<AdapterFruit.FruitViewHolder> {

    private Context context;
    private ArrayList<Product> dsFruit;
    private ArrayList<Depot> dsDepot;
    private IFruit iFruit;

    private DecimalFormat dcf = new DecimalFormat("###,###,###");


    public AdapterFruit(Context context, ArrayList<Product> dsFruit, IFruit iFruit, ArrayList<Depot> dsDepot) {
        this.context = context;
        this.dsFruit = dsFruit;
        this.iFruit = iFruit;
        this.dsDepot = dsDepot;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewFruit = LayoutInflater.from(context).inflate(R.layout.item_fruit, parent, false);
        return new FruitViewHolder(viewFruit);
    }

    @Override
    public void onBindViewHolder(@NonNull final FruitViewHolder holder, final int position) {
        holder.tv_fName.setText(dsFruit.get(position).getTen());
        holder.tv_fPrice.setText(dcf.format(dsFruit.get(position).getGia()));
        holder.tv_fOrigin.setText(dsFruit.get(position).getXuatXu());
        holder.tv_fExpiry.setText(dsFruit.get(position).gethSD()+"");
        for (Depot i : dsDepot){
            if((i.getSoLuong() > 0) && (i.getTenTraiCay().equals(dsFruit.get(position).getTen()))){
                holder.tv_fExist.setText("Còn hàng");
                holder.btn_fBuy.setVisibility(View.VISIBLE);
            }
            if ((i.getSoLuong() > 0) && (i.getSoLuong() <= 5) && (i.getTenTraiCay().equals(dsFruit.get(position).getTen()))){
                holder.tv_fExist.setText("Sắp hết hàng");
                holder.btn_fBuy.setVisibility(View.VISIBLE);
            }

            if ((i.getSoLuong() == 0) && (i.getTenTraiCay().equals(dsFruit.get(position).getTen()))){
                holder.tv_fExist.setText("Hết hàng");
                holder.btn_fBuy.setVisibility(View.INVISIBLE);
            }

        }

        Picasso.get().load(dsFruit.get(position).getHinh()).into(holder.img_fImage);

        holder.btn_fBuy.setTag(position);
        holder.itemView.setTag(position);

        holder.btn_fBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.orders.isEmpty() == true){
                    MainActivity.orders.add(new Order(dsFruit.get(position).getTen(), Calendar.getInstance().getTime().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), dsFruit.get(position).getHinh(), 1, dsFruit.get(position).getGia()));
                    Toast.makeText(context, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
                }else {
                    int tmp = 0;
                    for (Order i : MainActivity.orders){
                        int n = 0; //Số lượng còn lại trong kho
                        for (Depot j : MainActivity.dsDepot){
                            if (j.getTenTraiCay().equals(holder.tv_fName.getText())){
                                n = j.getSoLuong();
                            }
                        }
                        if (i.getTen().equals(dsFruit.get(position).getTen())){
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
                    if (tmp > (MainActivity.orders.size()-1)){
                        MainActivity.orders.add(new Order(dsFruit.get(position).getTen(), Calendar.getInstance().getTime().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), dsFruit.get(position).getHinh(), 1, dsFruit.get(position).getGia()));
                        Toast.makeText(context, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
                    }
                }
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
