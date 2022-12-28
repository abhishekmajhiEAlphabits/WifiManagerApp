package com.example.wifimanagerapp;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class WifiViewModel extends ViewModel {

    public static ArrayList<String> results;

    public WifiViewModel(ArrayList<String> devices){
        results=devices;

    }

    public ArrayList<String> getDevices(){
        return results;
    }
}
