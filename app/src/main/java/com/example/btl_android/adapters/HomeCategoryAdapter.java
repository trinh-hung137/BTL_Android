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
import com.example.btl_android.activities.CallActivity;
import com.example.btl_android.activities.CategoryAllActivity;
import com.example.btl_android.activities.DetailedProductActivity;
import com.example.btl_android.activities.ManageCategoryActivity;
import com.example.btl_android.models.HomeCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHoder> {

    Context context;
    List<HomeCategory> categoryList;

    public HomeCategoryAdapter(Context context, List<HomeCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_cat,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(categoryList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null){
                    String userId = firebaseUser.getUid();
                    // Truy vấn cơ sở dữ liệu Firestore để kiểm tra quyền admin
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Admins")
                            .document(userId)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Người dùng có quyền admin
                                    Intent intent = new Intent(context, ManageCategoryActivity.class);
//                                    intent.putExtra("detail", list.get(position));
                                    context.startActivity(intent);
                                } else {
                                    // Người dùng không có quyền admin
                                    Intent intent = new Intent(context, CategoryAllActivity.class);
                                    intent.putExtra("type", categoryList.get(position).getType());
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
        return categoryList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView name;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);

            catImg = itemView.findViewById(R.id.home_cat_img);
            name = itemView.findViewById(R.id.cat_home_name);
        }
    }
}
