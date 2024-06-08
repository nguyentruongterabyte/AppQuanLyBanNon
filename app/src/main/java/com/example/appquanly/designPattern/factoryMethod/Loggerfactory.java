package com.example.appquanly.designPattern.factoryMethod;

import android.content.Context;
import android.widget.Toast;

import com.example.appquanly.activity.MainActivity;
import com.example.appquanly.utils.Utils;

import java.util.Properties;

public class Loggerfactory {
    private static String getLogType() {
       String logType = Utils.logType;
       return logType;
    }

    public static LogUtils getLogger() {

        String logType = getLogType();
        if (logType != null && logType.equals("log")) {
            return new Loge();
        } else {
            return new ToastLog();
        }
    }
}
