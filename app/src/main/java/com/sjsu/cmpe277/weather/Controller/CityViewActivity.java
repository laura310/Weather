package com.sjsu.cmpe277.weather.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sjsu.cmpe277.weather.DataModel.OpenWeatherMapConnector;
import com.sjsu.cmpe277.weather.DataModel.WeatherConstants;
import com.sjsu.cmpe277.weather.R;

/**
 * Created by laurazhou on 10/30/17.
 */

public class CityViewActivity extends AppCompatActivity {
//    ListView day5Forcast;
    OpenWeatherMapConnector weatherConn;
    TextView cityNameTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityview);

        weatherConn = new OpenWeatherMapConnector();

        String cityName = getIntent().getStringExtra(WeatherConstants.LIST_VIEW_ITEM);
        cityNameTxtView = (TextView) findViewById(R.id.txtViewCityName);
        cityNameTxtView.setText(cityName);

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
}
