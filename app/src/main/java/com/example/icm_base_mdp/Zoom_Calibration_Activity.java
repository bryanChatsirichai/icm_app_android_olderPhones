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

public class Zoom_Calibration_Activity extends AppCompatActivity {
    private static final String TAG = "Zoom_Calibration_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar zoom_calibration_bar;
    Button zoom_calibration_decrease_button,zoom_calibration_increase_button,zoom_calibration_set_button;
    TextView zoom_calibration_max_rotation_textview,zoom_calibration_current_step_textview,zoom_calibration_direction_textview;
    MyGlobals myGlobals;

    //two parts set min then max to get range, first time min, second time max
    private  boolean firstTime = true;
    private  boolean negative = true;
    private int temp_current_steps = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_calibration);
        init();
        //each tap should move gear by 1 step
        zoom_calibration_decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp_current_steps - 1 < -myGlobals.MOTOR_STEPS){
                    //do nothing
                     return;
                }
                else if(!firstTime && temp_current_steps - 1 < 0){
                    //do nothing
                    return;
                }
                else{
                    //temp_current_steps = temp_current_steps - 1;
                    if(temp_current_steps < 0){
                        //invert it
                        negative = true;
                        //zoom_calibration_bar.setProgress(-temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //zoom_calibration_current_step_textview.setText(text_str);
                        String str = "zoomMoveMin";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }
                    else{
                        negative = false;
                        //zoom_calibration_bar.setProgress(temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //zoom_calibration_current_step_textview.setText(text_str);
                        String str = "zoomMoveMin";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

                    }
                }
            }
        });
        //each tap should move gear by 1 step
        zoom_calibration_increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp_current_steps + 1 > myGlobals.MOTOR_STEPS){
                    //do nothing
                    return;
                }
                else{
                    if(temp_current_steps < 0){
                        //invert it
                        negative = true;
                        //zoom_calibration_bar.setProgress(-temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //zoom_calibration_current_step_textview.setText(text_str);
                        String str = "zoomMoveMax";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }
                    else{
                        negative = false;
                        //zoom_calibration_bar.setProgress(temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //zoom_calibration_current_step_textview.setText(text_str);
                        String str = "zoomMoveMax";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

                    }
                }

            }
        });
        zoom_calibration_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstTime){

                    //after set to minimum ask for maximum
                    //minimum is the new 0
                    //firstTime = false;
                    //zoom_calibration_direction_textview.setText("Set to max");
                    //temp_current_steps = 0;
                    //zoom_calibration_bar.setProgress(temp_current_steps);
                    //String text_str = temp_current_steps + " steps";
                    //zoom_calibration_current_step_textview.setText(text_str);
                    // maybe no need send message
                     String str = "zoomSetMin";
                     BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }
                else{
                    myGlobals.zoom_current = temp_current_steps / 2;
                    myGlobals.zoom_range = temp_current_steps;
                    //should set to middle between min and max
                    String str = "zoomSetMax";
                    str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(myGlobals.zoom_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }
            }
        });

    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        zoom_calibration_bar = findViewById(R.id.zoom_calibration_bar);
        zoom_calibration_decrease_button = findViewById(R.id.zoom_calibration_decrease_button);
        zoom_calibration_increase_button = findViewById(R.id.zoom_calibration_increase_button);
        zoom_calibration_set_button = findViewById(R.id.zoom_calibration_set_button);
        zoom_calibration_max_rotation_textview = findViewById(R.id.zoom_calibration_max_rotation_textview);
        zoom_calibration_current_step_textview = findViewById(R.id.zoom_calibration_current_step_textview);
        zoom_calibration_direction_textview = findViewById(R.id.zoom_calibration_direction_textview);

        //initial set to maximum
        myGlobals.zoom_current = 0;
        myGlobals.zoom_range = 0;
        String text_str = myGlobals.zoom_current + " steps";
        zoom_calibration_current_step_textview.setText(text_str);
        String text = "1 Rotation " +  String.valueOf(myGlobals.MOTOR_STEPS) + " steps";
        zoom_calibration_max_rotation_textview.setText(text);
        zoom_calibration_direction_textview.setText("Set to minimum");
        zoom_calibration_bar.setMax(myGlobals.MOTOR_STEPS);
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
            if(Objects.equals(functionName, "zoomMoveMin")){
                temp_current_steps = temp_current_steps - 1;
                if(negative){
                    zoom_calibration_bar.setProgress(-temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    zoom_calibration_current_step_textview.setText(text_str);
                }
                else{
                    zoom_calibration_bar.setProgress(temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    zoom_calibration_current_step_textview.setText(text_str);
                }
                Toast.makeText(Zoom_Calibration_Activity.this, "Moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMoveMax")){
                temp_current_steps = temp_current_steps + 1;
                if(negative){
                    zoom_calibration_bar.setProgress(-temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    zoom_calibration_current_step_textview.setText(text_str);
                }
                else{
                    zoom_calibration_bar.setProgress(temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    zoom_calibration_current_step_textview.setText(text_str);
                }
                Toast.makeText(Zoom_Calibration_Activity.this, "Moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomSetMin")){
                if(firstTime){
                    //swap to setting max range
                    firstTime = false;
                    zoom_calibration_direction_textview.setText("Set to max");
                    temp_current_steps = 0;
                    zoom_calibration_bar.setProgress(temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    zoom_calibration_current_step_textview.setText(text_str);
                }
                Toast.makeText(Zoom_Calibration_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomSetMax")){
                Toast.makeText(Zoom_Calibration_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}