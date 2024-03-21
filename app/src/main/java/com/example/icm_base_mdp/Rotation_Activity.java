package com.example.icm_base_mdp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class Rotation_Activity extends AppCompatActivity {
    private static final String TAG = "Rotation_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    Button rotation_rear_motor_button,rotation_front_motor_button;
    MyGlobals myGlobals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        init();
        rotation_rear_motor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Rotation_Activity.this, Rotation_rear_motor_Activity.class);
                startActivity(i);
            }
        });
        rotation_front_motor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Rotation_Activity.this, Rotation_front_motor_Activity.class);
                startActivity(i);
            }
        });
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        rotation_rear_motor_button = findViewById(R.id.rotation_rear_motor_0_button);
        rotation_front_motor_button = findViewById(R.id.rotation_front_motor_button);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));

    }
    //BLUETOOTH SECTION, Receiving important command from RPI
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receiving Msg...");
            String msg = intent.getStringExtra("receivingMsg");
            //depending on the message, decode action an values to do
            //...
        }
    };
}