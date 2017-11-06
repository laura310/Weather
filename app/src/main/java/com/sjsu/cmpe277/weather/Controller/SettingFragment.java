package com.sjsu.cmpe277.weather.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;


import com.sjsu.cmpe277.weather.R;

/**
 * Created by Ran on 11/2/17.
 */

public class SettingFragment extends PreferenceFragmentCompat {
    public static final String TAG = SettingFragment.class.getSimpleName();
    //boolean mBindingPreferences = false;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref);

    }

    private SharedPreferences.OnSharedPreferenceChangeListener mListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals(getString(R.string.pref_temp_key))) {
                            Preference tempUnitsPref = findPreference(key);
                            tempUnitsPref.setSummary(sharedPreferences.getString(key, getString(R.string.pref_C_value)));
                        Log.i("info", sharedPreferences.getString(key, getString(R.string.pref_C_value)));
                        }

                    }

                };



    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(mListener);
    }
}

