package com.example.icm_base_mdp;
import java.util.ArrayList;
import java.util.List;

//contains all the global variable should be same as pico upon syncing
public class MyGlobals {
    private static MyGlobals instance;
    public int global_number = 0;
    public int zoom_range = 0;
    public int focus_range = 0;
    public int zoom_current = 0;
    public int focus_current = 0;
    public int orientation = 0;
    public int excess_option_set = 0; //default
    public int camera_shutter_open = 0; //default 0 is not open
    public int rear_rotation_direction = 0; //default
    public int front_rotation_direction = 0; //default
    public int shutter_time = 0;
    public int max_shutter_time = 0;
    public int motor_time = 0;
    public int max_motor_time = 0;
    public int MOTOR_STEPS = 0;

    private MyGlobals() {
        // Private constructor to prevent instantiation
    }

    public static synchronized MyGlobals getInstance() {
        if (instance == null) {
            instance = new MyGlobals();
        }
        return instance;
    }

    public  List<String> decode_pico_message(String input) {
        List<String> parts = new ArrayList<>();

        // Split the input string using '_' as delimiter
        String[] tokens = input.split("_");

        // Add each part to the list
        for (String token : tokens) {
            parts.add(token);
        }
        //functionName_param1_param2_
        //parts -> [functionName_param1_param2,...,...]
        return parts;
    }

}
