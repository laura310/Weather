package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by laurazhou on 10/30/17.
 */

public class JsonParser {
    JSONObject jsonObject;
    Context context;

    public JsonParser(String jsonText, Context context) throws JSONException {
        this.jsonObject = new JSONObject(jsonText);
        this.context = context;
    }

    /**
     * Get today's temperature. Including:
     * current temperature (AppConstants.CURRENT)
     * today's lowest temperature (AppConstants.LOW)
     * today's highest temperature (AppConstants.HIGH)
     * @param level
     * @return
     * @throws JSONException
     */
    public String getTemp(String level) throws JSONException {
        String temp = "";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        JSONObject mainObject = jsonObject.getJSONObject(AppConstants.CUR_MAIN);
        long curTempK = mainObject.getLong(AppConstants.CUR_MAIN_TEMP);
        long lowTempK = mainObject.getLong(AppConstants.CUR_MAIN_LOW);
        long highTempK = mainObject.getLong(AppConstants.CUR_MAIN_High);

        switch (level) {
            case AppConstants.CURRENT:
                temp =  "" + Util.convertTemperature(curTempK, preferences);
                break;

            case AppConstants.LOW:
                temp =  "" + Util.convertTemperature(lowTempK, preferences);
                break;

            case AppConstants.HIGH:
                temp = "" + Util.convertTemperature(highTempK, preferences);
                break;

            default:
                break;
        }

        return temp;
    }

    public String getCurStatus() throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        return jsonArray.getJSONObject(0).getString("main");
    }

//    /**
//     * example: return the middle part of this url
//     * //https://maps.googleapis.com/maps/api/timezone/json?location=
//     * //35,139&timestamp=1369824698
//     * //&key=AIzaSyCABeDp8t8r7RkOm_S2T-oOtHwziv1ZxIQ
//     *
//     * @return
//     * @throws JSONException
//     */
//    public String getTimeZoneURLParaPart() throws JSONException {
//        JSONObject coordObject = jsonObject.getJSONObject(AppConstants.CUR_COORD);
//        String lon = coordObject.getString("lon");
//        String lat = coordObject.getString("lat");
//        String latLonPair = lat + "," + lon;
//
//        String timeZoneURLParaPart = latLonPair + "&timestamp=" + getTimeStamp();
//        return timeZoneURLParaPart;
//    }

    public String getTimeStamp() throws JSONException {
        return "" +  jsonObject.getLong(AppConstants.CUR_DT);
    }


//    public String getTodayDate() throws JSONException {
//        long todayEpoch = jsonObject.getLong(AppConstants.CUR_DT);
//
//        JSONObject coordObject = jsonObject.getJSONObject(AppConstants.CUR_COORD);
//        String lon = coordObject.getString("lon");
//        String lat = coordObject.getString("lat");
//        String latLonPair = lat + "," + lon;
//        String timestamp = "" + todayEpoch;
//
//        String google_url = AppConstants.GOOGLE_TIMEZONE_API_URL_BASE1 + latLonPair + AppConstants.GOOGLE_TIMEZONE_API_URL_BASE2
//                            + timestamp + AppConstants.GOOGLE_TIMEZONE_API_URL_BASE2;
//        Log.i("INFO", google_url);
//        URLConnector timezoneConn = new URLConnector(google_url);
//        JSONObject jsonObj = new JSONObject(timezoneConn.getResponse(""));
//        String timeZoneId = jsonObj.getString("timeZoneId");
//
//        Date date = new Date(todayEpoch * 1000L);
//        DateFormat format = new SimpleDateFormat("EEE, MMM d, HH-MM, y");
////        format.setTimeZone(TimeZone.getTimeZone(timeZoneId));
//        format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
//
//
//        String todayDate = format.format(date);
//
//        return todayDate;
//
////        return timestamp;
//    }
}
