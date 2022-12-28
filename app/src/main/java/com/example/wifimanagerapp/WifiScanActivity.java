package com.example.wifimanagerapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifimanagerapp.databinding.ActivityWifiScanBinding;

import java.util.ArrayList;
import java.util.List;

public class WifiScanActivity extends AppCompatActivity {

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private List<String> results;
    private WifiManager wifiManager;
    ListView listView;
    WifiConnectionReceiver wifiConnectionReceiver;

    ActivityWifiScanBinding binding;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_wifi_scan);

        results = new ArrayList<>();
        results.add("No Devices Found");
        binding = ActivityWifiScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, results);
        binding.scanResultsList.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Location permission is granted");
            }
        } else {
            Log.i("TAG", "Location permission not granted");
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }



        //adapter = new ArrayAdapter(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,arrayList);
        //listView.setAdapter(adapter);

        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }

                if (wifiManager.isWifiEnabled()) {
                    Toast.makeText(getApplicationContext(), "Wifi is enabled\" + networkSSID + \" \" + networkPassword", Toast.LENGTH_LONG).show();
                    Log.i("TAG", "Wifi is enabled");
                    wifiConnectionReceiver = new WifiConnectionReceiver(wifiManager, listView);
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                    registerReceiver(wifiConnectionReceiver, intentFilter);
                    wifiManager.startScan();
                    if (WifiViewModel.results != null) {
                        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, WifiViewModel.results);
                        binding.scanResultsList.setAdapter(adapter);
                    }
                    Log.i("TAG", "Wifi lists : " + WifiViewModel.results);
                    adapter.notifyDataSetChanged();


                } else {
                    Log.i("TAG", "Wifi is not enabled");
                    Toast.makeText(getApplicationContext(), "Wifi is not enabled", Toast.LENGTH_LONG).show();
                }
                //scanWifi();

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiConnectionReceiver);
    }

    private void scanWifi() {


    }


}