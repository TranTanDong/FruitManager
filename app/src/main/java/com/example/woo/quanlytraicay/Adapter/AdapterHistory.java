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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterHistory extends  RecyclerView.Adapter<AdapterHistory.HistoryViewHolder>{
    private Context context;
    private ArrayList<Order> dsHistory;
    private TextView sumHis;

    private DecimalFormat dcf = new DecimalFormat("###,###,###");

    public AdapterHistory(Context context, ArrayList<Order> dsHistory, TextView sumHis) {
        this.context = context;
        this.dsHistory = dsHistory;
        this.sumHis = sumHis;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHis = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new AdapterHistory.HistoryViewHolder(viewHis);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.tv_hName.setText(dsHistory.get(position).getTen());
        holder.tv_hPrice.setText(dcf.format(dsHistory.get(position).getGia())+"");
        holder.tv_hAmount.setText(dsHistory.get(position).getSoLuong()+"");
        holder.tv_hTime.setText(dsHistory.get(position).getThoiGian()+"");
        holder.tv_hTotal.setText(dcf.format(dsHistory.get(position).getSoLuong()*dsHistory.get(position).getGia())+"đ");
        Picasso.get().load(dsHistory.get(position).getHinh()).into(holder.img_hImage);
        xuLySumHis(dsHistory);
    }

    private void xuLySumHis(ArrayList<Order> dsHistory) {
        int s = 0;
        int sl, gia;
        for (Order i : dsHistory){
            sl = i.getSoLuong();
            gia = i.getGia();
            s += sl*gia;
        }
        sumHis.setText(dcf.format(s)+"đ");
    }

    @Override
    public int getItemCount() {
        return dsHistory.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_hImage;
        private TextView tv_hName, tv_hPrice, tv_hAmount, tv_hTotal, tv_hTime;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            tv_hName    = itemView.findViewById(R.id.tv_hName);
            tv_hPrice   = itemView.findViewById(R.id.tv_hPrice);
            tv_hAmount  = itemView.findViewById(R.id.tv_hAmount);
            tv_hTotal   = itemView.findViewById(R.id.tv_hTotal);
            img_hImage  = itemView.findViewById(R.id.img_hImage);
            tv_hTime    = itemView.findViewById(R.id.tv_hTime);
        }
    }
}
