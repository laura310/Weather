package com.sjsu.cmpe277.weather.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.sjsu.cmpe277.weather.DataModel.CityDB;
import com.sjsu.cmpe277.weather.DataModel.WeatherConstants;
import com.sjsu.cmpe277.weather.R;

import org.apache.log4j.chainsaw.Main;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    ListView listView;
    CityDB cityDB;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityDB = new CityDB(this);

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


        listView = (ListView) findViewById(R.id.listView);
        /*cityDB.insertCity("san jose");
        cityDB.insertCity("sunnyvale");
        cityDB.insertCity("sf");*/
     /*   String [] cities = new String[3];
        cities[0] = "Los Angeles";
        cities[1] = "San Jose";
        cities[2] = "London";*/
        List<String> cities = cityDB.getAllCities();


        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                cities
        );
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                String cityName = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, CityViewActivity.class);
                intent.putExtra(WeatherConstants.LIST_VIEW_ITEM, cityName);
                startActivity(intent);
            }

        });


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
                }
                cityDB.insertCity(place.getName().toString());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Log.i("INFO", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("ERROR", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
