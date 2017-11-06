package com.sjsu.cmpe277.weather.DataModel;

import android.content.SharedPreferences;

import com.sjsu.cmpe277.weather.R;

/**
 * Created by Ran on 11/2/17.
 */

public class Util {
        public static int convertTemperature(long temperature, SharedPreferences sp) {
            if (sp.getString("tempUnit", "C").equals("C")) {
                return (int) Util.kelvinToCelsius(temperature);
            } else if (sp.getString("tempUnit", "C").equals("F")) {
                return (int) Util.kelvinToFahrenheit(temperature);
            } else {
                return (int) temperature;
            }
        }

        public static int kelvinToCelsius(long kelvinTemp) {
            return (int) (kelvinTemp - 273.15);
        }

        public static int kelvinToFahrenheit(long kelvinTemp) {
            return (((9 * kelvinToCelsius(kelvinTemp)) / 5) + 32);
        }
}
