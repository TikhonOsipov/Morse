package com.tixon.morse.ime;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.tixon.morse.Morse;
import com.tixon.morse.MyApplication;
import com.tixon.morse.R;
import com.tixon.morse.views.MorseKeyboardView;

/**
 * Created by tikhon.osipov on 07.04.2016.
 */
public class MorseIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private MorseKeyboardView kv;
    private Keyboard keyboard;
    private InputConnection ic;
    private InputMethodManager imm;
    private MyApplication app;
    private String lastLetter = "";

    Morse morse;
    Vibrator vibrator;

    @Override
    public void onPress(int primaryCode) {
        Log.d("myLogs", "key pressed");
        morse.onActionDown();
    }

    @Override
    public void onRelease(int primaryCode) {
        Log.d("myLogs", "key released");
        morse.onActionUp();
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        /*Log.d("myLogs", "onKey: primaryCode = " + primaryCode);
        InputConnection ic = getCurrentInputConnection();
        if(primaryCode == -1) {
            ic.commitText("M", 1);
        }*/
    }

    @Override
    public void onText(CharSequence text) {
        Log.d("myLogs", "onText: " + text);
    }

    @Override
    public void swipeLeft() {
        Log.d("myLogs", "swipe left");
        kv.clearCode();
        ic = getCurrentInputConnection();
        ic.deleteSurroundingText(1, 0);
        vibrator.vibrate(80);
    }

    @Override
    public void swipeRight() {
        Log.d("myLogs", "swipe right");
        kv.clearCode();
        ic = getCurrentInputConnection();
        ic.commitText(" ", 1);
        vibrator.vibrate(80);
    }

    @Override
    public void swipeDown() {
        Log.d("myLogs", "swipe down");
    }

    @Override
    public void swipeUp() {
        Log.d("myLogs", "swipe up");
        imm.showInputMethodPicker();
        /*Intent keyboardSettings = new Intent(getApplicationContext(), ActivityKeyboardSettings.class);
        keyboardSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(keyboardSettings);*/
        vibrator.vibrate(80);
    }

    private void commitCorrectLetter(InputConnection ic, String letter) {
        String s = "";
        if(!lastLetter.isEmpty()) {
            char ll = lastLetter.charAt(0);
            if(ll == '.' || ll == '?' || ll == '!') {
                s = " " + Character.toUpperCase(letter.charAt(0));
                lastLetter += Character.toUpperCase(letter.charAt(0));
            } else if(ll == ':' || ll == ';' || ll == ',' || ll == ')') {
                s = " " + Character.toLowerCase(letter.charAt(0));
                lastLetter += Character.toLowerCase(letter.charAt(0));
            } else {
                s += Character.toLowerCase(letter.charAt(0));
            }
        } else {
            s += Character.toUpperCase(letter.charAt(0));
            lastLetter += Character.toUpperCase(letter.charAt(0));
        }
        ic.commitText(s, s.length());
    }

    @Override
    public View onCreateInputView() {
        LayoutInflater inflater = getLayoutInflater();
        kv = (MorseKeyboardView) inflater.inflate(R.layout.morse_keyboard, null);
        keyboard = new Keyboard(this, R.xml.morse);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        app = (MyApplication) getApplication();

        vibrator = app.getVibrator();
        imm = app.getInputMethodManager();

        morse = new Morse(getApplicationContext()) {
            @Override
            public void onGenerateLetter(String letter) {
                /*ic = getCurrentInputConnection();
                Log.d("myLogs", "generated letter: " + letter);
                ic.commitText(letter, 1);*/
                commitCorrectLetter(getCurrentInputConnection(), letter);
            }

            @Override
            public void onGenerateCode(String code) {
                Log.d("myLogs", "generated code: " + code);
                kv.viewCode(code);
            }

            @Override
            public void onPress() {
                Log.d("myLogs", "MorseIME: onPress");
                kv.setReleased(false);
                vibrator.vibrate(30);
            }

            @Override
            public void onRelease() {
                Log.d("myLogs", "MorseIME: onRelease");
                kv.setReleased(true);
            }

            @Override
            public void onNewCode() {

            }
        };
        morse.setVibrator(vibrator);
        return kv;
    }
}
