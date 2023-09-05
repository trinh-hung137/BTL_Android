package com.example.btl_android.ui;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.btl_android.R;
import com.example.btl_android.MainActivity;
import com.example.btl_android.adapters.MyCartAdapter;
import com.example.btl_android.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyCartsFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    Button buyNow;
    //progressbar
    ProgressBar progressBar;

    TextView overTotalAmount;


    public MyCartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);

        //notification
        createNotificationChannel();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recycle_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        buyNow = root.findViewById(R.id.button_buy_now);
        progressBar = root.findViewById(R.id.progressbar);
        recyclerView.setVisibility(View.GONE);

        overTotalAmount = root.findViewById(R.id.total_amount);


        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);

        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                //lay id
                                String documentId = documentSnapshot.getId();

                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);

                                cartModel.setDocumentId(documentId);

                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                            caculateTotalAmount(cartModelList);
                        }
                    }
                });

        //xu ly button buy now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra("itemList", (Serializable) cartModelList);
                for (MyCartModel model : cartModelList) {
                    final HashMap<String, Object> cartMap = new HashMap<>();

                    cartMap.put("productName", model.getProductName());
                    cartMap.put("productPrice", model.getProductPrice());
                    cartMap.put("currentDate", model.getCurrentDate());
                    cartMap.put("currentTime", model.getCurrentTime());
                    cartMap.put("totalQuantity", model.getTotalQuantity());
                    cartMap.put("totalPrice", model.getTotalPrice());


                    Task<DocumentReference> task = db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("MyOrder")
                            .add(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                }
                            });

                }

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        snapshot.getReference().delete();
                                    }
                                }
                            }
                        });

                startActivity(intent);
                displayNotifications();

            }
        });

        return root;
        //
    }

    private void displayNotifications() {
        String id = "noti_buynow";
        //sử dụng lớp NotificationCompat.Builder để tạo 1 thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.requireContext(), id)
                .setContentTitle("Thông báo đặt hàng")
                .setContentText("Bạn đã đặt hàng thành công")
                .setSmallIcon(R.drawable.ic_notifications1)
                .setColor(Color.RED)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setDefaults(NotificationCompat.DEFAULT_SOUND);
        //sử dụng lớp NotificationManagerCompat để hiển thị thông báo
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this.requireContext());
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //hiển thị với 1 mã định danh duy nhất
        managerCompat.notify(1, builder.build());
    }

    //tạo kênh thông báo
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("noti_buynow", name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);//đkí với hẹ thống
        }
    }
    private void caculateTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount = 0.0;
        for (MyCartModel myCartModel : cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }
        overTotalAmount.setText("Total Amount :"+ totalAmount);
    }

}