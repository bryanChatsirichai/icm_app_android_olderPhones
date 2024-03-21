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

public class Motor_Time_Activity extends AppCompatActivity {
    private static final String TAG = "Motor_Time_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar motor_time_bar;
    Button motor_time_decrease_button,motor_time_increase_button,motor_time_set_button;
    TextView current_motor_time_textview,max_motor_time_textview;
    MyGlobals myGlobals;
    private int temp_motor_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_time);
        init();

        motor_time_decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp_motor_time == 0){
                    //do nothing
                    return;
                }
                temp_motor_time = temp_motor_time - 1;
                String str = temp_motor_time + " sec";
                motor_time_bar.setProgress(temp_motor_time);
                current_motor_time_textview.setText(str);

            }
        });

        motor_time_increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max_motor_time = myGlobals.max_motor_time;
                if(temp_motor_time == max_motor_time){
                    //do nothing
                    return;
                }
                temp_motor_time = temp_motor_time + 1;
                String str = temp_motor_time + " sec";
                motor_time_bar.setProgress(temp_motor_time);
                current_motor_time_textview.setText(str);
            }
        });
        motor_time_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sending command to pico to sync the changes
                String str = "setMotorTime";
                String motor_time_str = String.valueOf(temp_motor_time);
                str = str + '_' + motor_time_str;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

            }
        });
    }

    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        motor_time_bar = findViewById(R.id.motor_time_bar);
        motor_time_decrease_button = findViewById(R.id.motor_time_decrease_button);
        motor_time_increase_button = findViewById(R.id.motor_time_increase_button);
        motor_time_set_button = findViewById(R.id.motor_time_set_button);
        current_motor_time_textview = findViewById(R.id.current_motor_time_textview);
        max_motor_time_textview = findViewById(R.id.max_motor_time_textview);
        //set up the page
        temp_motor_time = myGlobals.motor_time;
        int current_motor_time = myGlobals.motor_time;
        int max_motor_time = myGlobals.max_motor_time;

        String str1 = current_motor_time + " sec";
        String str2 = "Max(sec): " + max_motor_time;
        current_motor_time_textview.setText(str1);
        max_motor_time_textview.setText(str2);
        motor_time_bar.setProgress(current_motor_time);
        //
        motor_time_bar.setMax(max_motor_time);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));

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
            if(Objects.equals(functionName, "setMotorTime")){
                //myGlobals.motor_time = temp_motor_time;
                myGlobals.motor_time =  Integer.parseInt(pico_message_parts_array.get(1));
                motor_time_bar.setProgress(myGlobals.motor_time);
                Toast.makeText(Motor_Time_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}