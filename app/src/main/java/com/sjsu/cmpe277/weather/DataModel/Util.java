package com.sjsu.cmpe277.weather.DataModel;

import android.content.SharedPreferences;

/**
 * Created by Ran on 11/2/17.
 */

public class Util {
        public static float convertTemperature(float temperature, SharedPreferences sp) {
            if (sp.getString("unit", "°C").equals("°C")) {
                return Util.kelvinToCelsius(temperature);
            } else if (sp.getString("unit", "°C").equals("°F")) {
                return Util.kelvinToFahrenheit(temperature);
            } else {
                return temperature;
            }
        }

        public static float kelvinToCelsius(float kelvinTemp) {
            return kelvinTemp - 273.15f;
        }

        public static float kelvinToFahrenheit(float kelvinTemp) {
            return (((9 * kelvinToCelsius(kelvinTemp)) / 5) + 32);
        }
}
