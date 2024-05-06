package com.example.appquanly.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appquanly.R;
import com.example.appquanly.adapter.UserAdapter;
import com.example.appquanly.model.User;
import com.example.appquanly.networking.UserApiCalls;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class KhachHangActivity extends AppCompatActivity {
    private ListView listView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);
        setControl();
        ActionToolBar();
        initData();

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Khi nhấn vào nút trở về thì trở về trang chủ
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initData() {
        // Gọi API để lấy danh sách người dùng
        UserApiCalls.getAll(userModel -> {
            if (userModel.getStatus() == 200) {
                List<User> userList = userModel.getResult();
                if (userList != null) {
                    // Gán danh sách người dùng vào Adapter
                    UserAdapter adapter = new UserAdapter(KhachHangActivity.this, userList);
                    // Gán Adapter cho ListView
                    listView.setAdapter(adapter);
                }
            } else {
                Toast.makeText(this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }

    private void setControl() {
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
