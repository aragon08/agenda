package com.example.orion.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Orion on 15/04/2017.
 */

public class DBEvent extends SQLiteOpenHelper {

    String SQL="create table event(idevent INTEGER PRIMARY KEY autoincrement, event varchar(80),dateEvent TEXT, timeEvent TEXT, descrip varchar(113),contact varchar(30),phone char(15))";

    public DBEvent(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
