package com.sjsu.cmpe277.weather.DataModel;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laurazhou on 10/30/17.
 */

public class JsonParser {
    final static Logger logger = Logger.getLogger(JsonParser.class);

    JSONObject jsonObject;

    public JsonParser(JSONObject jsonObject) {
        this.jsonObject = new JSONObject();
    }

    public String getCityName() {
        String cityName = "";

        try {
            cityName = (String) jsonObject.get(JsonConstants.NAME);

        } catch (JSONException e) {
            logger.error("Exception from getting cityName", e);
        }

        return cityName;
    }

//    public int getHighest() {
//
//    }

//    public int getLowest() {
//
//    }
}
