package com.example.prog_movil_final.Clases;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.prog_movil_final.Activities.HomeActivity;
import com.example.prog_movil_final.R;

import java.io.File;

public class Utils {

    //Chequear si el dispositivo esta conectado a Internet.
    public static boolean isConnected(ConnectivityManager cmg) {
        NetworkInfo networkInfo = cmg.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //Existencia de la base de datos
    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public static void loadDatabase(Context context){
        DBHandler db = new DBHandler(context);

//        Log.e("DDEBUG_CREACION","");
        //Piso 0 -> Planta Baja
        db.insertAula("Aula 1",0, R.drawable.aula1_pb);
        db.insertAula("Aula 2",0,R.drawable.aula2_pb);
        db.insertAula("Aula 3",0,R.drawable.aula3_pb);
        db.insertAula("Aula 4",0,R.drawable.aula4_pb);
        db.insertAula("Aula 5",0,R.drawable.aula5_pb);

        //Piso 5 -> Laboratorio Hidráulica
        db.insertAula("Aula 6",5,R.drawable.nave_aula6);
        db.insertAula("Aula 7",5,R.drawable.nave_aula7);

        db.insertAula("Aula 8",0,R.drawable.aula8_pb);

        //Piso 3 -> Tercer piso
        db.insertAula("Aula 9",3,R.drawable.p3_aula9);
        db.insertAula("Aula 10",3,R.drawable.p3_aula10);

        db.insertAula("Aula Magna",0,R.drawable.aulamagna_pb);

        //Piso 1 -> Primer piso
        db.insertAula("Laboratorio de Informática 1",1,R.drawable.lab1_p1);
        db.insertAula("Laboratorio de Informática 2",1,R.drawable.lab2_p1);
        db.insertAula("Laboratorio de Informática 3",1,R.drawable.lab3_p1);
        db.insertAula("Laboratorio de Informática 4",1,R.drawable.lab4_p1);

        //Piso 5 -> Laboratorio Hidráulica

        db.insertAula("Laboratorio de Informática 5",5,R.drawable.nave_lab5);

        db.insertAula("Laboratorio de Electrónica",1,R.drawable.elect_p1);
        db.insertAula("Aula FICH-CIMNE",1,R.drawable.cimne_p1);

        //Piso 2 -> Segundo piso
        db.insertAula("Aula de Dibujo",2,R.drawable.p2_dibujo);
        db.insertAula("Laboratorio de Química y Ambiente",2,R.drawable.p2_labqcaamb);


    }


}