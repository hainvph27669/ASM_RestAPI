package com.example.asm_restapi.Cars;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.asm_restapi.API.APIService;
import com.example.asm_restapi.MainActivity_TrangChu;
import com.example.asm_restapi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarAdapter extends BaseAdapter {

    private APIService apiService;
    private List<Car> listCars;
    private CarAdapter  carAdapter;

    List<Car> carModelList;
    Context context;

    public CarAdapter(Context context,List<Car> carModelList){
        this.carModelList = carModelList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return carModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return carModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_car, viewGroup, false);


        TextView txtMaXe =(TextView) rowView.findViewById(R.id.txtMaXe);
        TextView txtTen = (TextView) rowView.findViewById(R.id.txtTen);
        TextView txtHang = (TextView) rowView.findViewById(R.id.txtHang);
        TextView txtNamSx = (TextView) rowView.findViewById(R.id.txtNamSx);
        TextView txtGia =(TextView) rowView.findViewById(R.id.txtGia);
        ImageView imgAnh = (ImageView) rowView.findViewById(R.id.imgAnh);

        Car car = carModelList.get(i);




        ImageView  imgDelete = (ImageView) rowView.findViewById(R.id.imgDelete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCarFromServer(car.get_id(), i);


            }
        });
        ImageView  imgEdit= (ImageView) rowView.findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainActivity_Sua_Car.class);

                // Truyền thông tin của xe vào Intent
                intent.putExtra("carId", car.get_id());  // ID của xe
                intent.putExtra("carName", car.getTen()); // Tên xe
                intent.putExtra("carBranch", car.getHang()); // Hãng xe
                intent.putExtra("carYear", car.getNamSX()); // Năm sản xuất
                intent.putExtra("carPrice", car.getGia()); // Giá xe
                intent.putExtra("carImage", car.getAnh()); // Ảnh xe

                // Nếu context là ApplicationContext, cần thêm cờ FLAG_ACTIVITY_NEW_TASK
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                context.startActivity(intent); // Mở activity chỉnh sửa xe


            }
        });

        // Cập nhật thông tin xe
       // txtMaXe.setText("Mã Xe: " + String.valueOf(carModelList.get(i).get_id()));
        txtMaXe.setText("Mã Xe: " + (i + 1));
        txtTen.setText("Tên Xe: " + String.valueOf(carModelList.get(i).getTen()));
        txtHang.setText("Hãng: " + String.valueOf(carModelList.get(i).getHang()));
        txtNamSx.setText("Năm SX: " + String.valueOf(carModelList.get(i).getNamSX()));
        txtGia.setText("Giá: " + String.valueOf(carModelList.get(i).getGia()) +"$");

        // Dùng Picasso hoặc Glide để tải ảnh từ URL
        String urlAnh = carModelList.get(i).getAnh(); // Lấy URL của ảnh từ danh sách
        if (urlAnh != null && !urlAnh.isEmpty()) {
            Picasso.get().load(urlAnh).into(imgAnh);
        } else {
            imgAnh.setImageResource(R.drawable.baseline_android_24); // Ảnh mặc định nếu không có URL
        }




        return rowView;
    }


    private void deleteCarFromServer(String carId, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)  // Đảm bảo DOMAIN chính xác
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);

        // Gửi yêu cầu xóa xe
        Call<Void> call = apiService.xoaXe(carId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa xe khỏi danh sách carModelList
                    carModelList.remove(position);
                    notifyDataSetChanged(); // Cập nhật lại ListView
                    Toast.makeText(context, "Đã xóa xe thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Có lỗi khi xóa xe.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối khi xóa xe.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

