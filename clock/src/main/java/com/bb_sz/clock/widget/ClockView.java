package com.bb_sz.clock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;


/**
 * Created by Administrator on 2017/5/26.
 */

public class ClockView extends View {
    private static final String TAG = ClockView.class.getSimpleName();
    private float cx;
    private float cy;
    private float radius;
    private Paint paintCircle;
    private Paint paintH;
    private float radiusH;
    private Paint secondPaint;
    private Paint minPaint;
    private Paint hourPaint;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background circle
        cx = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        cy = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        radius = Math.min(cx, cy);
        if (null == paintCircle) {
            paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        canvas.drawCircle(cx, cy, radius, paintCircle);

        //draw Clock dial
        radiusH = 5;//5 px
        if (null == paintH) {
            paintH = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintH.setColor(Color.WHITE);
        }

        for (int i = 1; i <= 12; i++) {
            canvas.drawCircle(getPaddingLeft() + getHX(i), getPaddingTop() + getHY(i), radiusH, paintH);
        }

        //draw second
        int second = getCurSecond();
        float secondX = getPaddingLeft() + getSecondX(second);
        float secondY = getPaddingTop() + getSecondY(second);
        if (null == secondPaint) {
            secondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            secondPaint.setColor(Color.WHITE);
            secondPaint.setStrokeWidth(1);//1 px
        }
        canvas.drawLine(cx, cy, secondX, secondY, secondPaint);


        //draw min
        int minute = getCurMin();
        float minuteX = getPaddingLeft() + getMinX(minute);
        float minuteY = getPaddingTop() + getMinY(minute);
        if (null == minPaint) {
            minPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            minPaint.setColor(Color.WHITE);
            minPaint.setStrokeWidth(2);//2 px
        }
        canvas.drawLine(cx, cy, minuteX, minuteY, minPaint);

        //draw hour
        int hour = getCurHour();
        float hx = getPaddingLeft() + getHX(hour, minute);
        float hy = getPaddingTop() + getHY(hour, minute);

        if (null == hourPaint) {
            hourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            hourPaint.setColor(Color.WHITE);
            hourPaint.setStrokeWidth(3);//3 px
        }

        canvas.drawLine(cx, cy, hx, hy, hourPaint);

        postInvalidateDelayed(100);
    }

    private float getSecondY(float second) {
        return (float) (cy - (double) radius * (13.0 / 15.0) * Math.cos(Math.PI * (360.0 / 60.0 * second) / 180.0));
    }

    private float getSecondX(float second) {
        return (float) (cx + (double) radius * (13.0 / 15.0) * Math.sin(Math.PI * (360.0 / 60.0 * second) / 180.0));
    }

    private float getMinY(float second) {
        return (float) (cy - (double) radius * (9.0 / 15.0) * Math.cos(Math.PI * (360.0 / 60.0 * second) / 180.0));
    }

    private float getMinX(float second) {
        return (float) (cx + (double) radius * (9.0 / 15.0) * Math.sin(Math.PI * (360.0 / 60.0 * second) / 180.0));
    }

    private float getHY(float i) {
        return (float) (cy - (double) radius * Math.cos(Math.PI * (30.0 * i) / 180.0));
    }

    private float getHX(float i, int minute) {
        return (float) (cx + (double) radius * (2.0 / 3.0) * Math.sin(Math.PI * (30.0 * i + 30.0 / 5.0 * (minute / (60.0 / 5.0))) / 180.0));
    }

    private float getHX(float i) {
        return (float) (cx + (double) radius * Math.sin(Math.PI * (30.0 * i) / 180.0));
    }

    private float getHY(float i, int minute) {
        return (float) (cy - (double) radius * (2.0 / 3.0) * Math.cos(Math.PI * (30.0 * i + 30.0 / 5.0 * (minute / (60.0 / 5.0))) / 180.0));
    }

    private int getCurHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    private int getCurSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    private int getCurMin() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
}
