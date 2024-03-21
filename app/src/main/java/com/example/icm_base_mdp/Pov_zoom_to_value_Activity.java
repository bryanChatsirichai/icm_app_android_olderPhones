package com.example.icm_base_mdp;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class Pov_zoom_to_value_Activity extends AppCompatActivity {
    private static final String TAG = "Pov_zoom_to_value_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar pov_zoom_to_value_bar;
    Button pov_zoom_to_value_decrease_button,pov_zoom_to_value_increase_button,pov_zoom_to_value_set_button;
    TextView pov_zoom_to_value_header,pov_zoom_to_value_max_range_textview,pov_zoom_value_current_step_textview,pov_zoom_to_value_textview;
    MyGlobals myGlobals;
    Dialog dialog;
    Intent intent;
    boolean gotBack ;
     int initialPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pov_zoom_to_value);
        init();

        pov_zoom_to_value_decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myGlobals.zoom_current - 1 < 0){
                    //do nothing
                    return;
                }
                else{
                    String str = "povZoomToValueMin";
                    str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(myGlobals.zoom_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }

            }
        });
        pov_zoom_to_value_increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myGlobals.zoom_current + 1 > myGlobals.zoom_range){
                    //do nothing
                    return;
                }
                else{
                    String str = "povZoomToValueMax";
                    str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(myGlobals.zoom_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }

            }
        });
        pov_zoom_to_value_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countdown
                showCountdownDialog("Zoom to value ");
            }
        });
    }
    private void init () {
        Intent intent = getIntent();
        gotBack = intent.getBooleanExtra("gotBack",false); // Replace "key" with the key you used in the sender activity
        myGlobals = MyGlobals.getInstance();
        initialPosition = myGlobals.zoom_current;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pov_zoom_to_value_bar = findViewById(R.id.pov_zoom_to_value_bar);
        pov_zoom_to_value_decrease_button = findViewById(R.id.pov_zoom_to_value_decrease_button);
        pov_zoom_to_value_increase_button = findViewById(R.id.pov_zoom_to_value_increase_button);
        pov_zoom_to_value_set_button = findViewById(R.id.pov_zoom_to_value_set_button);
        pov_zoom_to_value_max_range_textview = findViewById(R.id.pov_zoom_to_value_max_range_textview);
        pov_zoom_value_current_step_textview = findViewById(R.id.pov_zoom_value_current_step_textview);
        pov_zoom_to_value_textview = findViewById(R.id.pov_zoom_to_value_textview);
        pov_zoom_to_value_header = findViewById(R.id.pov_zoom_to_value_header);

        if(gotBack){
            pov_zoom_to_value_header.setText("|-Adjust Zoom Pov (Back)-|");
        }
        else{
            pov_zoom_to_value_header.setText("|-Adjust Zoom Pov-|");

        }

        String text = "Zoom Max range: " +  String.valueOf(myGlobals.zoom_range) + " steps";
        pov_zoom_to_value_max_range_textview.setText(text);
        String text2 = String.valueOf(myGlobals.zoom_current) + " steps";
        pov_zoom_value_current_step_textview.setText(text2);

        pov_zoom_to_value_textview.setText("Adjust Zoom Pov");
        pov_zoom_to_value_bar.setMax(myGlobals.zoom_range);
        pov_zoom_to_value_bar.setProgress(myGlobals.zoom_current);

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
            if(Objects.equals(functionName, "povZoomToValueMin")){
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                pov_zoom_to_value_bar.setProgress(myGlobals.zoom_current);
                String text_str = myGlobals.zoom_current + " steps";
                pov_zoom_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_zoom_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZoomToValueMax")){
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                pov_zoom_to_value_bar.setProgress(myGlobals.zoom_current);
                String text_str = myGlobals.zoom_current + " steps";
                pov_zoom_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_zoom_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZoomToValueBackSet")){
                dialog.dismiss();
                Toast.makeText(Pov_zoom_to_value_Activity.this, "povZoomToValueBack completed!",
                        Toast.LENGTH_SHORT).show();
                //go back prev simple actions activity
                //finish();
            }
            else if(Objects.equals(functionName, "povZoomToValueSet")){
                dialog.dismiss();
                Toast.makeText(Pov_zoom_to_value_Activity.this, "povZoomToValue completed!",
                        Toast.LENGTH_SHORT).show();
                //go back prev simple activity
                //finish();
            }
        }
    };

    private void showCountdownDialog(String itemText) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.countdown_dialog);
        // Set this to false to prevent dialog cancellation by touching outside
        dialog.setCanceledOnTouchOutside(false);

        final TextView countdownText = dialog.findViewById(R.id.countdownTextView);

        //add 1sec more for buffer
        CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String str = "Action starting in "  + String.valueOf(seconds)  + " Sec";
                countdownText.setText(String.valueOf(str));
            }

            @Override
            //triggers when timer hit 0
            public void onFinish() {
                //dialog.dismiss();
                String str = "Action Start!";
                countdownText.setText(String.valueOf(str));
                actionAfterCountDown(itemText,dialog);
            }
        };

        countDownTimer.start();
        dialog.show();

    }
    //Do button specific Action after countdown eg. send message to pico.
    // use the dialog at th
    private void actionAfterCountDown(String itemText,Dialog dialog) {
        //send message to pico, go back to initial position then go to that value
        String str;
        if(gotBack){
            str = "povZoomToValueBackSet";
        }
        else{
            str = "povZoomToValueSet";
        }
        int zoomTarget = myGlobals.zoom_current;
        myGlobals.zoom_current = initialPosition;
        str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(zoomTarget);
        //go back to action, move this part to on receive
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

    }
}