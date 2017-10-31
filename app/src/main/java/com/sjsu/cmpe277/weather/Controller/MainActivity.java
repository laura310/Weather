package com.sjsu.cmpe277.weather.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.sjsu.cmpe277.weather.R;

import org.apache.log4j.chainsaw.Main;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addButton = (Button) findViewById(R.id.addbutton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent myIntent = new Intent(MainActivity.this,
                        SearchCityActivity.class);
                startActivity(myIntent);
            }
        });


        listView = (ListView) findViewById(R.id.listView);

        String [] cities = new String[3];
        cities[0] = "Los Angeles";
        cities[1] = "San Jose";
        cities[2] = "London";

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                cities
        );
        listView.setAdapter(listViewAdapter);
    }
}
