package com.example.prog_movil_final.Clases;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    //Chequear si el dispositivo esta conectado a Internet.
    public static boolean isConnected(ConnectivityManager cmg) {
        NetworkInfo networkInfo = cmg.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}