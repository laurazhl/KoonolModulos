package com.lzacatzontetlh.koonolmodulos.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BDKoonol.db";
    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }
    //Creamos la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
