package com.tixon.morse.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tixon.morse.R;

/**
 * Created by tikhon.osipov on 07.04.2016.
 */
public class RingProgressBar extends View {

    private int degree;
    private RectF oval = new RectF();
    private Paint paint = new Paint();
    private Paint circlesPaint = new Paint();
    Path path = new Path();
    private float density;

    public RingProgressBar(Context context) {
        super(context);
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    int center_x, center_y, radius, innerRadius, outerRadius;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        density = getResources().getDisplayMetrics().density;
        paint.setColor(getResources().getColor(R.color.codeBackground));
        paint.setStrokeWidth((int) (density * 4 + 0.5f));
        paint.setStyle(Paint.Style.STROKE); // заливаем
        paint.setAntiAlias(true);

        circlesPaint.setColor(getResources().getColor(R.color.codeBackground));
        circlesPaint.setStyle(Paint.Style.FILL);
        circlesPaint.setAntiAlias(true);

        center_x = (int) (density * 16 + 0.5f);
        center_y = (int) (density * 16 + 0.5f);
        radius = (int) (density * 14 + 0.5f);
        innerRadius = (int) (density * 12 + 0.5f);
        outerRadius = (int) (density * 16 + 0.5f);

        drawArc(canvas, 0, degree, paint);
    }

    private Point calculatePointOnArc(int circleCeX, int circleCeY, int circleRadius, float endAngle)
    {
        Point point = new Point();
        double endAngleRadian = endAngle * (Math.PI / 180);
        int pointX = (int) Math.round((circleCeX + circleRadius * Math.cos(endAngleRadian)));
        int pointY = (int) Math.round((circleCeY + circleRadius * Math.sin(endAngleRadian)));
        point.x = pointX;
        point.y = pointY;
        return point;
    }

    private void drawArc(Canvas canvas, float startAngle, float sweepDegrees, Paint paint) {

        oval.set(center_x - radius, center_y - radius, center_x + radius,
                center_y + radius);
        canvas.drawArc(oval, startAngle, startAngle+sweepDegrees, false, paint);
        path.reset();
        Point startPoint = calculatePointOnArc(center_x, center_y, radius, startAngle);
        Point endPoint = calculatePointOnArc(center_x, center_y, radius, startAngle + sweepDegrees);
        path.addCircle(startPoint.x, startPoint.y, ((int) (density * 2 + 0.5f)), Path.Direction.CW);
        path.addCircle(endPoint.x, endPoint.y, ((int) (density * 2 + 0.5f)), Path.Direction.CW);
        path.close();
        canvas.drawPath(path, circlesPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }
}
