package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.R.attr.action;

/**
 * Created by Juanmi on 22/05/2017.
 */

public class BluetoothManager {

    private List<BtDevice> btDevList = new ArrayList();
    private static BluetoothManager instance = null;

    private BluetoothManager() {
    }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    public List<BtDevice> getBtDevList(){
        return btDevList;
    }
    public void addToBtDevList(BtDevice device){
        btDevList.add(device);
    }

    public boolean connect(BtDevice deviceToConnect) {
        /////////////////////////////CONEXION/////////////////////////////
        return false;
    }

    private List<BtDevice> getAvailableDevices(BluetoothAdapter btAdapter) { //Devuelve los dispositivos asociados.
        List<BtDevice> availableDevices = new ArrayList();
        Set<BluetoothDevice> devs = btAdapter.getBondedDevices();
        for (BluetoothDevice dev : devs) {
            BtDevice device = new BtDevice(dev.getName(), dev.getAddress(), false);
            availableDevices.add(device);
        }
        return availableDevices;
    }
}