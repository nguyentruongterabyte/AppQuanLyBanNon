package com.example.appquanly.model;

public class DoanhThu {
    private String thang;
    private String tong;

    public DoanhThu() {
    }

    public DoanhThu(String thang, String tong) {
        this.thang = thang;
        this.tong = tong;
    }

    public String getThang() {
        return thang;
    }

    public void setThang(String thang) {
        this.thang = thang;
    }

    public String getTong() {
        return tong;
    }

    public void setTong(String tong) {
        this.tong = tong;
    }
}
