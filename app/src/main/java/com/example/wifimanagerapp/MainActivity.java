package com.example.wifimanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.wifimanagerapp.databinding.ActivityMainBinding;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WifiManager wifiManager;
    String networkSSID, networkPassword;
    ActivityMainBinding binding;
    ListView results;
    ArrayAdapter arr;
    List<ScanResult> networks;
    ConnectivityManager conManager;
    NetworkInfo networkInfo;
    public ProgressDialog progressBar;
    private EditText edtSSID,edtPassword;
    private Button btnConnect, btnStatus;
    private NetworkChangeReceiver networkChangeReceiver;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialisation();
        onClickEvents();
    }

    private void initialisation(){
        edtSSID = (EditText) findViewById(R.id.editTextSSID);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnConnect = (Button) findViewById(R.id.connectWifiButton);
        btnStatus = (Button) findViewById(R.id.connectStatus);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private void onClickEvents(){
        btnConnect.setOnClickListener(this);
        btnStatus.setOnClickListener(this);
    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    private void showProgressDialogue(){
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Connecting...");
        progressBar.show();
    }

    private void hideProgressDialogue(){
        progressBar.hide();
    }

    //method called on connect to wifi button click
    private void connectToWifi(final String networkSSID, final String networkPassword) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (wifiManager.isWifiEnabled()) {
             Toast.makeText(getApplicationContext(), "Connecting..", Toast.LENGTH_LONG).show();
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", networkSSID);
        conf.preSharedKey = String.format("\"%s\"", networkPassword);

        int netId = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    public void wifiStatus() {
        conManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Wifi is not enabled", Toast.LENGTH_LONG).show();
        }
            if (isConnected) {
            WifiInfo networkInfo = wifiManager.getConnectionInfo();
            Log.i("TAG", networkInfo.toString());
            if (networkInfo != null) {
                String bssid = networkInfo.getBSSID();
                Log.i("TAG", networkInfo.toString());
                Toast.makeText(getApplicationContext(), "Network Info"+networkInfo.toString(), Toast.LENGTH_LONG).show();
            }

        } else {

            Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.connectWifiButton){
            showProgressDialogue();
            networkSSID = edtSSID.getText().toString();
            networkPassword = edtPassword.getText().toString();
            if (networkSSID != null && networkPassword != null) {
                connectToWifi(networkSSID, networkPassword);
            }
            else if (networkSSID == null && networkPassword == null){
                hideProgressDialogue();
                Toast.makeText(getApplicationContext(), "Enter Wifi credentials", Toast.LENGTH_LONG).show();
            }
            else {
                hideProgressDialogue();
                Toast.makeText(getApplicationContext(), "Enter Wifi credentials", Toast.LENGTH_LONG).show();
            }
        }
        if(view.getId() == R.id.connectStatus){
            wifiStatus();
        }
    }


    public class NetworkChangeReceiver  extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (isOnline(context)) {
                    hideProgressDialogue();
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                } else {
                    hideProgressDialogue();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}