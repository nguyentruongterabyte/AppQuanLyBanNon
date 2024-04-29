package com.example.appquanly.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
    Button btnThem;
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

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
                startActivity(intent);
            }
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

        ProductApiCalls.getInAPage(page, sanPhamList -> {
            if (sanPhamAdapter == null) {
                mangSanPham = sanPhamList;
                sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
                recyclerViewDSSanPham.setAdapter(sanPhamAdapter);
            } else {
                int soLuongAdd = sanPhamList.size();
                for (int i = 0; i < soLuongAdd; i++) {
                    mangSanPham.add(sanPhamList.get(i));
                }
            }

        }, compositeDisposable);
    }

    private void setControl() {

        recyclerViewDSSanPham = findViewById(R.id.recyclerViewDSSanPham);
        toolbar = findViewById(R.id.toolbar);

//        layoutManager = new GridLayoutManager(this, 2);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewDSSanPham.setLayoutManager(layoutManager);
        recyclerViewDSSanPham.setHasFixedSize(true);


        imgSearch = findViewById(R.id.imgSearch);

        mangSanPham = new ArrayList<>();

        btnThem = findViewById(R.id.btnThem);

    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    protected void onResume() {


        super.onResume();
    }
}