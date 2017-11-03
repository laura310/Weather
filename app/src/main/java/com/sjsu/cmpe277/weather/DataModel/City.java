package com.sjsu.cmpe277.weather.DataModel;

/**
 * Created by Ran on 11/2/17.
 */

public class City {
    String Name;
    String lat;
    String lon;
    public City(String Name, String lat, String lon) {
        this.Name = Name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return Name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
