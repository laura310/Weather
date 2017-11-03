package com.sjsu.cmpe277.weather;

import com.sjsu.cmpe277.weather.DataModel.URLConnector;
import org.junit.Test;

/**
 * Created by laurazhou on 10/30/17.
 */

public class TestOpenWeatherMapConnector {
    URLConnector connector = new URLConnector();

    @Test
    public void testGetCurWeatByCityName() {
        connector.getResponse("London");

    }
}
