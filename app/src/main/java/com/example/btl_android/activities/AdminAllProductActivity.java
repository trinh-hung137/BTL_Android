package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.btl_android.R;
import com.example.btl_android.adapters.HomeProductAdapter;
import com.example.btl_android.models.AllProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminAllProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;


    //All product
    List<AllProductModel> productList;
    HomeProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_product);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycle_admin_product);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        productList = new ArrayList<>();
        productAdapter = new HomeProductAdapter(this,productList);
        recyclerView.setAdapter(productAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                AllProductModel allProductModel = document.toObject(AllProductModel.class);
                                productList.add(allProductModel);
                                productAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(AdminAllProductActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}