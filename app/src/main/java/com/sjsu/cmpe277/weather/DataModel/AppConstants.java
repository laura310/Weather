package com.sjsu.cmpe277.weather.DataModel;

/**
 * Created by laurazhou on 10/30/17.
 */

import java.security.PublicKey;

/**
 * For reference:
 * https://openweathermap.org/current
 */

public class AppConstants {
    public static final String CUR_DT = "dt";

    public static final String CUR_WEATHER_URL_BASE = "http://api.openweathermap.org/data/2.5/weather?APPID=b4631e5c54e1a3a9fdda89fca90d4114&q=";
    public static final String FORECAST_WEATHER_URL_BASE = "http://api.openweathermap.org/data/2.5/forecast?APPID=b4631e5c54e1a3a9fdda89fca90d4114&q=";
    public static final String GOOGLE_TIMEZONE_API_URL_BASE1 = "https://maps.googleapis.com/maps/api/timezone/json?location=";
    //"38.908133,-77.047119&timestamp=1458000000"
    public static final String GOOGLE_TIMEZONE_API_URL_BASE2 = "&key=AIzaSyCABeDp8t8r7RkOm_S2T-oOtHwziv1ZxIQ";

    //For MainActivity and CityViewActivity
    public static final String LIST_VIEW_CityName = "CityName";
    public static final String LIST_VIEW_Position = "Position";
    public static final String LIST_VIEW_CurrentCityName = "CurrentCityName";
    public static final String LIST_VIEW_Array = "Arrays";

    // for current weather api
    public static final String CUR_MAIN = "main";
    public static final String CUR_MAIN_TEMP = "temp";
    public static final String CUR_MAIN_LOW = "temp_max";
    public static final String CUR_MAIN_High = "temp_min";
    public static final String CURRENT = "C";
    public static final String HIGH = "H";
    public static final String LOW = "L";
    public static final String CUR_COORD = "coord";
}
