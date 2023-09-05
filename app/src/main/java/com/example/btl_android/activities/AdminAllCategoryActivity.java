package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.btl_android.R;
import com.example.btl_android.adapters.HomeCategoryAdapter;
import com.example.btl_android.adapters.HomePopularAdapter;
import com.example.btl_android.models.HomeCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminAllCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    // Category
    List<HomeCategory> categoryList;
    HomeCategoryAdapter homeCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_category);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycle_admin_category);

        //All Category
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        categoryList = new ArrayList<>();
        homeCategoryAdapter = new HomeCategoryAdapter(this,categoryList);
        recyclerView.setAdapter(homeCategoryAdapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                                homeCategoryAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(AdminAllCategoryActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}