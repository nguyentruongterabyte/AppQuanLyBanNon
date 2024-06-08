package com.example.appquanly.retrofit;

import com.example.appquanly.model.DoanhThu;
import com.example.appquanly.model.DonHang;
import com.example.appquanly.model.ResponseObject;
import com.example.appquanly.model.SanPham;
import com.example.appquanly.model.SanPhamThongKe;
import com.example.appquanly.model.ToaDo;
import com.example.appquanly.model.User;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
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
    @GET("api/user/get-all")
    Observable<ResponseObject<List<User>>> getUsers();

    // Đơn hàng
    @GET("api/order/history")
    Observable<ResponseObject<List<DonHang>>> xemDonHang(
            @Query("userId") int userId
    );

    @GET("api/order/history-all")
    Observable<ResponseObject<List<DonHang>>> xemTatCaDonHang(
    );

    @PUT("api/order/update-status")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> capNhatTrangThaiDonHang(
            @Field("maDonHang") int maDonHang,
            @Field("trangThai") String trangThai
    );

    //    Sản phẩm
    @POST("api/product/create")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> taoMoiSanPham(
            @Field("tenSanPham") String tenSanPham,
            @Field("giaSanPham") long giaSanPham,
            @Field("soLuong") int soLuong,
            @Field("mauSac") String mauSac,
            @Field("hinhAnh") String hinhAnh,
            @Field("gioiTinh") String gioiTinh
    );

    @GET("api/product/page")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPham(
            @Query("page") int page,
            @Query("amount") int amount
    );

    @PUT("api/product/update")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> capNhapSanPham(
            @Field("maSanPham") int maSanPham,
            @Field("tenSanPham") String tenSanPham,
            @Field("giaSanPham") long giaSanPham,
            @Field("soLuong") int soLuong,
            @Field("mauSac") String mauSac,
            @Field("hinhAnh") String hinhAnh,
            @Field("gioiTinh") String gioiTinh
    );

    @HTTP(method = "DELETE", path = "api/product/delete", hasBody = true)
    @FormUrlEncoded
    Observable<ResponseObject<Void>> xoaSanPham(
            @Field("maSanPham") int maSanPham
    );


    @GET("api/product/search")
    Observable<ResponseObject<List<SanPham>>> getDanhSachSanPhamTimKiem(
            @Query("key") String key
    );


    @Multipart
    @POST("api/product/upload-image")
    Call<ResponseObject<String>> uploadFile(
            @Part MultipartBody.Part file,
            @Part("maSanPham") RequestBody maSanPham
    );

    // Báo cáo
    @GET("api/reports/revenue")
    Observable<ResponseObject<List<DoanhThu>>> layBaoCaoDoanhThu(@Query("year") int nam);
    @GET("api/reports/products")
    Observable<ResponseObject<List<SanPhamThongKe>>> layBaoCaoSanPham(@Query("year") int nam);

    // Hóa đơn
    @GET("api/bill/get")
    Observable<ResponseObject<DonHang>> getHoaDon(@Query("maDonHang") int maDonHang);

    // Tọa độ
    @GET("api/location/get")
    Observable<ResponseObject<ToaDo>> getToaDo();

    @POST("api/location/create")
    @FormUrlEncoded
    Observable<ResponseObject<Void>> taoTaoDo(@Field("tenViTri") String tenViTri, @Field("kinhDo") Double kinhDo, @Field("viDo") Double viDo);
}