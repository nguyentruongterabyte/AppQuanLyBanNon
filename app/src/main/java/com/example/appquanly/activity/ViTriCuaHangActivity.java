package com.example.appquanly.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.appquanly.R;
import com.example.appquanly.model.ToaDo;
import com.example.appquanly.networking.LocationApiCalls;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ViTriCuaHangActivity extends AppCompatActivity {

    TextInputEditText edtTenViTri, edtKinhDo,edtViDo;
    Button btnMap, btnUpdate;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ToaDo toaDo = new ToaDo();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationApiCalls.initialize(this);
        setContentView(R.layout.activity_vi_tri_cua_hang);
        setControl();
        ActionToolBar();
        initData();
        setEvent();
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initData() {

        double kinhDo = getIntent().getDoubleExtra("kinhDo", -1000);
        double viDo = getIntent().getDoubleExtra("viDo", -1000);

        if (kinhDo == -1000 && viDo == -1000) {
            LocationApiCalls.getLocation(toaDoModel -> {
                if (toaDoModel.getStatus() == 200) {
                    toaDo = toaDoModel.getResult();
                    edtTenViTri.setText(toaDo.getTenViTri());
                    edtKinhDo.setText(String.valueOf(toaDo.getKinhDo()));
                    edtViDo.setText(String.valueOf(toaDo.getViDo()));
                } else {
                    Toast.makeText(this, toaDoModel.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }, compositeDisposable);
        } else {
            edtViDo.setText(String.valueOf(viDo));
            edtKinhDo.setText(String.valueOf(kinhDo));
        }

    }

    private void setEvent() {
        btnUpdate.setOnClickListener(v -> {
            String tenViTri, viDo, kinhDo;
            tenViTri = Objects.requireNonNull(edtTenViTri.getText()).toString();
            viDo = Objects.requireNonNull(edtViDo.getText()).toString();
            kinhDo = Objects.requireNonNull(edtKinhDo.getText()).toString();
            if (TextUtils.isEmpty(tenViTri)) {
                Toast.makeText(ViTriCuaHangActivity.this, "Bạn chưa nhập tên vị trí", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(viDo)) {
                Toast.makeText(ViTriCuaHangActivity.this, "Bạn chưa nhập vĩ độ", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(kinhDo)) {
                Toast.makeText(ViTriCuaHangActivity.this, "Bạn chưa nhập kinh độ", Toast.LENGTH_SHORT).show();
            } else {
                toaDo = new ToaDo(Double.parseDouble(kinhDo), Double.parseDouble(viDo), tenViTri);
                LocationApiCalls.createLocation(toaDo, toaDoModel -> {
                    if (toaDoModel.getStatus() == 200) {
                        Toast.makeText(ViTriCuaHangActivity.this, "Cập nhật tọa độ shop thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ViTriCuaHangActivity.this, toaDoModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }, compositeDisposable);
            }
        });

        btnMap.setOnClickListener(v -> {
            Intent map = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(map);
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);

        edtKinhDo = findViewById(R.id.edtKinhDo);
        edtTenViTri = findViewById(R.id.edtTenViTri);
        edtViDo = findViewById(R.id.edtViDo);

        btnMap = findViewById(R.id.btnMap);
        btnUpdate = findViewById(R.id.btnUpdate);



        edtKinhDo.setText(toaDo.getKinhDo() != null ? String.valueOf(toaDo.getKinhDo()) : "");
        edtViDo.setText(toaDo.getViDo() != null ? String.valueOf(toaDo.getViDo()): "");
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}