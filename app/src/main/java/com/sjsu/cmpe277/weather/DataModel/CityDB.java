package com.sjsu.cmpe277.weather.DataModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ran on 10/31/17.
 */

public class CityDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "city.db";
   // public static final String CITY_TABLE_ID = "ID";
    public static final String CITY_TABLE_NAME = "CityNames";

    public static final String CITY_COLUMN_NAME = "name";
    public static final String CITY_COLUMN_Lat = "lat";
    public static final String CITY_COLUMN_Lon = "lon";

    public CityDB(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String createTable = "CREATE TABLE " + CITY_TABLE_NAME + " ( " + CITY_COLUMN_NAME + " text primary key, "+ CITY_COLUMN_Lat +" text, " + CITY_COLUMN_Lon + " text )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String dropTable = "DROP TABLE IF EXISTS " + CITY_TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }

    public boolean insertCity (String name, String lat, String lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_COLUMN_NAME, name);
        contentValues.put(CITY_COLUMN_Lat, lat);
        contentValues.put(CITY_COLUMN_Lon, lon);
        db.insert(CITY_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectNameFromTable = "select * from " +  CITY_TABLE_NAME+ " where " + CITY_COLUMN_NAME +"="+name;
        Cursor res =  db.rawQuery(selectNameFromTable, null );
        return res;
    }
    public String getLat(String name) {
        Cursor res = getData(name);
        return res.getString(res.getColumnIndex(CITY_COLUMN_Lat));
    }
    public String getLon(String name) {
        Cursor res = getData(name);
        return res.getString(res.getColumnIndex(CITY_COLUMN_Lon));
    }
    public City getCityData(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        City city = null;
        String selectNameFromTable = "select * from " +  CITY_TABLE_NAME+ " where " + CITY_COLUMN_NAME +"="+name;
        Cursor res =  db.rawQuery(selectNameFromTable, null );
        res.moveToFirst();
        city = new City(res.getString(res.getColumnIndex(CITY_COLUMN_NAME)), res.getString(res.getColumnIndex(CITY_COLUMN_Lat)),res.getString(res.getColumnIndex(CITY_COLUMN_Lon)));
        return city;
    }

    public Integer deleteCity (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CITY_TABLE_NAME,
                CITY_COLUMN_NAME + " = ? ",
                new String[] { name });
    }

    public List<String> getAllCities() {
        List<String> set = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectAllFromTable = "select * from " + CITY_TABLE_NAME;
        Cursor res =  db.rawQuery(selectAllFromTable, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            set.add(res.getString(res.getColumnIndex(CITY_COLUMN_NAME)));
            res.moveToNext();
        }
        return set;
    }
    public List<City> getAllCityObject() {
        List<City> ret = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectAllFromTable = "select * from " + CITY_TABLE_NAME;
        Cursor res =  db.rawQuery(selectAllFromTable, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            City city = new City(res.getString(res.getColumnIndex(CITY_COLUMN_NAME)), res.getString(res.getColumnIndex(CITY_COLUMN_Lat)),res.getString(res.getColumnIndex(CITY_COLUMN_Lon)));
            ret.add(city);
            res.moveToNext();
        }
        return ret;
    }
}
