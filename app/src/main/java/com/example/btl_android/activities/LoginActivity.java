package com.example.btl_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android.MainActivity;
import com.example.btl_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    EditText email, password;
    TextView signUp;
    FirebaseAuth auth;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signIn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        signUp = findViewById(R.id.sign_up);

        //đăng kí
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
        //đăng nhập
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
                progressBar.setVisibility(View.VISIBLE);

            }

            private void loginUser() {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(LoginActivity.this, "Email không được trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(userEmail)) {
                    Toast.makeText(LoginActivity.this, "Email không hợp lệ!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(LoginActivity.this, "Password không được trống!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (userPassword.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Độ dài Password phải để hơn 6 kí tự", Toast.LENGTH_LONG).show();
                    return;
                }
                //Login User
                auth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    // Kiểm tra quyền admin của người dùng
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (firebaseUser != null) {
                                        String userId = firebaseUser.getUid();
                                        // Truy vấn Firestore để kiểm tra quyền admin
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("Admins")
                                                .document(userId)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        //Admin
                                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công (Admin)", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                                    } else {
                                                        // Khách hàng
                                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    }
                                                }else {
                                                    Toast.makeText(LoginActivity.this, "Tài khoản k chính xác", Toast.LENGTH_LONG).show();
                                                }
                                                //kết thúc loginActivy chuyển dang adminActivity hoặc MainActivity
                                                finish();
                                            }
                                        });

                                    }else {
                                        Toast.makeText(LoginActivity.this, "Tài khoản k tồn tại", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}