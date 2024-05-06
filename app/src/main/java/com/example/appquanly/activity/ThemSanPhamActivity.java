package com.example.appquanly.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.appquanly.Interface.ImageUploadCallback;
import com.example.appquanly.R;
import com.example.appquanly.model.ResponseObject;
import com.example.appquanly.model.SanPham;
import com.example.appquanly.networking.ProductApiCalls;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ThemSanPhamActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText edtTenSanPham, edtGiaSanPham, edtMauSac, edtHinhAnh, edtSoLuong;
    RadioGroup radGioiTinh;
    RadioButton radNam, radNu, radUnisex;
    AppCompatButton btnThem;
    ImageView btnImage;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SanPham sanPham = new SanPham();
    Boolean isCreating = true;
    ProgressBar progressBar;
    String mediaPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        setControl();
        ActionToolBar();
        setEvent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    private void setEvent() {
        radNam.setChecked(true);
        Intent intent = getIntent();
        if (intent.hasExtra("sanPham")) {
            // The intent contains data for the "sanPham" key
            sanPham = (SanPham) intent.getSerializableExtra("sanPham");
            isCreating = false;
            assert sanPham != null;
            edtTenSanPham.setText(sanPham.getTenSanPham());
            edtGiaSanPham.setText(sanPham.getGiaSanPham());
            edtHinhAnh.setText(sanPham.getHinhAnh());
            edtSoLuong.setText(String.valueOf(sanPham.getSoLuong()));
            edtMauSac.setText(String.valueOf(sanPham.getMauSac()));
            switch (sanPham.getGioiTinh()) {
                case "Nam":
                    radNam.setChecked(true);
                    break;
                case "Nữ":
                    radNu.setChecked(true);
                    break;
                case "Unisex":
                    radUnisex.setChecked(true);
                    break;
            }
            // Perform actions with the sanPham object
        } else {
            // The intent does not contain data for the "sanPham" key
            // Handle the case where "sanPham" data is not available
            isCreating = true;
            sanPham.setMaSanPham(-1);
        }


        btnImage.setOnClickListener(v -> ImagePicker.with(ThemSanPhamActivity.this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start());



        btnThem.setOnClickListener(v -> {
            String tenSanPham = Objects.requireNonNull(edtTenSanPham.getText()).toString();
            String giaSanPham = Objects.requireNonNull(edtGiaSanPham.getText()).toString();
            String mauSac = Objects.requireNonNull(edtMauSac.getText()).toString();
            String hinhAnh = Objects.requireNonNull(edtHinhAnh.getText()).toString();



            if (TextUtils.isEmpty(tenSanPham)) {
                Toast.makeText(ThemSanPhamActivity.this, "Bạn chưa nhập tên", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(giaSanPham)) {
                Toast.makeText(ThemSanPhamActivity.this, "Bạn chưa nhập giá", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mauSac)) {
                Toast.makeText(ThemSanPhamActivity.this, "Bạn chưa nhập màu sắc", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(hinhAnh)) {
                // Sau này up lên fire base
                Toast.makeText(ThemSanPhamActivity.this, "Bạn chưa nhập hình ảnh", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(edtSoLuong.getText().toString())) {
                Toast.makeText(ThemSanPhamActivity.this, "Bạn chưa nhập số lượng", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                if (mediaPath != null) {

                    Uri uri = Uri.parse(mediaPath);

                    File file = new File(getPath(uri));
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    // Upload hình lên trước
                    // Sau khi thành công thì mới tạo hoặc sửa những thông tin khác của sản phẩm
                    ProductApiCalls.uploadImage(fileToUpload, sanPham.getMaSanPham(), new ImageUploadCallback() {
                        @Override
                        public void onSuccess(ResponseObject<String> imageFile) {
                            edtHinhAnh.setText(imageFile.getResult());
                            int soLuong;
                            soLuong = Integer.parseInt(Objects.requireNonNull(edtSoLuong.getText()).toString());
                            String gioiTinh = "";
                            if (radNam.isChecked()) {
                                gioiTinh = "Nam";
                            } else if (radNu.isChecked()) {
                                gioiTinh = "Nữ";
                            } else if (radUnisex.isChecked()) {
                                gioiTinh = "Unisex";
                            }
                            sanPham.setTenSanPham(tenSanPham);
                            sanPham.setGiaSanPham(giaSanPham);
                            sanPham.setHinhAnh(imageFile.getResult());
                            sanPham.setMauSac(mauSac);
                            // Test
                            sanPham.setGioiTinh(gioiTinh);
                            sanPham.setSoLuong(soLuong);

                            if (isCreating) {
                                ProductApiCalls.create(sanPham, sanPhamModel -> {
                                    if (sanPhamModel.getStatus() == 200) {
                                        Toast.makeText(ThemSanPhamActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                        edtTenSanPham.setText("");
                                        edtGiaSanPham.setText("");
                                        edtHinhAnh.setText("");
                                        edtMauSac.setText("");
                                        edtSoLuong.setText("");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent quanLySanPham = new Intent(getApplicationContext(), QuanLySanPhamActivity.class);
                                        startActivity(quanLySanPham);
                                        finish();
                                    } else {
                                        Toast.makeText(ThemSanPhamActivity.this, sanPhamModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, compositeDisposable);
                            } else {
                                ProductApiCalls.update(sanPham, sanPhamModel -> {
                                    if (sanPhamModel.getStatus() == 200) {
                                        Toast.makeText(ThemSanPhamActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();

                                        edtTenSanPham.setText("");
                                        edtGiaSanPham.setText("");
                                        edtHinhAnh.setText("");
                                        edtMauSac.setText("");
                                        edtSoLuong.setText("");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent quanLySanPham = new Intent(getApplicationContext(), QuanLySanPhamActivity.class);
                                        startActivity(quanLySanPham);
                                        finish();
                                    } else {
                                        Toast.makeText(ThemSanPhamActivity.this, sanPhamModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        System.out.println("Truong: " + sanPhamModel.getMessage());
                                    }
                                }, compositeDisposable);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(ThemSanPhamActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Nếu không chọn file ảnh thì chỉ cần update những thông tin khác
                    int soLuong;
                    soLuong = Integer.parseInt(Objects.requireNonNull(edtSoLuong.getText()).toString());
                    String gioiTinh = "";
                    if (radNam.isChecked()) {
                        gioiTinh = "Nam";
                    } else if (radNu.isChecked()) {
                        gioiTinh = "Nữ";
                    } else if (radUnisex.isChecked()) {
                        gioiTinh = "Unisex";
                    }
                    sanPham.setTenSanPham(tenSanPham);
                    sanPham.setGiaSanPham(giaSanPham);
                    sanPham.setHinhAnh(hinhAnh);
                    sanPham.setMauSac(mauSac);
                    // Test
                    sanPham.setGioiTinh(gioiTinh);
                    sanPham.setSoLuong(soLuong);

                    if (isCreating) {
                        ProductApiCalls.create(sanPham, sanPhamModel -> {
                            if (sanPhamModel.getStatus() == 200) {
                                Toast.makeText(ThemSanPhamActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                edtTenSanPham.setText("");
                                edtGiaSanPham.setText("");
                                edtHinhAnh.setText("");
                                edtMauSac.setText("");
                                edtSoLuong.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent quanLySanPham = new Intent(getApplicationContext(), QuanLySanPhamActivity.class);
                                startActivity(quanLySanPham);
                                finish();
                            } else {
                                Toast.makeText(ThemSanPhamActivity.this, sanPhamModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, compositeDisposable);
                    } else {
                        ProductApiCalls.update(sanPham, sanPhamModel -> {
                            if (sanPhamModel.getStatus() == 200) {
                                Toast.makeText(ThemSanPhamActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();

                                edtTenSanPham.setText("");
                                edtGiaSanPham.setText("");
                                edtHinhAnh.setText("");
                                edtMauSac.setText("");
                                edtSoLuong.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent quanLySanPham = new Intent(getApplicationContext(), QuanLySanPhamActivity.class);
                                startActivity(quanLySanPham);
                                finish();
                            } else {
                                Toast.makeText(ThemSanPhamActivity.this, sanPhamModel.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println("Truong: " + sanPhamModel.getMessage());
                            }
                        }, compositeDisposable);
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            mediaPath = data.getDataString();
            if (mediaPath != null) {
                Uri uri = Uri.parse(mediaPath);
                String path = getPath(uri);
                edtHinhAnh.setText(path);
            } else {
                Toast.makeText(getApplicationContext(), "Không hình ảnh nào được chọn cả", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String getPath(Uri uri) {
        String result;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);

        edtTenSanPham = findViewById(R.id.edtTenSanPham);
        edtGiaSanPham = findViewById(R.id.edtGiaSanPham);
        edtMauSac = findViewById(R.id.edtMauSac);
        edtHinhAnh = findViewById(R.id.edtHinhAnh);
        edtSoLuong = findViewById(R.id.edtSoLuong);

        radGioiTinh = findViewById(R.id.radGioiTinh);
        btnThem = findViewById(R.id.btnThem);

        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        radUnisex = findViewById(R.id.radUnisex);
        btnImage = findViewById(R.id.btnImage);

        progressBar = findViewById(R.id.processBarAdd);
    }
}