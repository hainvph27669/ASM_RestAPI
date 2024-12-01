package com.example.asm_restapi.Cars;

public class Car {
             private String _id;
            private String ten;
            private String hang;
             private int namSX;
            private int gia;
            private String anh;

        // Getters và Setters


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Car() {
    }

    public Car(String _id, String ten,  String hang,int namSX, int gia, String anh) {
        this._id = _id;
        this.ten = ten;
        this.hang = hang;
        this.namSX = namSX;
        this.gia = gia;
        this.anh = anh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getNamSX() {
        return namSX;
    }

    public void setNamSX(int namSX) {
        this.namSX = namSX;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
}
