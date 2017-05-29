package com.juanmi.motospeakerlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommunicateActivity extends AppCompatActivity {

    BtDevice asociatedBtDevice; //Dispositivo bluetooth de la persona con la que se realiza la comunicación.
    Button sendButton;
    EditText etToReceive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        asociatedBtDevice = (BtDevice)getIntent().getExtras().getSerializable("parameter");
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Cuando se pulsa, se delega en el mánager para enviar lo que se haya escrito.
                EditText etToSend = (EditText)findViewById(R.id.meEditText);
                String text = etToSend.getText().toString();
                BluetoothManager.getInstance().sendText(text);
            }
        });
        etToReceive = (EditText)findViewById(R.id.heSheEditText);
        connect();
    }

    private void connect(){ //Conectar con el dispositivo seleccionado.
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (btManager.connect(asociatedBtDevice)){ //Si conecta con el dispositivo en cuestión.
            btManager.setEditText(etToReceive);
            sendButton.setEnabled(true); //En ese caso se activa el botón de envío.
        }
        else{
            Toast.makeText(getBaseContext(), "Imposible conectar con "+ asociatedBtDevice.getName()+".", Toast.LENGTH_SHORT).show();
        }
    }


}
