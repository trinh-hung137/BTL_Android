package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.btl_android.R;
import com.example.btl_android.adapters.CategoryAllAdapter;
import com.example.btl_android.models.AllProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CategoryAllAdapter categoryAllAdapter;
    List<AllProductModel> allProductModelList;

    //toolbar
    Toolbar toolbar;

    //firebase
    FirebaseFirestore firestore;

    //progressbarap
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar= findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        firestore = FirebaseFirestore.getInstance();

        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        allProductModelList = new ArrayList<>();
        categoryAllAdapter = new CategoryAllAdapter(this, allProductModelList);
        recyclerView.setAdapter(categoryAllAdapter);


        //getting fruit
        if(type != null && type.equalsIgnoreCase("fruit")){
            firestore.collection("AllProducts").whereEqualTo("type", "fruit")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        AllProductModel allProductModel = documentSnapshot.toObject(AllProductModel.class);
                        allProductModelList.add(allProductModel);
                        categoryAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //getting vegetable
        if(type != null && type.equalsIgnoreCase("vegetable")){
            firestore.collection("AllProducts").whereEqualTo("type", "vegetable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        AllProductModel allProductModel = documentSnapshot.toObject(AllProductModel.class);
                        allProductModelList.add(allProductModel);
                        categoryAllAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //getting drink
        if(type != null && type.equalsIgnoreCase("drink")){
            firestore.collection("AllProducts").whereEqualTo("type", "drink")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                AllProductModel allProductModel = documentSnapshot.toObject(AllProductModel.class);
                                allProductModelList.add(allProductModel);
                                categoryAllAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

        //getting milk
        if(type != null && type.equalsIgnoreCase("milk")){
            firestore.collection("AllProducts").whereEqualTo("type", "milk")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                AllProductModel allProductModel = documentSnapshot.toObject(AllProductModel.class);
                                allProductModelList.add(allProductModel);
                                categoryAllAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
        //getting egg
        if(type != null && type.equalsIgnoreCase("egg")){
            firestore.collection("AllProducts").whereEqualTo("type", "egg")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                AllProductModel allProductModel = documentSnapshot.toObject(AllProductModel.class);
                                allProductModelList.add(allProductModel);
                                categoryAllAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
        //getting fish
        if(type != null && type.equalsIgnoreCase("fish")){
            firestore.collection("AllProducts").whereEqualTo("type", "fish")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                AllProductModel allProductModel = documentSnapshot.toObject(AllProductModel.class);
                                allProductModelList.add(allProductModel);
                                categoryAllAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

    }
}