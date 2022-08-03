package com.example.prog_movil_final.Clases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "BedeliaDB";
    private static final String TABLE_Aulas = "Aulas";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PISO = "piso";

    private static final String KEY_MAP = "plano";

    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Aulas + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
                + KEY_PISO + " INTEGER," + KEY_MAP + " INTEGER)";
        db.execSQL(CREATE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Dropea la tabla vieja si existe
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Aulas);
        // Crea de nuevo la tabla
        onCreate(db);
    }

    //Agregar un nuevo piso
    public void insertAula(String name, Integer piso, Integer map){
        //Abrir la bd en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();
        //Creo el content value para guardar los datos luego
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, name);
        cValues.put(KEY_PISO, piso);
        cValues.put(KEY_MAP, map);
        //Insertar el nuevo valor
        db.insert(TABLE_Aulas,null, cValues);
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<String> getAulas(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> aulasList = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_Aulas, null, null, null, null, null, null, null);
        if (cursor.moveToNext()){

            do {
                aulasList.add(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            } while(cursor.moveToNext());

        }
        return  aulasList;
    }

    @SuppressLint("Range")
    public String getPiso(String aula){
        SQLiteDatabase db = this.getWritableDatabase();
        String piso = "";
        Cursor cursor = db.query(TABLE_Aulas, new String[]{KEY_PISO}, KEY_NAME+"=?"
                , new String[]{String.valueOf(aula)}, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
             piso = cursor.getString(cursor.getColumnIndex(KEY_PISO));
        }
        if(piso.equals("0")) { return "PLANTA BAJA";}
        if(piso.equals("1") || piso.equals("2") || piso.equals("3")) { return "PISO " + piso; }
        if(piso.equals("5")) { return "NAVE"; }
        return "";

    }

    @SuppressLint("Range")
    public String getPlano(String aula){

        SQLiteDatabase db = this.getWritableDatabase();
        String plano = "";
        Cursor cursor = db.query(TABLE_Aulas, new String[]{KEY_MAP}, KEY_NAME+"=?"
                    , new String[]{String.valueOf(aula)}, null, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            plano = cursor.getString(cursor.getColumnIndex(KEY_MAP));
        }

        return plano;

    }

    public void clearAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Aulas,null,null);
    }



}
