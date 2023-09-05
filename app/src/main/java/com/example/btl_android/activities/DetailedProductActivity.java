package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.models.AllProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DetailedProductActivity extends AppCompatActivity {

    ImageView detatiledImg,addItem, removeItem;
    TextView price, rating, description;
    Button addToCart;

    //quantity, totalPrice
    TextView quantity;
    int totalQuantity = 1;
    int totalPrice=0;

    //toolbar
    Toolbar toolbar;

    //firebase
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    AllProductModel allProductModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_product);

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //lấy dự liệu từ intent qua key detail, gán vào 1 object
        final Object object = getIntent().getSerializableExtra("detail");
        //nếu object là 1 instance của AllProductModel thì gán gtrị vào biến allProductModel
        if (object instanceof AllProductModel) {
            allProductModel = (AllProductModel) object;
        }

        quantity = findViewById(R.id.quantity);

        detatiledImg = findViewById(R.id.detail_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        price = findViewById(R.id.detail_price);
        rating = findViewById(R.id.detail_rating);
        description = findViewById(R.id.detail_description);

        if (allProductModel != null) {
            Glide.with(getApplicationContext()).load(allProductModel.getImg_url()).into(detatiledImg);
            rating.setText(allProductModel.getRating());
            description.setText(allProductModel.getDescription());
            price.setText("Price :$" + allProductModel.getPrice() + "/kg");
            if (allProductModel.getType().equals("egg")) {
                price.setText("Price :$" + allProductModel.getPrice() + "/dozen");

            }
            if (allProductModel.getType().equals("milk")) {
                price.setText("Price :$" + allProductModel.getPrice() + "/litre");
            }

            totalPrice = allProductModel.getPrice() * totalQuantity;

            //Add to cart
            addToCart = findViewById(R.id.button_add_to_cart);
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addedToCart();
                }
            });


            //Thêm số lượng
            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(totalQuantity < 10){
                        totalQuantity++;
                        quantity.setText(String.valueOf(totalQuantity));
                        totalPrice = allProductModel.getPrice() * totalQuantity;

                    }
                }
            });
            //Giảm số lượng
            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(totalQuantity > 1){
                        totalQuantity--;
                        quantity.setText(String.valueOf(totalQuantity));
                        totalPrice = allProductModel.getPrice() * totalQuantity;

                    }
                }
            });
        }
    }

    private void addedToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        saveCurrentDate = currentDate.format(date);

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(date);

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", allProductModel.getName( ));
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedProductActivity.this, "Thêm giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}