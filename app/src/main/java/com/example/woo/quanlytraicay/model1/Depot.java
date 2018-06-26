package com.example.woo.quanlytraicay.model1;

public class Depot {
    private String idTraiCay, thoiGian;
    private int gia, soLuong;

    public Depot() {

    }

    public Depot(String idTraiCay, String thoiGian, int gia, int soLuong) {
        this.idTraiCay = idTraiCay;
        this.thoiGian = thoiGian;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public String getIdTraiCay() {
        return idTraiCay;
    }

    public void setIdTraiCay(String idTraiCay) {
        this.idTraiCay = idTraiCay;
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
