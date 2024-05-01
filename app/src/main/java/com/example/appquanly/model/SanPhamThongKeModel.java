package com.example.appquanly.model;

import java.util.List;

public class SanPhamThongKeModel {
    private int status;
    private String message;
    private List<SanPhamThongKe> object;

    public SanPhamThongKeModel(int status, String message, List<SanPhamThongKe> object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SanPhamThongKe> getObject() {
        return object;
    }

    public void setObject(List<SanPhamThongKe> object) {
        this.object = object;
    }
}
