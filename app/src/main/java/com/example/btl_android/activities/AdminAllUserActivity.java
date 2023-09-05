package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.adapters.AdminUserAdapter;
import com.example.btl_android.adapters.HomeCategoryAdapter;
import com.example.btl_android.models.HomeCategory;
import com.example.btl_android.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminAllUserActivity extends AppCompatActivity {

    List<UserModel> list;
    AdminUserAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_user);

        recyclerView = findViewById(R.id.recycle_admin_user);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        list = new ArrayList<>();
        adapter = new AdminUserAdapter(this,list);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()){
                            String img = userSnapshot.child("profileImg").getValue(String.class);
                            String name = userSnapshot.child("name").getValue(String.class);
                            String email = userSnapshot.child("email").getValue(String.class);
                            String password = userSnapshot.child("password").getValue(String.class);
                            String sdt = userSnapshot.child("number").getValue(String.class);
                            String address = userSnapshot.child("address").getValue(String.class);

                            list.add(new UserModel(name,email,password,img, sdt, address));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}