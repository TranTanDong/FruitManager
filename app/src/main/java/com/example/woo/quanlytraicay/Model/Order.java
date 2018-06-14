package com.example.woo.quanlytraicay.Model;

public class Order {
    private String id, idTraiCay, thoiGian, mail;
    private int soLuong, gia, hinh;

    public Order() {

    }

    public Order(String id, String idTraiCay, String thoiGian, String mail, int soLuong, int gia, int hinh) {
        this.id = id;
        this.idTraiCay= idTraiCay;
        this.thoiGian = thoiGian;
        this.mail = mail;
        this.soLuong = soLuong;
        this.gia = gia;
        this.hinh = hinh;
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

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }
}
