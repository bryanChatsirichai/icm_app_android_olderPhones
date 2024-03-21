package com.example.icm_base_mdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Camera_Settings_Activity extends AppCompatActivity {
    MyGlobals myGlobals;
    Button shutter_time_button,motor_time_button,excess_button;
    TextView setting_header_layout_shutter_time_textview;
    TextView setting_header_layout_motor_time_textview;
    TextView setting_header_layout_excess_time_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_settings);
        init();

        shutter_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Camera_Settings_Activity.this, Shutter_Time_Activity.class);
                startActivity(i);
            }
        });
        motor_time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Camera_Settings_Activity.this, Motor_Time_Activity.class);
                startActivity(i);
            }
        });

        excess_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Camera_Settings_Activity.this, Excess_Time_Activity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHeader();
    }
    private void init () {
        myGlobals = MyGlobals.getInstance();
        shutter_time_button = findViewById(R.id.shutter_time_button);
        motor_time_button = findViewById(R.id.motor_time_button);
        excess_button = findViewById(R.id.excess_button);
        setting_header_layout_shutter_time_textview = findViewById(R.id.setting_header_layout_shutter_time_textview);
        setting_header_layout_motor_time_textview = findViewById(R.id.setting_header_layout_motor_time_textview);
        setting_header_layout_excess_time_textview = findViewById(R.id.setting_header_layout_excess_time_textview);
        refreshHeader();
        //set the text_views whenever come back to this page
//        String shutter_time_str = getResources().getString(R.string.shutter_time)+ ' ' + myGlobals.shutter_time;
//        setting_header_layout_shutter_time_textview.setText(shutter_time_str);
//        String motor_time_str = getResources().getString(R.string.motor_time) + ' ' + myGlobals.motor_time;
//        setting_header_layout_motor_time_textview.setText(motor_time_str);
//        String excess_option_set_str = "";
//        switch (myGlobals.excess_option_set) {
//            case 0:
//                // code block
//                excess_option_set_str = getResources().getString(R.string.excess_option_set) + ' ' + "Pre";
//                setting_header_layout_excess_time_textview.setText(excess_option_set_str);
//
//                break;
//            case 1:
//                // code block
//                excess_option_set_str = getResources().getString(R.string.excess_option_set) + ' ' + "Split";
//                setting_header_layout_excess_time_textview.setText(excess_option_set_str);
//                break;
//            case 2:
//                // code block
//                excess_option_set_str = getResources().getString(R.string.excess_option_set) + ' ' + "After";
//                setting_header_layout_excess_time_textview.setText(excess_option_set_str);
//                break;
//            // more cases as needed
//            default:
//                // code block executed if expression doesn't match any case
//        }

    }

    private void refreshHeader(){
        // This code will be executed every time the activity becomes visible
        // This includes when navigating back to the activity
        //set the text_views whenever come back to this page
        String shutter_time_str = getResources().getString(R.string.shutter_time)+ ' ' + myGlobals.shutter_time;
        setting_header_layout_shutter_time_textview.setText(shutter_time_str);
        String motor_time_str = getResources().getString(R.string.motor_time) + ' ' + myGlobals.motor_time;
        setting_header_layout_motor_time_textview.setText(motor_time_str);
        String excess_option_set_str = "";
        switch (myGlobals.excess_option_set) {
            case 0:
                // code block
                excess_option_set_str = getResources().getString(R.string.excess_option_set) + ' ' + "Pre";
                setting_header_layout_excess_time_textview.setText(excess_option_set_str);

                break;
            case 1:
                // code block
                excess_option_set_str = getResources().getString(R.string.excess_option_set) + ' ' + "Split";
                setting_header_layout_excess_time_textview.setText(excess_option_set_str);
                break;
            case 2:
                // code block
                excess_option_set_str = getResources().getString(R.string.excess_option_set) + ' ' + "After";
                setting_header_layout_excess_time_textview.setText(excess_option_set_str);
                break;
            // more cases as needed
            default:
                // code block executed if expression doesn't match any case
        }
    }
}