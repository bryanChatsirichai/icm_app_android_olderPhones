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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class ICM_Home_Activity extends AppCompatActivity {
    private static final String TAG = "ICM_Home_Activity";

    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    MyGlobals myGlobals;
    //View Components
    Button camera_config_button,sync_button,camera_simple_actions_button,camera_preset_actions_button;
    TextView action_textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icm_home);
        init();
        camera_config_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ICM_Home_Activity.this, Configuration_Menu_Activity.class);
                startActivity(i);
            }
        });
        camera_simple_actions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ICM_Home_Activity.this, Simple_Actions_Activity.class);
                startActivity(i);
            }
        });
        camera_preset_actions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ICM_Home_Activity.this, Preset_Actions_Activity.class);
                startActivity(i);
            }
        });

        sync_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to sync pico and app values, values fetch from pico.
                // to do ....
                String str = "syncDevices";
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }
    //called during start up, initialize anything necessary
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        camera_config_button = findViewById(R.id.camera_config_button);
        camera_simple_actions_button = findViewById(R.id.camera_simple_actions_button);
        camera_preset_actions_button = findViewById(R.id.camera_preset_actions_button);
        sync_button = findViewById(R.id.sync_button);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));
    }


    //BLUETOOTH SECTION,Receiving important command from RPI
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receiving Msg...");
            //depending on the message, decode action an values to do
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            // dummy action to test message send and reply
            if(Objects.equals(functionName, "Action1")){
                //... testing logic

            } else if (Objects.equals(functionName, "syncDevices")) {
                myGlobals.focus_range = Integer.parseInt(pico_message_parts_array.get(1));
                myGlobals.zoom_range = Integer.parseInt(pico_message_parts_array.get(2));
                myGlobals.focus_current = Integer.parseInt(pico_message_parts_array.get(3));
                myGlobals.zoom_current = Integer.parseInt(pico_message_parts_array.get(4));
                myGlobals.orientation = Integer.parseInt(pico_message_parts_array.get(5));
                myGlobals.shutter_time =  Integer.parseInt(pico_message_parts_array.get(6));
                myGlobals.max_shutter_time =  Integer.parseInt(pico_message_parts_array.get(7));
                myGlobals.motor_time =  Integer.parseInt(pico_message_parts_array.get(8));
                myGlobals.max_motor_time =  Integer.parseInt(pico_message_parts_array.get(9));
                myGlobals.excess_option_set =  Integer.parseInt(pico_message_parts_array.get(10));
                myGlobals.rear_rotation_direction = Integer.parseInt(pico_message_parts_array.get(11));
                myGlobals.front_rotation_direction =  Integer.parseInt(pico_message_parts_array.get(12));
                myGlobals.MOTOR_STEPS = Integer.parseInt(pico_message_parts_array.get(13));
                Toast.makeText(ICM_Home_Activity.this, "Synced Successful!-manual",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    //dummy to be remove
    public void send_test_message(View v){
        String str = "Action1";
        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
    }


}