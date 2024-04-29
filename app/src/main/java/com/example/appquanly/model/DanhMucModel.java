package com.example.appquanly.model;

import java.util.List;

public class DanhMucModel {
    private boolean success;
    private String message;
    private List<DanhMuc> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DanhMuc> getResult() {
        return result;
    }

    public void setResult(List<DanhMuc> result) {
        this.result = result;
    }
}

