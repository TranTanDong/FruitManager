package com.example.woo.quanlytraicay.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.woo.quanlytraicay.Model.Order;
import com.example.woo.quanlytraicay.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderViewHolder> {
    private Context context;
    private ArrayList<Order> orders;

    private DecimalFormat dcf = new DecimalFormat("###,###");

    public AdapterOrder(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewOrder = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new AdapterOrder.OrderViewHolder(viewOrder);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        holder.img_orderImage.setImageResource(orders.get(position).getHinh());
        holder.tv_orderName.setText(orders.get(position).getIdTraiCay());
        holder.tv_orderPrice.setText(orders.get(position).getGia()+"");
        if (orders.get(position).getSoLuong() > 0){
            holder.tv_orderExist.setText("Còn hàng");
        }else holder.tv_orderExist.setText("Hết hàng");

//        holder.tv_orderAmount.setText("0");
//        holder.tv_orderTotal.setText("0");

        holder.btn_orderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(holder.tv_orderAmount.getText().toString());
                if (a < 10){
                    a++;
                    int p = orders.get(position).getGia();
                    int s = p*a;
                    holder.tv_orderAmount.setText(a+"");
                    holder.tv_orderTotal.setText(s+"");
                }
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
                    holder.tv_orderAmount.setText(a+"");
                    holder.tv_orderTotal.setText(s+"");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_orderImage, btn_orderMinus, btn_orderPlus;
        private TextView tv_orderName, tv_orderPrice, tv_orderExist, tv_orderOrigin, tv_orderAmount, tv_orderTotal;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tv_orderName    = itemView.findViewById(R.id.tv_orderName);
            tv_orderPrice   = itemView.findViewById(R.id.tv_orderPrice);
            tv_orderExist   = itemView.findViewById(R.id.tv_orderExist);
            tv_orderOrigin  = itemView.findViewById(R.id.tv_orderOrigin);
            tv_orderAmount  = itemView.findViewById(R.id.tv_orderAmount);
            tv_orderTotal   = itemView.findViewById(R.id.tv_orderTotal);
            img_orderImage  = itemView.findViewById(R.id.img_orderImage);
            btn_orderPlus   = itemView.findViewById(R.id.btn_orderPlus);
            btn_orderMinus  = itemView.findViewById(R.id.btn_orderMinus);

        }
    }
}
