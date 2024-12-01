package com.example.asm_restapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_restapi.API.APIService;
import com.example.asm_restapi.Cars.Car;
import com.example.asm_restapi.Cars.CarAdapter;
import com.example.asm_restapi.Cars.MainActivity_Them_Car;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class MainActivity_TrangChu extends AppCompatActivity {
    private ListView lvCar;
    private Button btnAddCar;
    private ImageView imgDelete, imgEdit;
    private APIService apiService;
    private List<Car> listCars;
    private CarAdapter  carAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_trang_chu);

        lvCar = findViewById(R.id.lvCar);
        btnAddCar= findViewById(R.id.btnAddCar);





        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN) //"http://192.168.56.2:3000/"
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
        Call<List<Car>> call = apiService.getCars();

        // Thực hiện gọi API để lấy danh sách xe


        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listCars = response.body();
                     carAdapter = new CarAdapter(getApplicationContext(), listCars);
                    lvCar.setAdapter(carAdapter);
                    Log.d("API Response", "Data received: " + listCars.size() + " cars found");
                    // Xử lý danh sách xe ở đây
                 //   for (Car car : listCars) {
                  //      Log.d("MainActivity", "Car: " + car.getTen());
                  //  }
                }else {
                    Toast.makeText(MainActivity_TrangChu.this, "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                    Log.e("Main", t.getMessage());
            }
        });



        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_TrangChu.this, MainActivity_Them_Car.class);
                startActivity(intent);
            }
        });

    }


}
