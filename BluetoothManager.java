package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Juanmi on 22/05/2017.
 */

public class BluetoothManager {

    private static BluetoothManager instance = null;
    private BluetoothManager(){}
    public static BluetoothManager getInstance(){
        if(instance == null){
            instance = new BluetoothManager();
        }
        return instance;
    }

    public List <BtDevice> scan(BluetoothAdapter btAdapter){
        List <BtDevice> btDevList = new ArrayList();
        btDevList.addAll(getAvailableDevices(btAdapter));
        return btDevList;
    }

    public boolean connect (String devName){
        return false;
    }

    private List <BtDevice> getAvailableDevices(BluetoothAdapter btAdapter){ //Devuelve los dispositivos asociados.
        List <BtDevice> availableDevices = new ArrayList();
        Set<BluetoothDevice> devs = btAdapter.getBondedDevices();
        for (BluetoothDevice dev : devs){
            BtDevice device = new BtDevice(dev.getName(), dev.getAddress(), false);
            availableDevices.add(device);
        }
        return availableDevices;
    }
}
