package com.example.appquanly.model;

import java.util.List;

public class DoanhThuModel {
    private int status;
    private String message;
    private List<DoanhThu> object;

    public DoanhThuModel() {
    }

    public DoanhThuModel(int status, String message, List<DoanhThu> object) {
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

    public List<DoanhThu> getObject() {
        return object;
    }

    public void setObject(List<DoanhThu> object) {
        this.object = object;
    }
}
