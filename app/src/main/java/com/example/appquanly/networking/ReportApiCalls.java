package com.example.appquanly.networking;

import com.example.appquanly.model.DoanhThu;
import com.example.appquanly.model.ResponseObject;
import com.example.appquanly.model.SanPhamThongKe;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;


import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ReportApiCalls {
    private static final ApiQuanLy apiQL = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);

    // Doanh thu theo tháng của 1 năm
    public static void getRevenue(int year, Consumer<ResponseObject<List<DoanhThu>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.layBaoCaoDoanhThu(year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }

    // get tối đa 10 sản phẩm bán chạy nhất
    public static void getMostProducts(int year, Consumer<ResponseObject<List<SanPhamThongKe>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.layBaoCaoSanPham(year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }
}
