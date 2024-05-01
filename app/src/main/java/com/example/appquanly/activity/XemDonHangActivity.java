package com.example.appquanly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appquanly.R;
import com.example.appquanly.adapter.DonHangAdapter;
import com.example.appquanly.model.User;
import com.example.appquanly.networking.OrderApiCalls;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class XemDonHangActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recyclerViewDonHang;
    Toolbar toolbar;


    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don_hang);
        setControl();
        ActionToolBar();
        getOrders();

    }

    private void getOrders() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        OrderApiCalls.getByUserId(user.getId(), donHangModel -> {
            if (donHangModel.isSuccess()) {
                DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                recyclerViewDonHang.setAdapter(adapter);
            } else {
                Toast.makeText(this, donHangModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }


    private void setControl() {
        recyclerViewDonHang = findViewById(R.id.recyclerViewDonHang);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewDonHang.setLayoutManager(layoutManager);
    }

    private void ActionToolBar() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle("Khách hàng: " + user.getUsername());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
