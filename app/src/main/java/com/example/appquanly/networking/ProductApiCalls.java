package com.example.appquanly.networking;

import androidx.annotation.NonNull;

import com.example.appquanly.Interface.ImageUploadCallback;
import com.example.appquanly.model.ImageFileModel;
import com.example.appquanly.model.MessageModel;
import com.example.appquanly.model.SanPham;
import com.example.appquanly.model.SanPhamModel;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import java.util.ArrayList;
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
    public static void create(SanPham sanPham, Consumer<SanPhamModel> callback, CompositeDisposable compositeDisposable) {
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
                    callback.accept(new SanPhamModel(false, throwable.getMessage()));
                })
        );

    }

//    Cập nhật sản phẩm
    public static void update(SanPham sanPham, Consumer<SanPhamModel> callback, CompositeDisposable compositeDisposable) {
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
                    callback.accept(new SanPhamModel(true, throwable.getMessage()));
                })
        );

    }

    // Xóa sản phẩm
    public static void delete(int maSanPham, Consumer<MessageModel> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.xoaSanPham(maSanPham)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback, throwable -> {
                    callback.accept(new MessageModel(false, throwable.getMessage()));
                })
        );
    }

//    Upload ảnh
    public static void uploadImage(MultipartBody.Part fileToUpload, int maSanPham, ImageUploadCallback callback) {

        RequestBody maSanPhamBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(maSanPham));
        Call<ImageFileModel> call = apiQL.uploadFile(fileToUpload, maSanPhamBody);
        call.enqueue(new Callback<ImageFileModel>() {
            @Override
            public void onResponse(@NonNull Call<ImageFileModel> call, @NonNull Response<ImageFileModel> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Upload failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageFileModel> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // lấy danh sách sản phẩm trên một trang

    public static void getInAPage(int page, Consumer<List<SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.getDanhSachSanPham(page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                callback.accept(sanPhamModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }



//    Tìm kiếm sản phẩm
    public static void search(String key, Consumer<List<SanPham>> callback, CompositeDisposable compositeDisposable) {
        compositeDisposable.add(apiQL.getDanhSachSanPhamTimKiem(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if (sanPhamModel.isSuccess()) {
                                callback.accept(sanPhamModel.getResult());
                            } else {
                                callback.accept(new ArrayList<>());
                            }
                        }, throwable -> {
                            callback.accept(new ArrayList<>());
                        }
                ));
    }
}
