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

    }

    public void scan(View view){ //Escanear y mostrar en lista los dispositivos bluetooth disponibles.
        //BluetoothManager btManager = BluetoothManager.getInstance();
        btAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReceiver, filter);
        /*
        //btManager.scan(btAdapter);
        //List <BtDevice> btDevList = btManager.getBtDevList();
        devicesLV = (ListView) findViewById(R.id.devicesLV);
        adapter = new ArrayAdapter<BtDevice>(this, android.R.layout.simple_list_item_1, btDevList);
        devicesLV.setAdapter(adapter);
        devicesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = ((TextView)view).getText().toString();
                Toast.makeText(getBaseContext(), "Conectando con "+item+"...", Toast.LENGTH_SHORT).show();
                connect(item);
            }
        });
        */
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

    private void connect(String devName){ //Conectar con el dispositivo seleccionado.
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (btManager.connect(devName)){ //Si conecta con el dispositivo en cuestión.
            /////////Abrir intent a ventana con cuadro dos cuadros de texto (envio y recepción).
        }
        else{
            Toast.makeText(getBaseContext(), "Imposible conectar con "+ devName+".", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){ //Cuando se detecta un nuevo dispositivo Bluetooth:
                BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BtDevice device = new BtDevice(dev.getName(), dev.getAddress(), false);
                btDevList.add(device);
                adapter.add(device);
                //adapter.notifyDataSetChanged();
                //devicesLV.setAdapter(new ArrayAdapter<BtDevice>(context, android.R.layout.simple_list_item_1, btDevList));
            }
        }
    };
}
