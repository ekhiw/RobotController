package com.led.led;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;


import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by Ekky Hermestian IW on 06-Jul-17.
 */

public class ledControl extends ActionBarActivity {

    //===============================================================================================
    //variable
    Button btnF, btnB, btnDis, btnLF, btnRF, btnLB, btnRB, btnL, btnR, btnF1, btnF2, btnF3, btnF4;
    SeekBar brightness;
    TextView lumn;
    String address = null;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    BluetoothDevice mmDevice;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //===================================
    //ngetest
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    volatile boolean isBtConnected;
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device
        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes
        btnF = (Button)findViewById(R.id.button2);
        btnB = (Button)findViewById(R.id.button3);
        btnLF = (Button)findViewById(R.id.button5);
        btnRF = (Button)findViewById(R.id.button7);
        btnL = (Button)findViewById(R.id.button8);
        btnLB = (Button)findViewById(R.id.button9);
        btnR = (Button)findViewById(R.id.button10);
        btnRB = (Button)findViewById(R.id.button11);
        btnF1 = (Button)findViewById(R.id.button15);
        btnF2 = (Button)findViewById(R.id.button13);
        btnF3 = (Button)findViewById(R.id.button14);
        btnF4 = (Button)findViewById(R.id.button12);
        btnDis = (Button)findViewById(R.id.button4);
        brightness = (SeekBar)findViewById(R.id.seekBar);
        lumn = (TextView)findViewById(R.id.lumn);

        new ConnectBT().execute(); //Call the class to connect

//        tmpIn = btSocket.getInputStream();
//        lumn.setText((CharSequence) tmpIn);
//        bluetoothIn = new Handler()
//        {
//            public void handleMessage(android.os.Message msg)
//            {
//                if (msg.what == handlerState)
//                {                                     //if message is what we want
//                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
//                    recDataString.append(readMessage);
//                    //int endOfLineIndex = recDataString.indexOf("~");
//                    //if (endOfLineIndex > 0)
//                    //{                                           // make sure there data before ~
//                    //  String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
//                        //lumn.setText(readMessage);
//                        lumn.setText(String.valueOf(readMessage));
//                    //}
//                }
//            }
//        };

        //==========================================================================================

        //commands to be sent to bluetooth
        btnLF.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        LF(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });

        btnF.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        F(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });

        btnRF.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        RF(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });

        btnL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        L(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });

        btnR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        R(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });

        btnLB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        LB(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });

        btnB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        B(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });
        btnRB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        RB(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });
        btnF1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        F1(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });
        btnF2.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        F2(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });
        btnF3.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        //beginListenForData(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });
        btnF4.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        F4(); // if you want to handle the touch event
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        turnOnLed(); // if you want to handle the touch event
                        return true;
                }
                return false;
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    lumn.setText(String.valueOf(progress));
                    try
                    {
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());
                    }
                    catch (IOException e)
                    {

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        btSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        btSocket.connect();
        mmInputStream = btSocket.getInputStream();

        beginListenForData();

    }
    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            lumn.setText(data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }
    private void readbt()
    {
        if (btSocket!=null)
            try
            {
                btSocket.getInputStream().read(readBuffer);
            }
    }
    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TF".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("Z".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    //==============================================================================================================================
    private void LF()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("A".toString().getBytes());

            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void F()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("B".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void RF()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("C".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void L()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("D".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void R()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("E".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void LB()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("F".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void B()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("G".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void RB()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("H".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void F1()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("I".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void F2()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("J".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void F3()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("K".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void F4()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("L".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Udah nyala belom? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
