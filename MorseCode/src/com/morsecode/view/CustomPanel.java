package com.morsecode.view;

import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class CustomPanel extends SurfaceView implements SurfaceHolder.Callback {
    private ViewThread mThread;

    private float mX, mY, mOldX, mOldY;
    private boolean mShouldDrawCircle;
    private boolean mShouldDrawRect;
    private int rad; // my variable for radius and movable x and ys

    public CustomPanel(Context context, AttributeSet as) {
        super(context, as);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
    }

    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawColor(Color.BLACK);
        if (mShouldDrawCircle) {
            rad++;
            if (rad > 50) {
                rad = 3;
                mShouldDrawCircle = false;
            }
            canvas.drawCircle(mX, mY, rad / 2, paint);
        } else {
            if (mShouldDrawRect) {
                canvas.drawRect(mOldX, mOldY, mX, mY, paint);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mThread.isAlive()) {
            mThread = new ViewThread(this);
            mThread.setRunning(true);
            mThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mThread.isAlive()) {
            mThread.setRunning(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public boolean getShouldDrawCircle() {
        return mShouldDrawCircle;
    }

    public void setShouldDrawCircle(boolean shouldDraw) {
        if (shouldDraw) {
            rad = 3;
        }
        mShouldDrawCircle = shouldDraw;
    }

    public void setShouldDrawRect(boolean shouldDraw) {
        mShouldDrawRect = shouldDraw;
    }

    public float getMX() {
        return mX;
    }

    public void setMX(float mX) {
        this.mX = mX;
    }

    public void setOldX(float mX) {
        this.mOldX = mX;
    }

    public float getMY() {
        return mY;
    }

    public void setMY(float mY) {
        this.mY = mY;
    }

    public void setOldY(float mX) {
        this.mOldX = mX;
    }
}

// ///////
class ViewThread extends Thread {
    private CustomPanel mPanel;
    private SurfaceHolder mHolder;
    private boolean mRun = false;

    public ViewThread(CustomPanel panel) {
        mPanel = panel;
        mHolder = mPanel.getHolder();
    }

    public void setRunning(boolean run) {
        mRun = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        while (mRun) {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                mPanel.doDraw(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
