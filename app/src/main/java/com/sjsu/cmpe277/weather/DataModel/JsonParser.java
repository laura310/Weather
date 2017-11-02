package com.sjsu.cmpe277.weather.DataModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sjsu.cmpe277.weather.Controller.MainActivity;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by laurazhou on 10/30/17.
 */

public class JsonParser {
    final static Logger logger = Logger.getLogger(JsonParser.class);

    JSONObject jsonObject;
    Context context;

    public JsonParser(String jsonText, Context context) throws JSONException {
        this.jsonObject = new JSONObject(jsonText);
        this.context = context;
    }

    public String getTemp(String level) throws JSONException {
        String temp = "";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        JSONObject mainObject = jsonObject.getJSONObject(JsonConstants.CUR_MAIN);
        long curTempK = mainObject.getLong(JsonConstants.CUR_MAIN_TEMP);
        long lowTempK = mainObject.getLong(JsonConstants.CUR_MAIN_LOW);
        long highTempK = mainObject.getLong(JsonConstants.CUR_MAIN_High);

        switch (level) {
            case JsonConstants.CURRENT:
//                temp =  "" + Util.convertTemperature(curTempK, preferences);
                temp = "" + (int)(curTempK - 273.15);
                break;

            case JsonConstants.LOW:
//                temp =  "" + Util.convertTemperature(lowTempK, preferences);
                temp = "" + (int)(lowTempK - 273.15);
                break;

            case JsonConstants.HIGH:
//                temp = "" + Util.convertTemperature(highTempK, preferences);
                temp = "" + (int)(highTempK - 273.15);
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

    public long getCurDateEpoch() throws JSONException {
        return jsonObject.getLong("dt");
    }

}
