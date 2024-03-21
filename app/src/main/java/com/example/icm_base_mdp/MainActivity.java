package com.example.icm_base_mdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launch_bluetooth_settings(View v){
        Intent i = new Intent(this,ConnectBT.class);
        startActivity(i);
    }

    public void launch_ICM_pico(View v){
        Intent i = new Intent(this, ICM_Home_Activity.class);
        startActivity(i);
    }
}