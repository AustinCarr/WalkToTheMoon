package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
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

    private static final String TAG = "PedometerLog";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;

    private static double TENSION = 800;
    private static double DAMPER = 20; //friction

    private ImageView mImageToAnimate, mImageToAnimate2;
    private SpringSystem mSpringSystem, mSpringSystem2;
    private Spring mSpring, mSpring2;

    private TextView mStepValueView;
    private int stepsTaken;
    private int mStepValue;
    private boolean mQuitting = false;
    private boolean mIsRunning = true;

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

        //////////////
        //mStepValue = stepsTaken;

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
    @Override
    public void onResume()
    {
        Log.i(TAG, "[ACTIVITY] onResume");
        super.onResume();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);

        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();

        if (!mIsRunning && mPedometerSettings.isNewStart()) {
            startStepService();
            bindStepService();
        }
        else if (mIsRunning) {
            bindStepService();
        }

        mPedometerSettings.clearServiceRunning();

        mStepValueView     = (TextView) findViewById(R.id.text_view_steps_count);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "[ACTIVITY] onPause");

        if(mIsRunning) {
            unbindStepService();
        }
        if (mQuitting) {
            mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        }
        else {
            mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "[ACTIVITY] onStop");
        super.onStop();
    }

    protected void onDestroy() {
        Log.i(TAG, "[ACTIVITY] onDestroy");
        super.onDestroy();
    }

    protected void onRestart() {
        Log.i(TAG, "[ACTIVITY] onRestart");
        super.onDestroy();
    }

    public void onShopPressed(View v) {
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void onProfilePressed(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    private StepService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder)service).getService();

            mService.registerCallback(mCallback);
            mService.reloadSettings();

        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void startStepService() {
        if (! mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(MainActivity.this, StepService.class));
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(MainActivity.this,
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }

    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(MainActivity.this,
                    StepService.class));
        }
        mIsRunning = false;
    }

    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }
    };

    private static final int STEPS_MSG = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STEPS_MSG:
                    mStepValue = (int) msg.arg1;
                    mStepValueView.setText("" + mStepValue);
                    break;
            }
        }
    };
}
