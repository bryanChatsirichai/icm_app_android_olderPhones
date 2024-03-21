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

public class Pov_focus_to_value_Activity extends AppCompatActivity {
    private static final String TAG = "Pov_focus_to_value_Activity";
    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    ProgressBar pov_focus_to_value_bar;
    Button pov_focus_to_value_decrease_button,pov_focus_to_value_increase_button,pov_focus_to_value_set_button;
    TextView pov_focus_to_value_header,pov_focus_to_value_max_range_textview,pov_focus_value_current_step_textview,pov_focus_to_value_textview;
    MyGlobals myGlobals;
    Dialog dialog;
    Intent intent;
    boolean gotBack ;
    int initialPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pov_focus_to_value);
        init();

        pov_focus_to_value_decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myGlobals.focus_current - 1 < 0){
                    //do nothing
                    return;
                }
                else{
                    String str = "povFocusToValueMin";
                    str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }

            }
        });
        pov_focus_to_value_increase_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myGlobals.focus_current + 1 > myGlobals.focus_range){
                    //do nothing
                    return;
                }
                else{
                    String str = "povFocusToValueMax";
                    str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(myGlobals.focus_range);
                    BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
                }

            }
        });
        pov_focus_to_value_set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countdown
                showCountdownDialog("Focus to value ");
            }
        });
    }
    private void init () {
        Intent intent = getIntent();
        gotBack = intent.getBooleanExtra("gotBack",false); // Replace "key" with the key you used in the sender activity
        myGlobals = MyGlobals.getInstance();
        initialPosition = myGlobals.focus_current;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pov_focus_to_value_bar = findViewById(R.id.pov_focus_to_value_bar);
        pov_focus_to_value_decrease_button = findViewById(R.id.pov_focus_to_value_decrease_button);
        pov_focus_to_value_increase_button = findViewById(R.id.pov_focus_to_value_increase_button);
        pov_focus_to_value_set_button = findViewById(R.id.pov_focus_to_value_set_button);
        pov_focus_to_value_max_range_textview = findViewById(R.id.pov_focus_to_value_max_range_textview);
        pov_focus_value_current_step_textview = findViewById(R.id.pov_focus_value_current_step_textview);
        pov_focus_to_value_textview = findViewById(R.id.pov_focus_to_value_textview);
        pov_focus_to_value_header = findViewById(R.id.pov_focus_to_value_header);

        if(gotBack){
            pov_focus_to_value_header.setText("|-Adjust Focus Pov (Back)-|");
        }
        else{
            pov_focus_to_value_header.setText("|-Adjust Focus Pov-|");

        }

        String text = "Focus Max range: " +  String.valueOf(myGlobals.focus_range) + " steps";
        pov_focus_to_value_max_range_textview.setText(text);
        String text2 = String.valueOf(myGlobals.focus_current) + " steps";
        pov_focus_value_current_step_textview.setText(text2);

        pov_focus_to_value_textview.setText("Adjust Focus Pov");
        pov_focus_to_value_bar.setMax(myGlobals.focus_range);
        pov_focus_to_value_bar.setProgress(myGlobals.focus_current);

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
            if(Objects.equals(functionName, "povFocusToValueMin")){
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_focus_to_value_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_focus_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_focus_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povFocusToValueMax")){
                String focus_current_str = pico_message_parts_array.get(1);
                String focus_range_str = pico_message_parts_array.get(2);
                myGlobals.focus_current = Integer.parseInt(focus_current_str);
                myGlobals.focus_range = Integer.parseInt(focus_range_str);
                pov_focus_to_value_bar.setProgress(myGlobals.focus_current);
                String text_str = myGlobals.focus_current + " steps";
                pov_focus_value_current_step_textview.setText(text_str);
                Toast.makeText(Pov_focus_to_value_Activity.this, "moved",
                        Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "povFocusToValueBackSet")){
                dialog.dismiss();
                Toast.makeText(Pov_focus_to_value_Activity.this, "povFocusToValueBack completed!",
                        Toast.LENGTH_SHORT).show();
                //go back prev simple actions activity
                //finish();
            }
            else if(Objects.equals(functionName, "povFocusToValueSet")){
                dialog.dismiss();
                Toast.makeText(Pov_focus_to_value_Activity.this, "povFocusToValue completed!",
                        Toast.LENGTH_SHORT).show();
                //go back prev simple actions activity
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
            str = "povFocusToValueBackSet";
        }
        else{
            str = "povFocusToValueSet";
        }
        int focusTarget = myGlobals.focus_current;
        myGlobals.focus_current = initialPosition;
        str = str + "_" + String.valueOf(myGlobals.focus_current) + '_' + String.valueOf(focusTarget);
        //go back to action, move this part to on receive
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

    }
}