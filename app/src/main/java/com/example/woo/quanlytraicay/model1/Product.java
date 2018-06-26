package com.example.woo.quanlytraicay.model1;

public class Product {
    private String ten, moTa, xuatXu, hinh;
    private int gia, hSD;


    public Product() {
    }

    public Product(String ten, String hinh, String moTa, String xuatXu, int gia, int hSD) {
        this.ten = ten;
        this.hinh = hinh;
        this.moTa = moTa;
        this.xuatXu = xuatXu;
        this.gia = gia;
        this.hSD = hSD;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int gethSD() {
        return hSD;
    }

    public void sethSD(int hSD) {
        this.hSD = hSD;
    }
}
