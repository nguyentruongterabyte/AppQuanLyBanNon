package com.example.appquanly.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquanly.R;
import com.example.appquanly.adapter.ChiTietDonHangAdapter;
import com.example.appquanly.adapter.DonHangAdapter;
import com.example.appquanly.model.DonHang;
import com.example.appquanly.model.User;
import com.example.appquanly.networking.OrderApiCalls;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ChiTietDonHangActivity extends AppCompatActivity {
    String[] trangThaiList = {"Chờ xác nhận", "Đang giao", "Đã giao", "Đã hủy"};
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    TextView maDonHang, totalItems, totalCost, tvKH, tvDC, tvPhuongThuc;
    RecyclerView recyclerViewChiTietDonHang;
    Spinner spinnerTrangThai;
    Button btnSua, btnPDF;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    DonHang donHang = new DonHang();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        setControl();
        initData();
        setEvent();


    }

    private void setEvent() {
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InHoaDonActivity.class);
                intent.putExtra("maDonHang", donHang.getMaDonHang());
                startActivity(intent);
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donHang.setTrangThai(trangThaiList[spinnerTrangThai.getSelectedItemPosition()]);
                OrderApiCalls.updateOrderStatus(donHang.getMaDonHang(), donHang.getTrangThai(), donHangModel -> {
                    if (donHangModel.isSuccess()) {
                        Toast.makeText(getApplicationContext(), "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cập nhật trạng thái không thành công", Toast.LENGTH_SHORT).show();

                    }
                }, compositeDisposable);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent != null) {
            donHang = (DonHang) intent.getSerializableExtra("donHang");
            if(donHang != null && donHang.getItems() != null) {
                maDonHang.setText(String.valueOf("Mã đơn hàng: " + donHang.getMaDonHang()));
                totalItems.setText(String.valueOf(donHang.getSoLuong() +  " sản phẩm"));
                DecimalFormat dft = new DecimalFormat("###,###,###");
                totalCost.setText("Thành tiền: " + dft.format(Long.parseLong(donHang.getTongTien())));

                tvKH.setText(String.valueOf("Mã khách hàng: " + donHang.getUserId()));
                tvDC.setText("Địa chỉ: " + donHang.getDiaChi());
                if (donHang.getToken().equals("")){
                    tvPhuongThuc.setText("Thanh toán khi nhận hàng");
                } else {
                    tvPhuongThuc.setText("Thanh toán bằng ZaloPay");
                }


                int trangThai = -1;
                for(int i = 0; i < trangThaiList.length; i++) {
                    if (donHang.getTrangThai().equals(trangThaiList[i])) {
                        trangThai = i;
                        break;
                    }
                }

                // Khởi tạo ArrayAdapter cho Spinner
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThaiList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTrangThai.setAdapter(spinnerAdapter);

                // Lấy trạng thái từ đối tượng hoaDon và thiết lập cho Spinner
                spinnerTrangThai.setSelection(trangThai);

                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        false);
                layoutManager.setInitialPrefetchItemCount(donHang.getItems().size());
                ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(getApplicationContext(), donHang.getItems());
                recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
                recyclerViewChiTietDonHang.setAdapter(chiTietDonHangAdapter);
                recyclerViewChiTietDonHang.setRecycledViewPool(viewPool);
                ChiTietDonHangAdapter adapter = new ChiTietDonHangAdapter(getApplicationContext(), donHang.getItems());
                recyclerViewChiTietDonHang.setAdapter(adapter);
            }
        }
    }

    private void setControl() {
        maDonHang =    findViewById(R.id.maDonHang);
        totalItems =    findViewById(R.id.totalItems);
        totalCost =    findViewById(R.id.totalCost);
        tvKH =    findViewById(R.id.tvKH);
        tvDC =    findViewById(R.id.tvDC);
        tvPhuongThuc =    findViewById(R.id.tvPhuongThuc);
        recyclerViewChiTietDonHang =  findViewById(R.id.recyclerViewChiTietDonHang);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        btnSua =    findViewById(R.id.btnSua);
        btnPDF = findViewById(R.id.btnPDF);
    }


}