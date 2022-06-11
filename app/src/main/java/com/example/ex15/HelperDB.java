package com.example.ex15;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbexam.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;

    public HelperDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate="CREATE TABLE "+Worker1.WORKERS_TABLE;
        strCreate+=" ("+Worker1.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Worker1.FIRST_NAME+" TEXT,";
        strCreate+=" "+Worker1.LAST_NAME+" TEXT,";
        strCreate+=" "+Worker1.COMPANY_NAME+" TEXT,";
        strCreate+=" "+Worker1.WORKER_ID+" TEXT,";
        strCreate+=" "+Worker1.PHONE_NUMBER+" TEXT,";
        strCreate+=" "+Worker1.ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+Restaurant1.TABLE_RESTAURANT;
        strCreate+=" ("+Restaurant1.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Restaurant1.NAME+" TEXT,";
        strCreate+=" "+Restaurant1.MAIN_PHONE+" TEXT,";
        strCreate+=" "+Restaurant1.SECONDARY_PHONE+" TEXT,";
        strCreate+=" "+Restaurant1.ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);


        strCreate="CREATE TABLE "+Order1.TABLE_ORDERS;
        strCreate+=" ("+Order1.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Order1.RESTAURANT_ID+" TEXT,";
        strCreate+=" "+Order1.USER_ID+" TEXT,";
        strCreate+=" "+Order1.DATE+" TEXT";
        strCreate+=" "+Order1.TIME+" TEXT,";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+Meal1.MEALS_TABLE;
        strCreate+=" ("+Meal1.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Meal1.FIRST_MEAL+" TEXT,";
        strCreate+=" "+Meal1.MAIN_MEAL+" TEXT,";
        strCreate+=" "+Meal1.EXTRA+" TEXT,";
        strCreate+=" "+Meal1.DESSERT+" TEXT,";
        strCreate+=" "+Meal1.DRINK+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        strDelete="DROP TABLE IF EXISTS "+Worker1.WORKERS_TABLE;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+Restaurant1.TABLE_RESTAURANT;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+Meal1.MEALS_TABLE;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+Order1.TABLE_ORDERS;
        db.execSQL(strDelete);

        onCreate(db);
    }
}
