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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appquanly.R;
import com.example.appquanly.adapter.ThongKeAdapter;
import com.example.appquanly.adapter.ThongKeSanPhamAdapter;
import com.example.appquanly.model.SanPham;
import com.example.appquanly.model.SanPhamThongKe;
import com.example.appquanly.networking.ReportApiCalls;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class TKSanPhamActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public String getYear;
    private int delay = 1000;
    Toolbar toolbar;
    AutoCompleteTextView tvMenuYear;
    CheckBox chkKieuXem;
    ListView listSanPham;
    PieChart pieChart;
    List<String> yearsList = new ArrayList<>();
    ArrayAdapter<String> yearAdapter;
    List<SanPhamThongKe> sanPhamThongKeList = new ArrayList<>();


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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tksan_pham);
        setControl();
        yearsList = generateYearsList();
        showDatePickerDialog();
        actionBar();

    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        pieChart = findViewById(R.id.pieChart);
        tvMenuYear = findViewById(R.id.tvMenuYear);
        listSanPham = findViewById(R.id.listTKSP);
        chkKieuXem = findViewById(R.id.checkboxKieu);
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
            showDatePickerDialog();
            return true;
        }
        else if(id == R.id.icDoanhThu) {
            Intent intent = new Intent(TKSanPhamActivity.this, ThongKeActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            finish();
            return true;
        }

    }
    private void showPieChart(List<SanPhamThongKe> sanPhamThongKeList) {
        pieChart.setVisibility(View.VISIBLE);
        listSanPham.setVisibility(View.GONE);
        ArrayList<PieEntry> dataChart = new ArrayList<>();
        for (SanPhamThongKe tmp : sanPhamThongKeList) {
            int tong = tmp.getTongSoLuong();
            String name = tmp.getTenSanPham();
            dataChart.add(new PieEntry(tong, name));
        }
        PieDataSet pieDataSet = new PieDataSet(dataChart, "Biểu đồ");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(pieData);
        pieChart.animateXY(delay, delay);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                if (entry instanceof PieEntry) {
                    PieEntry selectedEntry = (PieEntry) entry;
                    String productName = selectedEntry.getLabel();
                    Toast.makeText(getApplicationContext(), "Sản phẩm: " + productName, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void getBaoCaoThongKeSanPham() {
        sanPhamThongKeList.clear();
        ReportApiCalls.getMostProducts(Integer.parseInt(getYear), sanPhamThongKeModel -> {
            if (sanPhamThongKeModel.getStatus() == 200) {
                sanPhamThongKeList = sanPhamThongKeModel.getObject();
                ThongKeSanPhamAdapter adapter = new ThongKeSanPhamAdapter(TKSanPhamActivity.this, R.layout.layout_item_san_pham_thong_ke, sanPhamThongKeList);
                listSanPham.setAdapter(adapter);
            } else {
                Toast.makeText(this, sanPhamThongKeModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);

    }

    private void showDatePickerDialog() {
        yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, yearsList);
        tvMenuYear.setAdapter(yearAdapter);
        tvMenuYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getYear = parent.getItemAtPosition(position).toString();
                chkKieuXem.setChecked(false);getBaoCaoThongKeSanPham();

            }
        });
        chkKieuXem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showPieChart(sanPhamThongKeList);
                }
                else {
                    pieChart.setVisibility(View.GONE);
                    listSanPham.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private List<String> generateYearsList() {
        List<String> yearsList = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 2000; i--) {
            yearsList.add(String.valueOf(i));
        }
        return yearsList;
    }
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}