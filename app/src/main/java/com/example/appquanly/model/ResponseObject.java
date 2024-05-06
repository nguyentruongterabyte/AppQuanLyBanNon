package com.example.appquanly.model;

public class ResponseObject<T> {
    private int status;
    private String message;
    private T result;

    public int getStatus() {
        return status;
    }

    public ResponseObject(int status, String message) {
        this.status = status;
        this.message = message;
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

