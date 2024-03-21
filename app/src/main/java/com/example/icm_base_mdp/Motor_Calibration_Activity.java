package com.example.icm_base_mdp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Motor_Calibration_Activity extends AppCompatActivity {
    private static final String TAG = "Motor_Calibration_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    Button motor_calibration_zoom_cali_button,motor_calibration_focus_cali_button,motor_calibration_pov_cali_button;
    TextView motor_header_layout_zoom_current_textview;
    TextView motor_header_layout_zoom_range_textview;
    TextView motor_header_layout_focus_current_textview;
    TextView motor_header_layout_focus_range_textview;
    MyGlobals myGlobals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_calibration);
        init();
        motor_calibration_zoom_cali_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Motor_Calibration_Activity.this, Zoom_Calibration_Activity.class);
                startActivity(i);
            }
        });
        motor_calibration_focus_cali_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Motor_Calibration_Activity.this, Focus_Calibration_Activity.class);
                startActivity(i);

            }
        });
        motor_calibration_pov_cali_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Motor_Calibration_Activity.this, Pov_Calibration_Activity.class);
                startActivity(i);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        refreshHeader();
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        motor_calibration_zoom_cali_button = findViewById(R.id.motor_calibration_zoom_cali_button);
        motor_calibration_focus_cali_button = findViewById(R.id.motor_calibration_focus_cali_button);
        motor_calibration_pov_cali_button = findViewById(R.id.motor_calibration_pov_cali_button);
        motor_header_layout_zoom_current_textview = findViewById(R.id.motor_header_layout_zoom_current_textview);
        motor_header_layout_zoom_range_textview = findViewById(R.id.motor_header_layout_zoom_range_textview);
        motor_header_layout_focus_current_textview = findViewById(R.id.motor_header_layout_focus_current_textview);
        motor_header_layout_focus_range_textview = findViewById(R.id.motor_header_layout_focus_range_textview);
        refreshHeader();
    }

    private void refreshHeader(){
        String zoom_current_str = getResources().getString(R.string.zoom_current)+ ' ' + myGlobals.zoom_current;
        motor_header_layout_zoom_current_textview.setText(zoom_current_str);
        String zoom_range_str = getResources().getString(R.string.zoom_range)+ ' ' + myGlobals.zoom_range;
        motor_header_layout_zoom_range_textview.setText(zoom_range_str);
        String focus_current_str = getResources().getString(R.string.focus_current)+ ' ' + myGlobals.focus_current;
        motor_header_layout_focus_current_textview.setText(focus_current_str);
        String focus_range_str = getResources().getString(R.string.focus_range)+ ' ' + myGlobals.focus_range;
        motor_header_layout_focus_range_textview.setText(focus_range_str);
    }
}