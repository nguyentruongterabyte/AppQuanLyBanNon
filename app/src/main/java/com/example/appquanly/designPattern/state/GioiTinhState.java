package com.example.appquanly.designPattern.state;

import com.example.appquanly.model.SanPham;

public interface GioiTinhState {
    public void doAction(SanPham sanPham);
}
