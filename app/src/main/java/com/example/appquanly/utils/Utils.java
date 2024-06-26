package com.example.appquanly.utils;

import android.annotation.SuppressLint;

import com.example.appquanly.model.User;

public class Utils {


    public static final String BASE_URL = "http://192.168.1.2:8000/hatshop/";

    public static final String logType = "toast";
    public static final String BASE_IMAGE_URL = "images/";

    public static final int ADMIN_ROLE = 1;

    // current user
    public static User currentUser = new User();

    // hàm định dạng số lượng đã bán
    @SuppressLint("DefaultLocale")
    public static String formatQuantity(int quantity) {
        if (quantity < 1000) {
            return String.valueOf(quantity);
        } else if (quantity < 1000000) {
            return String.format("%.1fk", quantity / 1000.0);
        } else {
            return String.format("%.1fm", quantity / 1000000.0);
        }
    }
}

