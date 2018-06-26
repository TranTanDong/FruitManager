package com.example.woo.quanlytraicay.model1;

public class Order {
    private String ten, thoiGian, mail, hinh;
    private int soLuong, gia;

    public Order() {

    }

    public Order(String ten, String thoiGian, String mail, String hinh, int soLuong, int gia) {
        this.ten = ten;
        this.thoiGian = thoiGian;
        this.mail = mail;
        this.hinh = hinh;
        this.soLuong = soLuong;
        this.gia = gia;
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

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }


    public String getHinh() {
        return hinh;
    }
}
