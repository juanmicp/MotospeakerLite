package com.juanmi.motospeakerlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class CommunicateActivity extends AppCompatActivity {

    BtDevice asociatedBtDevice; //Dispositivo bluetooth de la persona con la que se realiza la comunicación.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        asociatedBtDevice = (BtDevice)getIntent().getExtras().getSerializable("parameter");
        connect();
    }

    private void connect(){ //Conectar con el dispositivo seleccionado.
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (btManager.connect(asociatedBtDevice)){ //Si conecta con el dispositivo en cuestión.
            Button sendButton = (Button) findViewById(R.id.sendButton);
            sendButton.setEnabled(true); //En ese caso se activa el botón de envío.
        }
        else{
            Toast.makeText(getBaseContext(), "Imposible conectar con "+ asociatedBtDevice.getName()+".", Toast.LENGTH_SHORT).show();
        }
    }
}
