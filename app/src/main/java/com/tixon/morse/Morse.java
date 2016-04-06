package com.tixon.morse;

import android.content.Context;
import android.os.Handler;
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

    private long timePressed = 0;
    private long timeReleased = 0;
    private long timeRange = 0;

    private ArrayList<String> russianAbc, russianMorse, letterCode;

    private Handler handler;
    private Runnable detectSymbolRunnable;

    public abstract void onGenerateLetter(String letter);
    public abstract void onGenerateCode(String code);

    public Morse(Context context) {
        this.context = context;
        letterCode = new ArrayList<>();
        russianAbc = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.russian_abc)));
        russianMorse = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.russian_morse)));

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
            }
        };
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

    public abstract void onNewCode();

    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setPressed(true);
            handler.postDelayed(detectSymbolRunnable, 400);
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
            timeReleased = System.currentTimeMillis();
            timeRange = timeReleased - timePressed;
            Log.d("myLogs", "pressed time: " + timeReleased + "; range = " + timeRange);
            if(letterCode.size() < 5) {
                if(timeRange > 0 && timeRange <= 100) {
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

    public boolean checkCorrect(String code, String letter) {
        return russianMorse.indexOf(code) == russianAbc.indexOf(letter);
    }
}
