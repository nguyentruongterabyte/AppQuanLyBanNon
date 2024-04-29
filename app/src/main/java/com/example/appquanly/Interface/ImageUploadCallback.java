package com.example.appquanly.Interface;

import com.example.appquanly.model.ImageFileModel;

public interface ImageUploadCallback {
    void onSuccess(ImageFileModel imageFile);

    void onFailure(Throwable t);
}
