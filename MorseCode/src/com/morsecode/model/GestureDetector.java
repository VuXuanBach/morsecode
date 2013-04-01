package com.morsecode.model;

import com.morsecode.MainActivity;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

public class GestureDetector implements OnTouchListener {

    private MainActivity mActivity;
    private boolean mIsTextView;
    private static final int MIN_DISTANCE = 10;
    private float downX, downY, upX, upY;
    private int count;

    public GestureDetector(MainActivity activity, boolean isTextView) {
        this.mActivity = activity;
        this.mIsTextView = isTextView;
    }

    private void append(String input) {
        mActivity.setInput(mActivity.getInput() + input);
    }

    private void remove() {
        String tmp = mActivity.getResult().getText().toString();
        if (!tmp.isEmpty()) {
            mActivity.getResult().setText(tmp.substring(0, tmp.length() - 1));
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            downY = event.getY();
            if (count < 1) {
                mActivity.getInteractionView().setOldX(downX);
                mActivity.getInteractionView().setOldY(downY);
                count++;
            }
            return true;
        }
        case MotionEvent.ACTION_UP: {
            if (mIsTextView) {
                remove();
                return true;
            }
            upX = event.getX();
            upY = event.getY();

            float deltaX = downX - upX;
            float deltaY = downY - upY;

            if (Math.abs(deltaX) > MIN_DISTANCE) {
                if (deltaX < 0 || deltaX > 0) {
                    this.append(MainActivity.LINE);
                    mActivity.getInteractionView().setShouldDrawRect(false);
                    count = 0;
                    return true;
                }
            } else {
                this.append(MainActivity.DOT);
                drawCircle();
                return true;
            }

            if (Math.abs(deltaY) > MIN_DISTANCE) {
                if (deltaY < 0 || deltaY > 0) {
                    this.append(MainActivity.LINE);
                    mActivity.getInteractionView().setShouldDrawRect(false);
                    count = 0;
                    return true;
                }
            } else {
                this.append(MainActivity.DOT);
                drawCircle();
                return true;
            }
        }
        }
        return false;
    }

    private void drawCircle() {
        mActivity.getInteractionView().setShouldDrawCircle(true);
        mActivity.getInteractionView().setMX(downX);
        mActivity.getInteractionView().setMY(downY);
    }

    private void drawLine() {
        mActivity.getInteractionView().setShouldDrawRect(true);
        mActivity.getInteractionView().setMX(downX);
        mActivity.getInteractionView().setMY(downY);
    }
}
