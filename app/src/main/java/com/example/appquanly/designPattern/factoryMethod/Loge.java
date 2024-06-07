package com.example.appquanly.designPattern.factoryMethod;

import android.content.Context;
import android.util.Log;

import java.util.Properties;

public class Loge implements LogUtils{
    @Override
    public void log(String message, Context context) {
        Properties p = new Properties();
        try {
            p.load(ClassLoader.getSystemResourceAsStream("logger.properties"));
            Log.d("soLuongSanPham", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
