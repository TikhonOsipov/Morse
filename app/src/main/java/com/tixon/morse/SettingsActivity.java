package com.tixon.morse;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tixon.morse.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    SettingsFragment settingsFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getFragmentManager();
        settingsFragment = SettingsFragment.newInstance();

        fm.beginTransaction().replace(R.id.containerSettings, settingsFragment).commit();
    }

}