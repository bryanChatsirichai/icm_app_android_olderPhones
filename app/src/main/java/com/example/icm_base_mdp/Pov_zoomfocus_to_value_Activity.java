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

public class Pov_zoomfocus_to_value_Activity extends AppCompatActivity {
    private static final String TAG = "Pov_zoomfocus_to_value_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar pov_zoomfocus_to_value_bar;
    Button pov_zoomfocus_to_value_decrease_button,pov_zoomfocus_to_value_increase_button,pov_zoomfocus_to_value_set_button;
    TextView pov_zoomfocus_to_value_header,pov_zoomfocus_to_value_max_range_textview,pov_zoomfocus_value_current_step_textview,pov_zoomfocus_to_value_textview;
    MyGlobals myGlobals;
    Dialog dialog;
    Intent intent;
    boolean gotBack ;
    int zoom_initialPosition,focus_initialPosition,zoom_target,focus_target;
    private boolean firstTime; //first-time zoom, second focus
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pov_zoomfocus_to_value);
        init();
        pov_zoomfocus_to_value_decrease_button.setOnClickListener(new View.OnClickListener() {
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
                        String str = "povZFToValueZMin";
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
                        String str = "povZFToValueFMin";
                        str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }

                }
            }
        });

        pov_zoomfocus_to_value_increase_button.setOnClickListener(new View.OnClickListener() {
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
                        String str = "povZFToValueZMax";
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
                        String str = "povZFToValueFMax";
                        str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                    }

                }
            }
        });
        pov_zoomfocus_to_value_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str;
                if(firstTime){
                    //zoom
                    str = "povZFToValueZSet";
                    zoom_target = myGlobals.zoom_current;
                    myGlobals.zoom_current = zoom_initialPosition;
                    str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(zoom_target);
                    //go back to action, move this part to on receive
                    firstTime = false;
                    String text = "Focus Max range: " +  String.valueOf(myGlobals.focus_range) + " steps";
                    pov_zoomfocus_to_value_max_range_textview.setText(text);
                    String text2 = String.valueOf(myGlobals.focus_current) + " steps";
                    pov_zoomfocus_value_current_step_textview.setText(text2);

                    pov_zoomfocus_to_value_textview.setText("Adjust Focus Pov");
                    pov_zoomfocus_to_value_bar.setMax(myGlobals.focus_range);
                    pov_zoomfocus_to_value_bar.setProgress(myGlobals.focus_current);

                }
                else{
                    //focus
                    str = "povZFToValueFSet";
                    focus_target = myGlobals.focus_current;
                    myGlobals.focus_current = focus_initialPosition;
                    showCountdownDialog("Zoom / Focus to value ");
                    str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(focus_target);
                    //go back to action, move this part to on receive
                }
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            }
        });
    }

    private void init() {
        firstTime = true;
        Intent intent = getIntent();
        gotBack = intent.getBooleanExtra("gotBack",false); // Replace "key" with the key you used in the sender activity
        myGlobals = MyGlobals.getInstance();
        //first time zoom
        zoom_initialPosition = myGlobals.zoom_current;
        focus_initialPosition = myGlobals.focus_current;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pov_zoomfocus_to_value_bar = findViewById(R.id.pov_zoomfocus_to_value_bar);
        pov_zoomfocus_to_value_decrease_button = findViewById(R.id.pov_zoomfocus_to_value_decrease_button);
        pov_zoomfocus_to_value_increase_button = findViewById(R.id.pov_zoomfocus_to_value_increase_button);
        pov_zoomfocus_to_value_set_button = findViewById(R.id.pov_zoomfocus_to_value_set_button);
        pov_zoomfocus_to_value_max_range_textview = findViewById(R.id.pov_zoomfocus_to_value_max_range_textview);
        pov_zoomfocus_value_current_step_textview = findViewById(R.id.pov_zoomfocus_value_current_step_textview);
        pov_zoomfocus_to_value_textview = findViewById(R.id.pov_zoomfocus_to_value_textview);
        pov_zoomfocus_to_value_header = findViewById(R.id.pov_zoomfocus_to_value_header);

        if(gotBack){
            if(firstTime){
                pov_zoomfocus_to_value_header.setText("|-Adjust Zoom Pov (Back)-|");
            }
            else{
                pov_zoomfocus_to_value_header.setText("|-Adjust Focus Pov (Back)-|");
            }
        }
        else{
            if(firstTime){
                pov_zoomfocus_to_value_header.setText("|-Adjust Zoom Pov-|");
            }
            else{
                pov_zoomfocus_to_value_header.setText("|-Adjust Focus Pov-|");
            }
        }
        if(firstTime){
            String text = "Zoom Max range: " +  String.valueOf(myGlobals.zoom_range) + " steps";
            pov_zoomfocus_to_value_max_range_textview.setText(text);
            String text2 = String.valueOf(myGlobals.zoom_current) + " steps";
            pov_zoomfocus_value_current_step_textview.setText(text2);

            pov_zoomfocus_to_value_textview.setText("Adjust Zoom Pov");
            pov_zoomfocus_to_value_bar.setMax(myGlobals.zoom_range);
            pov_zoomfocus_to_value_bar.setProgress(myGlobals.zoom_current);
        }

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
            if(Objects.equals(functionName, "povZFToValueZMin")){
                //zoom pov decrease
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                pov_zoomfocus_to_value_bar.setProgress(myGlobals.zoom_current);
                String text_str = myGlobals.zoom_current + " steps";
                pov_zoomfocus_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_zoomfocus_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZFToValueZMax")){
                //zoom pov increase
                String zoom_current_str = pico_message_parts_array.get(1);
                String zoom_range_str = pico_message_parts_array.get(2);
                myGlobals.zoom_current = Integer.parseInt(zoom_current_str);
                myGlobals.zoom_range = Integer.parseInt(zoom_range_str);
                pov_zoomfocus_to_value_bar.setProgress(myGlobals.zoom_current);
                String text_str = myGlobals.zoom_current + " steps";
                pov_zoomfocus_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_zoomfocus_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZFToValueFMin")){
                //focus pov decrease
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_zoomfocus_to_value_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_zoomfocus_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_zoomfocus_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZFToValueFMax")){
                //focus pov increase
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_zoomfocus_to_value_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_zoomfocus_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_zoomfocus_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povZFToValueZSet")){
                //swap to focus
                firstTime = false;
                String text = "Focus Max range: " +  String.valueOf(myGlobals.focus_range) + " steps";
                pov_zoomfocus_to_value_max_range_textview.setText(text);
                String text2 = String.valueOf(myGlobals.focus_current) + " steps";
                pov_zoomfocus_value_current_step_textview.setText(text2);

                if(gotBack){
                    if(firstTime){
                        pov_zoomfocus_to_value_header.setText("|-Adjust Zoom Pov (Back)-|");
                    }
                    else{
                        pov_zoomfocus_to_value_header.setText("|-Adjust Focus Pov (Back)-|");
                    }
                }
                else{
                    if(firstTime){
                        pov_zoomfocus_to_value_header.setText("|-Adjust Zoom Pov-|");
                    }
                    else{
                        pov_zoomfocus_to_value_header.setText("|-Adjust Focus Pov-|");
                    }
                }
                pov_zoomfocus_to_value_textview.setText("Adjust Focus Pov");
                pov_zoomfocus_to_value_bar.setMax(myGlobals.focus_range);
                pov_zoomfocus_to_value_bar.setProgress(myGlobals.focus_current);
            }
            else if(Objects.equals(functionName, "povZFToValueFSet")){
                //open dialog and countdown to start action
                //showCountdownDialog("Zoom / Focus to value ");
            }
            //finished action
            else if(Objects.equals(functionName, "povZFToValueStart")){
                //close dialog
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                Toast.makeText(Pov_zoomfocus_to_value_Activity.this, "povZFToValue completed!",
                        Toast.LENGTH_SHORT).show();
            }
            //finished action
            else if(Objects.equals(functionName, "povZFToValueBackStart")){
                //close dialog
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                Toast.makeText(Pov_zoomfocus_to_value_Activity.this, "povZFToValue back completed!",
                        Toast.LENGTH_SHORT).show();
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
                String str = "Action Start!";
                countdownText.setText(String.valueOf(str));
                actionAfterCountDown(itemText);
            }
        };

        countDownTimer.start();
        dialog.show();


    }
    //Do button specific Action after countdown eg. send message to pico.
    // use the dialog at th
    private void actionAfterCountDown(String itemText) {
        //send message to pico, go back to initial position then go to that value
        //send the start action message to pico
        String str = "";
        if(gotBack){
            str = "povZFToValueBackStart";
        }
        else{
            str = "povZFToValueStart";
        }
        str = str + "_" + String.valueOf(myGlobals.zoom_current) + '_' + String.valueOf(zoom_target) + '_' +  String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(focus_target) ;
        //go back to action, move this part to on receive
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

    }

    @Override
    public void onBackPressed() {
        // Your custom back button behavior here
        // For example, you can show a confirmation dialog or navigate to a specific activity
        // If you want to perform the default back button behavior, call super.onBackPressed()
        dialog = null;
        super.onBackPressed();
    }
}