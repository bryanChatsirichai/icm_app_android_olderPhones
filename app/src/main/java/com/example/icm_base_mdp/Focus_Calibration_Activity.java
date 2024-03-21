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

public class Focus_Calibration_Activity extends AppCompatActivity {
    private static final String TAG = "Focus_Calibration_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar focus_calibration_bar;
    Button focus_calibration_decrease_button,focus_calibration_increase_button,focus_calibration_set_button;
    TextView focus_calibration_max_rotation_textview,focus_calibration_current_step_textview,focus_calibration_direction_textview;
    MyGlobals myGlobals;

    //two parts set min then max to get range, first time min, second time max
    private  boolean firstTime = true;
    private  boolean negative = true;

    private int temp_current_steps = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_calibration);
        init();
        //each tap should move gear by 1 step
        focus_calibration_decrease_button.setOnClickListener(new View.OnClickListener() {
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
                    if(temp_current_steps < 0){
                        //invert it
                        //invert it
                        negative = true;
                        //focus_calibration_bar.setProgress(-temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //focus_calibration_current_step_textview.setText(text_str);
                        String str = "focusMoveMin";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }
                    else{
                        negative = false;
                        //focus_calibration_bar.setProgress(temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //focus_calibration_current_step_textview.setText(text_str);
                        String str = "focusMoveMin";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

                    }
                }
            }
        });
        //each tap should move gear by 1 step
        focus_calibration_increase_button.setOnClickListener(new View.OnClickListener() {
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
                        //focus_calibration_bar.setProgress(-temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //focus_calibration_current_step_textview.setText(text_str);
                        String str = "focusMoveMax";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }
                    else{
                        negative = false;
                        //focus_calibration_bar.setProgress(temp_current_steps);
                        //String text_str = temp_current_steps + " steps";
                        //focus_calibration_current_step_textview.setText(text_str);
                        String str = "focusMoveMax";
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

                    }
                }

            }
        });
        focus_calibration_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstTime){

                    //after set to minimum ask for maximum
                    //minimum is the new 0
                    //firstTime = false;
                    //focus_calibration_direction_textview.setText("Set to max");
                    //temp_current_steps = 0;
                    //focus_calibration_bar.setProgress(temp_current_steps);
                    //String text_str = temp_current_steps + " steps";
                    //focus_calibration_current_step_textview.setText(text_str);
                    // maybe no need send message
                    String str = "focusSetMin";
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }
                else{
                    myGlobals.focus_current = temp_current_steps / 2;
                    myGlobals.focus_range = temp_current_steps;
                    //should set to middle between min and max
                    String str = "focusSetMax";
                    str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

                }
            }
        });

    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        focus_calibration_bar = findViewById(R.id.focus_calibration_bar);
        focus_calibration_decrease_button = findViewById(R.id.focus_calibration_decrease_button);
        focus_calibration_increase_button = findViewById(R.id.focus_calibration_increase_button);
        focus_calibration_set_button = findViewById(R.id.focus_calibration_set_button);
        focus_calibration_max_rotation_textview = findViewById(R.id.focus_calibration_max_rotation_textview);
        focus_calibration_current_step_textview = findViewById(R.id.focus_calibration_current_step_textview);
        focus_calibration_direction_textview = findViewById(R.id.focus_calibration_direction_textview);

        //initial set to maximum
        myGlobals.focus_current = 0;
        myGlobals.focus_range = 0;
        String text_str = myGlobals.focus_current + " steps";
        focus_calibration_current_step_textview.setText(text_str);
        String text = "1 Rotation " +  String.valueOf(myGlobals.MOTOR_STEPS) + " steps";
        focus_calibration_max_rotation_textview.setText(text);
        focus_calibration_direction_textview.setText("Set to minimum");
        focus_calibration_bar.setMax(myGlobals.MOTOR_STEPS);
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
            if(Objects.equals(functionName, "focusMoveMin")){
                temp_current_steps = temp_current_steps - 1;
                if(negative){
                    focus_calibration_bar.setProgress(-temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    focus_calibration_current_step_textview.setText(text_str);
                }
                else{
                    focus_calibration_bar.setProgress(temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    focus_calibration_current_step_textview.setText(text_str);
                }
                Toast.makeText(Focus_Calibration_Activity.this, "Moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusMoveMax")){
                temp_current_steps = temp_current_steps + 1;
                if(negative){
                    focus_calibration_bar.setProgress(-temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    focus_calibration_current_step_textview.setText(text_str);
                }
                else{
                    focus_calibration_bar.setProgress(temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    focus_calibration_current_step_textview.setText(text_str);
                }
                Toast.makeText(Focus_Calibration_Activity.this, "Moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusSetMin")){
                if(firstTime){
                    //swap to setting max range
                    firstTime = false;
                    focus_calibration_direction_textview.setText("Set to max");
                    temp_current_steps = 0;
                    focus_calibration_bar.setProgress(temp_current_steps);
                    String text_str = temp_current_steps + " steps";
                    focus_calibration_current_step_textview.setText(text_str);
                }
                Toast.makeText(Focus_Calibration_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusSetMax")){
                Toast.makeText(Focus_Calibration_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}