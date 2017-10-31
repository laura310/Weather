package com.sjsu.cmpe277.weather;

import com.sjsu.cmpe277.weather.DataModel.OpenWeatherMapConnector;
import org.junit.Test;

/**
 * Created by laurazhou on 10/30/17.
 */

public class TestOpenWeatherMapConnector {
    OpenWeatherMapConnector connector = new OpenWeatherMapConnector();

    @Test
    public void testGetCurWeatByCityName() {
        connector.getCurWeatByCityName("London");

    }
}
