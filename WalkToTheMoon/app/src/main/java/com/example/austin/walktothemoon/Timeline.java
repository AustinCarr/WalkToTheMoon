package com.example.austin.walktothemoon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * Created by Austin on 2/18/15.
 */
public class Timeline extends SurfaceView implements SurfaceHolder.Callback{

    private final double STEPS_TO_MOON = 478000000.0;
    private final int IMAGE_PADDING = 15;

    private SurfaceHolder surfaceHolder;
    private Bitmap bmpMoon, bmpEarth, bmpUser;
    private BitmapDrawable bgImage;
    private Paint paint;
    private Path dashedLine;

    private ScaleGestureDetector mScaleDetector;
    private InteractionMode mode;
    private Matrix mMatrix = new Matrix();
    private float mScaleFactor = 1.f;
    private float mTouchX;
    private float mTouchY;
    private float mTouchBackupX;
    private float mTouchBackupY;
    private float mTouchDownX;
    private float mTouchDownY;

    private float screenX;

    private float xPos, yPos;
    private int stepsTaken;

    private UserDataSource datasource;

    public Timeline(Context context) {
        super(context);
        init(context);
    }

    public Timeline(Context context,
                    AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Timeline(Context context,
                    AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        datasource = new UserDataSource(context);

        surfaceHolder = this.getHolder();
        bmpMoon = BitmapFactory.decodeResource(getResources(),
                R.drawable.moon_big_alt);
        bmpMoon = Bitmap.createScaledBitmap(bmpMoon, (bmpMoon.getWidth() / 3), (bmpMoon.getHeight() / 3 ), false);
        //bmpEarth = BitmapFactory.decodeResource(getResources(), R.drawable.earth_icon);

        bmpEarth = BitmapFactory.decodeResource(getResources(), R.drawable.earth_big);
        bmpEarth = Bitmap.createScaledBitmap(bmpEarth, (bmpEarth.getWidth() / 2), (bmpEarth.getHeight() / 2 ), false);

        bmpUser = BitmapFactory.decodeResource(getResources(), R.drawable.astronaut_walking_main);
        bmpUser = Bitmap.createScaledBitmap(bmpUser, (bmpUser.getWidth() / 8), (bmpUser.getHeight() / 8), false);
        //bgImage = new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(getResources(),
        //        R.drawable.spacebgtest));
        //bgImage.setBounds(-5000, -5000, 4000, 4000);
        //bgImage.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        surfaceHolder.addCallback(this);

        // for zooming (scaling) the view with two fingers
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        setFocusable(true);

        // initial center/touch point of the view (otherwise the view would jump
        // around on first pan/move touch
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mTouchX = metrics.widthPixels / 2;
        mTouchY = metrics.heightPixels / 2;

        screenX = metrics.widthPixels;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAlpha(175);
        paint.setStrokeWidth(20f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[] { 50, 35 }, 20));

        dashedLine = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        if (!this.mScaleDetector.isInProgress()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    // similar to ScaleListener.onScaleEnd (as long as we don't
                    // handle indices of touch events)
                    mode = InteractionMode.None;
                case MotionEvent.ACTION_DOWN:

                    mTouchDownX = event.getX();
                    mTouchDownY = event.getY();
                    mTouchBackupX = mTouchX;
                    mTouchBackupY = mTouchY;

                    // pan/move started
                    mode = InteractionMode.Pan;
                    break;
                case MotionEvent.ACTION_MOVE:
                    // make sure we don't handle the last move event when the first
                    // finger is still down and the second finger is lifted up
                    // already after a zoom/scale interaction. see
                    // ScaleListener.onScaleEnd
                    if (mode == InteractionMode.Pan) {

                        // get current location
                        final float x = event.getX();
                        //final float y = event.getY();

                        // get distance vector from where the finger touched down to
                        // current location
                        final float diffX = x - mTouchDownX;
                        //final float diffY = y - mTouchDownY;

                        mTouchX = mTouchBackupX + diffX;
                        //mTouchY = mTouchBackupY + diffY;

                        CalculateMatrix(true);
                    }

                    break;
            }
        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.concat(mMatrix);

        dashedLine.moveTo((IMAGE_PADDING * 2) + (bmpEarth.getWidth() / 2), getHeight()/2);
        dashedLine.lineTo((getWidth() * 2) - (IMAGE_PADDING * 2), getHeight()/2);

        canvas.drawPath(dashedLine, paint);
        canvas.drawBitmap(bmpMoon, (getWidth() * 2), getHeight()/2 - (bmpMoon.getHeight()/2), null);
        canvas.drawBitmap(bmpEarth, (bmpEarth.getWidth() / -2), getHeight()/2 - (bmpEarth.getHeight()/2), null);

        // Get number of steps from database!!
        datasource.open();
        User user = datasource.getUser();
        stepsTaken = user.getBoostedSteps();
        datasource.close();

        double progress = stepsTaken / STEPS_TO_MOON;
        double progressBarWidth = (getWidth() * 2) - (bmpEarth.getWidth() / 2) - bmpMoon.getWidth();
        double progressBarValue = progressBarWidth * progress;
        double offset = bmpEarth.getWidth() / 2;


        //Check if user reached the moon
        if (stepsTaken >= STEPS_TO_MOON)
            userWon();

        xPos = (int)(progressBarValue + offset);
        yPos = (getHeight()/2) - (bmpUser.getHeight()/2);

        canvas.drawBitmap(bmpUser, xPos, yPos, null);
        canvas.restoreToCount(saveCount);
    }

    private void userWon() {
        Intent intent = new Intent(getContext(), WinningActivity.class);
        getContext().startActivity(intent);
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // otherwise onDraw(Canvas) won't be called
        this.setWillNotDraw(false);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

    void CalculateMatrix(boolean invalidate) {
        float sizeX = this.getWidth() / 2;
        float sizeY = this.getHeight() / 2;

        mMatrix.reset();

        // move the view so that it's center point is located in 0,0
        mMatrix.postTranslate(-sizeX, -sizeY);

        // scale the view
        //System.out.println(mScaleFactor +" ,"+ mScaleFactor);
        mMatrix.postScale(mScaleFactor, mScaleFactor);

        // re-move the view to it's desired location
        if(mTouchX  >= 1650) {
            mTouchX  = 1650;
        }


        System.out.println(mTouchX +" ,"+ mTouchY);
        mMatrix.postTranslate(mTouchX, mTouchY);




        if (invalidate)
            invalidate(); // re-draw
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        float mFocusStartX;
        float mFocusStartY;
        float mZoomBackupX;
        float mZoomBackupY;

        public ScaleListener() {
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            mode = InteractionMode.Zoom;

            mFocusStartX = detector.getFocusX();
            mFocusStartY = detector.getFocusY();
            mZoomBackupX = mTouchX;
            mZoomBackupY = mTouchY;

            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            mode = InteractionMode.None;

            super.onScaleEnd(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            if (mode != InteractionMode.Zoom)
                return true;

            // get current scale and fix its value
            float scale = detector.getScaleFactor();
            mScaleFactor *= scale;
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 2.0f));

            // get current focal point between both fingers (changes due to
            // movement)
            //float focusX = detector.getFocusX();
            //float focusY = detector.getFocusY();

            // get distance vector from initial event (onScaleBegin) to current
            //float diffX = focusX - mFocusStartX;
            //float diffY = focusY - mFocusStartY;

            // scale the distance vector accordingly
            //diffX *= scale;
            //diffY *= scale;

            // set new touch position
            //mTouchX = mZoomBackupX + diffX;
            //mTouchY = mZoomBackupY + diffY;

            CalculateMatrix(true);

            return true;
        }

    }

}