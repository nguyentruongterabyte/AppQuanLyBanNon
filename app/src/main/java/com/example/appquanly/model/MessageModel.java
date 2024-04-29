package com.example.appquanly.model;

public class MessageModel {
    private boolean success;
    private String message;

    public MessageModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public MessageModel() {
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
