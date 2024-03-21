package com.example.icm_base_mdp;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

public class CustomAdapter2 extends BaseAdapter {

    private final Context context;
    private final List<String> data;

    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    MyGlobals myGlobals;
    //View Components
    private Dialog dialog;
    private Resources getResources;


    public CustomAdapter2(Context context, List<String> data, Resources getResources) {
        this.context = context;
        this.data = data;
        myGlobals = MyGlobals.getInstance();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LocalBroadcastManager.getInstance(this.context).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));
        this.getResources = getResources;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
        }

        // Get views and set data for each item
        Button button1 = convertView.findViewById(R.id.button1);

        // Set text based on the data
        String itemText = data.get(position);
        button1.setText(itemText);

        // Set button click listeners or any other functionality
        // Set click listeners with different actions for each button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performActionA(itemText);
            }
        });
        return convertView;
    }

    // Define your actions
    private void performActionA(String itemText) {
        // Implement the action for button1
        showCountdownDialog(itemText);
    }

    //BLUETOOTH SECTION,Receiving important command from RPI
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("CustomAdapter2", "Receiving Msg...");
            //depending on the message, decode action an values to do
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            // dummy action to test message send and reply
            if(Objects.equals(functionName, "bokeh")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"bokeh completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "fireworkFocus")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"fireworkFocus completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "fireworkZoomFocus")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"fireworkZoomFocus completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomBlurMin")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomBlurMin completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomBlurMax")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomBlurMax completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "sinWave1")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"sinWave1 completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "sinWave2")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"sinWave2 completed!", Toast.LENGTH_SHORT).show();
            }
        }
    };




    //idea: send count down 3,2,1 then send pico message
    private void showCountdownDialog(String itemText) {
        dialog = new Dialog(this.context);
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

        //need go to pov screen if any
        if(Objects.equals(itemText, "1")){
            //no countdown initially as need go pov activity
            //Intent i = new Intent(this.context, Pov_zoom_to_value_Activity.class);
            //i.putExtra("1", false); // Replace "key" and "value" with your actual data
            //context.startActivity(i);

        }
        //need go to pov screen if any
        else if (Objects.equals(itemText, "2")){
            //no countdown initially as need go pov activity
            //Intent i = new Intent(this.context, Pov_zoom_to_value_Activity.class);
            //i.putExtra("2", true); // Replace "key" and "value" with your actual data
            //context.startActivity(i);

        }
        else{
            countDownTimer.start();
            dialog.show();

        }

    }

    //Do button specific Action after countdown eg. send message to pico.
    // use the dialog at th
    private void actionAfterCountDown(String itemText,Dialog dialog) {
        if(Objects.equals(itemText, this.getResources.getString(R.string.Bokeh))){
            String str = "bokeh";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Firework_Focus))){
            String str = "fireworkFocus";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();
        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Firework_Zoom_Focus))){
            String str = "fireworkZoomFocus";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();
        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Blur_Min))){
            String str = "zoomBlurMin";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();
        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Blur_Max))){
            String str = "zoomBlurMax";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();
        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.SinWave_1))){
            String str = "sinWave1";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();
        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.SinWave_2))){
            String str = "sinWave2";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();
        }
    }
}
