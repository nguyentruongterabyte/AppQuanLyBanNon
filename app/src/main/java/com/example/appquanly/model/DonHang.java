package com.example.appquanly.model;

import java.io.Serializable;
import java.util.List;

public class DonHang implements Serializable {

    private int maDonHang;
    private String sdt;
    private String email;
    private int userId;
    private String tongTien;
    private String diaChi;
    private int soLuong;
    private String chiTiet;
    List<SanPham> items;
    private String trangThai;

    private String ngayTao;

    private String token;

    private int hasToken;


    public DonHang() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public List<SanPham> getItems() {
        return items;
    }

    public void setItems(List<SanPham> items) {
        this.items = items;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    public int getHasToken() {
        return hasToken;
    }

    public void setHasToken(int hasToken) {
        this.hasToken = hasToken;
    }
}
