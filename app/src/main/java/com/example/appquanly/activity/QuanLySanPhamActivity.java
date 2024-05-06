package com.example.appquanly.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appquanly.R;
import com.example.appquanly.adapter.SanPhamAdapter;
import com.example.appquanly.model.SanPham;
import com.example.appquanly.networking.ProductApiCalls;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class QuanLySanPhamActivity extends AppCompatActivity {



    RecyclerView recyclerViewDSSanPham;
    ApiQuanLy apiQuanLy;
    List<SanPham> mangSanPham;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Toolbar toolbar;
    ImageView imgSearch;
    LinearLayoutManager layoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    AppCompatButton btnThem;
    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);
        apiQuanLy = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);
        setControl();
        ActionToolBar();
        getDanhSachSanPham(page);
        addEventLoad();
        setEventClick();

    }


    private void setEventClick() {

        btnThem.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
            startActivity(intent);
        });

        imgSearch.setOnClickListener(v -> {
            Intent timKiem = new Intent(getApplicationContext(), TimKiemSanPhamActivity.class);
            startActivity(timKiem);
        });
    }



    private void addEventLoad() {
        recyclerViewDSSanPham.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    // Trong khi user scroll màn hình
                    int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == mangSanPham.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMore() {
        handler.post(() -> {
            // add null vào mảng để hàm getItemViewType xác định được
            // user kéo đến phần tử cuối cùng
            mangSanPham.add(null);
            sanPhamAdapter.notifyItemInserted(mangSanPham.size() - 1);
        });
        handler.postDelayed(() -> {
            // remove null
            for (int i = mangSanPham.size() - 1; i >= 0; i--) {
                if (mangSanPham.get(i) == null) {
                    mangSanPham.remove(i);
                    break;
                }
            }
            sanPhamAdapter.notifyItemRemoved(mangSanPham.size());
            page += 1;
            getDanhSachSanPham(page);
            sanPhamAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 1000);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getDanhSachSanPham(int page) {

        ProductApiCalls.getInAPage(page, sanPhamModel -> {

            if (sanPhamModel.getStatus() == 200) {
                if (sanPhamAdapter == null) {
                    mangSanPham = sanPhamModel.getResult();
                    sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
                    recyclerViewDSSanPham.setAdapter(sanPhamAdapter);
                } else {
                    int soLuongAdd = sanPhamModel.getResult().size();
                    for (int i = 0; i < soLuongAdd; i++) {
                        mangSanPham.add(sanPhamModel.getResult().get(i));
                    }
                }
            } else {
                Toast.makeText(this, sanPhamModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }, compositeDisposable);
    }

    private void setControl() {

        recyclerViewDSSanPham = findViewById(R.id.recyclerViewDSSanPham);
        toolbar = findViewById(R.id.toolbar);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewDSSanPham.setLayoutManager(layoutManager);
        recyclerViewDSSanPham.setHasFixedSize(true);


        imgSearch = findViewById(R.id.imgSearch);

        btnThem = findViewById(R.id.btnThem);
        mangSanPham = new ArrayList<>();

    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

}