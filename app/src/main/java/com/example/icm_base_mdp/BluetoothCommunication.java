package com.example.icm_base_mdp;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class BluetoothCommunication {

    private static final String TAG = "BluetoothCommunication";
    private static Context mmContext;

    private static BluetoothSocket mmSocket;
    private static InputStream inputStream;
    private static OutputStream outPutStream;
    private static BluetoothDevice BTConnectionDevice;

    private static final int BUFFER_SIZE = 1024;

    public static void startCommunication(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = tmpIn;
        outPutStream = tmpOut;

        //Buffer store for the stream
        byte[] buffer = new byte[BUFFER_SIZE];

        int numbytes; // bytes returned from read()

        while (true) {
            //Read from the InputStream
            try {
                numbytes = inputStream.read(buffer);

                String incomingMessage = new String(buffer, 0, numbytes, StandardCharsets.UTF_8);

                //BROADCAST INCOMING MSG
                Intent incomingMsgIntent = new Intent("IncomingMsg");
                incomingMsgIntent.putExtra("receivingMsg", incomingMessage);
                LocalBroadcastManager.getInstance(mmContext).sendBroadcast(incomingMsgIntent);
                buffer = new byte[BUFFER_SIZE];

            } catch (IOException e) {

                //BROADCAST CONNECTION MSG
                Intent connectionStatusIntent = new Intent("btConnectionStatus");
                connectionStatusIntent.putExtra("ConnectionStatus", "disconnect");
                connectionStatusIntent.putExtra("Device",BTConnectionDevice);
                LocalBroadcastManager.getInstance(mmContext).sendBroadcast(connectionStatusIntent);
                e.printStackTrace();
                break;

            } catch (Exception e){
                e.printStackTrace();

            }

        }
    }

    // send message to remote device
    public static void write(byte[] bytes) {

        String text = new String(bytes, Charset.defaultCharset());

        try {
            outPutStream.write(bytes);
        } catch (IOException | NullPointerException e) {
            // make a toast
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));

        }
    }


    // start communicating with remote device
    static void connected(BluetoothSocket mmSocket, BluetoothDevice BTDevice, Context context) {

        BTConnectionDevice = BTDevice;
        mmContext = context;
        startCommunication(mmSocket);
    }

    // write to connect thread (unsynchronise manner)
    public static void writeMsg(byte[] out) {
        write(out);
    }
}