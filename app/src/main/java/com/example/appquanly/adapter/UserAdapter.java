package com.example.appquanly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.appquanly.R;
import com.example.appquanly.activity.XemDonHangActivity;
import com.example.appquanly.model.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users) {

        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_khach_hang, parent, false);
        }
        TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
        TextView textViewUsername = convertView.findViewById(R.id.textViewUsername);
        TextView textViewMobile = convertView.findViewById(R.id.textViewMobile);

        assert user != null;
        textViewEmail.setText(String.format("Email: %s", user.getEmail()));
        textViewUsername.setText(String.format("Username: %s", user.getUsername()));
        textViewMobile.setText(String.format("Mobile: %s", user.getMobile()));

        // Set OnClickListener to open ChiTietDonHangActivity
        convertView.setOnClickListener(v -> {
            // Open ChiTietDonHangActivity

            Intent intent = new Intent(context, XemDonHangActivity.class);
            intent.putExtra("user", user);
            // Pass any necessary data to the ChiTietDonHangActivity using intent.putExtra if needed

            context.startActivity(intent);
        });

        return convertView;
    }

}

