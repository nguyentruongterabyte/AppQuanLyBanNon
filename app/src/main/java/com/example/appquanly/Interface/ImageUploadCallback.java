package com.example.appquanly.Interface;

import com.example.appquanly.model.ResponseObject;

public interface ImageUploadCallback {
    void onSuccess(ResponseObject<String> imageFile);

    void onFailure(Throwable t);
}
