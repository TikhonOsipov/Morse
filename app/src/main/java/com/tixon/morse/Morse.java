package com.tixon.morse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tikhon.osipov on 06.04.2016.
 */
public abstract class Morse {

    private Context context;
    private SharedPreferences preferences;
    private Vibrator vibrator = null;

    private long timePressed = 0;
    private long timeReleased = 0;
    private long timeRange = 0;
    private long step;

    private ArrayList<String> russianAbc, russianMorse, letterCode;

    private Handler handler;
    private Runnable detectSymbolRunnable;

    public abstract void onGenerateLetter(String letter);
    public abstract void onGenerateCode(String code);
    public abstract void onNewCode();
    public abstract void onPress();
    public abstract void onRelease();

    public Morse(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        letterCode = new ArrayList<>();
        russianAbc = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.russian_abc)));
        russianMorse = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.russian_morse)));

        //get values from defaultSharedPreferences
        updateSavedValues();

        handler = new Handler();
        detectSymbolRunnable = new Runnable() {
            @Override
            public void run() {
                String code = getCode(letterCode);
                String letter = "";
                int index = russianMorse.indexOf(code);
                if(index != -1) {
                    letter = russianAbc.get(index);
                }
                onGenerateLetter(letter);
                //vibrate if vibrator is set
                if(vibrator != null) {
                    vibrateCode(code, vibrator);
                }
            }
        };
    }

    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    private String getCode(ArrayList<String> letterCode) {
        String s = "";
        if(letterCode != null) {
            for(int i = 0; i < letterCode.size(); i++) {
                s += letterCode.get(i);
            }
        }
        return s;
    }

    public void onActionDown() {
        onPress();
        //handler.postDelayed(detectSymbolRunnable, 400);
        Log.d("myLogs", "handler post delayed");
        timePressed = System.currentTimeMillis();
        Log.d("myLogs", "pressed time: " + timePressed);
        long timeReturn = timePressed - timeReleased;
        Log.d("myLogs", "time Return = " + timeReturn);

        if(timeReturn != timePressed) {
            if(timeReturn < 390) {
                handler.removeCallbacks(detectSymbolRunnable);
                Log.d("myLogs", "handler remove callbacks");
            } else {
                letterCode.clear();
                onNewCode();
            }
        }
    }

    public void onActionUp() {
        onRelease();
        timeReleased = System.currentTimeMillis();
        timeRange = timeReleased - timePressed;
        Log.d("myLogs", "pressed time: " + timeReleased + "; range = " + timeRange);
        //not add in letterCode list when its size reaches 5
        if(letterCode.size() < 5) {
            //use stepTime
            if(timeRange > 0 && timeRange <= step) {
                letterCode.add("."); //add dit
            } else {
                letterCode.add("_"); //add dah
            }
        }
        onGenerateCode(getCode(letterCode));
        handler.postDelayed(detectSymbolRunnable, 400);
        Log.d("myLogs", "handler post delayed (second)");
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setPressed(true);
            onPress();
            //handler.postDelayed(detectSymbolRunnable, 400);
            Log.d("myLogs", "handler post delayed");
            timePressed = System.currentTimeMillis();
            Log.d("myLogs", "pressed time: " + timePressed);
            long timeReturn = timePressed - timeReleased;
            Log.d("myLogs", "time Return = " + timeReturn);

            if(timeReturn != timePressed) {
                if(timeReturn < 390) {
                    handler.removeCallbacks(detectSymbolRunnable);
                    Log.d("myLogs", "handler remove callbacks");
                } else {
                    letterCode.clear();
                    onNewCode();
                }
            }
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.setPressed(false);
            onRelease();
            timeReleased = System.currentTimeMillis();
            timeRange = timeReleased - timePressed;
            Log.d("myLogs", "pressed time: " + timeReleased + "; range = " + timeRange);
            //not add in letterCode list when its size reaches 5
            if(letterCode.size() < 5) {
                //use stepTime
                if(timeRange > 0 && timeRange <= step) {
                    letterCode.add("."); //add dit
                } else {
                    letterCode.add("_"); //add dah
                }
            }
            onGenerateCode(getCode(letterCode));
            handler.postDelayed(detectSymbolRunnable, 400);
            Log.d("myLogs", "handler post delayed (second)");
            return true;
        }
        return false;
    }

    public void updateSavedValues() {
        step = preferences.getInt(context.getString(R.string.keyStep), 100);
        Log.d("myLogs", "step = " + step);
    }

    public boolean checkCorrect(String code, String letter) {
        return russianMorse.indexOf(code) == russianAbc.indexOf(letter);
    }

    /*Handler vibrateCodeHandler = new Handler();
    Runnable vibrateCodeRunnable = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < )
        }
    };*/

    private ArrayList<Long> pattern = new ArrayList<>();
    private long[] patternArray;
    long dot = 50l;
    long dash = 150l;
    long pause = 50l;

    public void vibrateCode(String code, Vibrator vibrator) {
        for(int i = 0; i < code.length(); i++) {
            if(code.charAt(i) == '.') {
                pattern.add(dot); //add dot
                pattern.add(pause); //add pause
            }
            if(code.charAt(i) == '_') {
                pattern.add(dash); //add dash
                pattern.add(pause); //add pause
            }
        }
        patternArray = new long[pattern.size()];
        for(int i = 0; i < pattern.size(); i++) {
            patternArray[i] = pattern.get(i);
        }
        vibrator.vibrate(patternArray, -1);
        pattern.clear();
    }
}
