package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sjsu.cmpe277.weather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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


    public List<String> getTodayForecastInfo(int curHour) {
        List<String> infos = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for(int i = 0; i <= 7; i++) {
                int hour = (curHour + i * 3) % 24;
                String hourStr = "";
                if(hour == 12) {
                    hourStr = "12PM";
                } else {
                    hourStr = hour < 12 ? hour + "AM" : hour % 12 + "PM";
                }

                infos.add(hourStr);
            }

            for(int i = 0; i < 8; i++) {
                String status = jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                infos.add(status);
            }

            for(int i = 0; i < 8; i++) {
                long temp = jsonArray.getJSONObject(i).getJSONObject("main").getLong("temp");
                String tempStr = Util.convertTemperature(temp, preferences) + "°";
                infos.add(tempStr);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return infos;
    }

    public List<String> getForecast4dayInfo() throws JSONException {
        List<String> infos = new ArrayList<>();
        int cntPerDay = jsonObject.getInt("cnt") / 4;
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        Date tmrw = getDate(jsonArray.getJSONObject(0).getString("dt_txt"));
        Calendar c = Calendar.getInstance();
        c.setTime(tmrw);
        c.add(Calendar.DATE, -1);

        for(int i = 0; i < 4; i++) {
            int dayNoonIndex = i * cntPerDay + cntPerDay / 2;
            JSONObject dayNoonObj = jsonArray.getJSONObject(dayNoonIndex);
            JSONObject dayNoonWeatherObj = dayNoonObj.getJSONArray("weather").getJSONObject(0);

            c.add(Calendar.DATE, 1);
            String dateDay = new SimpleDateFormat("EEE").format(c.getTime());
            String status = dayNoonWeatherObj.getString("main");

            long dayHigh = getDayHigh(jsonArray, i, cntPerDay);
            long dayLow = getDayLow(jsonArray, i, cntPerDay);
            String high = Util.convertTemperature(dayHigh, preferences) + "°";
            String low = Util.convertTemperature(dayLow, preferences) + "°";

            infos.add(dateDay);
            infos.add(status);
            infos.add(high);
            infos.add(low);
        }

        Log.i("INFO", "infos: " + infos);
        return infos;
    }

    public long getDayHigh(JSONArray jsonArray, int day, int cntPerDay) {
        int dayHigh = Integer.MIN_VALUE;

        try {
            for(int i = day * cntPerDay; i < (day + 1) * cntPerDay; i++) {

                dayHigh = Math.max(dayHigh, (int)jsonArray.getJSONObject(i).getJSONObject("main").getLong("temp_max"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dayHigh;
    }

    public long getDayLow(JSONArray jsonArray, int day, int cntPerDay) {
        int dayLow = Integer.MAX_VALUE;

        try {
            for(int i = day * cntPerDay; i < (day + 1) * cntPerDay; i++) {

                dayLow = Math.min(dayLow, (int)jsonArray.getJSONObject(i).getJSONObject("main").getLong("temp"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dayLow;
    }

    private Date getDate(String dateStr) {
        Date date = new Date();

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
