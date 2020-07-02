package com.tixon.morse.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tixon.morse.R;

/**
 * Created by tikhon.osipov on 07.04.2016.
 */
public class CodeView extends LinearLayout {
    private LinearLayout layout;
    private int horizontalMargin;
    private float scale;
    private boolean isDark = false;

    public CodeView(Context context) {
        super(context);
        init();
    }

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CodeView, 0, 0);
        try {
            isDark = a.getBoolean(R.styleable.CodeView_isDark, false);
        } finally {
            a.recycle();
        }
        init();
    }

    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CodeView, defStyleAttr, 0);
        try {
            isDark = a.getBoolean(R.styleable.CodeView_isDark, false);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.code_view_layout, this, true);
        layout = (LinearLayout) v.findViewById(R.id.codeLayout);
        scale = getResources().getDisplayMetrics().density;
        //horizontalMargin = (int) (2 * scale + 0.5f);
        horizontalMargin = getResources().getDimensionPixelSize(R.dimen.codePaddingHorizontal);
    }

    public void addDot() {
        View dot = new View(getContext());
        LayoutParams params = new LayoutParams((int) getResources().getDimension(R.dimen.dotWidth),
                (int) getResources().getDimension(R.dimen.codeHeight));
        if(isDark) {
            dot.setBackground(getResources().getDrawable(R.drawable.code_background_dark));
        } else {
            dot.setBackground(getResources().getDrawable(R.drawable.code_background));
        }
        params.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        dot.setLayoutParams(params);
        layout.addView(dot);
    }

    public void addDash() {
        View dash = new View(getContext());
        LayoutParams params = new LayoutParams((int) getResources().getDimension(R.dimen.dashWidth),
                (int) getResources().getDimension(R.dimen.codeHeight));
        if(isDark) {
            dash.setBackground(getResources().getDrawable(R.drawable.code_background_dark));
        } else {
            dash.setBackground(getResources().getDrawable(R.drawable.code_background));
        }
        params.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        dash.setLayoutParams(params);
        layout.addView(dash);
    }

    public void clear() {
        layout.removeAllViews();
        layout.invalidate();
    }
}
