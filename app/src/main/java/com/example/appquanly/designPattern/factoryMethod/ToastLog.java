package com.example.appquanly.designPattern.factoryMethod;

import android.content.Context;
import android.widget.Toast;

public class ToastLog implements LogUtils{

    @Override
    public void log(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
