package com.example.appquanly.networking;


import com.example.appquanly.model.DonHang;
import com.example.appquanly.model.ResponseObject;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderApiCalls {
    private static final ApiQuanLy apiQL = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);

    // Xem đơn hàng user id
    public static void getByUserId(int userId, Consumer<ResponseObject<List<DonHang>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.xemDonHang(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                }));
    }

//    Xem tất cả đơn hàng
    public static void getAll(Consumer<ResponseObject<List<DonHang>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.xemTatCaDonHang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                }));
    }

    // Cập nhật trạng thái đơn hàng
    public static void updateOrderStatus(int maDonHang, String trangThai, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.capNhatTrangThaiDonHang(maDonHang, trangThai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                }));
    }
}
