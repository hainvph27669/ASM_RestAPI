package com.example.asm_restapi.Cars;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asm_restapi.API.APIService;
import com.example.asm_restapi.R;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity_Sua_Car extends AppCompatActivity {

    private EditText edtMaXeEdit, edtTenXeEdit, edtHangEdit, edtNamsxEdit, edtGiaEdit, edtAnhXeEdit;
    private Button btnSuaXe;
    private APIService apiService;
    private  Car updatedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_sua_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtMaXeEdit = findViewById(R.id.edtMaXeEdit);
        edtTenXeEdit = findViewById(R.id.edtTenXeEdit);
        edtHangEdit = findViewById(R.id.edtHangEdit);
        edtNamsxEdit = findViewById(R.id.edtNamsxEdit);
        edtGiaEdit = findViewById(R.id.edtGiaEdit);
        edtAnhXeEdit = findViewById(R.id.edtAnhXeEdit);
        btnSuaXe = findViewById(R.id.btnSuaCar);  // Khởi tạo nút sửa

        // Nhận thông tin xe từ Intent
        Intent intent = getIntent();
        String carId = intent.getStringExtra("carId");
        String carName = intent.getStringExtra("carName");
        String carBrand = intent.getStringExtra("carBranch");
        String carYear = intent.getStringExtra("carYear");
        String carPrice = intent.getStringExtra("carPrice");
        String carImage = intent.getStringExtra("carImage");

        // Đưa thông tin vào các EditText
        edtMaXeEdit.setText(carId);
        edtTenXeEdit.setText(carName);
        edtHangEdit.setText(carBrand);
        edtNamsxEdit.setText(carYear);
        edtGiaEdit.setText(carPrice);
        edtAnhXeEdit.setText(carImage);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN) //"http://192.168.56.2:3000/"
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Xử lý sự kiện khi nhấn nút sửa
        btnSuaXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin từ các EditText
                String updatedId = edtMaXeEdit.getText().toString();
                String updatedName = edtTenXeEdit.getText().toString();
                String updatedBrand = edtHangEdit.getText().toString();
                String updatedYear = edtNamsxEdit.getText().toString();
                String updatedPrice = edtGiaEdit.getText().toString();
                String updatedImage = edtAnhXeEdit.getText().toString();

                int year = 0;
                int price = 0;
                try {
                    year = Integer.parseInt(updatedYear); // Chuyển đổi năm thành int
                    price = Integer.parseInt(updatedPrice); // Chuyển đổi giá thành int
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi, xử lý lỗi
                    Toast.makeText(MainActivity_Sua_Car.this, "Vui lòng nhập giá trị hợp lệ cho năm và giá", Toast.LENGTH_SHORT).show();
                    return;  // Dừng lại nếu có lỗi
                }

                // Tạo đối tượng Car với các thông tin đã sửa
                updatedCar = new Car(updatedId, updatedName, updatedBrand, year, price, updatedImage);

                // Gọi API để gửi yêu cầu cập nhật
                apiService = retrofit.create(APIService.class);
                Call<Car> call = apiService.updateXe(updatedId, updatedCar); // Truyền ID và đối tượng Car vào

                // Thực hiện yêu cầu
                call.enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(Call<Car> call, Response<Car> response) {
                        if (response.isSuccessful()) {
                            // Nếu cập nhật thành công
                            Toast.makeText(MainActivity_Sua_Car.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            // Quay lại màn hình trước
                            finish();
                        } else {
                            // Nếu có lỗi
                            Toast.makeText(MainActivity_Sua_Car.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Car> call, Throwable t) {
                        // Xử lý lỗi nếu không thể kết nối
                        Toast.makeText(MainActivity_Sua_Car.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
