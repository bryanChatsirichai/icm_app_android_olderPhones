package com.example.icm_base_mdp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ConnectBT extends AppCompatActivity {

    private static final String TAG = "ConnectBT";

    public ArrayList<BluetoothDevice> bluetoothDevicesArrayList = new ArrayList<>();
    public ArrayList<BluetoothDevice> pairedBluetoothDevicesArrayList = new ArrayList<>();


    // my own device
    static BluetoothDevice btDevice;
    // device that is getting connected
    BluetoothDevice btConnectToDevice;

    BluetoothAdapter btAdapter;
    public DeviceListAdapter deviceListAdapter;
    public DeviceListAdapter pairedDeviceListAdapter;

    ListView newDevicesLV;
    ListView pairedDevicesLV;
    EditText sendMessageET;
    TextView incomingMessageTV;
    StringBuilder incomingMessageSB;
    Button discoverBTN;
    Button connectBTN;
    TextView searchStatusTV;
    TextView pairedDeviceTV;
    ProgressDialog progressDialog;
    Intent strtconnectServiceIntent;
    ProgressBar connectingBtProgressBar;
    View overlay;
    MyGlobals myGlobals;

    // UUID
    private static final UUID mdpUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static BluetoothDevice getBluetoothDevice() {
        return btDevice;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bt);
        myGlobals = MyGlobals.getInstance();

        connectBTN = findViewById(R.id.btnConnect);
        discoverBTN = findViewById(R.id.btnStartDiscover);
        newDevicesLV = findViewById(R.id.newdeviceLV);
        pairedDevicesLV = findViewById(R.id.pairedDeviceLV);
        searchStatusTV = findViewById(R.id.searchStatID);
        pairedDeviceTV = findViewById(R.id.pairedDeviceTV);
        connectingBtProgressBar = findViewById(R.id.connectingBtProgressBar);
        connectingBtProgressBar.setVisibility(View.INVISIBLE);
        overlay = findViewById(R.id.overlay);
        overlay.setVisibility(View.INVISIBLE);

        incomingMessageSB = new StringBuilder();
        btDevice = null;

        bluetoothDevicesArrayList = new ArrayList<>();
        pairedBluetoothDevicesArrayList = new ArrayList<>();
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register BroadcastReceiver for ACTION_STATE_CHANGED
        IntentFilter enBTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(enBluetoothBroadcastReceiver, enBTIntent);

        // Register BroadcastReceiver for ACTION_SCAN_MODE_CHANGED
        IntentFilter enDiscoverabilityintent = new IntentFilter(btAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(ENABLEdiscoverabilityBroadcastReceiver, enDiscoverabilityintent);

        // Register BroadcastReceiver for ACTION_DISCOVERY_STARTED
        IntentFilter discoverStartedIntent = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(discoveryStartedBroadcastReceiver, discoverStartedIntent);

        // Register BroadcastReceiver for ACTION_DISCOVERY_FINISHED
        IntentFilter discoverFinishedIntent = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishedBroadcastReceiver, discoverFinishedIntent);

        // Register receiver for bluetooth connection status
        LocalBroadcastManager.getInstance(this).registerReceiver(btConnectionStatusReceiver, new IntentFilter("btConnectionStatus"));

        // Register BroadcastReceiver for ACTION_FOUND
        IntentFilter discoverDevicesInfoIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoverDeviceInfoBroadcastReceiver, discoverDevicesInfoIntent);

        // Register BroadcastReceiver for ACTION_BOND_STATE_CHANGED
        // for pairing of devices
        IntentFilter bondIntent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(bondBroadcastReceiver, bondIntent);

        // Register receiver for incoming message
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("IncomingMsg"));


        pairedDevicesLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //CANCEL DEVICE SEARCH DISCOVERY
                        btAdapter.cancelDiscovery();

                        btDevice = pairedBluetoothDevicesArrayList.get(i);

                        //UnSelect Search Device List
                        newDevicesLV.setAdapter(deviceListAdapter);
                        //pairedDeviceTV.setText(pairedBluetoothDevicesArrayList.get(i).getName());
                        searchStatusTV.setText("");

                        Log.d(TAG, "Paired Device = " + pairedBluetoothDevicesArrayList.get(i).getName());
                        //Log.d(TAG, "DeviceAddress = " + pairedBluetoothDevicesArrayList.get(i).getAddress());

                    }
                }
        );

        newDevicesLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //CANCEL DEVICE SEARCH DISCOVERY
                        btAdapter.cancelDiscovery();

                        Log.d(TAG, "onItemClick: Item Selected");

                        String deviceName = bluetoothDevicesArrayList.get(i).getName();
                        //String deviceAddress = bluetoothDevicesArrayList.get(i).getAddress();

                        //UnSelect Paired Device List
                        pairedDevicesLV.setAdapter(pairedDeviceListAdapter);


                        Log.d(TAG, "Device Name = " + deviceName);
                        //Log.d(TAG, "Device Address = " + deviceAddress);

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            Log.d(TAG, "Trying to pair with: " + deviceName);

                            // create bond
                            bluetoothDevicesArrayList.get(i).createBond();

                            //ASSIGN SELECTED DEVICE INFO TO myBTDevice
                            btDevice = bluetoothDevicesArrayList.get(i);

                        }

                    }
                }
        );

        discoverBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                connectingBtProgressBar.setVisibility(View.VISIBLE);
                connectingBtProgressBar.bringToFront();
                overlay.setVisibility(View.VISIBLE);
                overlay.bringToFront();
                enableBT();
                bluetoothDevicesArrayList.clear();
            }
        });


        connectBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (btDevice == null) {

                    Toast.makeText(ConnectBT.this, "No Paired Device! Please discover/select a device.",
                            Toast.LENGTH_SHORT).show();
                } else if (btAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED) {
                    Toast.makeText(ConnectBT.this, "Bluetooth Already Connected",
                            Toast.LENGTH_SHORT).show();
                } else {

                    // start connect with device that is bonded
                    startBTConnection(btDevice, mdpUUID);
                    //pairedDeviceTV.setText(btDevice.getName());


                }
                pairedDevicesLV.setAdapter(pairedDeviceListAdapter);
            }
        });
    }

    /*
       Broadcast Receiver for ACTION_FOUND
   */
    private final BroadcastReceiver enBluetoothBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // when discovery found a device
            if (action.equals(btAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, btAdapter.ERROR);

                switch (state) {
                    //BLUETOOTH TURNED OFF STATE
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "OnReceiver: STATE OFF");
                        break;
                    //BLUETOOTH TURNING OFF STATE
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "OnReceiver: STATE TURNING OFF");
                        break;
                    //BLUETOOTH TURNED ON STATE
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "OnReceiver: STATE ON");

                        //TURN DISCOVERABILITY ON
                        discoverabilityON();

                        break;
                    //BLUETOOTH TURNING ON STATE
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "OnReceiver: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    /*
Broadcast Receiver to enable discovery of devices
*/
    private final BroadcastReceiver ENABLEdiscoverabilityBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //DEVICE IS IN DISCOVERABLE MODE
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "OnReceiver: DISCOVERABILITY ENABLED");

                        //check paired have pico connection before
//                        if(connectPairedDevice()){
//                            return;
//                        }

                        // start discover devices
                        startDiscover();

                        // Start bluetooth connection service -> start accept thread for connection
                        strtconnectServiceIntent = new Intent(ConnectBT.this, BTConnectionService.class);
                        strtconnectServiceIntent.putExtra("serviceType", "listen");
                        startService(strtconnectServiceIntent);

                        //original
                        chkPairedDevice();
                        break;

                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "OnReceiver: DISCOVERABILITY DISABLED, ABLE TO RECEIVE CONNECTION");
                        break;

                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "OnReceiver: DISCOVERABILITY DISABLED, NOT ABLE TO RECEIVE CONNECTION");
                        break;

                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "OnReceiver: CONNECTING");
                        break;

                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "OnReceiver: CONNECTED");
                        break;
                }
            }
        }
    };

    /*
 Broadcast Receiver to start discovery of devices
*/
    private final BroadcastReceiver discoveryStartedBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {

                Log.d(TAG, "STARTED DISCOVERY!!!");

                searchStatusTV.setText(R.string.searchBTdevices);

            }
        }
    };

    /*
  Broadcast Receiver to end discovery of devices
  */
    private final BroadcastReceiver discoveryFinishedBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

                Log.d(TAG, "ENDED DISCOVERY!!!");

                searchStatusTV.setText(R.string.searchBTdevicesDone);

            }
        }
    };

    /*
        Broadcast Receiver for bluetooth connection status
    */
    BroadcastReceiver btConnectionStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectingBtProgressBar.setVisibility(View.INVISIBLE);
            overlay.setVisibility(View.INVISIBLE);


            Log.d(TAG, "Receiving btConnectionStatus Msg!!!");
            String connectionStatus = intent.getStringExtra("ConnectionStatus");
            btConnectToDevice = intent.getParcelableExtra("Device");

            //DISCONNECTED FROM BLUETOOTH CHAT
            if (connectionStatus.equals("disconnect")) {

                Log.d("ConnectAcitvity:", "Device Disconnected");

                //CHECK FOR NOT NULL
                if (strtconnectServiceIntent != null) {
                    //Stop Bluetooth Connection Service
                    stopService(strtconnectServiceIntent);
                }

                //RECONNECT DIALOG MSG
                AlertDialog alertDialog = new AlertDialog.Builder(ConnectBT.this).create();
                alertDialog.setTitle("Bluetooth Disconnected");
                alertDialog.setMessage("Connection with device has ended. Do you want to reconnect?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startBTConnection(btConnectToDevice, mdpUUID);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(ConnectBT.this, "Disconnected with  " + btConnectToDevice.getName(),
                                        Toast.LENGTH_SHORT).show();
                                pairedDeviceTV.setText("");
                            }
                        });
                alertDialog.show();
            }

            //SUCCESSFULLY CONNECTED TO BLUETOOTH DEVICE
            else if (connectionStatus.equals("connect")) {
                Log.d("ConnectAcitvity:", "Device Connected");
                Toast.makeText(ConnectBT.this, "Connected to " + btConnectToDevice.getName(),
                        Toast.LENGTH_SHORT).show();
                pairedDeviceTV.setText(btConnectToDevice.getName());

                //sync device values app and picoW
                String str = "syncDevices";
                BluetoothCommunication.writeMsg(str.getBytes(Charset.defaultCharset()));

            }

            //BLUETOOTH CONNECTION FAILED
            else if (connectionStatus.equals("connectionFail")) {
                Toast.makeText(ConnectBT.this, "Connection Failed: " + btConnectToDevice.getName(),
                        Toast.LENGTH_SHORT).show();
            }

        }
    };


    /*
       Broadcast Receiver for listing devices that are not yet paired
       Executed by start discovery button.
     */
    private final BroadcastReceiver discoverDeviceInfoBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "SEARCH ME!");

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!bluetoothDevicesArrayList.contains(device)) {
                    if(!pairedBluetoothDevicesArrayList.contains(device)) {
                        bluetoothDevicesArrayList.add(device);
                    }
                }


                Log.d(TAG, "OnReceive: " + device.getName() + ": " + device.getAddress());
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, bluetoothDevicesArrayList);
                newDevicesLV.setAdapter(deviceListAdapter);



                //discovered picoW
                if(device.getName()!= null && device.getName().contains("PicoW")){
                    Toast.makeText(ConnectBT.this, "PicoW found: " + device.getName(),
                            Toast.LENGTH_SHORT).show();

                    //not first time
                    if(pairedBluetoothDevicesArrayList.contains(device)){
                        Toast.makeText(ConnectBT.this, "PicoW connected before: " + device.getName(),
                                Toast.LENGTH_SHORT).show();

                        btDevice = device;
                        startBTConnection(btDevice, mdpUUID);
                    }
                    else{
                        //first time
                        //CANCEL DEVICE SEARCH DISCOVERY
                        Toast.makeText(ConnectBT.this, "PicoW connected first time: " + device.getName(),
                                Toast.LENGTH_SHORT).show();
                        btAdapter.cancelDiscovery();

                        String deviceName = device.getName() ;
                        //String deviceAddress = bluetoothDevicesArrayList.get(i).getAddress();
                        //UnSelect Paired Device List
                        pairedDevicesLV.setAdapter(pairedDeviceListAdapter);

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            Log.d(TAG, "Trying to pair with: " + deviceName);

                            // create bond
                            device.createBond();

                        }
                    }

                }

            }
        }
    };


    /*
        Create a BroadcastReceiver for ACTION_FOUND (Pairing Devices).
    */
    private final BroadcastReceiver bondBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            connectingBtProgressBar.setVisibility(View.INVISIBLE);
            overlay.setVisibility(View.INVISIBLE);
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // device bonded
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

                    Log.d(TAG, "Bonded with: " + device.getName());

                    progressDialog.dismiss();

                    Toast.makeText(ConnectBT.this, "Bonded successfully with: " + device.getName(),
                            Toast.LENGTH_SHORT).show();
                    btDevice = device;
                    startBTConnection(btDevice, mdpUUID);
                    //chkPairedDevice();
                    //newDevicesLV.setAdapter(deviceListAdapter);

                }
                // bond w device
                if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "Bonding with another device");

                    progressDialog = ProgressDialog.show(ConnectBT.this, "Bonding with device..", "Please Wait...", true);


                }
                // break bond between device
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {


                    progressDialog.dismiss();

                    //DIALOG MSG POPUP
                    AlertDialog alertDialog = new AlertDialog.Builder(ConnectBT.this).create();
                    alertDialog.setTitle("Bonding Status");
                    alertDialog.setMessage("Bond Disconnected!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    // reset bluetooth device to null
                    btDevice = null;
                }

            }
        }
    };


    /*
        Broadcast Receiver for incoming msg
    */
    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Receiving Msg...");
            //depending on the message, decode action an values to do
            String pico_message = intent.getStringExtra("receivingMsg");
            assert pico_message != null;
            List<String> pico_message_parts_array = myGlobals.decode_pico_message(pico_message);
            String functionName = pico_message_parts_array.get(0);
            // dummy action to test message send and reply
            if(Objects.equals(functionName, "Action1")){
                //... testing logic

            } else if (Objects.equals(functionName, "syncDevices")) {
                myGlobals.focus_range = Integer.parseInt(pico_message_parts_array.get(1));
                myGlobals.zoom_range = Integer.parseInt(pico_message_parts_array.get(2));
                myGlobals.focus_current = Integer.parseInt(pico_message_parts_array.get(3));
                myGlobals.zoom_current = Integer.parseInt(pico_message_parts_array.get(4));
                myGlobals.orientation = Integer.parseInt(pico_message_parts_array.get(5));
                myGlobals.shutter_time =  Integer.parseInt(pico_message_parts_array.get(6));
                myGlobals.max_shutter_time =  Integer.parseInt(pico_message_parts_array.get(7));
                myGlobals.motor_time =  Integer.parseInt(pico_message_parts_array.get(8));
                myGlobals.max_motor_time =  Integer.parseInt(pico_message_parts_array.get(9));
                myGlobals.excess_option_set =  Integer.parseInt(pico_message_parts_array.get(10));
                myGlobals.rear_rotation_direction = Integer.parseInt(pico_message_parts_array.get(11));
                myGlobals.front_rotation_direction =  Integer.parseInt(pico_message_parts_array.get(12));
                myGlobals.MOTOR_STEPS = Integer.parseInt(pico_message_parts_array.get(13));
                Toast.makeText(ConnectBT.this, "Synced Successful!-auto",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void discoverabilityON() {

        // enable device discoverability for 900secs
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 900);
        startActivity(discoverableIntent);

    }

    public void enableBT() {
        // device does not support bluetooth
        if (btAdapter == null) {
            Toast.makeText(ConnectBT.this, "Device Does Not Support Bluetooth.",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        // bluetooth not enabled
        if (!btAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);


        }
        // bluetooth enabled
        if (btAdapter.isEnabled()) {
            discoverabilityON();
        }

    }

    /*
     Check BT permission in manifest (For Start Discovery)
 */
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);

            permissionCheck += ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != 0) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
        } else {
            Log.d(TAG, "check bluetooth permissions : No need to check permissions. SDK version < LOLLIPOP.");

        }
    }


    /*
      START DISCOVERING OTHER DEVICES
  */
    private void startDiscover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        //DISCOVER OTHER DEVICES
        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
            Log.d(TAG, "BTDiscovery: canceling discovery");

            //check BT permission in manifest
            checkBTPermissions();

            btAdapter.startDiscovery();
            Log.d(TAG, "BTDiscovery: enable discovery");


        }
        if (!btAdapter.isDiscovering()) {

            //check BT permission in manifest
            checkBTPermissions();

            btAdapter.startDiscovery();
            Log.d(TAG, "BTDiscovery: enable discovery");


        }
    }


    /*
      start bluetooth connection service
    */
    public void startBTConnection(BluetoothDevice device, UUID uuid) {

        strtconnectServiceIntent = new Intent(ConnectBT.this, BTConnectionService.class);
        strtconnectServiceIntent.putExtra("serviceType", "connect");
        strtconnectServiceIntent.putExtra("device", device);
        strtconnectServiceIntent.putExtra("id", uuid);

        Log.d(TAG, "StartBTConnection: Starting Bluetooth Connection Service!");

        startService(strtconnectServiceIntent);

    }

    public void chkPairedDevice() {

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        pairedBluetoothDevicesArrayList.clear();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d(TAG, "Paired devices: " + device.getName() + "," + device.getAddress());
                pairedBluetoothDevicesArrayList.add(device);

            }
            pairedDeviceListAdapter = new DeviceListAdapter(this, R.layout.device_adapter_view, pairedBluetoothDevicesArrayList);
            pairedDevicesLV.setAdapter(pairedDeviceListAdapter);

        } else {
            Log.d(TAG, "No paired devices");
        }
    }

    // unregister all BroadcastReceivers
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(enBluetoothBroadcastReceiver);
        unregisterReceiver(ENABLEdiscoverabilityBroadcastReceiver);
        unregisterReceiver(discoveryStartedBroadcastReceiver);
        unregisterReceiver(discoveryFinishedBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(btConnectionStatusReceiver);
        unregisterReceiver(discoverDeviceInfoBroadcastReceiver);
        unregisterReceiver(bondBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);

        Log.d(TAG, "All BroadcastReceiver unregistered");
    }

}
