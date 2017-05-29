package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by Juanmi on 25/05/2017.
 */

public class Server extends Thread {

    private BluetoothServerSocket serverSocket;
    private BluetoothSocket socket = null;
    private EditText etToReceive;
    String text;

    public Server (BluetoothAdapter bluetoothAdapter, String uuid) { //Constructor para el que ejerce de servidor en el momento de la conexión.
        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Motospeaker", UUID.fromString(uuid));
        } catch (IOException e) {
            Log.d("Server", "Socket's listen() method failed: " + e.toString());
        }
        while (true) {
            try {
                this.socket = serverSocket.accept();
            } catch (Exception e) {
                Log.d("Server", "Socket's accept() method failed: " + e.toString());
                break;
            }
            if (socket != null){
                try {
                    serverSocket.close();
                }catch (IOException e){
                    Log.d("Server", "Could not close ServerSocket:" + e.toString());
                }
                break;
            }
        }

    }

    public Server(BluetoothSocket socket){ //Para el caso de que sea cliente a la hora de conectar.
        this.socket = socket;
    }

    public void run() {
        int oldWritten = 0;
        while (true) {
            try {
                BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));

                /*
                StringBuffer buffer=new StringBuffer();
                while (true) {
                    int ch=in.read();
                    if (ch<0 || ch=='\n')
                        break;
                    buffer.append((char) ch);
                }
                */
                int written = in.read();
                text= String.valueOf(written);

                if (written != oldWritten && written != 0){
                    etToReceive.setText(text);
                }
                oldWritten = written;
                //socket.close();
            } catch (Exception e) {
                Log.d("Server", "Could not read: " + e.toString());
                break;
            }
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.d("Server", "Could not close the connected socket: " + e.toString());
        }
    }

    public BluetoothSocket getSocket(){
        return socket;
    }

    public String getText() {
        return text;
    }

    public void setEtToReceive(EditText etToReceive) {
        this.etToReceive = etToReceive;
    }

}
