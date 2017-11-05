package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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
                String status = jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                infos.add(status);
            }

            for(int i = 0; i < 8; i++) {
                long temp = jsonArray.getJSONObject(i).getJSONObject("main").getLong("temp");
                String tempStr = "" + Util.convertTemperature(temp, preferences);
                infos.add(tempStr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return infos;
    }

    // grid view
    public List<String> getForecast5dayInfo() throws JSONException {
        List<String> infos = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        for(int i = 1; -4 + i * 8 +1< jsonArray.length(); i++) {
            int dayNoonIndex = -4 + i * 8;
            JSONObject dayNoonObj = jsonArray.getJSONObject(dayNoonIndex);
            JSONObject dayNoonWeatherObj = dayNoonObj.getJSONArray("weather").getJSONObject(0);
            JSONObject dayNoonMainObj = dayNoonObj.getJSONObject("main");
            Log.i("&&&&", dayNoonMainObj.toString());
            String dateDay = getDateDay(dayNoonObj.getString("dt_txt"));

            String status = dayNoonWeatherObj.getString("main");

            long dayHigh = Math.max(dayNoonMainObj.getLong("temp_min"),
                                    jsonArray.getJSONObject(dayNoonIndex + 1).getJSONObject("main").getLong("temp_min"));
            long dayLow = Math.min(jsonArray.getJSONObject(dayNoonIndex - 4).getJSONObject("main").getLong("temp_min"),
                                   jsonArray.getJSONObject(dayNoonIndex + 4).getJSONObject("main").getLong("temp_min"));
//            long dayHigh = getDayHigh();
//            long dayLow = getDayLow();

            String high = Util.convertTemperature(dayHigh, preferences) + "";
            String low = Util.convertTemperature(dayLow, preferences) + "";

            infos.add(dateDay);
            infos.add(status);
            infos.add(high);
            infos.add(low);
        }
        Log.i("INFO", "infos: " + infos);
        return infos;
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
