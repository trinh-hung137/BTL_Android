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

public class HomePopularAdapter extends RecyclerView.Adapter<HomePopularAdapter.ViewHolder> {

    private Context context;
    private List<AllProductModel> popularModelList;

    public HomePopularAdapter(Context context, List<AllProductModel> popularModelList) {
        this.context = context;
        this.popularModelList = popularModelList;
    }

    @NonNull
    @Override
    public HomePopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_popular,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePopularAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(popularModelList.get(position).getImg_url()).into(holder.popImg);
        holder.name.setText(popularModelList.get(position).getName());
        holder.rating.setText(popularModelList.get(position).getRating());
        holder.description.setText(popularModelList.get(position).getDescription());
//        holder.discount.setText(popularModelList.get(position).getDiscount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedProductActivity.class);
                intent.putExtra("detail", popularModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView popImg;
        TextView name, description, rating, discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            popImg = itemView.findViewById(R.id.pop_img);
            name = itemView.findViewById(R.id.pop_name);
            description = itemView.findViewById(R.id.pop_des);
//            discount = itemView.findViewById(R.id.pop_discount);
            rating = itemView.findViewById(R.id.pop_rating);
        }
    }
}
