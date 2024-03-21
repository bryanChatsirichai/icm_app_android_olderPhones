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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class Rotation_rear_motor_Activity extends AppCompatActivity {
    private static final String TAG = "Rotation_rear_motor_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    Button rotation_rear_motor_0_button,rotation_rear_motor_1_button;
    MyGlobals myGlobals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation_rear_motor);
        init();
        rotation_rear_motor_0_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "switchRearMotorRotation";
                //myGlobals.rear_rotation_direction = 0;
                String rear_rotation_direction_str = String.valueOf(0);
                str = str + '_' + rear_rotation_direction_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
        rotation_rear_motor_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "switchRearMotorRotation";
                //myGlobals.rear_rotation_direction = 1;
                String rear_rotation_direction_str = String.valueOf(1);
                str = str + '_' + rear_rotation_direction_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        rotation_rear_motor_0_button = findViewById(R.id.rotation_rear_motor_0_button);
        rotation_rear_motor_1_button = findViewById(R.id.rotation_rear_motor_1_button);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));
        switch (myGlobals.rear_rotation_direction) {
            case 0:
                // code block
                rotation_rear_motor_0_button.setText("Rotation-0 Selected");
                rotation_rear_motor_1_button.setText("Rotation-1");
                break;
            case 1:
                // code block
                rotation_rear_motor_0_button.setText("Rotation-0");
                rotation_rear_motor_1_button.setText("Rotation-1 Selected");
                break;

            // more cases as needed
            default:
                // code block executed if expression doesn't match any case
        }
    }
    //BLUETOOTH SECTION, Receiving important command from RPI
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receiving Msg...");
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            if(Objects.equals(functionName, "switchRearMotorRotation")){
                myGlobals.rear_rotation_direction = Integer.parseInt(pico_message_parts_array.get(1));
                switch (myGlobals.rear_rotation_direction) {
                    case 0:
                        // code block
                        rotation_rear_motor_0_button.setText("Rotation-0 Selected");
                        rotation_rear_motor_1_button.setText("Rotation-1");
                        break;
                    case 1:
                        // code block
                        rotation_rear_motor_0_button.setText("Rotation-0");
                        rotation_rear_motor_1_button.setText("Rotation-1 Selected");
                        break;

                    // more cases as needed
                    default:
                        // code block executed if expression doesn't match any case
                }
                Toast.makeText(Rotation_rear_motor_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}