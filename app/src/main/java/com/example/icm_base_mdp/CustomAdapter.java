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

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> data;

    // Bluetooth Connection
    BluetoothAdapter bluetoothAdapter;
    MyGlobals myGlobals;
    //View Components
    private Dialog dialog;
    private Resources getResources;


    public CustomAdapter(Context context, List<String> data, Resources getResources) {
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
            Log.d("CustomAdapter", "Receiving Msg...");
            //depending on the message, decode action an values to do
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            // dummy action to test message send and reply
            if(Objects.equals(functionName, "Action2")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"Simple Action completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomToMin")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomToMin completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomToMax")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomToMax completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomToMinBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomToMinBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomToMaxBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomToMaxBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusToMin")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"focusToMin completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusToMax")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"focusToMax completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusToMinBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"focusToMinBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "focusToMaxBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"focusToMaxBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMinFocusMin")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMinFocusMin completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMaxFocusMax")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMaxFocusMax completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMinFocusMax")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMinFocusMax completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMaxFocusMin")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMaxFocusMin completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMinFocusMinBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMinFocusMinBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMaxFocusMaxBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMaxFocusMaxBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMinFocusMaxBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMinFocusMaxBack completed!", Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(functionName, "zoomMaxFocusMinBack")){
                //close the dialog after pico done action
                dialog.dismiss();
                Toast.makeText(context,"zoomMaxFocusMinBack completed!", Toast.LENGTH_SHORT).show();
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

        if(Objects.equals(itemText, this.getResources.getString(R.string.zoom_to_value))){
            //no countdown initially as need go pov activity
            Intent i = new Intent(this.context, Pov_zoom_to_value_Activity.class);
            i.putExtra("gotBack", false); // Replace "key" and "value" with your actual data
            context.startActivity(i);

        }
        else if (Objects.equals(itemText, this.getResources.getString(R.string.zoom_to_value_and_back))){
            //no countdown initially as need go pov activity
            Intent i = new Intent(this.context, Pov_zoom_to_value_Activity.class);
            i.putExtra("gotBack", true); // Replace "key" and "value" with your actual data
            context.startActivity(i);

        }
        else if (Objects.equals(itemText, this.getResources.getString(R.string.focus_to_value))){
            //no countdown initially as need go pov activity
            Intent i = new Intent(this.context, Pov_focus_to_value_Activity.class);
            i.putExtra("gotBack", false); // Replace "key" and "value" with your actual data
            context.startActivity(i);

        }
        else if (Objects.equals(itemText, this.getResources.getString(R.string.focus_to_value_and_back))){
            //no countdown initially as need go pov activity
            Intent i = new Intent(this.context, Pov_focus_to_value_Activity.class);
            i.putExtra("gotBack", true); // Replace "key" and "value" with your actual data
            context.startActivity(i);

        }
        else if (Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Focus_to_value))){
            //no countdown initially as need go pov activity
            Intent i = new Intent(this.context, Pov_zoomfocus_to_value_Activity.class);
            i.putExtra("gotBack", false); // Replace "key" and "value" with your actual data
            context.startActivity(i);

        }
        else if (Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Focus_to_value_and_back))){
            //no countdown initially as need go pov activity
            Intent i = new Intent(this.context, Pov_zoomfocus_to_value_Activity.class);
            i.putExtra("gotBack", true); // Replace "key" and "value" with your actual data
            context.startActivity(i);

        }
        else{
            countDownTimer.start();
            dialog.show();
        }

    }

    //Do button specific Action after countdown eg. send message to pico.
    // use the dialog at th
    private void actionAfterCountDown(String itemText,Dialog dialog) {
        if(Objects.equals(itemText, this.getResources.getString(R.string.zoom_to_min))){
            String str = "zoomToMin";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.zoom_to_max))){
            String str = "zoomToMax";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.zoom_to_min_and_back))){
            String str = "zoomToMinBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.zoom_to_max_and_back))){
            String str = "zoomToMaxBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.focus_to_min))){
            String str = "focusToMin";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.focus_to_max))){
            String str = "focusToMax";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.focus_to_min_and_back))){
            String str = "focusToMinBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.focus_to_max_and_back))){
            String str = "focusToMaxBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Min_Focus_Min))){
            String str = "zoomMinFocusMin";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Max_Focus_Max))){
            String str = "zoomMaxFocusMax";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Min_Focus_Max))){
            String str = "zoomMinFocusMax";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Max_Focus_Min))){
            String str = "zoomMaxFocusMin";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Min_Focus_Min_and_back))){
            String str = "zoomMinFocusMinBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Max_Focus_Max_and_back))){
            String str = "zoomMaxFocusMaxBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Min_Focus_Max_and_back))){
            String str = "zoomMinFocusMaxBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }
        else if(Objects.equals(itemText, this.getResources.getString(R.string.Zoom_Max_Focus_Min_and_back))){
            String str = "zoomMaxFocusMinBack";
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
            //wait for pico reply say finished then close dialog.
            //dialog.dismiss();

        }

//        else{
//            Toast.makeText(context, "Action C for " + itemText, Toast.LENGTH_SHORT).show();
//            String str = "Action2";
//            BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));
//            //
//            //wait for pico reply say finished then close dialog.
//            //dialog.dismiss();
//        }
    }
}
