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
import com.sjsu.cmpe277.weather.DataModel.URLConnector;
import com.sjsu.cmpe277.weather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    TextView curStatusTxtView;
    TextView curTempTxtView;
    TextView curDateTxtView;
    TextView curHighLowTxtView;

    GridView todayForecastGridView;
    GridView forecastGridView;


    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    int position;
    List<String> cities;
    String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityview);

        String cityName = getIntent().getStringExtra(AppConstants.LIST_VIEW_CityName);
        currentCity = getIntent().getStringExtra(AppConstants.LIST_VIEW_CurrentCityName);
        position = getIntent().getIntExtra(AppConstants.LIST_VIEW_Position, 0);
        cities = getIntent().getStringArrayListExtra(AppConstants.LIST_VIEW_Array);

        cityNameTxtView = (TextView) findViewById(R.id.txtViewCityName);
        curStatusTxtView = (TextView) findViewById(R.id.txtViewWeatherStatus);
        curTempTxtView = (TextView) findViewById(R.id.txtViewCurTemp);
        curDateTxtView = (TextView) findViewById(R.id.txtViewCurDate);
        curHighLowTxtView = (TextView) findViewById(R.id.txtViewCurHighLow);
        todayForecastGridView = (GridView) findViewById(R.id.gridViewTodayForecast);
        forecastGridView = (GridView) findViewById(R.id.gridView5dayForecast);

        cityNameTxtView.setText(cityName);
        Log.i("@@@@", "cityNameTxtView.setText(cityName): cityname: " + cityName);
        new FetchCurWeatherTask(cityName, this).execute();
        new FetchTodayForecastTask(cityName, this).execute();
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

                // "38.908133,-77.047119&timestamp=1458000000"
                // TODO Directly get lat and lon after DB modification
                String lat = "";
                String lon = "";
                CityDB cityDB = new CityDB(context);
                List<City> cities = cityDB.getAllCityObject();
                for(City city : cities) {
                    if(city.getName().equals(cityName)) {
                        lat = city.getLat();
                        lon = city.getLon();
                        break;
                    }
                }

                String timeZoneURLParaPart = lat + "," + lon + "&timestamp=" + jsonParser.getTimeStamp();
                new FetchTodayDateTask(timeZoneURLParaPart, jsonParser.getTimeStampLong(), context).execute();

                String curTempHighLow = jsonParser.getTemp(AppConstants.HIGH) + "  " + jsonParser.getTemp(AppConstants.LOW);
                curHighLowTxtView.setText(curTempHighLow);

            } catch (JSONException e) {
                Log.i("Exception", "Exception from onPostExecute(String curWeatherInfo). " + e);
            }

        }

        private String getDateTime(long epochTime) {
            Date date = new Date(epochTime * 1000);
            DateFormat format = new SimpleDateFormat("EEE, MMM d");
            return format.format(date);
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
                        toSwipe(position - 1);
                        Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                    } else {
                        toSwipe(position + 1);
                        Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                    }
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
            Intent intent = new Intent(CityViewActivity.this, CityViewActivity.class);
            intent.putExtra(AppConstants.LIST_VIEW_CityName, cities.get(current));
            intent.putExtra(AppConstants.LIST_VIEW_Position, current);
            intent.putStringArrayListExtra(AppConstants.LIST_VIEW_Array, (ArrayList<String>) cities);
            intent.putExtra(AppConstants.LIST_VIEW_CurrentCityName, currentCity);
            startActivity(intent);
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
                todayForecastInfos = jsonParserForecast.getTodayForecastInfo();

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
        long timestamp;
        Context context;

        FetchTodayDateTask(String timeZoneURLParaPart, Long timestamp, Context context) {
            this.timeZoneURLParaPart = timeZoneURLParaPart;
            this.timestamp = timestamp;
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
            Date date = new Date(timestamp * 1000L);
            DateFormat format = new SimpleDateFormat("EEE, MMM d, HH:MM, y");
            format.setTimeZone(TimeZone.getTimeZone(timezoneId));
            String todayDate = format.format(date);

            curDateTxtView.setText(todayDate);
        }
    }
}
