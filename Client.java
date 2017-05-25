package com.juanmi.motospeakerlite;

import android.bluetooth.BluetoothDevice;
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

public class Client extends Thread {

    private BluetoothSocket socket;
    private String toSend;
    private PrintWriter out;
    private BufferedReader in;

    public Client (BluetoothDevice device, String uuid) {
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        } catch (IOException e) {
            Log.d("Client","Could not create RFCOMM socket:" + e.toString());
            return;
        }
        socket = tmp;

        try {
            socket.connect();
            this.out=new PrintWriter(socket.getOutputStream());
            this.in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            Log.d("Client","Could not connect: " + e.toString());
            try {
                socket.close();
            } catch (IOException e2) {
                Log.d("Client", "Could not close connection:" + e.toString());
                return;
            }
        }
    }

    public void run() {
        out.println(this.toSend);
        out.flush();
        boolean salir=false;
        while (!salir) {
            try {
                StringBuffer buffer=new StringBuffer();
                while (true) {
                    int ch=in.read();
                    if (ch<0 || ch=='\n')
                        break;
                    buffer.append((char) ch);
                }

                String texto=buffer.toString();
                salir=true;
            }
            catch (Exception e) {
                Log.d("Client", "In run:" + e.toString());
                salir=true;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            Log.d("Client", "In run: Could not close connection:" + e.toString());
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.d("Client", "Could not close the client connection:" + e.toString());
        }
    }

    public void setRequest(String toSend) {
        this.toSend=toSend;
    }

}
