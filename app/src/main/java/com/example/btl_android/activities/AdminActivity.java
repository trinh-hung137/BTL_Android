package com.example.btl_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.btl_android.R;

public class AdminActivity extends AppCompatActivity {

    Button productManage, categoryManage, userManage, back_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        productManage = findViewById(R.id.product_admin_btn);
        categoryManage = findViewById(R.id.category_admin_btn);
        userManage = findViewById(R.id.user_admin_btn);
        back_admin = findViewById(R.id.back_admin);


        back_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        categoryManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminAllCategoryActivity.class);
                startActivity(intent);
            }
        });
        productManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminAllProductActivity.class);
                startActivity(intent);
            }
        });
        userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminAllUserActivity.class);
                startActivity(intent);
            }
        });

    }
}