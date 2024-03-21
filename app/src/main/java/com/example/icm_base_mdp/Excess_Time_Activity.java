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

public class Excess_Time_Activity extends AppCompatActivity {
    private static final String TAG = "Excess_Time_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    Button excess_time_pre_button,excess_time_split_button,excess_time_After_button;
    MyGlobals myGlobals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excess_time);
        init();
        excess_time_pre_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "setExcessTime";
                //myGlobals.excess_option_set = 0;
                String excess_option_set_str = String.valueOf(0);
                str = str + '_' + excess_option_set_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
        excess_time_split_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "setExcessTime";
                //myGlobals.excess_option_set = 1;
                String excess_option_set_str = String.valueOf(1);
                str = str + '_' + excess_option_set_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));            }
        });
        excess_time_After_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "setExcessTime";
                //myGlobals.excess_option_set = 2;
                String excess_option_set_str = String.valueOf(2);
                str = str + '_' + excess_option_set_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        excess_time_pre_button = findViewById(R.id.excess_time_pre_button);
        excess_time_split_button = findViewById(R.id.excess_time_split_button);
        excess_time_After_button = findViewById(R.id.excess_time_After_button);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));
        switch (myGlobals.excess_option_set) {
            case 0:
                // code block
                excess_time_pre_button.setText("Pre - Selected");
                excess_time_split_button.setText("Split");
                excess_time_After_button.setText("After");

                break;
            case 1:
                // code block
                excess_time_pre_button.setText("Pre");
                excess_time_split_button.setText("Split - Selected");
                excess_time_After_button.setText("After");
                break;
            case 2:
                // code block
                excess_time_pre_button.setText("Pre");
                excess_time_split_button.setText("Split");
                excess_time_After_button.setText("After - Selected.");
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
            if(Objects.equals(functionName, "setExcessTime")){
                //change color to selected option
                myGlobals.excess_option_set =  Integer.parseInt(pico_message_parts_array.get(1));
                switch (myGlobals.excess_option_set) {
                    case 0:
                        // code block
                        excess_time_pre_button.setText("Pre - Selected");
                        excess_time_split_button.setText("Split");
                        excess_time_After_button.setText("After");

                        break;
                    case 1:
                        // code block
                        excess_time_pre_button.setText("Pre");
                        excess_time_split_button.setText("Split - Selected");
                        excess_time_After_button.setText("After");
                        break;
                    case 2:
                        // code block
                        excess_time_pre_button.setText("Pre");
                        excess_time_split_button.setText("Split");
                        excess_time_After_button.setText("After - Selected.");
                        break;
                    // more cases as needed
                    default:
                        // code block executed if expression doesn't match any case
                }
                Toast.makeText(Excess_Time_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}