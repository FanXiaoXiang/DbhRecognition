package com.bjfu.androidlib.ui;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by 11827 on 2016/8/1.
 */
public class TouchPadView extends FrameLayout implements ZoomableImageView.OnScaleListener {

    private float responseRate = 1;

    private OnTouchPadTouchedListener touchedListener;
    private boolean isActionMove = false;
    private float startX, startY;
    private float endX, endY;
    private float deltaX, deltaY;

    public TouchPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                deltaX = endX - startX;
                startX = endX;

                endY = event.getY();
                deltaY = endY - startY;
                startY = endY;

                isActionMove = true;
                notifyActionMove(isActionMove);
                break;
            default:
                reset();
        }
        return super.onTouchEvent(event);
    }

    private void notifyActionMove(boolean isActionMove) {
        if (!isActionMove) return;
        if (touchedListener == null) return;
        touchedListener.onTouched(responseRate * deltaX, responseRate * deltaY);
    }

    private void reset() {
        isActionMove = false;
        startX = startY = 0;
        endX = endY = 0;
        deltaX = deltaY = 0;
    }

    @Override
    public void onScaleChange(float scale) {
        responseRate = 1.317f - 0.317f * scale;
    }

    public void setTouchedListener(OnTouchPadTouchedListener touchedListener) {
        this.touchedListener = touchedListener;
    }


    public interface OnTouchPadTouchedListener {
        /**
         * 当触控板被触摸时触发
         */
        void onTouched(float deltaX, float deltaY);
    }
}
