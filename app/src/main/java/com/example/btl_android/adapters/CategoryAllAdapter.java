package com.example.btl_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.activities.DetailedProductActivity;
import com.example.btl_android.models.AllProductModel;

import java.util.List;

public class CategoryAllAdapter extends RecyclerView.Adapter<CategoryAllAdapter.ViewHolder> {

    Context context;
    List<AllProductModel> list;

    public CategoryAllAdapter(Context context, List<AllProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_category, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAllAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.rating.setText(list.get(position).getRating());
        holder.price.setText(list.get(position).getPrice()+"/kg");


        if(list.get(position).getType().equals("egg")){
            holder.price.setText(list.get(position).getPrice()+"/dozen");
        }
        if(list.get(position).getType().equals("milk")){
            holder.price.setText(list.get(position).getPrice()+"/litre");
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedProductActivity.class);
                intent.putExtra("detail", list.get(position));
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, description, rating, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.view_img);
            name = itemView.findViewById(R.id.view_name);
            description = itemView.findViewById(R.id.view_description);
            rating = itemView.findViewById(R.id.view_rating);
            price = itemView.findViewById(R.id.view_price);
        }
    }
}
