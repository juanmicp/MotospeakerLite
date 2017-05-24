package com.juanmi.motospeakerlite;

import java.io.Serializable;

/**
 * Created by Juanmi on 22/05/2017.
 */

@SuppressWarnings("serial")
class BtDevice implements Serializable { //Implementa serializable para poder pasar un objeto de tipo BtDevice como parámetro.

    private String name;
    private String address;
    private boolean connected;

    public BtDevice(String name, String address, boolean connected) {
        this.name = name;
        this.address = address;
        this.connected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        return name;
    }
}
