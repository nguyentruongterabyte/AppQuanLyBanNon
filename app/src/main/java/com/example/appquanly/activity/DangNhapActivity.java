package com.example.appquanly.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appquanly.R;
import com.example.appquanly.networking.UserApiCalls;
import com.example.appquanly.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DangNhapActivity extends AppCompatActivity {
    ToggleButton toggleButtonVisibility;
    ProgressBar progressBar;
    TextInputEditText edtPassword, edtEmail;
    TextView tvResetPass;
    AppCompatButton btnDangNhap;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserApiCalls.initialize(this);
        setContentView(R.layout.activity_dang_nhap);
        setControl();
        setEvent();
    }

    private void setEvent() {

        // Xử lý khi text view reset pass được nhấn
        tvResetPass.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            startActivity(intent);
        });

        // xử lý sự kiện nút bật tắt xem mật khẩu được nhấn
        toggleButtonVisibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleButtonVisibility.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_visibility_on,
                        0,
                        0,
                        0);
            } else {
                // Hide password
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleButtonVisibility.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_visibility_off,
                        0,
                        0,
                        0);
            }
        });

        // Xử lý sự kiện khi button đăng nhập được nhấn
        btnDangNhap.setOnClickListener(v -> {
            String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(edtPassword.getText()).toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(DangNhapActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                // Lưu email và mật khẩu cho lần đăng nhập sau
                Paper.book().write("email", email);
                Paper.book().write("password", password);

                //post data
                progressBar.setVisibility(View.VISIBLE);
                dangNhap(email, password);

            }
        });
    }

    public void dangNhap(String email, String password) {
        UserApiCalls.login(email, password, userModel -> {
            if (userModel.getStatus() == 200) {
                if (userModel.getResult().getRoleId() != Utils.ADMIN_ROLE) {
                    Toast.makeText(DangNhapActivity.this, "Bạn không có quyền truy cập vào ứng dụng này", Toast.LENGTH_SHORT).show();
                } else {
                    isLogin = true;
                    // Lưu thông tin người dùng
                    Paper.book().write("isLogin", isLogin);
                    Utils.currentUser = userModel.getResult();
                    Paper.book().write("user", userModel.getResult());

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("refreshToken", Utils.currentUser.getRefreshToken());
                    editor.putString("accessToken", Utils.currentUser.getAccessToken());
                    editor.apply();
                    // Nếu đăng nhập thành công, chuyển về màn hình trang chủ
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                // Thông báo lỗi nếu email hoặc mật khẩu không đúng
                Toast.makeText(DangNhapActivity.this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }, compositeDisposable);
    }

    private void setControl() {
        // Khởi tạo paper để nhớ email và mật khẩu khi thoát khỏi ứng dụng
        Paper.init(this);

        toggleButtonVisibility = findViewById(R.id.toggleButtonVisibility);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        tvResetPass = findViewById(R.id.tvResetPass);

        btnDangNhap = findViewById(R.id.btnDangNhap);

        progressBar = findViewById(R.id.processBarLogin);


        // đọc dữ liệu của email và password những lần đăng nhập trước đó
        if (Paper.book().read("email") != null && Paper.book().read("password") != null) {
            edtEmail.setText(Paper.book().read("email"));
            edtPassword.setText(Paper.book().read("password"));
            if (Paper.book().read("isLogin") != null) {
                boolean flag = Boolean.TRUE.equals(Paper.book().read("isLogin"));
                if (flag) {
                    new Handler().postDelayed(() -> dangNhap(
                            Paper.book().read("email"),
                            Paper.book().read("password")
                    ), 1000);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.currentUser.getEmail() != null && Utils.currentUser.getPassword() != null) {
            edtEmail.setText(Utils.currentUser.getEmail());
            edtPassword.setText(Utils.currentUser.getPassword());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}