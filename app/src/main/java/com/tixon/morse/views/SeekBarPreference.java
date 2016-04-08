package com.tixon.morse.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tixon.morse.R;

/**
 * Created by tikhon.osipov on 07.04.2016.
 */
public class SeekBarPreference extends Preference {

    private SeekBar seekBar;
    private TextView indicatorText, titleText, summaryText;
    private String key;

    private int progress;

    private SharedPreferences preferences;

    public SeekBarPreference(Context context) {
        super(context);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View getView(View convertView, final ViewGroup parent) {
        View v = convertView;
        if(convertView == null) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seek_bar_preference_layout, null);
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        key = getKey();

        indicatorText = (TextView) v.findViewById(R.id.seekBarIndicator);
        titleText = (TextView) v.findViewById(R.id.seekBarPreferenceTitle);
        //summaryText = (TextView) v.findViewById(R.id.seekBarPreferenceSummary);

        //set textViews
        titleText.setText(getTitle());
        //summaryText.setText(getSummary());

        //set seekBar
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        seekBar.setMax(500);

        //set saved values from preferences
        progress = preferences.getInt(key, 100);
        seekBar.setProgress(progress);
        indicatorText.setText(parent.getContext().getResources().getString(R.string.seekBarPreferenceIndicator, progress));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int p, boolean fromUser) {
                progress = p;
                indicatorText.setText(parent.getContext().getResources().getString(R.string.seekBarPreferenceIndicator, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //save set value in preferences
                preferences.edit().putInt(key, progress).apply();
            }
        });
        return v;
    }
}
