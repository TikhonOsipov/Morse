package com.tixon.morse.ime;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.tixon.morse.Morse;
import com.tixon.morse.R;
import com.tixon.morse.views.CodeView;

/**
 * Created by tikhon.osipov on 07.04.2016.
 */
public class MorseIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView kv;
    private Keyboard keyboard;
    private InputConnection ic;

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
        ic = getCurrentInputConnection();
        ic.deleteSurroundingText(1, 0);
        vibrator.vibrate(80);
    }

    @Override
    public void swipeRight() {
        Log.d("myLogs", "swipe right");
    }

    @Override
    public void swipeDown() {
        Log.d("myLogs", "swipe down");
    }

    @Override
    public void swipeUp() {
        Log.d("myLogs", "swipe up");
    }

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.morse_keyboard, null);
        keyboard = new Keyboard(this, R.xml.morse);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        //final CodeView codeView = (CodeView) kv.findViewById(R.id.codeViewKeyboard);

        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        morse = new Morse(getApplicationContext()) {
            @Override
            public void onGenerateLetter(String letter) {
                ic = getCurrentInputConnection();
                Log.d("myLogs", "generated letter: " + letter);
                ic.commitText(letter, 1);
            }

            @Override
            public void onGenerateCode(String code) {
                Log.d("myLogs", "generated code: " + code);
                //codeView.clear();
                //viewCode(code, codeView);
            }

            @Override
            public void onPress() {
                vibrator.vibrate(30);
            }

            @Override
            public void onRelease() {

            }

            @Override
            public void onNewCode() {

            }
        };
        morse.setVibrator(vibrator);
        return kv;
    }

    private void viewCode(String code, CodeView codeView) {
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
