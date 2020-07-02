package com.tixon.morse;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by tikhon.osipov on 08.04.2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public InputMethodManager getInputMethodManager() {
        return (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public Vibrator getVibrator() {
        return (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
}
