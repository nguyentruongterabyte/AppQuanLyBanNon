package com.example.appquanly.designPattern.state;

import com.example.appquanly.model.SanPham;

public class UnisexState implements GioiTinhState{
    @Override
    public void doAction(SanPham sanPham) {
        sanPham.setGioiTinh(this.toString());
    }

    public String toString()
    {
        return "Unisex";
    }
}
