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
import com.example.btl_android.activities.CategoryAllActivity;
import com.example.btl_android.activities.DetailedProductActivity;
import com.example.btl_android.activities.ManageCategoryActivity;
import com.example.btl_android.activities.ManageProductActivity;
import com.example.btl_android.models.AllProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    private Context context;
    private List<AllProductModel> allProductList;

    public HomeProductAdapter(Context context, List<AllProductModel> allProductList) {
        this.context = context;
        this.allProductList = allProductList;
    }

    @NonNull
    @Override
    public HomeProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeProductAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(allProductList.get(position).getImg_url()).into(holder.productImg);
        holder.name.setText(allProductList.get(position).getName());
        holder.rating.setText(allProductList.get(position).getRating());
        holder.description.setText(allProductList.get(position).getDescription());
//        holder.price.setText(String.valueOf(allProductList.get(position).getPrice()));

        holder.price.setText(allProductList.get(position).getPrice()+"/kg");


        if(allProductList.get(position).getType().equals("egg")){
            holder.price.setText(allProductList.get(position).getPrice()+"/dozen");
        }
        if(allProductList.get(position).getType().equals("milk")){
            holder.price.setText(allProductList.get(position).getPrice()+"/litre");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    String userId = firebaseUser.getUid();
                    // Truy vấn cơ sở dữ liệu Firestore để kiểm tra quyền admin
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference adminRef = db.collection("Admins").document(userId);

                    adminRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Người dùng có quyền admin
                                    Intent intent = new Intent(context, ManageProductActivity.class);
//                                    intent.putExtra("detail", list.get(position));
                                    context.startActivity(intent);
                                } else {
                                    // Người dùng không có quyền admin
                                    Intent intent = new Intent(context, DetailedProductActivity.class);
                                    intent.putExtra("detail", allProductList.get(position));
                                    context.startActivity(intent);
                                }
                            }
                        }
                    });

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return allProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImg;
        TextView name, description, rating, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.product_img);
            name = itemView.findViewById(R.id.product_name);
            description = itemView.findViewById(R.id.product_des);
            rating = itemView.findViewById(R.id.product_rating);
            price = itemView.findViewById(R.id.product_price);

        }
    }
}
