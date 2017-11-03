package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laurazhou on 11/2/17.
 */

public class JsonParserForecast {

    JSONObject jsonObject;
    Context context;

    public JsonParserForecast(String jsonText, Context context) throws JSONException {
        this.jsonObject = new JSONObject(jsonText);
        this.context = context;
    }

    public String[][] getForecast5dayInfo() throws JSONException {
        String[][] info = new String[5][2];
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        // for day 0
        String[] day1 = new String[2];
        JSONObject day1Obj = jsonArray.getJSONObject(4);

        JSONObject day1WeatherObj = day1Obj.getJSONArray("weather").getJSONObject(0);
        String status = day1WeatherObj.getString("main");
        day1[0] = status;

        JSONObject day1MainObj = day1Obj.getJSONObject("main");
        long temp_max = day1MainObj.getLong("temp_max");
        long temp_min = day1MainObj.getLong("temp_min");
        day1[1] = temp_max + "  " + temp_min;

        info[0] = day1;


        // for day 1 - 4
        for(int i = 1; i < 5; i++) {
            String[] day = new String[2];
            JSONObject dayObj = jsonArray.getJSONObject(4 + i * 8);

            JSONObject dayWeatherObj = dayObj.getJSONArray("weather").getJSONObject(0);
            String dayStatus = dayWeatherObj.getString("main");
            day[0] = dayStatus;

            JSONObject dayMainObj = dayObj.getJSONObject("main");
            long day_temp_max = dayMainObj.getLong("temp_max");
            long day_temp_min = dayMainObj.getLong("temp_min");
            day[1] = temp_max + "  " + temp_min;

            info[i] = day;
        }
        return info;
    }

}
