package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.action;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter;
    private ListView devicesLV;
    private List<BtDevice> btDevList = new ArrayList();
    private ArrayAdapter<BtDevice> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null) { //Si nuestro dispositivo no soporta bluetooth, se cierra la aplicación.
            Toast t = Toast.makeText(getApplicationContext(), "Adaptador Bluetooth no soportado.", Toast.LENGTH_SHORT);
            t.show();
            System.exit(0);
        }

        if (!btAdapter.isEnabled()) { //Si el bluetooth no está activado, se activa. Para activarse, pregunta al usuario si permite esa acción.
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 1);
            Toast t = Toast.makeText(getApplicationContext(), "Bluetooth habilitado.", Toast.LENGTH_SHORT);
            t.show();
        }
        devicesLV = (ListView)findViewById(R.id.devicesLV);
        adapter = new ArrayAdapter<BtDevice>(this, android.R.layout.simple_list_item_1, btDevList);
        devicesLV.setAdapter(adapter);
        devicesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btAdapter.cancelDiscovery(); //Se deja de buscar dispositivos.
                BtDevice btd = (BtDevice) devicesLV.getItemAtPosition(i);
                btd.setConnected(true);
                Toast.makeText(getBaseContext(), "Conectando con "+ btd.getName()+"...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), CommunicateActivity.class);
                intent.putExtra("parameter", btd); //Se manda como parámetro a la nueva activity el dispositivo con el que conectamos.
                startActivity(intent);
            }
        });

    }

    public void scan(View view){ //Escanear y mostrar en lista los dispositivos bluetooth disponibles.
        if(btDevList != null) {
            btDevList.clear();
            adapter.notifyDataSetChanged();
        }
        if(btAdapter.isDiscovering()) //Si está buscando, se reinicia la búsqueda.
            btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReceiver, filter);
    }

    public void makeVisible (View view){
        if (btAdapter.isEnabled()){
            Intent visibleBt = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            visibleBt.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
            startActivity(visibleBt);
        }
        else{
            Toast.makeText(getBaseContext(), "No puebe hacerse visible. Bluetooth deshabilitado.", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){ //Cuando se detecta un nuevo dispositivo Bluetooth:
                BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BtDevice device = new BtDevice(dev.getName(), dev.getAddress(), false);
                boolean exist = false;
                for(BtDevice btd: btDevList){
                    if (btd.equals(device))
                        exist=true;
                }
                if (!exist) {
                    btDevList.add(device);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };
}
