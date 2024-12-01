package com.example.asm_restapi.API;

import com.example.asm_restapi.Cars.Car;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;  // Thêm dòng này để sử dụng List



public interface APIService {

       String DOMAIN = "http://192.168.56.2:3000/";

    @GET("/api/list")
    Call<List<Car>> getCars();

    @POST("/api/add_xe")
    Call<List<Car>> addXe(@Body Car car);

    @DELETE("/api/xoa_xe/{id}")
    Call<Void> xoaXe(@Path("id") String id);

    @PUT("/api/update_xe/{carId}")
    Call<Car> updateXe(@Path("carId") String carId, @Body Car car);

}
