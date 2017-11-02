package com.sjsu.cmpe277.weather.DataModel;

/**
 * Created by laurazhou on 10/30/17.
 */

/**
 * For reference:
 * https://openweathermap.org/current
 */

public class JsonConstants {
    public static final String COORD= "coord";
    public static final String WEATHER = "weather";
    public static final String DT = "dt";

    // for current weather api
    public static final String CUR_MAIN = "main";
    public static final String CUR_MAIN_TEMP = "temp";
    public static final String CUR_MAIN_LOW = "temp_max";
    public static final String CUR_MAIN_High = "temp_min";
    public static final String CURRENT = "C";
    public static final String HIGH = "H";
    public static final String LOW = "L";
}
