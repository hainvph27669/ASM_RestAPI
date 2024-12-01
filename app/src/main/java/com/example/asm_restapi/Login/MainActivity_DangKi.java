package com.example.asm_restapi.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asm_restapi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_DangKi extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnDangKi;
    private EditText edtEmailDK, edtPassWordDK, edtPassWord2DK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dang_ki);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        btnDangKi = findViewById(R.id.btnDangKiDK);
        edtEmailDK = findViewById(R.id.edtEmailDK);
        edtPassWordDK = findViewById(R.id.edtPassWordDK);
        edtPassWord2DK = findViewById(R.id.edtPassWord2DK);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Thiết lập sự kiện khi nhấn vào nút đăng ký
        btnDangKi.setOnClickListener(v -> registerUser());
    }

    // Hàm đăng ký tài khoản bằng email và mật khẩu
    private void registerUser() {
        String email = edtEmailDK.getText().toString().trim();
        String password = edtPassWordDK.getText().toString().trim();
        String confirmPassword = edtPassWord2DK.getText().toString().trim();

        // Kiểm tra các trường có bị bỏ trống không
        if (TextUtils.isEmpty(email)) {
            edtEmailDK.setError("Vui lòng nhập email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassWordDK.setError("Vui lòng nhập mật khẩu");
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            edtPassWord2DK.setError("Vui lòng nhập lại mật khẩu");
            return;
        }

        // Kiểm tra mật khẩu khớp
        if (!password.equals(confirmPassword)) {
            edtPassWord2DK.setError("Mật khẩu nhập lại không khớp");
            return;
        }

        // Đăng ký tài khoản với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Lấy thông tin người dùng
                            String uid = user.getUid();  // UID của người dùng
                            String emailRegistered = user.getEmail();  // Email của người dùng

                            // Hiển thị thông tin người dùng
                            Toast.makeText(MainActivity_DangKi.this, "Đăng ký thành công! Email: " + emailRegistered + ", UID: " + uid, Toast.LENGTH_SHORT).show();

                            // Sau khi đăng ký thành công, chuyển sang màn hình đăng nhập và truyền thông tin email
                            Intent intent = new Intent(MainActivity_DangKi.this, MainActivity_DangNhap.class);
                            intent.putExtra("email", email); // Gửi thông tin email
                            startActivity(intent);
                            finish();  // Đóng màn hình đăng ký sau khi chuyển
                        }
                    } else {
                        // Đăng ký thất bại, hiển thị lỗi
                        Toast.makeText(MainActivity_DangKi.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
