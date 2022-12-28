package com.example.wifimanagerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifimanagerapp.databinding.ActivityWifiScanBinding;
import com.example.wifimanagerapp.databinding.ListviewBinding;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectionReceiver extends BroadcastReceiver {
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private List<ScanResult> results;
    private WifiManager wifiManager;
    ListView listView;


    public WifiConnectionReceiver(WifiManager wifiManager, ListView listView) {
        this.wifiManager = wifiManager;
        this.listView = listView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            results = wifiManager.getScanResults();
            Log.i("TAG", "Wifi is  enabled" + results.toString());

            for (ScanResult scanResult : results) {
                //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities);
                arrayList.add(scanResult.SSID);

            }
            Log.i("TAG", "devices" + arrayList.toString());


            WifiViewModel wifiViewModel = new WifiViewModel(arrayList);


        }


    }
}
