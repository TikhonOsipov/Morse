package com.tixon.morse.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import com.tixon.morse.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tikhon.osipov on 08.04.2016.
 */
public class MorseKeyboardView extends KeyboardView {

    private Context context;
    private int horizontalMargin, dotWidth, dashWidth, codeHeight;
    private int yLevel, keyboardWidth;
    private Drawable dot, keyBackground;

    private ArrayList<Integer> centers;
    private ArrayList<Code> codes;

    private enum Code {DOT, DASH}
    private boolean released = false;

    public MorseKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MorseKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        centers = new ArrayList<>();
        codes = new ArrayList<>();
        horizontalMargin = (int) context.getResources().getDimension(R.dimen.codePaddingHorizontal);
        dotWidth = (int) context.getResources().getDimension(R.dimen.dotWidth);
        dashWidth = (int) context.getResources().getDimension(R.dimen.dashWidth);
        codeHeight = (int) context.getResources().getDimension(R.dimen.codeHeight);

        dot = context.getResources().getDrawable(R.drawable.code_background);
        keyBackground = context.getResources().getDrawable(R.drawable.button_background);
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Key> keys = getKeyboard().getKeys();
        Key key = keys.get(0);
        keyboardWidth = key.width;
        yLevel = key.height/4;
        if(keyBackground != null) {
            keyBackground.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
            keyBackground.draw(canvas);
        }
        drawCode(canvas); //нарисует код, только если codes и centers не пустые и равны
        Log.d("myLogs", "MorseKeyboardView: onDraw()");
    }

    private void drawCode(Canvas canvas) {
        if((codes.size() == centers.size()) && !codes.isEmpty()) {
            if(released) {
                moveToCenter();
            }
            int center;
            for(int i = 0; i < codes.size(); i++) {
                center = centers.get(i);
                if(codes.get(i) == Code.DOT) {
                    dot.setBounds(center-dotWidth/2, yLevel-codeHeight/2,
                            center+dotWidth/2, yLevel+codeHeight/2);
                    dot.draw(canvas);
                } else if(codes.get(i) == Code.DASH) {
                    dot.setBounds(center-dashWidth/2, yLevel-codeHeight/2,
                            center+dashWidth/2, yLevel+codeHeight/2);
                    dot.draw(canvas);
                }
            }
        }
    }

    //формирует координаты по центру
    private void moveToCenter() {
        int totalWidth = getTotalWidth(); //общая ширина кода
        int difference = (keyboardWidth - totalWidth) / 2;
        for(int i = 0; i < centers.size(); i++) {
            centers.set(i, centers.get(i) + difference);
        }
    }

    private int getTotalWidth() {
        int totalWidth = 0;
        for(int i = 0; i < codes.size(); i++) {
            if(codes.get(i) == Code.DOT) {
                totalWidth += dotWidth + horizontalMargin*2;
            } else if(codes.get(i) == Code.DASH) {
                totalWidth += dashWidth + horizontalMargin*2;
            }
        }
        return totalWidth;
    }

    private void addDot() {
        //если код пустой
        if(centers.isEmpty()) {
            centers.add(dotWidth/2 + horizontalMargin);
        } else {
            //если последний символ точка
            if(codes.get(codes.size()-1) == Code.DOT) {
                centers.add(centers.get(centers.size()-1) + dotWidth + horizontalMargin*2);
                //если последний символ тире
            } else if(codes.get(codes.size() - 1) == Code.DASH) {
                centers.add(centers.get(centers.size()-1) + dotWidth * 2 + horizontalMargin*2);
            }
        }
        codes.add(Code.DOT);
    }

    private void addDash() {
        //если код пустой
        if(centers.isEmpty()) {
            centers.add(dashWidth/2 + horizontalMargin);
        } else {
            //если последний символ точка
            if(codes.get(codes.size()-1) == Code.DOT) {
                centers.add(centers.get(centers.size()-1) + dotWidth * 2 + horizontalMargin*2);
                //если последний символ тире
            } else if(codes.get(codes.size() - 1) == Code.DASH) {
                centers.add(centers.get(centers.size()-1) + dashWidth + horizontalMargin*2);
            }
        }
        codes.add(Code.DASH);
    }

    public void viewCode(String code) {
        codes.clear();
        centers.clear();
        for(int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if(c == '.') {
                addDot();
            }
            if(c == '_') {
                addDash();
            }
        }
        invalidate();
    }

    public void clearCode() {
        codes.clear();
        centers.clear();
        invalidate();
    }
}