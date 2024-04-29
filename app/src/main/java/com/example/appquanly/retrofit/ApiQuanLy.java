package com.example.appquanly.retrofit;

import com.example.appquanly.model.DonHangModel;
import com.example.appquanly.model.ImageFileModel;
import com.example.appquanly.model.MessageModel;
import com.example.appquanly.model.SanPhamModel;
import com.example.appquanly.model.User;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

import java.util.List;

public interface ApiQuanLy {
    //    Khách hàng
    @GET("api/user/get-all.php") // Đường dẫn của API
    Call<List<User>> getUsers();

    // Đơn hàng
    @GET("api/order/history.php")
    Observable<DonHangModel> xemDonHang(
            @Query("userId") int userId
    );

    @GET("api/order/history-all.php")
    Observable<DonHangModel> xemTatCaDonHang(
    );

    @PUT("api/order/update-status.php")
    @FormUrlEncoded
    Observable<DonHangModel> capNhatTrangThaiDonHang(
            @Field("maDonHang") int maDonHang,
            @Field("trangThai") String trangThai
    );

    //    Sản phẩm
    @POST("api/product/create.php")
    @FormUrlEncoded
    Observable<SanPhamModel> taoMoiSanPham(
            @Field("tenSanPham") String tenSanPham,
            @Field("giaSanPham") long giaSanPham,
            @Field("soLuong") int soLuong,
            @Field("mauSac") String mauSac,
            @Field("hinhAnh") String hinhAnh,
            @Field("gioiTinh") String gioiTinh
    );

    @GET("api/product/page.php")
    Observable<SanPhamModel> getDanhSachSanPham(
            @Query("page") int page,
            @Query("amount") int amount
    );

    @PUT("api/product/update.php")
    @FormUrlEncoded
    Observable<SanPhamModel> capNhapSanPham(
            @Field("maSanPham") int maSanPham,
            @Field("tenSanPham") String tenSanPham,
            @Field("giaSanPham") long giaSanPham,
            @Field("soLuong") int soLuong,
            @Field("mauSac") String mauSac,
            @Field("hinhAnh") String hinhAnh,
            @Field("gioiTinh") String gioiTinh
    );

    @HTTP(method = "DELETE", path = "api/product/delete.php", hasBody = true)
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
            @Field("maSanPham") int maSanPham
    );


    @GET("api/product/search.php")
    Observable<SanPhamModel> getDanhSachSanPhamTimKiem(
            @Query("key") String key
    );


    @Multipart
    @POST("api/product/upload-image.php")
    Call<ImageFileModel> uploadFile(
            @Part MultipartBody.Part file,
            @Part("maSanPham") RequestBody maSanPham
    );
}

