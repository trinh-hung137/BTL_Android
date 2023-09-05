package com.example.btl_android.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.adapters.HomeCategoryAdapter;
import com.example.btl_android.adapters.HomeProductAdapter;
import com.example.btl_android.adapters.HomePopularAdapter;
import com.example.btl_android.models.AllProductModel;
import com.example.btl_android.models.HomeCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    RecyclerView popularRec,homeCatRec,productRec;
    FirebaseFirestore db;


    //Popular Items
    List<AllProductModel> popularModelList;
    HomePopularAdapter homePopularAdapter;


    //Search view
    EditText search_box;
    private List<AllProductModel> allProductModelList;
    private RecyclerView recyclerViewSearch;
    private HomeProductAdapter viewAllAdapter;

    // Home Category
    List<HomeCategory> categoryList;
    HomeCategoryAdapter homeCategoryAdapter;

    //All product
    List<AllProductModel> productList;
    HomeProductAdapter productAdapter;


    //ScrollView, ProgressBar
    ScrollView scrollView;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home,container,false);
        db = FirebaseFirestore.getInstance();

        popularRec = root.findViewById(R.id.pop_rec);
        homeCatRec = root.findViewById(R.id.explore_rec);
        productRec = root.findViewById(R.id.all_product_rec);

        scrollView = root.findViewById(R.id.scroll_view);
        progressBar = root.findViewById(R.id.progressbar);


        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        //Popular Items
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularModelList = new ArrayList<>();
        homePopularAdapter = new HomePopularAdapter(getActivity(),popularModelList);
        popularRec.setAdapter(homePopularAdapter);

        db.collection("AllProducts")
                .whereEqualTo("mode","popular")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                AllProductModel popularModel = document.toObject(AllProductModel.class);
                                popularModelList.add(popularModel);
                                homePopularAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        }else {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Home Category
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        homeCategoryAdapter = new HomeCategoryAdapter(getActivity(),categoryList);
        homeCatRec.setAdapter(homeCategoryAdapter);

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
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Home Product
        productRec.setLayoutManager(new GridLayoutManager(getActivity(),3));
        productList = new ArrayList<>();
        productAdapter = new HomeProductAdapter(getActivity(),productList);
        productRec.setAdapter(productAdapter);

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
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //Search view
        recyclerViewSearch = root.findViewById(R.id.search_rec);
        search_box = root.findViewById(R.id.search_box);
        allProductModelList = new ArrayList<>();
        viewAllAdapter = new HomeProductAdapter(getContext(), allProductModelList);


        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recyclerViewSearch.setAdapter(viewAllAdapter);
        recyclerViewSearch.setHasFixedSize(true); //set kích thước các mục cố định
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (input.isEmpty()){
                    allProductModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                }else{
                    searchProduct(input.trim());
                }

            }
        });


        return root;
    }

    private void searchProduct(String name) {
        if(!name.isEmpty()) {
                db.collection("AllProducts")
                        .whereGreaterThanOrEqualTo("name", name)
                        .whereLessThanOrEqualTo("name", name + "\uf8ff")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    allProductModelList.clear();
                                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                        AllProductModel allProductModel = doc.toObject(AllProductModel.class);
                                        allProductModelList.add(allProductModel);

                                    }
                                    viewAllAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }else {
            allProductModelList.clear();
            viewAllAdapter.notifyDataSetChanged();
        }
        }

}