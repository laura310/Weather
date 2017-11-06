package com.sjsu.cmpe277.weather.Controller;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.sjsu.cmpe277.weather.DataModel.AppConstants;
import com.sjsu.cmpe277.weather.DataModel.City;
import com.sjsu.cmpe277.weather.DataModel.CityDB;
import com.sjsu.cmpe277.weather.DataModel.CustomListAdapter;
import com.sjsu.cmpe277.weather.DataModel.JsonParser;
import com.sjsu.cmpe277.weather.DataModel.NewItem;
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
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    final int MY_PERMISSION_REQUEST_CODE = 1;

    Button addButton;
    Button addCurrentButton;
    ListView listView;

    CityDB cityDB;
    LocationManager locationManager;
    CustomListAdapter gridViewAdapter = null;

    String currentCity = "";
    List<String> cities;
    ArrayList<NewItem> citiesInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityDB = new CityDB(this);
        listView = (ListView) findViewById(R.id.custom_list);
        cities = cityDB.getAllCities();

        addButton = (Button) findViewById(R.id.addbutton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        addCurrentButton = (Button) findViewById(R.id.addCurrentbutton);
        addCurrentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                        City c = getCurrentCity2();
                        if (c == null) {
                            return;
                        }
                        List<String> cities = cityDB.getAllCities();
                        if (cities.contains(c.getName())) {
                            Toast.makeText(MainActivity.this, "Current location has been added",Toast.LENGTH_LONG).show();
                        } else {
                            cityDB.insertCity(c.getName(), c.getLat(), c.getLon());
                            new FetchCityInfosTask(cityDB, MainActivity.this).execute();
                        }
                }
        });
        new FetchCityInfosTask(cityDB, this).execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewItem cityObject = (NewItem) listView.getItemAtPosition(position);
                String cityName = cityObject.getCity();
                cities = cityDB.getAllCities();
                Log.i("Info", cityName);

                currentCity = getCurrentCity2().getName();
                Intent intent = new Intent(MainActivity.this, CityViewActivity.class);
                intent.putExtra(AppConstants.LIST_VIEW_CityName, cityName);
                intent.putExtra(AppConstants.LIST_VIEW_Position, position);
                intent.putStringArrayListExtra(AppConstants.LIST_VIEW_Array, (ArrayList<String>) cities);
                intent.putExtra(AppConstants.LIST_VIEW_CurrentCityName, currentCity);
                startActivity(intent);
            }

        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String s = preferences.getString(getString(R.string.pref_temp_key), getString(R.string.pref_C_value));
        Log.i("Info", "current unit is " + s);


        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {
                //Do your tasks here


                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MainActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        NewItem deleteCity = (NewItem) gridViewAdapter.getItem(position);

                        cityDB.deleteCity(deleteCity.getCity());
                        cities = cityDB.getAllCities();
                        Log.i("Info", "remove city" + gridViewAdapter.getItem(position).toString());
                        citiesInfos.remove(gridViewAdapter.getItem(position));
                        gridViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });


    }

    public City getCurrentCity2(){
        City c = null;
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_CODE);
            }
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                currentCity = hereLocation(location.getLatitude(), location.getLongitude());
                c = new City(currentCity, String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                Log.i("Info", currentCity);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Current Location Not Available", Toast.LENGTH_LONG).show();
                Log.e("Info", "error");
            }
        }
            return c;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            try{
                                currentCity = hereLocation(location.getLatitude(), location.getLongitude());
                                Log.i("Info", currentCity);
                                List<String> cities = cityDB.getAllCities();
                                if (cities.contains(currentCity)) {
                                    Toast.makeText(MainActivity.this, "Current location has been added",Toast.LENGTH_LONG).show();
                                } else {
                                    cityDB.insertCity(currentCity, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                                    //gridViewAdapter.add(currentCity);
                                    //gridViewAdapter.notifyDataSetChanged();
                                }
                            } catch(Exception e){
                                Toast.makeText(MainActivity.this, "Location Not Available",Toast.LENGTH_LONG).show();
                                Log.e("error", e.toString());
                            }
                    }
                } else {
                    Log.e("Error", "No permission");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                List<String> cities = cityDB.getAllCities();
                if (cities.contains(place.getName())) {
                   Toast.makeText(MainActivity.this, "The cities you entered has been added before", Toast.LENGTH_LONG).show();
                    Log.i("Info", "places added");
                } else {
                    Log.i("Info", "lat is " + String.valueOf(place.getLatLng().latitude));

                    cityDB.insertCity(place.getName().toString(), String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));
                    new FetchCityInfosTask(cityDB, this).execute();

                    Log.i("INFO", "Place: " + place.getName());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("ERROR", status.getStatusMessage().toString());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public String hereLocation(double lat, double lon) {
        String curCity = "";

        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        List<android.location.Address> addressList;
        try{
            addressList = geocoder.getFromLocation(lat,lon,1);
            if (addressList.size()> 0) {
                curCity = addressList.get(0).getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curCity;
    }


    private class FetchCityInfosTask extends AsyncTask<CityDB, Void, List<List<String>>> {
        List<String> cityNames;
        Context context;

        FetchCityInfosTask(CityDB cityDB, Context context) {
            this.cityNames = cityDB.getAllCities();
            this.context = context;
        }

        @Override
        protected List<List<String>> doInBackground(CityDB... params) {
            List<List<String>> citiesJsonTxts = new ArrayList<>();
            List<String> citiesTimes = new ArrayList<>();
            List<String> citiesTemps = new ArrayList<>();
            citiesJsonTxts.add(citiesTimes);
            citiesJsonTxts.add(citiesTemps);

            for(String cityName : cityDB.getAllCities()) {
                URLConnector urlConn = new URLConnector(AppConstants.CUR_WEATHER_URL_BASE);

                String lat = cityDB.getCityData(cityName).getLat();
                String lon = cityDB.getCityData(cityName).getLon();
                JsonParser jsonParser = null;

                long timestamp = Calendar.getInstance().getTimeInMillis()/1000;
                String timeZoneURLParaPart = lat + "," + lon + "&timestamp=" + timestamp;

                URLConnector timezoneUrlConn = new URLConnector(AppConstants.GOOGLE_TIMEZONE_API_URL_BASE1 + timeZoneURLParaPart +
                                                                AppConstants.GOOGLE_TIMEZONE_API_URL_BASE2);

                citiesTimes.add(timezoneUrlConn.getResponse(""));
                citiesTemps.add(urlConn.getResponse(cityName));
            }

            return citiesJsonTxts;
        }

        @Override
        protected void onPostExecute(List<List<String>> citiesJsonTxts) {
            citiesInfos = new ArrayList<>();
            List<String> citiesTimes = citiesJsonTxts.get(0);
            List<String> citiesTemps = citiesJsonTxts.get(1);

            try {
                for(int i = 0; i < citiesTimes.size(); i++) {
                    String cityName = cityNames.get(i);

                    JsonParser jsonParser = new JsonParser(citiesTemps.get(i), context);
                    String cur_temp = jsonParser.getTemp(AppConstants.CURRENT);
                    String timezoneId = new JSONObject(citiesTimes.get(i)).getString("timeZoneId");
                    String localTime = TimeConverter.getTimeInFormat(Calendar.getInstance().getTimeInMillis()/1000, timezoneId, "HH:mm");


                    citiesInfos.add(new NewItem(cityName, cur_temp, localTime));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            gridViewAdapter = new CustomListAdapter (
                    context,
                    citiesInfos
            );
            listView.setAdapter(gridViewAdapter);
        }
    }

}
