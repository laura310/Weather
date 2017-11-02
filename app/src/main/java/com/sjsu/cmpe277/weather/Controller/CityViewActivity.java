package com.sjsu.cmpe277.weather.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sjsu.cmpe277.weather.DataModel.JsonConstants;
import com.sjsu.cmpe277.weather.DataModel.JsonParser;
import com.sjsu.cmpe277.weather.DataModel.OpenWeatherMapConnector;
import com.sjsu.cmpe277.weather.DataModel.WeatherConstants;
import com.sjsu.cmpe277.weather.R;

import org.apache.log4j.Logger;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityview);


        String cityName = getIntent().getStringExtra(WeatherConstants.LIST_VIEW_ITEM);
        cityNameTxtView = (TextView) findViewById(R.id.txtViewCityName);
        cityNameTxtView.setText(cityName);

        curTempTxtView = (TextView) findViewById(R.id.txtViewCurTemp);
        curStatusTxtView = (TextView) findViewById(R.id.txtViewWeatherStatus);
        curDateTxtView = (TextView) findViewById(R.id.txtViewCurDate);
        curHighLowTxtView = (TextView) findViewById(R.id.txtViewCurHighLow);

        new FetchCurWeatherTask(cityName, this).execute();





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


    private class FetchCurWeatherTask extends AsyncTask<String, Void, String> {

        private final Context Asyntaskcontext;
        String cityName;

        FetchCurWeatherTask(String cityName, Context context) {
            this.cityName = cityName;
            Asyntaskcontext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            OpenWeatherMapConnector weatherConn = new OpenWeatherMapConnector();
            return weatherConn.getCurWeatByCityName(cityName);
        }

        @Override
        protected void onPostExecute(String curWeatherInfo) {
            try {
                JsonParser jsonParser = new JsonParser(curWeatherInfo, Asyntaskcontext);
                String curTemp = jsonParser.getTemp(JsonConstants.CURRENT);
                curTempTxtView.setText(curTemp);

                String curStatus = jsonParser.getCurStatus();
                curStatusTxtView.setText(curStatus);

                long curDateEpoch = jsonParser.getCurDateEpoch();
                curDateTxtView.setText(getDateTime(curDateEpoch) + " Today");

                String curTempHighLow = jsonParser.getTemp(JsonConstants.HIGH) + "  " + jsonParser.getTemp(JsonConstants.LOW);
                curHighLowTxtView.setText(curTempHighLow);

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
}
