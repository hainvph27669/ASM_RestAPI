package com.example.asm_restapi.Cars;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_restapi.API.APIService;
import com.example.asm_restapi.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity_Them_Car extends AppCompatActivity {

    private EditText edtTenAdd, edtHangAdd, edtNamsxAdd, edtGiaAdd, edtAnhxeAdd;
    private Button btnAddCar;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_them_car);

        // Khởi tạo các view
        edtTenAdd = findViewById(R.id.edtTenAdd);
        edtHangAdd = findViewById(R.id.edtHangAdd);
        edtNamsxAdd = findViewById(R.id.edtNamsxAdd);
        edtGiaAdd = findViewById(R.id.edtGiaAdd);
        edtAnhxeAdd = findViewById(R.id.edtAnhxeAdd);
        btnAddCar = findViewById(R.id.btnAddCar);

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN) // URL API chính xác
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(APIService.class);

        // Nút thêm xe
        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenXe = edtTenAdd.getText().toString().trim();
                String hangXe = edtHangAdd.getText().toString().trim();
                String namSxStr = edtNamsxAdd.getText().toString().trim();
                String giaStr = edtGiaAdd.getText().toString().trim();
                String anhXe = edtAnhxeAdd.getText().toString().trim();

                // Kiểm tra dữ liệu
                if (tenXe.isEmpty() || hangXe.isEmpty() || namSxStr.isEmpty() || giaStr.isEmpty() || anhXe.isEmpty()) {
                    Toast.makeText(MainActivity_Them_Car.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int namSx, gia;
                try {
                    namSx = Integer.parseInt(namSxStr);
                    gia = Integer.parseInt(giaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity_Them_Car.this, "Năm sản xuất và giá phải là số!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo đối tượng Car mới
                Car xeMoi = new Car(null, tenXe,  hangXe,namSx, gia, anhXe);

                // Gửi yêu cầu thêm xe
                Call<List<Car>> callAddXe = apiService.addXe(xeMoi);
                callAddXe.enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(MainActivity_Them_Car.this, "Thêm xe thành công!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Gửi tín hiệu về MainActivity_TrangChu
                            finish(); // Kết thúc Activity
                        } else {
                            Toast.makeText(MainActivity_Them_Car.this, "Thêm xe thất bại. Kiểm tra API.", Toast.LENGTH_SHORT).show();
                            Log.e("API_ERROR", "Mã lỗi: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Car>> call, Throwable t) {
                        Toast.makeText(MainActivity_Them_Car.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("API_FAILURE", t.getMessage(), t);
                    }
                });
            }
        });
    }
}
