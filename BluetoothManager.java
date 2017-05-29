package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.R.attr.action;

/**
 * Created by Juanmi on 22/05/2017.
 */

public class BluetoothManager {

    private BluetoothAdapter btAdapter;
    private List<BtDevice> btDevList = new ArrayList();
    private static BluetoothManager instance = null;
    private String uuid = "ea33eca1-c704-4062-a111-98f7b323e824";
    private Server btServer;
    private Client btClient;

    private BluetoothManager() {
    }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    public void createBtAdapter() {
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();;
    }

    public List<BtDevice> getBtDevList(){
        return btDevList;
    }
    public void addToBtDevList(BtDevice device){
        btDevList.add(device);
    }

    public boolean connect(BtDevice deviceToConnect) {
        boolean conectado = false;
        /////////////////////////////CONEXION/////////////////////////////
        BluetoothSocket btSocket = null;
        if (deviceToConnect == null){ //Conexión en la parte servidora.
            btServer = new Server(btAdapter, uuid);
            while (btSocket == null){
                btSocket = btServer.getSocket();
            }
            btClient = new Client(btSocket);
            conectado = true;
        }else{ //Conexión en la parte cliente.
            btClient = new Client(deviceToConnect.getDevice(), uuid);
            btSocket = btClient.getSocket();
            btServer = new Server(btSocket);
            conectado = true;
        }
        return conectado;
    }

    public void sendText (String text){
        btClient.send(text);
    }

    private List<BtDevice> getAvailableDevices() { //Devuelve los dispositivos asociados.
        List<BtDevice> availableDevices = new ArrayList();
        Set<BluetoothDevice> devs = btAdapter.getBondedDevices();
        for (BluetoothDevice dev : devs) {
            BtDevice device = new BtDevice(dev, false);
            availableDevices.add(device);
        }
        return availableDevices;
    }

    public void setEditText(EditText editText) {

    }
}