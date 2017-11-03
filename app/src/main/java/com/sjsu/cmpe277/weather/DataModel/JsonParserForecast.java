package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by laurazhou on 11/2/17.
 */

public class JsonParserForecast {

    JSONObject jsonObject;
    Context context;
    SharedPreferences preferences;

    public JsonParserForecast(String jsonText, Context context) throws JSONException {
        this.jsonObject = new JSONObject(jsonText);
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public List<String> getTodayForecastInfo() {
        List<String> infos = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            int curHour = 3; // NEEDS TO BE CHANGED!!!!!

            for(int i = 0; i <= 7; i++) {
                infos.add(curHour + i * 3 + "");
            }

            for(int i = 0; i < 8; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String status = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                infos.add(status);
            }

            for(int i = 0; i < 8; i++) {
                long temp = jsonObject.getJSONObject("main").getLong("temp");
//                String tempStr = "" + Util.convertTemperature(temp, preferences);
//                infos.add(tempStr);
                infos.add("temp");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return infos;
    }

    // list view
//    public List<String> getForecast5dayInfo() throws JSONException {
//        List<String> infos = new ArrayList<>();
//        JSONArray jsonArray = jsonObject.getJSONArray("list");
//
//        //for day 0
//        JSONObject day0obj = jsonArray.getJSONObject(4);
//        JSONObject day0WeatherObj = day0obj.getJSONArray("weather").getJSONObject(0);
//        JSONObject day0MainObj = day0obj.getJSONObject("main");
//
//        String dateDay0 = getDateDay(day0obj.getString("dt_txt"));
//        String status0 = day0WeatherObj.getString("main");
//        String high0 = day0MainObj.getString("temp_max");
//        String low0 = day0MainObj.getString("temp_min");
//
//        infos.add(formatList(dateDay0, status0, high0, low0));
//
//        //for remaining days
//        for(int i = 1; 4 + i * 8 < jsonArray.length(); i++) {
//            JSONObject dayObj = jsonArray.getJSONObject(4 + i * 8);
//            JSONObject dayWeatherObj = dayObj.getJSONArray("weather").getJSONObject(0);
//            JSONObject dayMainObj = dayObj.getJSONObject("main");
//            String dateDay = getDateDay(dayObj.getString("dt_txt"));
//
//            String status = dayWeatherObj.getString("main");
//            String high = dayMainObj.getString("temp_max");
//            String low = dayMainObj.getString("temp_min");
//
//            infos.add(formatList(dateDay, status, high, low));
//        }
//
//        return infos;
//    }

    // grid view
    public List<String> getForecast5dayInfo() throws JSONException {
        List<String> infos = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        //for day 0
        JSONObject day0obj = jsonArray.getJSONObject(4);
        JSONObject day0WeatherObj = day0obj.getJSONArray("weather").getJSONObject(0);
        JSONObject day0MainObj = day0obj.getJSONObject("main");

        String dateDay0 = getDateDay(day0obj.getString("dt_txt"));
        String status0 = day0WeatherObj.getString("main");
        String high0 = day0MainObj.getString("temp_max");
        String low0 = day0MainObj.getString("temp_min");

        infos.add(dateDay0);
        infos.add(status0);
        infos.add(high0);
        infos.add(low0);

        //for remaining days
        for(int i = 1; 4 + i * 8 < jsonArray.length(); i++) {
            JSONObject dayObj = jsonArray.getJSONObject(4 + i * 8);
            JSONObject dayWeatherObj = dayObj.getJSONArray("weather").getJSONObject(0);
            JSONObject dayMainObj = dayObj.getJSONObject("main");
            String dateDay = getDateDay(dayObj.getString("dt_txt"));

            String status = dayWeatherObj.getString("main");
            String high = dayMainObj.getString("temp_max");
            String low = dayMainObj.getString("temp_min");

            infos.add(dateDay);
            infos.add(status);
            infos.add(high);
            infos.add(low);
        }

        return infos;
    }

    private String formatList(String dateDay, String status, String high, String low) {
        return dateDay + "      " + status + "      " + high + "        " + low;
    }

    private String getDateDay(String dateStr) {
        String dateDay = "";

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(dateStr);
            format = new SimpleDateFormat("EEE");
            dateDay = format.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateDay;
    }
}
