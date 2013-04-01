package com.morsecode;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class TransformationalActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new CustomPanel(this));
    }
}

// ////////

class CustomPanel extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap mBitmap;
    private ViewThread mThread;

    private int mX;
    private int mY;
    private int rad; // my variable for radius and movable x and ys

    public CustomPanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        mThread = new ViewThread(this);
    }

    public void doDraw(Canvas canvas) {

        /* because of the thread this is looping like draw() in Processing */

        // simple way to make circle grow then go small again

        rad++;
        if (rad > 300) {
            rad = 3;

        }

        canvas.drawColor(Color.BLUE); // background colour

        Paint paint = new Paint();

        canvas.drawLine(33, 0, 33, 100, paint);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawLine(56, 0, 56, 100, paint);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(25);
        for (int y = 30, alpha = 255; alpha > 2; alpha >>= 1, y += 10) {
            paint.setAlpha(alpha);

            // canvas.drawRect(cx, cy, radius, paint);
            canvas.drawLine(0, y, 100, y, paint);

        }

        paint.setColor(Color.CYAN);
        // canvas.drawCircle(mX-20, mY-20, rad, paint); //use my rad variable
        // for radius

        // strange solution to drawing a stable sized moving rect
        paint.setColor(Color.RED);
        Rect simplr = new Rect();
        simplr.set(rad + 120, 130, rad + 156, 156);
        canvas.drawRect(simplr, paint);
        // //////
        paint.setColor(Color.CYAN);
        canvas.drawCircle(rad * 2, 220, 30, paint); // faster circle

        paint.setColor(Color.RED);
        canvas.drawRect(rad, 67, 68, 45, paint);
        // ////////////////
        paint.setColor(Color.GREEN);
        canvas.drawCircle(rad, 125, 45, paint); // move circle across screen
        // ////////////////////

        paint.setColor(Color.MAGENTA);
        canvas.drawCircle(125, rad, rad / 2, paint); // move circle down screen

        paint.setColor(Color.YELLOW);

        canvas.drawRect(rad, rad, 45, 45, paint);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub
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
        mX = (int) event.getX() - mBitmap.getWidth() / 2;
        mY = (int) event.getY() - mBitmap.getHeight() / 2;
        return super.onTouchEvent(event);
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
