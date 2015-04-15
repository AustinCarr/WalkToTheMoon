package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;


public class MainActivity extends Activity{

    private static double TENSION = 800;
    private static double DAMPER = 20; //friction

    private ImageView mImageToAnimate, mImageToAnimate2;
    private SpringSystem mSpringSystem, mSpringSystem2;
    private Spring mSpring, mSpring2;

    private int stepsTaken;

    private UserDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView sfvTrack = (SurfaceView)findViewById(R.id.timeLine);
        sfvTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = sfvTrack.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");

        // Get number of steps from database!!
        datasource = new UserDataSource(this);
        datasource.open();
        User user = datasource.getUser();
        stepsTaken = user.getBoostedSteps();
        datasource.close();

        TextView textview = (TextView) findViewById(R.id.text_view_steps_count);
        textview.setTypeface(tobiBlack);
        textview.setText(String.valueOf(stepsTaken));

        textview = (TextView) findViewById(R.id.text_view_steps_text);
        textview.setTypeface(tobiBlack);


        // Creates Spring Animations

        mImageToAnimate = (ImageView) findViewById(R.id.button_shop);
        //mImageToAnimate.setOnTouchListener(this);

        mImageToAnimate2 = (ImageView) findViewById(R.id.button_profile);
        //mImageToAnimate2.setOnTouchListener(this);

        mSpringSystem = SpringSystem.create();
        mSpringSystem2 = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(new SpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mImageToAnimate.setScaleX(scale);
                mImageToAnimate.setScaleY(scale);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {

            }
        });

        mSpring2 = mSpringSystem2.createSpring();
        mSpring2.addListener(new SpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mImageToAnimate2.setScaleX(scale);
                mImageToAnimate2.setScaleY(scale);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {

            }
        });

        View.OnTouchListener profileListener =new View.OnTouchListener(){
            public boolean onTouch(    View v,    MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //mSpring.setEndValue(0.75f);
                        mSpring2.setEndValue(0.75f);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onProfilePressed(findViewById(R.id.button_profile));
                        //mSpring.setEndValue(0f);
                        mSpring2.setEndValue(0f);
                        return true;
                }

                return false;
            }
        };

        View.OnTouchListener shopListener =new View.OnTouchListener(){
            public boolean onTouch(    View v,    MotionEvent event){
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSpring.setEndValue(0.75f);
                        //mSpring2.setEndValue(0.75f);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onShopPressed(findViewById(R.id.button_shop));
                        mSpring.setEndValue(0f);
                        //mSpring2.setEndValue(0f);
                        return true;
                }

                return false;
            }
        };

        mImageToAnimate.setOnTouchListener(shopListener);
        mImageToAnimate2.setOnTouchListener(profileListener);


        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        SpringConfig config2 = new SpringConfig(TENSION, DAMPER);
        //SpringConfig config2 = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);
        mSpring2.setSpringConfig(config2);

        //mImageToAnimate = (ImageView) findViewById(R.id.button_shop);
        //mImageToAnimate.setOnTouchListener(this);
    }

    public void onShopPressed(View v) {
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void onProfilePressed(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
