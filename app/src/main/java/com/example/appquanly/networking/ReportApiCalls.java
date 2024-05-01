package com.example.appquanly.networking;

import com.example.appquanly.model.DoanhThuModel;
import com.example.appquanly.model.SanPhamThongKeModel;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;


import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ReportApiCalls {
    private static final ApiQuanLy apiQL = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);

    // Doanh thu theo tháng của 1 năm
    public static void getRevenue(int year, Consumer<DoanhThuModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.layBaoCaoDoanhThu(year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new DoanhThuModel(500, throwable.getMessage(), new ArrayList<>()));
                })
        );
    }

    // get tối đa 10 sản phẩm bán chạy nhất
    public static void getMostProducts(int year, Consumer<SanPhamThongKeModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.layBaoCaoSanPham(year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new SanPhamThongKeModel(500, throwable.getMessage(), new ArrayList<>()));
                })
        );
    }
}
