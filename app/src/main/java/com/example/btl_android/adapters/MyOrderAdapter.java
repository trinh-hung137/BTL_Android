package com.example.btl_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.models.MyOrderModel;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    List<MyOrderModel> list;

    public MyOrderAdapter(Context context, List<MyOrderModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getProductName());
        holder.price.setText(list.get(position).getProductPrice());
        holder.date.setText(list.get(position).getCurrentDate());
        holder.time.setText(list.get(position).getCurrentTime());
        holder.quantity.setText(list.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,price,date,time,quantity,totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.order_product_name);
            price = itemView.findViewById(R.id.order_product_price);
            date = itemView.findViewById(R.id.order_current_date);
            time = itemView.findViewById(R.id.order_current_time);
            quantity = itemView.findViewById(R.id.order_total_quantity);
            totalPrice = itemView.findViewById(R.id.order_total_price);
        }
    }
}
