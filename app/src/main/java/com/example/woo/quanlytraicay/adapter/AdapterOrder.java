package com.example.woo.quanlytraicay.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.woo.quanlytraicay.fruitmanager.MainActivity;
import com.example.woo.quanlytraicay.fruitmanager.OrderActivity;
import com.example.woo.quanlytraicay.model.Depot;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderViewHolder> {
    private Context context;
    private ArrayList<Order> orders;
    private TextView bigSum;

    private DecimalFormat dcf = new DecimalFormat("###,###,###");

    public AdapterOrder(Context context, ArrayList<Order> orders, TextView bigSum) {
        this.context = context;
        this.orders = orders;
        this.bigSum = bigSum;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewOrder = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new AdapterOrder.OrderViewHolder(viewOrder);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        //Picasso.get().load(orders.get(position).getHinh()).into(holder.img_orderImage);
        Glide.with(context).load(orders.get(position).getHinh())
                .apply(RequestOptions
                        .overrideOf(120, 120)
                        .placeholder(R.drawable.ic_errorimage)
                        .error(R.drawable.ic_errorimage)
                        .formatOf(DecodeFormat.PREFER_RGB_565)
                        .timeout(3000)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.img_orderImage);
        holder.tv_orderName.setText(orders.get(position).getTen());
        holder.tv_orderPrice.setText(dcf.format(orders.get(position).getGia())+"");
        holder.tv_orderAmount.setText(dcf.format(orders.get(position).getSoLuong())+"");
//        for (Order l : orders){
//            int n = 0; //Số lượng còn lại trong kho
//            for (Depot i : MainActivity.dsDepot){
//                if (i.getTenTraiCay().equals(holder.tv_orderName.getText())){
//                    n = i.getSoLuong();
//                }
//            }
//            if (l.getSoLuong() > 10 && n > 10){//SL order < kho và kho <= 10
//                l.setSoLuong(10);
//            }else if (l.getSoLuong() > 10 && l.getSoLuong() < n){
//                l.setSoLuong(10);
//            }
//        }
        holder.tv_orderTotal.setText(dcf.format(orders.get(position).getSoLuong()*orders.get(position).getGia())+"đ");
        xuLyBigSum(orders);
        holder.btn_orderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(holder.tv_orderAmount.getText().toString());
                int n = 0; //Số lượng còn lại trong kho
                for (Depot i : MainActivity.dsDepot){
                    if (i.getTenTraiCay().equals(holder.tv_orderName.getText())){
                        n = i.getSoLuong();
                    }
                }
                if (a < 10 && a < n){
                    a++;
                    int p = orders.get(position).getGia();
                    int s = p*a;
                    holder.tv_orderAmount.setText(dcf .format(a)+"");
                    holder.tv_orderTotal.setText(dcf.format(s)+"đ");
                    orders.get(position).setSoLuong(a);
                    xuLyBigSum(orders);
                }else Toast.makeText(context, R.string.toast_maximum, Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_orderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(holder.tv_orderAmount.getText().toString());
                if (a > 0){
                    a--;
                    int p = orders.get(position).getGia();
                    int s = p*a;
                    holder.tv_orderAmount.setText(dcf .format(a)+"");
                    holder.tv_orderTotal.setText(dcf.format(s)+"đ");
                    orders.get(position).setSoLuong(a);
                    xuLyBigSum(orders);
                }else if (a == 0){
                    orders.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                delItem(position);
                return true;
            }
        });

    }

    private void delItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_product);
        builder.setCancelable(false);
        builder.setNegativeButton("CÓ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orders.remove(pos);
                xuLyBigSum(orders);
                OrderActivity.showStatusCart();
                notifyDataSetChanged();
                dialogInterface.dismiss();
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

    private void xuLyBigSum(ArrayList<Order> order) {
        int s = 0;
        int sl, gia;
        for (Order i : order){
            sl = i.getSoLuong();
            gia = i.getGia();
            s += sl*gia;
        }
        bigSum.setText(dcf.format(s)+"đ");
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_orderImage, btn_orderMinus, btn_orderPlus;
        private TextView tv_orderName, tv_orderPrice, tv_orderExist, tv_orderAmount, tv_orderTotal;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tv_orderName    = itemView.findViewById(R.id.tv_orderName);
            tv_orderPrice   = itemView.findViewById(R.id.tv_orderPrice);
            tv_orderExist   = itemView.findViewById(R.id.tv_orderExist);
            tv_orderAmount  = itemView.findViewById(R.id.tv_orderAmount);
            tv_orderTotal   = itemView.findViewById(R.id.tv_orderTotal);
            img_orderImage  = itemView.findViewById(R.id.img_orderImage);
            btn_orderPlus   = itemView.findViewById(R.id.btn_orderPlus);
            btn_orderMinus  = itemView.findViewById(R.id.btn_orderMinus);

        }
    }
}
