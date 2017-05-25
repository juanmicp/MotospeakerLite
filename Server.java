package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

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

    public Server (BluetoothAdapter bluetoothAdapter, String uuid) {
        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("ModernTriType", UUID.fromString(uuid));
        } catch (IOException e) {
            Log.d("Server", "Socket's listen() method failed: " + e.toString());
        }
    }

    public void run() {
        while (true) {
            try {
                BluetoothSocket socket = serverSocket.accept();
                PrintWriter out=new PrintWriter(socket.getOutputStream());
                BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));

                StringBuffer buffer=new StringBuffer();
                while (true) {
                    int ch=in.read();
                    if (ch<0 || ch=='\n')
                        break;
                    buffer.append((char) ch);
                }

                String text=buffer.toString();
                out.println(text.toString());
                out.flush();
                socket.close();
            } catch (Exception e) {
                Log.d("Server", "Socket's accept() method failed: " + e.toString());
                break;
            }
        }
    }

    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.d("Server", "Could not close the connected socket: " + e.toString());
        }
    }

}
