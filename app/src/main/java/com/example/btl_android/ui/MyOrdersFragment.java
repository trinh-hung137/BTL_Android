package com.example.btl_android.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.btl_android.R;
import com.example.btl_android.adapters.MyOrderAdapter;
import com.example.btl_android.models.MyCartModel;
import com.example.btl_android.models.MyOrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    MyOrderAdapter orderAdapter;
    List<MyOrderModel> orderModels;


    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_my_orders, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recycle_view_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        orderModels = new ArrayList<>();
        orderAdapter = new MyOrderAdapter(getActivity(),orderModels);

        recyclerView.setAdapter(orderAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                //lay id
                                String documentId = documentSnapshot.getId();
                                MyOrderModel orderModel = documentSnapshot.toObject(MyOrderModel.class);
                                orderModel.setDocumentId(documentId);
                                orderModels.add(orderModel);
                                orderAdapter.notifyDataSetChanged();

                            }
                        }
                    }

                });
        return root;
    }
}