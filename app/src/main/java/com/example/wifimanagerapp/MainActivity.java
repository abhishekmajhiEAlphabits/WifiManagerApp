package com.example.wifimanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifimanagerapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    String networkSSID, networkPassword;

    ActivityMainBinding binding;
    ListView results;
    ArrayAdapter arr;
    List<ScanResult> networks;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        //arr = new ArrayAdapter(this, android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,networks);
        //results.setAdapter(arr);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        binding.connectWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkSSID = binding.editTextSSID.getText().toString();
                networkPassword = binding.editTextPassword.getText().toString();
                connectToWifi(networkSSID, networkPassword);
            }
        });

        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //wifiScan();
                startActivity(new Intent(getApplicationContext(),WifiScanActivity.class));

            }
        });

    }

    private void wifiScan() {

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        if (wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Wifi is enabled\" + networkSSID + \" \" + networkPassword", Toast.LENGTH_LONG).show();
            Log.i("TAG", "Wifi is enabled" + networkSSID + " " + networkPassword);
        } else {
            Log.i("TAG", "Wifi is not enabled");
            Toast.makeText(getApplicationContext(), "Wifi is not enabled", Toast.LENGTH_LONG).show();
        }


        wifiManager.startScan();
        List<ScanResult> availNetworks = wifiManager.getScanResults();
        networks = availNetworks;
        if (availNetworks.size() > 0) {
            String wifis[] = new String[availNetworks.size()];
            // Get Each network detail
            for (int i = 0; i < availNetworks.size(); i++) {
                wifis[i] = availNetworks.get(i).toString();
                Toast.makeText(getApplicationContext(), "Available wifi networks found" +wifis, Toast.LENGTH_LONG).show();
            }

        }

        else{
            Toast.makeText(getApplicationContext(), "no wifi networks found", Toast.LENGTH_LONG).show();
        }
    }



    //method called on connect to wifi button click
    private void connectToWifi(final String networkSSID, final String networkPassword) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        if (wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Wifi is enabled\" + networkSSID + \" \" + networkPassword", Toast.LENGTH_LONG).show();
            Log.i("TAG", "Wifi is enabled" + networkSSID + " " + networkPassword);
        } else {
            Log.i("TAG", "Wifi is not enabled");
            Toast.makeText(getApplicationContext(), "Wifi is not enabled", Toast.LENGTH_LONG).show();
        }


        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", networkSSID);
        conf.preSharedKey = String.format("\"%s\"", networkPassword);

        int netId = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }
}