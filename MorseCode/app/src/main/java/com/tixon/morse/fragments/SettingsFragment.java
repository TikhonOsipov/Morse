package com.tixon.morse.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tixon.morse.R;

/**
 * Created by tikhon on 06.04.16.
 */
public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        PreferenceManager pm = getPreferenceManager();
        SharedPreferences sp = pm.getSharedPreferences();
        boolean vibrate = sp.getBoolean("vibrate_setting", false);
        Log.d("myLogs", "vibrate = " + vibrate);
    }
}
