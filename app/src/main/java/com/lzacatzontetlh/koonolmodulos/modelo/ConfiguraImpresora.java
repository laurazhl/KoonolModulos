package com.lzacatzontetlh.koonolmodulos.modelo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfiguraImpresora extends DialogFragment {
    private static final String TAG = "ConfiguraImpresora";
    private static final boolean DEBUG = true;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothService mService = new BluetoothService(getContext(), new Handler());

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;

    public static final String DEVICE_NAME = "Device Name";
    public static final String TOAST = "TOAST";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public static ConfiguraImpresora newInstance(int title) {
        ConfiguraImpresora frag = new ConfiguraImpresora();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    private void SendDataByte(byte[] data) {
        /*if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(getContext(), "Conecte el dispositivo Bluetoot", Toast.LENGTH_SHORT).show();
            return;
        }*/
        mService.write(data);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        List<String> s = new ArrayList<String>();
        for (BluetoothDevice bt : pairedDevices)
            s.add(bt.getAddress());

        final CharSequence[] fol_list = s.toArray(new CharSequence[s.size()]);
        if (fol_list.length == 0) {
            return new AlertDialog.Builder(getActivity())
                    // Set Dialog Title
                    .setTitle("Sin impresoras")
                    // Set Dialog Message
                    .setMessage("No hay impresoras configuradas en el dispositivo, por favor configure una.")
                    .setCancelable(false)
                    // Positive button
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentOpenBluetoothSettings = new Intent();
                            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(intentOpenBluetoothSettings);
                        }
                    }).create();
        } else {
            return new AlertDialog.Builder(getActivity())
                    // Set Dialog Title
                    .setTitle("Seleccione una impresora de la lista")
                    .setCancelable(false)
                    // Set Dialog Message
                    .setItems(fol_list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences prefs =
                                    PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = prefs.edit();
                            String impresora = fol_list[i].toString();
                            editor.putString("impresora", impresora);
                            editor.apply();
                            editor.commit();
                            Toast.makeText(getActivity(), impresora, Toast.LENGTH_SHORT).show();
                            BluetoothDevice device = mBluetoothAdapter
                                    .getRemoteDevice(impresora);
                            try {

                                device.createRfcommSocketToServiceRecord(MY_UUID);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            mService.connect(device);
                              try {

                                Thread.sleep(2000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                Toast.makeText(getActivity(),"Conexión Correcta!!", Toast.LENGTH_SHORT).show();
                                SendDataByte("Conexion correcta a: \n".getBytes("GBK"));
                                SendDataByte(impresora.getBytes("GBK"));
                                SendDataByte("\n\n\n".getBytes("GBK"));
                                mService.stop();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            dismiss();
                        }
                    })
                    .create();
        }
    }
}