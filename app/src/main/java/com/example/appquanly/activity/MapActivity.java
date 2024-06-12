package com.example.appquanly.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appquanly.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.appquanly.model.ToaDo;
import com.example.appquanly.networking.LocationApiCalls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button btnSelected;
    private GoogleMap gMap;
    private ToaDo toaDo = new ToaDo();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationApiCalls.initialize(this);
        setContentView(R.layout.activity_map);
        initData();
        setControl();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);
        setEvent();
    }

    private void initData() {
        LocationApiCalls.getLocation(toaDoModel -> {
            if (toaDoModel.getStatus() == 200) {
                toaDo = toaDoModel.getResult();
            } else {
                Toast.makeText(this, toaDoModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }

    private void setEvent() {
        btnSelected.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ViTriCuaHangActivity.class);
            intent.putExtra("viDo", toaDo.getViDo());
            intent.putExtra("kinhDo", toaDo.getKinhDo());
            startActivity(intent);
            finish();
        });
    }

    private void setControl() {
        btnSelected = findViewById(R.id.btnSelected);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LocationApiCalls.getLocation(toaDoModel -> {
            if (toaDoModel.getStatus() == 200) {
                toaDo = toaDoModel.getResult();
                Log.d("mylog", toaDo.toString());
                gMap = googleMap;

                //        LatLng location = new LatLng(Double.parseDouble(toaDo.getViDo()), Double.parseDouble(toaDo.getKinhDo()));
                LatLng location = new LatLng(toaDo.getViDo(), toaDo.getKinhDo());
                gMap.addMarker(new MarkerOptions().position(location).title(toaDo.getTenViTri()));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                // Set on map click listener
                gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        // Display a marker at the clicked location
                        gMap.clear(); // Clear existing markers
                        gMap.addMarker(new MarkerOptions().position(latLng)); // Add new marker
                        // Update the selected location
                        toaDo.setKinhDo(latLng.longitude);
                        toaDo.setViDo(latLng.latitude);
                    }
                });

                updateSelectedLocationOnMap();
            } else {
                Toast.makeText(this, toaDoModel.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, compositeDisposable);
    }


    private void updateSelectedLocationOnMap() {
        if (toaDo != null) {
            LatLng location = new LatLng(toaDo.getViDo(), toaDo.getKinhDo());
            gMap.addMarker(new MarkerOptions().position(location).title(toaDo.getTenViTri()));
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
        }
    }
    private void enableMyLocation() {
        if (gMap != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gMap.setMyLocationEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
