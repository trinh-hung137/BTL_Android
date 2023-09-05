package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android.R;
import com.example.btl_android.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    Button signUp;
    EditText name, email, password,number,address;
    TextView signIn;
    FirebaseDatabase database;
    FirebaseAuth auth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressBar= findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signUp = findViewById(R.id.login_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        number = findViewById(R.id.number_reg);
        address = findViewById(R.id.address_reg);
        signIn = findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity (new Intent( RegistrationActivity. this, LoginActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatUser();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void creatUser() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userNumber = number.getText().toString();
        String userAddress = address.getText().toString();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Tên không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Email không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidEmail(userEmail)) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Password không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userNumber)){
            Toast.makeText(this, "Tên không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userAddress)){
            Toast.makeText(this, "Tên không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(userPassword.length() <6){
            Toast.makeText(this, "Độ dài Password phải để hơn 6 kí tự", Toast.LENGTH_LONG).show();
            return;
        }
        // Tạo User
        //sd Realtime database để lưu ttin tkhoản đăng kí
        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserModel userModel = new UserModel(userName,userEmail,userPassword,userNumber,userAddress); //tạo đối tượng lưu ttin đã nhập
                            String id = task.getResult().getUser().getUid(); //lấy id của kqua trả về hàm createUserWithEmailAndPassword
                            //node Users, key là id và setValue sẽ lưu đối tượng user vào
                            database.getReference().child("Users").child(id).setValue(userModel);

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Đăng kí thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}