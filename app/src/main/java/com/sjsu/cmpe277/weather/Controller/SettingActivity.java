package com.sjsu.cmpe277.weather.Controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sjsu.cmpe277.weather.R;

/**
 * Created by laurazhou on 10/30/17.
 */

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "unit": setListPreferenceSummary(key);
        }
    }
    private void setListPreferenceSummary(String preferenceKey) {
        ListPreference preference = (ListPreference) findPreference(preferenceKey);
        preference.setSummary(preference.getEntry());
    }
}
