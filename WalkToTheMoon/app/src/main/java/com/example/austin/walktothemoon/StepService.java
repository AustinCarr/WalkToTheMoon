package com.example.austin.walktothemoon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ericmusliner on 4/8/15.
 */
public class StepService extends Service {

    private static final String TAG = "com.example.ericmusliner.pedometer.StepService";

    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private SharedPreferences mState;
    private SharedPreferences.Editor mStateEditor;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    private StepDisplayer mStepDisplayer;
    private NotificationManager mNM;
    private PowerManager.WakeLock wakeLock;

    private int mSteps;
    private UserDataSource datasource;

    public class StepBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "[SERVICE] onCreate");
        super.onCreate();

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //showNotification();

        // Load settings
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        mState = getSharedPreferences("state", 0);

        acquireWakeLock();

        // Start detecting
        mStepDetector = new StepDetector();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerDetector();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);

        mStepDisplayer = new StepDisplayer(mPedometerSettings);
        //mStepDisplayer.setSteps(mSteps = mState.getInt("steps", 0));
        ///////
        // Get number of steps from database!!
        datasource = new UserDataSource(this);
        datasource.open();
        User user = datasource.getUser();
        mStepDisplayer.setSteps(user.getBoostedSteps());
        datasource.close();
        //////////
        mStepDisplayer.addListener(mStepListener);
        mStepDetector.addStepListener(mStepDisplayer);

        // Tell the user we started.
        //Toast.makeText(this, "Started Pedometer", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "[SERVICE] onStart");
        //super.onStart(intent, startId);
        super.startService(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[SERVICE] onDestroy");

        // Unregister our receiver.
        unregisterReceiver(mReceiver);
        unregisterDetector();

        mStateEditor = mState.edit();
        mStateEditor.putInt("steps", mSteps);

        ///SAVE STEPS TO DATABASE!!/////////////////
        datasource = new UserDataSource(this);
        datasource.open();
        User user = datasource.getUser();
        user.setBoostedSteps(mSteps);
        datasource.updateUser(user);
        datasource.close();

        mStateEditor.commit();

        mNM.cancel(R.string.app_name);

        wakeLock.release();

        super.onDestroy();

        // Stop detecting
        mSensorManager.unregisterListener(mStepDetector);


        // Tell the user we stopped.
        //Toast.makeText(this, "Stopped Pedometer", Toast.LENGTH_SHORT).show();
    }

    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER /*|
            Sensor.TYPE_MAGNETIC_FIELD |
            Sensor.TYPE_ORIENTATION*/);
        mSensorManager.registerListener(mStepDetector, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterDetector() {
        mSensorManager.unregisterListener(mStepDetector);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[SERVICE] onBind");
        return mBinder;
    }

    /**
     * Receives messages from activity.
     */
    private final IBinder mBinder = new StepBinder();

    public interface ICallback {
        public void stepsChanged(int value);
    }

    private ICallback mCallback;

    public void registerCallback(ICallback cb) {
        mCallback = cb;
        //mStepDisplayer.passValue();
        //mPaceListener.passValue();
    }

    public void reloadSettings() {
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        if (mStepDetector != null) {
            mStepDetector.setSensitivity(
                    Float.valueOf(mSettings.getString("sensitivity", "10"))
            );
        }

        if (mStepDisplayer != null) mStepDisplayer.reloadSettings();

    }

    public void resetValues() {
        mStepDisplayer.setSteps(0);

    }

    private StepDisplayer.Listener mStepListener = new StepDisplayer.Listener() {
        public void stepsChanged(int value) {
            mSteps = value;
            passValue();
        }
        public void passValue() {
            if (mCallback != null) {
                mCallback.stepsChanged(mSteps);
            }
        }
    };

    /*private void showNotification() {
        CharSequence text = getText(R.string.app_name);
        Notification notification = new Notification(R.drawable.ic_notification, null,
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        Intent pedometerIntent = new Intent();
        pedometerIntent.setComponent(new ComponentName(this, PedometerMain.class));
        pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                pedometerIntent, 0);
        notification.setLatestEventInfo(this, text,
                getText(R.string.notification_subtitle), contentIntent);

        mNM.notify(R.string.app_name, notification);
    }*/

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Unregisters the listener and registers it again.
                StepService.this.unregisterDetector();
                StepService.this.registerDetector();
                if (mPedometerSettings.wakeAggressively()) {
                    wakeLock.release();
                    acquireWakeLock();
                }
            }
        }
    };

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
        if (mPedometerSettings.wakeAggressively()) {
            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
        }
        else if (mPedometerSettings.keepScreenOn()) {
            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK;
        }
        else {
            wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
        }
        wakeLock = pm.newWakeLock(wakeFlags, TAG);
        wakeLock.acquire();
    }

}

