package com.example.appquanly.networking;

import com.example.appquanly.model.ToaDo;
import com.example.appquanly.model.ToaDoModel;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LocationApiCalls {
    private static final ApiQuanLy apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);

    public static void getLocation(Consumer<ToaDoModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.getToaDo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable ->
                {
                    callback.accept(new ToaDoModel(500, throwable.getMessage()));
                })
        );
    }

    public static void createLocation(ToaDo toaDo, Consumer<ToaDoModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiBanHang.taoTaoDo(toaDo.getTenViTri(), toaDo.getKinhDo(), toaDo.getViDo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable ->
                {
                    callback.accept(new ToaDoModel(500, throwable.getMessage()));
                })
        );
    }
}
