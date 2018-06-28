package com.example.woo.quanlytraicay.model;

public class Depot {
    private String tenTraiCay, thoiGian;
    private int gia, soLuong;

    public Depot() {
    }

    public Depot(String tenTraiCay, String thoiGian, int gia, int soLuong) {
        this.tenTraiCay = tenTraiCay;
        this.thoiGian = thoiGian;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public String getTenTraiCay() {
        return tenTraiCay;
    }

    public void setTenTraiCay(String tenTraiCay) {
        this.tenTraiCay = tenTraiCay;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
