package com.example.btl_android.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CloudMediaProvider;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.activities.LoginActivity;
import com.example.btl_android.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    Button logout;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseFirestore database;

    CircleImageView profileImg;
    EditText name,email, number,address,password;
    Button update;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container,false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        profileImg = root.findViewById(R.id.profile_img);
        name = root.findViewById(R.id.profile_name);
        password = root.findViewById(R.id.profile_password);
        email = root.findViewById(R.id.profile_email);
        number = root.findViewById(R.id.profile_number);
        address = root.findViewById(R.id.profile_address);
        update = root.findViewById(R.id.update);



        //lấy ảnh từ firebase đưa ra màn hình
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        Glide.with(getContext()).load(userModel.getProfileImg()).into(profileImg);
                        name.setText(userModel.getName());
                        email.setText(userModel.getEmail());
                        password.setText(userModel.getPassword());
                        number.setText(userModel.getNumber());
                        address.setText(userModel.getAddress());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //thông báo rằng sẽ lấy 1 nguồn ảnh từ bên ngoài có kiểu image/*
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                someActivityResultLauncher.launch(intent);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
        return root;
    }

    private void updateUserProfile() {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userNumber = number.getText().toString();
        String userAddress = address.getText().toString();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(getContext(), "Tên không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(getContext(), "Email không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidEmail(userEmail)) {
            Toast.makeText(getContext(), "Email không hợp lệ!", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(getContext(), "Password không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userNumber)){
            Toast.makeText(getContext(), "Tên không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userAddress)){
            Toast.makeText(getContext(), "Tên không được trống!", Toast.LENGTH_LONG).show();
            return;
        }
        if(userPassword.length() <6){
            Toast.makeText(getContext(), "Độ dài Password phải để hơn 6 kí tự", Toast.LENGTH_LONG).show();
            return;
        }
        //update pass bên authen
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(userPassword);

        //update pass ben realtime
        UserModel updateUserModel = new UserModel(userName,userEmail,userPassword,userNumber,userAddress);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(updateUserModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //xử lý kết quả trả về khi chọn 1 ảnh trong máy
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if(data.getData() != null){
                            Uri profileUri = data.getData();
                            profileImg.setImageURI(profileUri);
                            //lưu url ảnh vào storage
                            final StorageReference reference = storage.getReference().child("profile_picture")
                                    .child(FirebaseAuth.getInstance().getUid());
                            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(), "Đã tải ảnh lên", Toast.LENGTH_SHORT).show();
                                    // storage lấy url ảnh vừa tải lên
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //set url lấy được vào thuộc tính profileImg trog realtime
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                                            .child("profileImg").setValue(uri.toString());
                                            Toast.makeText(getContext(), "Ảnh hồ sơ đã được tải", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        }
                    }
                }

            });
}