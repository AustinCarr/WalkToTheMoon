package com.example.austin.walktothemoon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Austin on 2/18/15.
 */
public class Timeline extends SurfaceView {
    private SurfaceHolder surfaceHolder;
    private Bitmap bmpIcon;

    public Timeline(Context context) {
        super(context);
        init();
    }

    public Timeline(Context context,
                         AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Timeline(Context context,
                         AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        surfaceHolder = getHolder();
        bmpIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);
        surfaceHolder.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas(null);
                drawSomething(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder,
                                       int format, int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }});
    }

    protected void drawSomething(Canvas canvas) {
        //canvas.drawColor(Color.rgb(0,0,0));
        canvas.drawColor(Color.rgb(56,63,96));
        canvas.drawBitmap(bmpIcon,
                getWidth()/2, getHeight()/2, null);
    }

}