package com.example.appquanly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.appquanly.model.SanPhamThongKe;
import com.example.appquanly.R;
import com.example.appquanly.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ThongKeSanPhamAdapter extends ArrayAdapter<SanPhamThongKe> {

    private Context mContext;
    private int mResource;

    public ThongKeSanPhamAdapter(Context context, int resource, List<SanPhamThongKe> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        ImageView imgSanPham = convertView.findViewById(R.id.imgSanPham);
        TextView tvTenSP = convertView.findViewById(R.id.tvTenSP);
        TextView tvTongSoLuong = convertView.findViewById(R.id.tvTongSoLuong);
        TextView tvSoLuongDaBan = convertView.findViewById(R.id.tvSoLuongDaBan);

        SanPhamThongKe currentItem = getItem(position);

        if (currentItem != null) {
            String monthImageName = currentItem.getHinhAnh();
            int monthImageResId = getContext().getResources().getIdentifier(monthImageName, "drawable", getContext().getPackageName());
            imgSanPham.setImageResource(monthImageResId);
            tvTenSP.setText(currentItem.getTenSanPham());
            tvTongSoLuong.setText(String.valueOf(currentItem.getSoLuongTon()));
            tvSoLuongDaBan.setText(String.valueOf(currentItem.getTongSoLuong()));

            if (currentItem.getHinhAnh().contains("http")) {
                Glide.with(mContext).load(currentItem.getHinhAnh()).into(imgSanPham);
            } else {
                Glide.with(mContext).load(Utils.BASE_URL + Utils.BASE_IMAGE_URL + "product/" + currentItem.getHinhAnh()).into(imgSanPham);
            }
        }

        return convertView;
    }
}
