package com.example.wifimanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wifimanagerapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    String networkSSID, networkPassword;

    ActivityMainBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkSSID = binding.editTextSSID.getText().toString();
                networkPassword = binding.editTextPassword.getText().toString();
                connectToWifi(networkSSID, networkPassword);
            }
        });

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