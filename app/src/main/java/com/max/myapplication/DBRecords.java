package com.max.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Max on 5/2/2016.
 */
public class DBRecords {

    // Данные базы данных и таблиц
    private static final String DATABASE_NAME = "puzzle.db";
    private static final int DATABASE_VERSION = 1;
    private static final String[] TABLE_NAME = {"Records0", "Records1", "Records2", "Records3"};
    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_TIME = "Time";
    // Номера столбцов private
    //static final int NUM_COLUMN_ID = 0;
    //private static final int NUM_COLUMN_NAME = 1;
    //private static final int NUM_COLUMN_TIME = 2;






    private SQLiteDatabase mDataBase;
    public DBRecords(Context context) {
        DBManager mOpenHelper = new DBManager(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    /**
    Adds only if time is better then 50th time
     */
    public long addRecord(String name, long milliseconds, int mode){
        Cursor mCursor = mDataBase.query(TABLE_NAME[mode], new String[]{COLUMN_TIME},
                null, null, null, null, COLUMN_TIME + " DESC", "" + 1);


        mCursor.moveToFirst();

        ArrayList<Long> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long time = mCursor.getLong(0);


                arr.add(time);
            } while (mCursor.moveToNext());
        }
        long time = (arr.size() >=50)?arr.get(0):Long.MAX_VALUE;
        if(milliseconds < time) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, name);
            cv.put(COLUMN_TIME, milliseconds);
            return mDataBase.insert(TABLE_NAME[mode], null, cv);
        }
        return -1;
    }

    public void deleteAll(int mode) {
        mDataBase.delete(TABLE_NAME[mode], null, null);
    }

    public ArrayList<Pair<String, Long>> selectAll(int mode, int length) {
        Cursor mCursor = mDataBase.query(TABLE_NAME[mode], new String[]{COLUMN_TIME,COLUMN_NAME},
                null, null, null, null, COLUMN_TIME);

        ArrayList<Pair<String, Long>> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long time = mCursor.getLong(0);
                String name = mCursor.getString(1);

                arr.add(new Pair<String, Long>(name,time));
            } while (mCursor.moveToNext());
        }
        return arr;
    }


    private class DBManager extends SQLiteOpenHelper {
        DBManager(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for(String tb : TABLE_NAME) {
                String query = "CREATE TABLE " + tb +
                        " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_TIME + " LONG, "
                        + COLUMN_NAME + " TEXT); ";
                db.execSQL(query);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
