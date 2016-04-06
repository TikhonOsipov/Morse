package com.tixon.morse.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by tikhon on 07.04.16.
 */
public class BaseFragment extends Fragment {
    protected Vibrator vibrator;
    protected SharedPreferences sharedPreferences;
    protected boolean shouldVibrate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldVibrate = sharedPreferences.getBoolean("vibrate_setting", false);
        Log.d("myLogs", "BaseFragment: shouldVibrate = " + shouldVibrate);
    }
}
