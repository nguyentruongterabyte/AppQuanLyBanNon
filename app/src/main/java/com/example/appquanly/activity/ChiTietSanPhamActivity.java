package com.example.appquanly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appquanly.R;

import com.example.appquanly.model.SanPham;
import com.example.appquanly.networking.ProductApiCalls;
import com.example.appquanly.utils.Utils;

import java.text.DecimalFormat;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    TextView tvTenSanPham, tvGiaSanPham, tvMoTaChiTiet;
    Button btnSua, btnXoa;
    ImageView imageChiTiet;
    Toolbar toolbar;
    SanPham sanPham;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        setControl();
        initData();
        ActionToolBar();
        setEvent();

    }

    private void setEvent() {

        // Xử lý khi nhấn vào nút sửa
        btnSua.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
            intent.putExtra("sanPham", sanPham);
            startActivity(intent);
        });

        btnXoa.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // Xử lý xóa sản phẩm
                    ProductApiCalls.delete(sanPham.getMaSanPham(), messageModel -> {
                        if (messageModel.getStatus() == 200) {
                            Toast.makeText(ChiTietSanPhamActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), QuanLySanPhamActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ChiTietSanPhamActivity.this, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }, compositeDisposable);
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());

    }


    private void initData() {
        sanPham = (SanPham) getIntent().getSerializableExtra("sanPham");
        if (sanPham != null) {
            DecimalFormat dft = new DecimalFormat("###,###,###");
            tvTenSanPham.setText(sanPham.getTenSanPham());
            tvGiaSanPham.setText(String.format("Giá: %s đ", dft.format(Double.parseDouble(sanPham.getGiaSanPham()))));
            String moTa = "- Màu sắc: " + sanPham.getMauSac() +
                    "\n- Giới tính: " + sanPham.getGioiTinh() +
                    "\n- Số lượng: " + sanPham.getSoLuong();
            tvMoTaChiTiet.setText(moTa);
            if (sanPham.getHinhAnh().contains("http")) {
                Glide.with(getApplicationContext()).load(sanPham.getHinhAnh()).into(imageChiTiet);
            } else {
                String hinhAnhURL = Utils.BASE_URL + Utils.BASE_IMAGE_URL + "product/" + sanPham.getHinhAnh();
                Glide.with(getApplicationContext()).load(hinhAnhURL).into(imageChiTiet);
            }
        }


    }

    private void setControl() {
        tvTenSanPham = findViewById(R.id.tvTenSanPham);
        tvGiaSanPham = findViewById(R.id.tvGiaSanPham);
        tvMoTaChiTiet = findViewById(R.id.tvMoTaChiTiet);

        imageChiTiet = findViewById(R.id.imageChiTiet);
        toolbar = findViewById(R.id.toolbar);

        btnSua = findViewById(R.id.btnSua);
        btnXoa  = findViewById(R.id.btnXoa);



    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}