package com.sjsu.cmpe277.weather;

import com.sjsu.cmpe277.weather.DataModel.JsonParser;
import com.sjsu.cmpe277.weather.DataModel.OpenWeatherMapConnector;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by laurazhou on 10/30/17.
 */

public class TestJsonParser {
    OpenWeatherMapConnector openWeatherMapConnector = new OpenWeatherMapConnector();
    JSONObject jsonObject = openWeatherMapConnector.getCurWeatByCityName("London");

    JsonParser jsonParser = new JsonParser(jsonObject);

    @Test
    public void testParse() {

    }
}
