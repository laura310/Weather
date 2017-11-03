package com.sjsu.cmpe277.weather.Controller;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.cmpe277.weather.DataModel.AppConstants;
import com.sjsu.cmpe277.weather.DataModel.CityDB;
import com.sjsu.cmpe277.weather.DataModel.JsonParser;
import com.sjsu.cmpe277.weather.DataModel.JsonParserForecast;
import com.sjsu.cmpe277.weather.DataModel.URLConnector;
import com.sjsu.cmpe277.weather.R;

import org.apache.log4j.Logger;
import org.json.JSONException;

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
    Logger logger = Logger.getLogger(CityViewActivity.class);

//    ListView day5Forcast;

    TextView cityNameTxtView;
    TextView curStatusTxtView;
    TextView curTempTxtView;
    TextView curDateTxtView;
    TextView curHighLowTxtView;


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
        cityNameTxtView.setText(cityName);

        curTempTxtView = (TextView) findViewById(R.id.txtViewCurTemp);
        curStatusTxtView = (TextView) findViewById(R.id.txtViewWeatherStatus);
        curDateTxtView = (TextView) findViewById(R.id.txtViewCurDate);
        curHighLowTxtView = (TextView) findViewById(R.id.txtViewCurHighLow);

        new FetchCurWeatherTask(cityName, this).execute();

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




/***************************************************************************/



//        day5Forcast = (ListView) findViewById(R.id.listView5dayForecast);
//
//        List<String> cityInfo = weatherConn.getCityInfo();
//
//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                cityInfo
//        );
//        day5Forcast.setAdapter(listViewAdapter);

/***************************************************************************/

    }



    private class FetchCurWeatherTask extends AsyncTask<String, Void, String[]> {

        private final Context Asyntaskcontext;
        String cityName;

        FetchCurWeatherTask(String cityName, Context context) {
            this.cityName = cityName;
            Asyntaskcontext = context;
        }

        @Override
        protected String[] doInBackground(String... params) {
            URLConnector weatherConn = new URLConnector(AppConstants.CUR_WEATHER_URL_BASE);
            URLConnector weatherForecastConn = new URLConnector(AppConstants.FORECAST_WEATHER_URL_BASE);

            String[] curANDforecastWeathers = new String[2];
            curANDforecastWeathers[0] = weatherConn.getResponse(cityName);
            curANDforecastWeathers[1] = weatherForecastConn.getResponse(cityName);

            return curANDforecastWeathers;
        }

        @Override
        protected void onPostExecute(String[] curANDforecastWeathers) {
            try {

                //For today
                JsonParser jsonParser = new JsonParser(curANDforecastWeathers[0], Asyntaskcontext);
                String curTemp = jsonParser.getTemp(AppConstants.CURRENT);
                curTempTxtView.setText(curTemp);

                String curStatus = jsonParser.getCurStatus();
                curStatusTxtView.setText(curStatus);

                // this will set today's date.
                String timeZoneURLParaPart = jsonParser.getTimeZoneURLParaPart();
                new FetchTodayDateTask(timeZoneURLParaPart, jsonParser.getTimeStamp(), Asyntaskcontext).execute();

                String curTempHighLow = jsonParser.getTemp(AppConstants.HIGH) + "  " + jsonParser.getTemp(AppConstants.LOW);
                curHighLowTxtView.setText(curTempHighLow);


                //For forecast
                JsonParserForecast jsonParserForecast = new JsonParserForecast(curANDforecastWeathers[1], Asyntaskcontext);
                String[][] info = jsonParserForecast.getForecast5dayInfo();

                //txtView1stDay
                //txtView1stStatus
                //txtView1stNoon
                //txtView2ndDay
                //txtView2ndStatus
                //txtView2ndNoon
                //txtView3rdDay
                //txtView3rdStatus
                //txtView3rdNoon
                //txtView4thDay
                //txtView4thStatus
                //


            } catch (JSONException e) {
                logger.error("Exception from onPostExecute. ", e);
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

    private class FetchTodayDateTask extends AsyncTask<String, Void, String[]> {
        String timeZoneURLParaPart;
        String timestamp;
        Context context;

        FetchTodayDateTask(String timeZoneURLParaPart, String timestamp, Context context) {
            this.timeZoneURLParaPart = timeZoneURLParaPart;
            this.timestamp = timestamp;
            this.context = context;
        }

        @Override
        protected String[] doInBackground(String... params) {
            String url = AppConstants.GOOGLE_TIMEZONE_API_URL_BASE1 + timeZoneURLParaPart + AppConstants.GOOGLE_TIMEZONE_API_URL_BASE2;
            URLConnector weatherConn = new URLConnector(url);

            String[] timezoneANDtimestamp = new String[2];
            timezoneANDtimestamp[0] = weatherConn.getResponse("");
            timezoneANDtimestamp[1] = timestamp;
            return timezoneANDtimestamp;
        }

        @Override
        protected void onPostExecute(String[] timezoneANDtimestamp) {

            String timeZoneId = timezoneANDtimestamp[0];
            String timestamp = timezoneANDtimestamp[1];
            long todayEpoch = Long.valueOf(timestamp);


            Date date = new Date(todayEpoch * 1000L);
            DateFormat format = new SimpleDateFormat("EEE, MMM d, HH:MM, y");
            Log.i("INFO", "todayEpoch: " + todayEpoch + "timeZoneId: " + timeZoneId + "&&&&&&&&&&&&&&");
            format.setTimeZone(TimeZone.getTimeZone(timeZoneId));
            String todayDate = format.format(date);

            curDateTxtView.setText(todayDate);

        }

    }
}
