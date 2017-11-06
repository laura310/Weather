package com.sjsu.cmpe277.weather.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.cmpe277.weather.DataModel.AppConstants;
import com.sjsu.cmpe277.weather.DataModel.City;
import com.sjsu.cmpe277.weather.DataModel.CityDB;
import com.sjsu.cmpe277.weather.DataModel.JsonParser;
import com.sjsu.cmpe277.weather.DataModel.JsonParserForecast;
import com.sjsu.cmpe277.weather.DataModel.TimeConverter;
import com.sjsu.cmpe277.weather.DataModel.URLConnector;
import com.sjsu.cmpe277.weather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by laurazhou on 10/30/17.
 */

public class CityViewActivity extends AppCompatActivity {
    ArrayAdapter listViewAdapter = null;
    ArrayAdapter gridViewForecastAdapter = null;
    List<String> todayForecastInfos = new ArrayList<>();
    List<String> forecastInfos = new ArrayList<>();

    TextView cityNameTxtView;
    TextView cityCurrentView;
    TextView curStatusTxtView;
    TextView curTempTxtView;
    TextView curDateTxtView;
    TextView curHighLowTxtView;

    GridView todayForecastGridView;
    GridView forecastGridView;

    int curHour;  // set in FetchCurWeatherTask, threfore FetchTodayForecastTask needs to be inside FetchCurWeatherTask

    private float x1,x2;
    static final int MIN_DISTANCE = 100;
    int position;
    List<String> cities;
    String currentCity;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityview);

        cityName = getIntent().getStringExtra(AppConstants.LIST_VIEW_CityName);
        currentCity = getIntent().getStringExtra(AppConstants.LIST_VIEW_CurrentCityName);
        position = getIntent().getIntExtra(AppConstants.LIST_VIEW_Position, 0);
        cities = getIntent().getStringArrayListExtra(AppConstants.LIST_VIEW_Array);

        cityNameTxtView = (TextView) findViewById(R.id.txtViewCityName);
        cityCurrentView = (TextView) findViewById(R.id.txtViewCurrent);
        curStatusTxtView = (TextView) findViewById(R.id.txtViewWeatherStatus);
        curTempTxtView = (TextView) findViewById(R.id.txtViewCurTemp);
        curDateTxtView = (TextView) findViewById(R.id.txtViewCurDate);
        curHighLowTxtView = (TextView) findViewById(R.id.txtViewCurHighLow);
        todayForecastGridView = (GridView) findViewById(R.id.gridViewTodayForecast);
        forecastGridView = (GridView) findViewById(R.id.gridView5dayForecast);

        cityNameTxtView.setText(cityName);
        Log.i("hhh", cityName);
        Log.i("hh", currentCity);

        if (cityName.equals(currentCity)) {
            cityCurrentView.setText("You are here");
        }
        Log.i("@@@@", "cityNameTxtView.setText(cityName): cityname: " + cityName);
        new FetchCurWeatherTask(cityName, this).execute();
//        new FetchTodayForecastTask(cityName, this).execute();
        new Fetch5DayForeCastTask(cityName, this).execute();

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private class FetchCurWeatherTask extends AsyncTask<String, Void, String> {
        private final Context context;
        String cityName;

        FetchCurWeatherTask(String cityName, Context context) {
            this.cityName = cityName;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            URLConnector weatherConn = new URLConnector(AppConstants.CUR_WEATHER_URL_BASE);
            String curWeatherInfo = weatherConn.getResponse(cityName);

            return curWeatherInfo;
        }


        @Override
        protected void onPostExecute(String curWeatherInfo) {
            try {
                JsonParser jsonParser = new JsonParser(curWeatherInfo, context);

                String curStatus = jsonParser.getCurStatus();
                curStatusTxtView.setText(curStatus);

                String curTemp = jsonParser.getTemp(AppConstants.CURRENT);
                curTempTxtView.setText(curTemp);

                City city = new CityDB(context).getCityData(cityName);
                String lat = city.getLat();
                String lon = city.getLon();

                String timeZoneURLParaPart = lat + "," + lon + "&timestamp=" + Calendar.getInstance().getTimeInMillis()/1000;
                new FetchTodayDateTask(timeZoneURLParaPart, context).execute();

                String curTempHighLow = jsonParser.getTemp(AppConstants.HIGH) + "  " + jsonParser.getTemp(AppConstants.LOW);
                curHighLowTxtView.setText(curTempHighLow);


                new FetchTodayForecastTask(cityName, context).execute();

            } catch (JSONException e) {
                Log.i("Exception", "Exception from onPostExecute(String curWeatherInfo). " + e);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if (deltaX > 0) {
                        position--;
                        //Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                    } else {
                        position++;
                       // Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                    }
                    toSwipe(position);
                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    private void toSwipe(int current){
        if (current < cities.size() && current >= 0) {
            cityName = cities.get(current);
            cityNameTxtView.setText(cityName);
            if (cityName.equals(currentCity)) {
                cityCurrentView.setText("You are here now!");
            } else {
                cityCurrentView.setText("");
            }
            Log.i("@@@@", "cityNameTxtView.setText(cityName): cityname: " + cities.get(current) + cities.size());
            new FetchCurWeatherTask(cityName, this).execute();
            new Fetch5DayForeCastTask(cityName, this).execute();
        }
    }

    private class Fetch5DayForeCastTask extends AsyncTask<String, Void, String> {
        String cityName;
        Context context;

        Fetch5DayForeCastTask(String cityName, Context context) {
            this.cityName = cityName;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            URLConnector weatherForecastConn = new URLConnector(AppConstants.FORECAST_WEATHER_URL_BASE);
            String forecastInfo = weatherForecastConn.getResponse(cityName);

            return forecastInfo;
        }

        @Override
        protected void onPostExecute(String forecastInfo) {
            try {
                JsonParserForecast jsonParserForecast = new JsonParserForecast(forecastInfo, context);
                forecastInfos = jsonParserForecast.getForecast5dayInfo();

                listViewAdapter = new ArrayAdapter<String> (
                        context,
                        android.R.layout.simple_list_item_1,
                        forecastInfos
                );

                forecastGridView.setAdapter(listViewAdapter);

            } catch (JSONException e) {
                Log.i("EXCEPTION", "Exception from Fetch5DayForeCastTask.onPostExecute.\n" + e);
            }

        }
    }

    private class FetchTodayForecastTask extends AsyncTask<String, Void, String> {
        String cityName;
        Context context;

        FetchTodayForecastTask(String cityName, Context context) {
            this.cityName = cityName;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            URLConnector weatherForecastConn = new URLConnector(AppConstants.FORECAST_WEATHER_URL_BASE);
            String forecastInfo = weatherForecastConn.getResponse(cityName);

            return forecastInfo;
        }

        @Override
        protected void onPostExecute(String forecastInfo) {
            try {
                JsonParserForecast jsonParserForecast = new JsonParserForecast(forecastInfo, context);
                Log.i("DEBUG", "curHour sent to getTodayForecastInfo is: " + curHour);
                todayForecastInfos = jsonParserForecast.getTodayForecastInfo(curHour);

                gridViewForecastAdapter = new ArrayAdapter<String> (
                    context,
                    android.R.layout.simple_list_item_1,
                    todayForecastInfos
                );

                todayForecastGridView.setAdapter(gridViewForecastAdapter);

            } catch (JSONException e) {
                Log.i("EXCEPTION", "Exception from FetchTodayForecastTask.onPostExecute.\n" + e);
            }
        }
    }

    private class FetchTodayDateTask extends AsyncTask<String, Void, String> {
        String timeZoneURLParaPart;
        Context context;

        FetchTodayDateTask(String timeZoneURLParaPart, Context context) {
            this.timeZoneURLParaPart = timeZoneURLParaPart;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = AppConstants.GOOGLE_TIMEZONE_API_URL_BASE1 + timeZoneURLParaPart + AppConstants.GOOGLE_TIMEZONE_API_URL_BASE2;
            Log.i("INFO", "calling google url: " + url + "\ntimeZoneURLParaPart: " + timeZoneURLParaPart);
            URLConnector weatherConn = new URLConnector(url);
            String timezoneId = "";

            try {
                JSONObject timezoneInfoObj = new JSONObject(weatherConn.getResponse(""));
                timezoneId = timezoneInfoObj.getString("timeZoneId");

            } catch (JSONException e) {
                Log.e("EXCEPTION", "Exception from FetchTodayDateTask.doInBackground: getting timezone when calling url: " + url + "\n"+ e);
            }

            return timezoneId;
        }

        @Override
        protected void onPostExecute(String timezoneId) {
            long tmpTimestamp = Calendar.getInstance().getTimeInMillis() / 1000;
            String todayDate = TimeConverter.getTimeInFormat(tmpTimestamp, timezoneId, "EEE, MMM d, y");
            curHour = Integer.valueOf(TimeConverter.getTimeInFormat(tmpTimestamp, timezoneId, "HH"));

            curDateTxtView.setText(todayDate);
        }
    }
}
