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

public class Options_Activity extends AppCompatActivity {
    private static final String TAG = "Options_Time_Activity";
    MyGlobals myGlobals;
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    Button options_switch_zf_button,options_rotation_button,options_reset_camera_button,options_reset_motor_Calibration_button;
    TextView options_header_layout_backCamera_textview;
    TextView options_header_layout_frontCamera_textview;
    TextView options_header_layout_backCamera_rotation_textview;
    TextView options_header_layout_frontCamera_rotation_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        init();

        options_switch_zf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Options_Activity.this, Switch_zoom_focus_position_Activity.class);
                startActivity(i);
            }
        });
        options_rotation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Options_Activity.this, Rotation_Activity.class);
                startActivity(i);
            }
        });
        options_reset_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //may need update the options header is in this page still, need set textview ...
                String str = "resetCamera";
                //myGlobals.shutter_time = 0;
                //myGlobals.motor_time = 0;
                //myGlobals.excess_option_set = 0;
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
        options_reset_motor_Calibration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //may need update the options header is in this page still, need set textview ...
                String str = "resetMotorCalibration";
                //myGlobals.focus_range = 0;
                //myGlobals.zoom_range = 0;
                //myGlobals.focus_current = 0;
                //myGlobals.zoom_current = 0;
                //myGlobals.orientation = 0;
                //myGlobals.rear_rotation_direction = 0;
                //myGlobals.front_rotation_direction = 0;
                //updatePage();
                refreshHeader();
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshHeader();
//        String back_motor_text = "";
//        String front_motor_text = "";
//        String back_motor_rotation_text = "";
//        String front_motor_rotation_text = "";
//        switch (myGlobals.orientation) {
//            case 0:
//                // code block
//                back_motor_text = getResources().getString(R.string.back_camera) + ' ' + "Zoom";
//                front_motor_text = getResources().getString(R.string.front_camera) + ' ' + "Focus";
//                options_header_layout_backCamera_textview.setText(back_motor_text);
//                options_header_layout_frontCamera_textview.setText(front_motor_text);
//                break;
//            case 1:
//                // code block
//                back_motor_text = getResources().getString(R.string.back_camera) + ' ' + "Focus";
//                front_motor_text = getResources().getString(R.string.front_camera) + ' ' + "Zoom";
//                options_header_layout_backCamera_textview.setText(back_motor_text);
//                options_header_layout_frontCamera_textview.setText(front_motor_text);
//                break;
//
//            // more cases as needed
//            default:
//                // code block executed if expression doesn't match any case
//        }
//                //set the front_rotation header description, to fix CW or ACW when confirmed
//        switch (myGlobals.rear_rotation_direction) {
//            case 0:
//                // code block
//                back_motor_rotation_text = getResources().getString(R.string.back_camera_rotation) + ' ' + Integer.toString(myGlobals.rear_rotation_direction);
//                options_header_layout_backCamera_rotation_textview.setText(back_motor_rotation_text);
//                break;
//            case 1:
//                // code block
//                back_motor_rotation_text = getResources().getString(R.string.back_camera_rotation) + ' ' + Integer.toString(myGlobals.rear_rotation_direction);
//                options_header_layout_backCamera_rotation_textview.setText(back_motor_rotation_text);
//
//                break;
//            // more cases as needed
//            default:
//                // code block executed if expression doesn't match any case
//        }
//            //set the front_rotation header description, to fix CW or ACW when confirmed
//        switch (myGlobals.front_rotation_direction) {
//            case 0:
//                // code block
//                front_motor_rotation_text = getResources().getString(R.string.front_rotation_direction) + ' ' + myGlobals.front_rotation_direction;
//                options_header_layout_frontCamera_rotation_textview.setText(front_motor_rotation_text);
//                break;
//            case 1:
//                // code block
//                front_motor_rotation_text = getResources().getString(R.string.front_camera_rotation) + ' ' + myGlobals.front_rotation_direction;
//                options_header_layout_frontCamera_rotation_textview.setText(front_motor_rotation_text);
//
//
//                break;
//            // more cases as needed
//            default:
//                // code block executed if expression doesn't match any case
//        }

    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        options_switch_zf_button = findViewById(R.id.options_switch_zf_button);
        options_rotation_button = findViewById(R.id.options_rotation_button);
        options_reset_camera_button = findViewById(R.id.options_reset_camera_button);
        options_reset_motor_Calibration_button = findViewById(R.id.options_reset_motor_Calibration_button);
        options_header_layout_backCamera_textview = findViewById(R.id.options_header_layout_backCamera_textview);
        options_header_layout_frontCamera_textview = findViewById(R.id.options_header_layout_frontCamera_textview);
        options_header_layout_backCamera_rotation_textview = findViewById(R.id.options_header_layout_backCamera_rotation_textview);
        options_header_layout_frontCamera_rotation_textview = findViewById(R.id.options_header_layout_frontCamera_rotation_textview);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));
        refreshHeader();
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
            if(Objects.equals(functionName, "resetCamera")){
                myGlobals.shutter_time = 0;
                myGlobals.motor_time = 0;
                myGlobals.excess_option_set = 0;
                Toast.makeText(Options_Activity.this, "Camera Setting Reset!",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "resetMotorCalibration")){
                // Inside your activity
                myGlobals.focus_range = 0;
                myGlobals.zoom_range = 0;
                myGlobals.focus_current = 0;
                myGlobals.zoom_current = 0;
                myGlobals.orientation = 0;
                myGlobals.rear_rotation_direction = 0;
                myGlobals.front_rotation_direction = 0;
                recreate();
                Toast.makeText(Options_Activity.this, "Motor Calibration Reset!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void refreshHeader(){
        String back_motor_text = "";
        String front_motor_text = "";
        String back_motor_rotation_text = "";
        String front_motor_rotation_text = "";

        // set the orientation header description
        switch (myGlobals.orientation) {
            case 0:
                // code block
                back_motor_text = getResources().getString(R.string.back_camera) + ' ' + "Zoom";
                front_motor_text = getResources().getString(R.string.front_camera) + ' ' + "Focus";
                options_header_layout_backCamera_textview.setText(back_motor_text);
                options_header_layout_frontCamera_textview.setText(front_motor_text);
                break;
            case 1:
                // code block
                back_motor_text = getResources().getString(R.string.back_camera) + ' ' + "Focus";
                front_motor_text = getResources().getString(R.string.front_camera) + ' ' + "Zoom";
                options_header_layout_backCamera_textview.setText(back_motor_text);
                options_header_layout_frontCamera_textview.setText(front_motor_text);
                break;
            // more cases as needed
            default:
                // code block executed if expression doesn't match any case
        }

        //set the front_rotation header description, to fix CW or ACW when confirmed
        switch (myGlobals.rear_rotation_direction) {
            case 0:
                // code block
                back_motor_rotation_text = getResources().getString(R.string.back_camera_rotation) + ' ' + Integer.toString(myGlobals.rear_rotation_direction);
                options_header_layout_backCamera_rotation_textview.setText(back_motor_rotation_text);
                break;
            case 1:
                // code block
                back_motor_rotation_text = getResources().getString(R.string.back_camera_rotation) + ' ' + Integer.toString(myGlobals.rear_rotation_direction);
                options_header_layout_backCamera_rotation_textview.setText(back_motor_rotation_text);

                break;
            // more cases as needed
            default:
                // code block executed if expression doesn't match any case
        }
        //set the front_rotation header description, to fix CW or ACW when confirmed
        switch (myGlobals.front_rotation_direction) {
            case 0:
                // code block
                front_motor_rotation_text = getResources().getString(R.string.front_camera_rotation) + ' ' + myGlobals.front_rotation_direction;
                options_header_layout_frontCamera_rotation_textview.setText(front_motor_rotation_text);
                break;
            case 1:
                // code block
                front_motor_rotation_text = getResources().getString(R.string.front_camera_rotation) + ' ' + myGlobals.front_rotation_direction;
                options_header_layout_frontCamera_rotation_textview.setText(front_motor_rotation_text);


                break;
            // more cases as needed
            default:
                // code block executed if expression doesn't match any case
        }
    }
}