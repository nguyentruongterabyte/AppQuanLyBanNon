package com.example.appquanly.networking;


import androidx.annotation.NonNull;

import com.example.appquanly.Interface.ImageUploadCallback;
import com.example.appquanly.model.ResponseObject;
import com.example.appquanly.model.SanPham;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductApiCalls {
    private static final ApiQuanLy apiQL = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);

    // Tạo mới sản phẩm
    public static void create(SanPham sanPham, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.taoMoiSanPham(
                        sanPham.getTenSanPham(),
                        Long.parseLong(sanPham.getGiaSanPham()),
                        sanPham.getSoLuong(),
                        sanPham.getMauSac(),
                        sanPham.getHinhAnh(),
                        sanPham.getGioiTinh())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );

    }

//    Cập nhật sản phẩm
    public static void update(SanPham sanPham, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.capNhapSanPham(
                        sanPham.getMaSanPham(),
                        sanPham.getTenSanPham(),
                        Long.parseLong(sanPham.getGiaSanPham()),
                        sanPham.getSoLuong(),
                        sanPham.getMauSac(),
                        sanPham.getHinhAnh(),
                        sanPham.getGioiTinh())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );

    }

    // Xóa sản phẩm
    public static void delete(int maSanPham, Consumer<ResponseObject<Void>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.xoaSanPham(maSanPham)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                })
        );
    }

//    Upload ảnh
    public static void uploadImage(MultipartBody.Part fileToUpload, int maSanPham, ImageUploadCallback callback) {

        RequestBody maSanPhamBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(maSanPham));
        Call<ResponseObject<String>> call = apiQL.uploadFile(fileToUpload, maSanPhamBody);
        call.enqueue(new Callback<ResponseObject<String>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseObject<String>> call, @NonNull Response<ResponseObject<String>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Upload failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseObject<String>> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // lấy danh sách sản phẩm trên một trang

    public static void getInAPage(int page, Consumer<ResponseObject<List<SanPham>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.getDanhSachSanPham(page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback, throwable -> {
                            callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                        }
                ));
    }



//    Tìm kiếm sản phẩm
    public static void search(String key, Consumer<ResponseObject<List<SanPham>>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.getDanhSachSanPhamTimKiem(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                       callback, throwable -> {
                            callback.accept(new ResponseObject<>(500, throwable.getMessage()));
                       }
                ));
    }
}
