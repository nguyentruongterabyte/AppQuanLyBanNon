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

import com.example.appquanly.model.DoanhThu;
import com.example.appquanly.R;

import java.text.DecimalFormat;
import java.util.List;

public class ThongKeAdapter extends ArrayAdapter<DoanhThu> {
    private Context mContext;
    private int mResource;

    public ThongKeAdapter(@NonNull Context context, int resource, @NonNull List<DoanhThu> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        ImageView imgThang = convertView.findViewById(R.id.imgThang);
        TextView tvThang = convertView.findViewById(R.id.tvThang);
        TextView tvTong = convertView.findViewById(R.id.tvTong);

        DoanhThu doanhThu = getItem(position);

        if (doanhThu != null) {
            // Set image
            String monthImageName = "thang"+ doanhThu.getThang();
            int monthImageResId = getContext().getResources().getIdentifier(monthImageName, "drawable", getContext().getPackageName());
            imgThang.setImageResource(monthImageResId);
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

            String tongDoanhThu = decimalFormat.format(Double.parseDouble(doanhThu.getTong()));

            tvThang.setText("Tháng " + doanhThu.getThang());
            tvTong.setText("Doanh Thu: " + tongDoanhThu + " VND");
        }

        return convertView;
    }
}
