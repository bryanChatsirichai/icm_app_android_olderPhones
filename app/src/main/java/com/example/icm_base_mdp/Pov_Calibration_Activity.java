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

public class Pov_Calibration_Activity extends AppCompatActivity {

    private static final String TAG = "Pov_Calibration_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar pov_calibration_bar;
    Button pov_calibration_decrease_button,pov_calibration_increase_button,pov_calibration_set_button;
    TextView pov_calibration_max_rotation_textview,pov_calibration_current_step_textview,pov_calibration_direction_textview;
    MyGlobals myGlobals;
    private  boolean firstTime = true; //first-time zoom, second focus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pov_calibration);
        init();
        pov_calibration_decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstTime){
                    //zoom
                    if(myGlobals.zoom_current - 1 < 0){
                        //do nothing
                        return;
                    }
                    else{
                        //reduce zoom
                        //myGlobals.zoom_current = myGlobals.zoom_current - 1;
                        //pov_calibration_bar.setProgress(myGlobals.zoom_current);
                        //String text_str = myGlobals.zoom_current + " steps";
                        //pov_calibration_current_step_textview.setText(text_str);
                        String str = "povZoomMin";
                        str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(myGlobals.zoom_range);
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }

                }
                else{
                    //focus
                    if(myGlobals.focus_current - 1 < 0){
                        //do nothing
                        return;
                    }
                    else{
                        //reduce focus
                        //myGlobals.focus_current = myGlobals.focus_current - 1;
                        //pov_calibration_bar.setProgress(myGlobals.focus_current);
                        //String text_str = myGlobals.focus_current + " steps";
                        //pov_calibration_current_step_textview.setText(text_str);
                        String str = "povFocusMin";
                        str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }

                }

            }
        });

        pov_calibration_increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstTime){
                    //zoom
                    if(myGlobals.zoom_current + 1 > myGlobals.zoom_range){
                        //do nothing
                        return;
                    }
                    else{
                        //reduce zoom
                        //myGlobals.zoom_current = myGlobals.zoom_current + 1;
                        //pov_calibration_bar.setProgress(myGlobals.zoom_current);
                        //String text_str = myGlobals.zoom_current + " steps";
                        //pov_calibration_current_step_textview.setText(text_str);
                        String str = "povZoomMax";
                        str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(myGlobals.zoom_range);
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }

                }
                else{
                    //focus
                    if(myGlobals.focus_current + 1 > myGlobals.focus_range){
                        //do nothing
                        return;
                    }
                    else{
                        //reduce focus
                        //myGlobals.focus_current = myGlobals.focus_current + 1;
                        //pov_calibration_bar.setProgress(myGlobals.focus_current);
                        //String text_str = myGlobals.focus_current + " steps";
                        //pov_calibration_current_step_textview.setText(text_str);
                        String str = "povFocusMax";
                        str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }

                }

            }
        });

        pov_calibration_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstTime){
                    //zoom
                    firstTime = false;
                    //swap to focus

                    //String text = "Focus Max range: " +  String.valueOf(myGlobals.focus_range) + " steps";
                    //pov_calibration_max_rotation_textview.setText(text);
                    //String text2 = String.valueOf(myGlobals.focus_current) + " steps";
                    //pov_calibration_current_step_textview.setText(text2);
                    //pov_calibration_direction_textview.setText("Adjust Focus Pov");
                    //pov_calibration_bar.setMax(myGlobals.focus_range);
                    //pov_calibration_bar.setProgress(myGlobals.focus_current);
                    String str = "povZoomSet";
                    str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(myGlobals.zoom_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }
                else{
                    //focus
                    String str = "povFocusSet";
                    str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }
            }
        });
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pov_calibration_bar = findViewById(R.id.pov_calibration_bar);
        pov_calibration_decrease_button = findViewById(R.id.pov_calibration_decrease_button);
        pov_calibration_increase_button = findViewById(R.id.pov_calibration_increase_button);
        pov_calibration_set_button = findViewById(R.id.pov_calibration_set_button);
        pov_calibration_max_rotation_textview = findViewById(R.id.pov_calibration_max_rotation_textview);
        pov_calibration_current_step_textview = findViewById(R.id.pov_calibration_current_step_textview);
        pov_calibration_direction_textview = findViewById(R.id.pov_calibration_direction_textview);

        //dummy
        String text = "Zoom Max range: " +  String.valueOf(myGlobals.zoom_range) + " steps";
        pov_calibration_max_rotation_textview.setText(text);
        String text2 = String.valueOf(myGlobals.zoom_current) + " steps";
        pov_calibration_current_step_textview.setText(text2);

        pov_calibration_direction_textview.setText("Adjust Zoom Pov");
        pov_calibration_bar.setMax(myGlobals.zoom_range);
        pov_calibration_bar.setProgress(myGlobals.zoom_current);

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));



    }
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receiving Msg...");
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            if(Objects.equals(functionName, "povZoomMin")){
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                pov_calibration_bar.setProgress(myGlobals.zoom_current);
                String text_str = myGlobals.zoom_current + " steps";
                pov_calibration_current_step_textview.setText(text_str);
                Toast.makeText(Pov_Calibration_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZoomMax")){
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                pov_calibration_bar.setProgress(myGlobals.zoom_current);
                String text_str = myGlobals.zoom_current + " steps";
                pov_calibration_current_step_textview.setText(text_str);
                Toast.makeText(Pov_Calibration_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZoomSet")){
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                //pov_calibration_bar.setProgress(myGlobals.zoom_current);
                //String text_str = myGlobals.zoom_current + " steps";
                //pov_calibration_current_step_textview.setText(text_str);
                //swap to focus
                String text = "Focus Max range: " +  String.valueOf(myGlobals.focus_range) + " steps";
                pov_calibration_max_rotation_textview.setText(text);
                String text2 = String.valueOf(myGlobals.focus_current) + " steps";
                pov_calibration_current_step_textview.setText(text2);
                pov_calibration_direction_textview.setText("Adjust Focus Pov");
                pov_calibration_bar.setMax(myGlobals.focus_range);
                pov_calibration_bar.setProgress(myGlobals.focus_current);
                Toast.makeText(Pov_Calibration_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povFocusMin")){
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_calibration_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_calibration_current_step_textview.setText(text_str);
                Toast.makeText(Pov_Calibration_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povFocusMax")){
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_calibration_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_calibration_current_step_textview.setText(text_str);
                Toast.makeText(Pov_Calibration_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povFocusSet")){
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_calibration_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_calibration_current_step_textview.setText(text_str);
                Toast.makeText(Pov_Calibration_Activity.this, "Set",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
}