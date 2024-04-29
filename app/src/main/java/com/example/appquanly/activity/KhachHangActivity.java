package com.example.appquanly.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appquanly.R;
import com.example.appquanly.adapter.UserAdapter;
import com.example.appquanly.model.User;
import com.example.appquanly.retrofit.ApiQuanLy;
import com.example.appquanly.retrofit.RetrofitClient;
import com.example.appquanly.utils.Utils;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class KhachHangActivity extends AppCompatActivity {
    private ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);

        listView = findViewById(R.id.listView);

        // Tạo Retrofit instance
        ApiQuanLy apiQL = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiQuanLy.class);

        // Gọi API để lấy danh sách người dùng
        Call<List<User>> call = apiQL.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    if (userList != null) {
                        // Gán danh sách người dùng vào Adapter
                        UserAdapter adapter = new UserAdapter(KhachHangActivity.this, userList);
                        // Gán Adapter cho ListView
                        listView.setAdapter(adapter);
                    }
                } else {
                    // Xử lý lỗi
                    Log.e("Error", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Xử lý lỗi
                Log.e("Error", "Network error: " + t.getMessage());
            }
        });
    }
}
