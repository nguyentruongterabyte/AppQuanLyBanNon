package com.example.appquanly.networking;

import android.content.Context;

import com.example.appquanly.model.ResponseObject;
import com.example.appquanly.model.User;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserApiCalls {
    private static ApiQuanLy apiQL;
    public static void initialize(Context context) {
        apiQL = RetrofitClient.getInstance(Utils.BASE_URL, context).create(ApiQuanLy.class);
    }

    public static void getAll(Consumer<ResponseObject<List<User>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }

    // Đăng nhập
    public static void login(String email, String password, Consumer<ResponseObject<User>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.dangNhap(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }

    // Gửi yêu cầu reset mật khẩu
    public static void requestResetPassword(String email, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.guiYeuCauResetMatKhau(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable ->
                        callback.accept(new ResponseObject<>(500, throwable.getMessage()))
                ));
    }

}
