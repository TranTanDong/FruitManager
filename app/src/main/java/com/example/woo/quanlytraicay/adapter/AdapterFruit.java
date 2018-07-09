package com.example.woo.quanlytraicay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.woo.quanlytraicay.ui.IFruit;
import com.example.woo.quanlytraicay.model.Order;
import com.example.woo.quanlytraicay.model.Product;
import com.example.woo.quanlytraicay.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdapterFruit extends RecyclerView.Adapter<AdapterFruit.FruitViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Product> dsFruit;
    private ArrayList<Depot> dsDepot;
    private IFruit iFruit;
    private ValueFilter valueFilter;
    private ArrayList<Product> dsFilter;

    private DecimalFormat dcf = new DecimalFormat("###,###,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm | dd-MM-yyyy");


    public AdapterFruit(Context context, ArrayList<Product> dsFruit, IFruit iFruit, ArrayList<Depot> dsDepot) {
        this.context = context;
        this.dsFruit = dsFruit;
        this.iFruit = iFruit;
        this.dsDepot = dsDepot;
        this.dsFilter = new ArrayList<Product>();
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
        //Set tình trạng hàng
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

        //Load hình
        Glide.with(context).load(dsFruit.get(position).getHinh())
                .apply(RequestOptions
                .overrideOf(120, 120)
                .placeholder(R.drawable.ic_errorimage)
                .error(R.drawable.ic_errorimage)
                .formatOf(DecodeFormat.PREFER_RGB_565)
                .timeout(3000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.img_fImage);
        //Picasso.get().load(dsFruit.get(position).getHinh()).into(holder.img_fImage);

        holder.btn_fBuy.setTag(position);
        holder.itemView.setTag(position);

        //Xử lý click thêm vào giỏ
        holder.btn_fBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderActivity.orders.isEmpty() == true){
                    OrderActivity.orders.add(new Order(dsFruit.get(position).getTen(), sdf.format(Calendar.getInstance().getTime())+"", FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), dsFruit.get(position).getHinh(), 1, dsFruit.get(position).getGia()));
                    Toast.makeText(context, R.string.toast_added_product, Toast.LENGTH_SHORT).show();
                }else {
                    int tmp = 0;
                    for (Order i : OrderActivity.orders){
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
                    if (tmp > (OrderActivity.orders.size()-1)){
                        OrderActivity.orders.add(new Order(dsFruit.get(position).getTen(), sdf.format(Calendar.getInstance().getTime())+"", FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), dsFruit.get(position).getHinh(), 1, dsFruit.get(position).getGia()));
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

    @Override
    public Filter getFilter() {
        if(valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if (dsFilter.isEmpty()){
                dsFilter.addAll(dsFruit);
            }

            if(constraint!=null && constraint.length()>0){
                ArrayList<Product> filterList=new ArrayList<Product>();
                for(int i=0;i<dsFilter.size();i++){
                    if((dsFilter.get(i).getTen().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        Product product = new Product(
                                dsFilter.get(i).getTen(),
                                dsFilter.get(i).getHinh(),
                                dsFilter.get(i).getMoTa(),
                                dsFilter.get(i).getXuatXu(),
                                dsFilter.get(i).getGia(),
                                dsFilter.get(i).gethSD()
                        );
                        filterList.add(product);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=dsFilter.size();
                results.values=dsFilter;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            dsFruit=(ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
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
