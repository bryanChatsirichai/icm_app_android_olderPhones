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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class Shutter_Time_Activity extends AppCompatActivity {
    private static final String TAG = "Shutter_Time_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar shutter_time_bar;
    Button shutter_time_decrease_button,shutter_time_increase_button,shutter_time_set_button;
    TextView current_shutter_time_textview,max_shutter_time_textview;
    MyGlobals myGlobals;
    private int temp_shutter_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shutter_time);
        init();

        shutter_time_decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp_shutter_time == 0){
                    //do nothing
                    return;
                }
                temp_shutter_time = temp_shutter_time - 1;
                String str = temp_shutter_time + " sec";
                shutter_time_bar.setProgress(temp_shutter_time);
                current_shutter_time_textview.setText(str);

            }
        });

        shutter_time_increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max_shutter_time = myGlobals.max_shutter_time;
                if(temp_shutter_time == max_shutter_time){
                    //do nothing
                    return;
                }
                temp_shutter_time = temp_shutter_time + 1;
                String str = temp_shutter_time + " sec";
                shutter_time_bar.setProgress(temp_shutter_time);
                current_shutter_time_textview.setText(str);
            }
        });
        shutter_time_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sending command to pico to sync the changes
                String str = "setShutterTime";
                String shutter_time_str = String.valueOf(temp_shutter_time);
                str = str + '_' + shutter_time_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }

    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        shutter_time_bar = findViewById(R.id.shutter_time_bar);
        shutter_time_decrease_button = findViewById(R.id.shutter_time_decrease_button);
        shutter_time_increase_button = findViewById(R.id.shutter_time_increase_button);
        shutter_time_set_button = findViewById(R.id.shutter_time_set_button);
        current_shutter_time_textview = findViewById(R.id.current_shutter_time_textview);
        max_shutter_time_textview = findViewById(R.id.max_shutter_time_textview);
        //set up the page
        temp_shutter_time = myGlobals.shutter_time;
        int current_shutter_time = myGlobals.shutter_time;
        int max_shutter_time = myGlobals.max_shutter_time;
        String str1 = current_shutter_time + " sec";
        String str2 = "Max(sec): " + max_shutter_time;
        current_shutter_time_textview.setText(str1);
        max_shutter_time_textview.setText(str2);
        shutter_time_bar.setProgress(current_shutter_time);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));

    }
    //BLUETOOTH SECTION, Receiving important command from RPI
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receiving Msg...");
            String msg = intent.getStringExtra("receivingMsg");
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            if(Objects.equals(functionName, "setShutterTime")){
                myGlobals.shutter_time = Integer.parseInt(pico_message_parts_array.get(1));;

                //max shutter time  = max motor time
                myGlobals.max_motor_time = myGlobals.shutter_time;
                if(myGlobals.motor_time > myGlobals.max_motor_time){
                    myGlobals.motor_time = myGlobals.max_motor_time;
                }
                shutter_time_bar.setProgress(myGlobals.shutter_time);
                Toast.makeText(Shutter_Time_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}