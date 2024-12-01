package com.example.asm_restapi.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_restapi.MainActivity_TrangChu;
import com.example.asm_restapi.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity_DangNhap extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnDangNhap, btnDangKi;
    private EditText edtEmailDN, edtPassWordDN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dang_nhap);

        // Ánh xạ
        btnDangNhap = findViewById(R.id.btnDangKiDK);
        edtEmailDN = findViewById(R.id.edtEmailDK);
        edtPassWordDN = findViewById(R.id.edtPassWordDK);
        btnDangKi = findViewById(R.id.btnDangKi);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Tải thông tin đăng nhập đã lưu (nếu có)
        loadLoginInfo();

        // Lấy email từ Intent nếu có
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        if (email != null) {
            edtEmailDN.setText(email); // Điền email đã đăng ký vào trường email
        }

        // Sự kiện khi nhấn nút đăng nhập
        btnDangNhap.setOnClickListener(v -> loginUser());

        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_DangNhap.this, MainActivity_DangKi.class);
                startActivity(intent);
            }
        });
    }

    // Hàm đăng nhập người dùng với email và mật khẩu
    private void loginUser() {
        String email = edtEmailDN.getText().toString().trim();
        String password = edtPassWordDN.getText().toString().trim();

        // Kiểm tra các trường có bị bỏ trống không
        if (TextUtils.isEmpty(email)) {
            edtEmailDN.setError("Vui lòng nhập email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassWordDN.setError("Vui lòng nhập mật khẩu");
            return;
        }

        // Đăng nhập với Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công
                        Toast.makeText(MainActivity_DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        // Lưu thông tin đăng nhập vào SharedPreferences
                        saveLoginInfo(email, password);

                        // Chuyển sang màn hình chính (hoặc màn hình khác)
                        Intent intent = new Intent(MainActivity_DangNhap.this, MainActivity_TrangChu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Đăng nhập thất bại, hiển thị lỗi
                        Toast.makeText(MainActivity_DangNhap.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm lưu thông tin đăng nhập vào SharedPreferences
    private void saveLoginInfo(String email, String password) {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    // Hàm tải thông tin đăng nhập từ SharedPreferences và tự động điền vào trường đăng nhập
    private void loadLoginInfo() {
        SharedPreferences sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String savedEmail = sharedPref.getString("email", "");
        String savedPassword = sharedPref.getString("password", "");

        edtEmailDN.setText(savedEmail);
        edtPassWordDN.setText(savedPassword);
    }
}
