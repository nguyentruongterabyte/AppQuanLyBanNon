package com.example.appquanly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appquanly.Interface.ItemClickListener;
import com.example.appquanly.R;
import com.example.appquanly.activity.ChiTietDonHangActivity;
import com.example.appquanly.model.DonHang;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> donHanglist;

    public DonHangAdapter(Context context, List<DonHang> donHanglist) {
        this.context = context;
        this.donHanglist = donHanglist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_don_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = donHanglist.get(position);
        DecimalFormat dft = new DecimalFormat("###,###,###");

        holder.tvMaDonHang.setText(String.format("Đơn hàng: %s", donHang.getMaDonHang()));
        holder.tvTotalItems.setText(String.format("%s sản phẩm", donHang.getSoLuong()));
        holder.tvTotalCost.setText(String.format("Thành tiền: đ%s", dft.format(Double.parseDouble(donHang.getTongTien()))));
        holder.tvStatus.setText(donHang.getTrangThai());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietDonHangActivity.class);
            intent.putExtra("donHang", donHang);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerViewChiTietDonHang.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItems().size());
        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(context, donHang.getItems());
        holder.recyclerViewChiTietDonHang.setLayoutManager(layoutManager);
        holder.recyclerViewChiTietDonHang.setAdapter(chiTietDonHangAdapter);
        holder.recyclerViewChiTietDonHang.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return donHanglist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMaDonHang, tvTotalItems, tvTotalCost, tvStatus;
        RecyclerView recyclerViewChiTietDonHang;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang = itemView.findViewById(R.id.tvMaDonHang);
            tvTotalItems = itemView.findViewById(R.id.totalItems);
            tvTotalCost = itemView.findViewById(R.id.totalCost);
            tvStatus = itemView.findViewById(R.id.trangThai);
            recyclerViewChiTietDonHang = itemView.findViewById(R.id.recyclerViewChiTietDonHang);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onClick(v, getAdapterPosition(), false);
            }
        }
    }
}
