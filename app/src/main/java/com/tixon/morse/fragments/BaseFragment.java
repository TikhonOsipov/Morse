package com.tixon.morse.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.tixon.morse.R;
import com.tixon.morse.views.CodeView;

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
        shouldVibrate = sharedPreferences.getBoolean(getString(R.string.keyVibrate), false);
        Log.d("myLogs", "BaseFragment: shouldVibrate = " + shouldVibrate);
    }

    protected void viewCode(String code, CodeView codeView) {
        for(int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if(c == '.') {
                codeView.addDot();
            }
            if(c == '_') {
                codeView.addDash();
            }
        }
    }
}
