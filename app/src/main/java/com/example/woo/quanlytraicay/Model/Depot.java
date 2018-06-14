package com.example.woo.quanlytraicay.Model;

public class Depot {
    private String id, idTraiCay, thoiGian;
    private int gia, soLuong;

    public Depot() {

    }

    public Depot(String id, String idTraiCay, String thoiGian, int gia, int soLuong) {
        this.id = id;
        this.idTraiCay = idTraiCay;
        this.thoiGian = thoiGian;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
