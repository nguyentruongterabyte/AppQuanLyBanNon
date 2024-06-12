package com.example.appquanly.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appquanly.R;
import com.example.appquanly.adapter.ThongKeAdapter;
import com.example.appquanly.model.DoanhThu;
import com.example.appquanly.networking.ReportApiCalls;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ThongKeActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public String getYear;
    Toolbar toolbar;
    AutoCompleteTextView tvMenuYear;
    ListView listThongKe;
    CheckBox ckbKieuXem;
    BarChart barChart;
    List<String> yearsList = new ArrayList<>();
    List<DoanhThu> doanhThuList = new ArrayList<>();
    int[] colors = new int[] {
            Color.rgb(255, 102, 0),   // Màu cam
            Color.rgb(255, 204, 0),   // Màu vàng
            Color.rgb(51, 102, 204),  // Màu xanh dương
            Color.rgb(153, 0, 204),   // Màu tím
            Color.rgb(255, 51, 153),  // Màu hồng
            Color.rgb(0, 153, 204),   // Màu lam
            Color.rgb(204, 0, 0),     // Màu đỏ
            Color.rgb(0, 204, 102),   // Màu xanh lá
            Color.rgb(153, 153, 153), // Màu xám
            Color.rgb(0, 204, 204),   // Màu cyan
            Color.rgb(255, 0, 255),   // Màu magenta
            Color.rgb(0, 128, 128)    // Màu teal
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReportApiCalls.initialize(this);
        setContentView(R.layout.activity_thong_ke);
        setControl();
        yearsList = generateYearsList();
        showDatePickerDialog();
        actionBar();
    }
    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        barChart = findViewById(R.id.barChart);
        ckbKieuXem = findViewById(R.id.checkboxKieu);
        listThongKe = findViewById(R.id.listThongKe);
        tvMenuYear = findViewById(R.id.tvMenuYear);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_thong_ke, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.icTKSP) {
            Intent intent = new Intent(ThongKeActivity.this, TKSanPhamActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.icDoanhThu) {

            return true;
        }
        else {
            finish();
            return true;
        }
    }
    private void showBarChart() {
        barChart.setVisibility(View.VISIBLE);
        listThongKe.setVisibility(View.GONE);
        barChart.getAxisRight().setDrawLabels (false);
        List<BarEntry> barEntries = new ArrayList<>();
        for (DoanhThu doanhThu : doanhThuList) {
            int thang = Integer.parseInt(doanhThu.getThang());
            String tongTien = String.valueOf(doanhThu.getTong());
            barEntries.add(new BarEntry(thang, Float.parseFloat(tongTien)));
        }
        barChart.getAxisRight().setDrawLabels (false);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        BarDataSet dataSet = new BarDataSet(barEntries, "Biểu đồ doanh thu năm " + getYear);
        dataSet.setValueTextSize(12f);
        dataSet.setColors (colors);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        int delay = 1000;
        barChart.animateXY(delay, delay);
        barChart.invalidate();
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
    }
    private void getDoanhThuTheoThang(String year) {
        doanhThuList.clear();
        ReportApiCalls.getRevenue(Integer.parseInt(year), doanhThuModel -> {
            if (doanhThuModel.getStatus() == 200) {
                doanhThuList = doanhThuModel.getResult();
                ThongKeAdapter adapter = new ThongKeAdapter(ThongKeActivity.this, R.layout.layout_item_thong_ke, doanhThuList);
                listThongKe.setAdapter(adapter);
            } else {
                Toast.makeText(this, doanhThuModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }
    private void showDatePickerDialog() {
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yearsList);
        tvMenuYear.setAdapter(yearAdapter);
        tvMenuYear.setOnItemClickListener((parent, view, position, id) -> {
            getYear = parent.getItemAtPosition(position).toString();
            ckbKieuXem.setChecked(false);
            getDoanhThuTheoThang(getYear);
        });
        ckbKieuXem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showBarChart();
            }
            else {
                barChart.setVisibility(View.GONE);
                listThongKe.setVisibility(View.VISIBLE);
            }
        });

    }
    private List<String> generateYearsList() {
        List<String> yearsList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 2010; i--) {
            yearsList.add(String.valueOf(i));
        }
        return yearsList;
    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}