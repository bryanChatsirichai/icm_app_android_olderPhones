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

public class Switch_zoom_focus_position_Activity extends AppCompatActivity {
    private static final String TAG = "Switch_zoom_focus_position_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    Button position_zoom_at_the_back_button,position_zoom_at_the_front_button;
    MyGlobals myGlobals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_zoom_focus_position);
        init();

        position_zoom_at_the_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "switchOrientation";
                //myGlobals.orientation = 0;
                String orientation_str = String.valueOf(0);
                str = str + '_' + orientation_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
        position_zoom_at_the_front_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "switchOrientation";
                //myGlobals.orientation = 1;
                String orientation_str = String.valueOf(1);
                str = str + '_' + orientation_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        position_zoom_at_the_back_button = findViewById(R.id.position_zoom_at_the_back_button);
        position_zoom_at_the_front_button = findViewById(R.id.position_zoom_at_the_front_button);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));
        switch (myGlobals.orientation) {
            case 0:
                // code block
                position_zoom_at_the_back_button.setText("Zoom at the Back - Selected");
                position_zoom_at_the_front_button.setText("Zoom at the Front");
                break;
            case 1:
                // code block
                position_zoom_at_the_back_button.setText("Zoom at the Back");
                position_zoom_at_the_front_button.setText("Zoom at the Front - Selected");
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
            if(Objects.equals(functionName, "switchOrientation")){
                myGlobals.orientation = Integer.parseInt(pico_message_parts_array.get(1));
                switch (myGlobals.orientation) {
                    case 0:
                        // code block
                        position_zoom_at_the_back_button.setText("Zoom at the Back - Selected");
                        position_zoom_at_the_front_button.setText("Zoom at the Front");
                        break;
                    case 1:
                        // code block
                        position_zoom_at_the_back_button.setText("Zoom at the Back");
                        position_zoom_at_the_front_button.setText("Zoom at the Front - Selected");
                        break;

                    // more cases as needed
                    default:
                        // code block executed if expression doesn't match any case
                }
                Toast.makeText(Switch_zoom_focus_position_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}