package com.example.appquanly.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.appquanly.R;

public class MainActivity extends AppCompatActivity {
    ImageButton btnSanPham, btnKhachHang, btnThongKe, btnDH, btnViTri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();



        btnKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang
                Intent intent = new Intent(MainActivity.this, KhachHangActivity.class);
                // Start Activity và chờ kết quả trả về
                startActivity(intent);
            }
        });


        btnDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang
                Intent intent = new Intent(MainActivity.this, XemDonHangAllActivity.class);
                // Start Activity và chờ kết quả trả về
                startActivity(intent);
            }
        });


        btnSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuanLySanPhamActivity.class);
                startActivity(intent);
            }
        });

        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThongKeActivity.class);
                startActivity(intent);
            }
        });

        btnViTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViTriCuaHangActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        btnSanPham = findViewById(R.id.btnSP);
        btnKhachHang = findViewById(R.id.btnKH);
        btnThongKe = findViewById(R.id.btnTK);
        btnDH = findViewById(R.id.btnDH);
        btnViTri = findViewById(R.id.btnViTri);
    }
}