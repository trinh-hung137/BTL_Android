package com.example.btl_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.models.UserModel;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {
    Context context;
    List<UserModel> userModelList;

    public AdminUserAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public AdminUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminUserAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(userModelList.get(position).getProfileImg()).into(holder.imageView);
        holder.name.setText(userModelList.get(position).getName());
        holder.email.setText(userModelList.get(position).getEmail());
        holder.sdt.setText(userModelList.get(position).getNumber());
        holder.address.setText(userModelList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,email,sdt,address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.user_img);
            name= itemView.findViewById(R.id.user_name);
            email= itemView.findViewById(R.id.user_email);
            sdt= itemView.findViewById(R.id.user_number);
            address= itemView.findViewById(R.id.user_address);

        }
    }
}
