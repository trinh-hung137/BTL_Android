package com.example.btl_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.btl_android.MainActivity;
import com.example.btl_android.R;

public class CallActivity extends AppCompatActivity {

    Button call_btn,back_btn,message_btl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        call_btn = findViewById(R.id.call_phone);
        message_btl = findViewById(R.id.message_phone);
        back_btn = findViewById(R.id.back_phone);

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khai bao intent ẩn
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"0978137372"));
                //yêu cầu đồng ý của người gọi
                if(ActivityCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                startActivity(callIntent);
            }
        });
        message_btl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+"0978137372"));
                startActivity(messIntent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}