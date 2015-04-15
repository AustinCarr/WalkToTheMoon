package com.example.austin.walktothemoon;

import android.content.SharedPreferences;
import android.text.format.Time;

/**
 * Created by ericmusliner on 4/8/15.
 */
public class PedometerSettings {

    SharedPreferences mSettings;

    public PedometerSettings(SharedPreferences settings) {
        mSettings = settings;
    }

    public float getStepLength() {
        return 0f;
    }

    public float getBodyWeight() {
        return 0f;
    }

    public boolean wakeAggressively() {
        return mSettings.getString("operation_level", "run_in_background").equals("wake_up");
    }
    public boolean keepScreenOn() {
        return mSettings.getString("operation_level", "run_in_background").equals("keep_screen_on");
    }

    public void saveServiceRunningWithTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", currentTimeInMillis());
        editor.commit();
    }

    public void saveServiceRunningWithNullTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public void clearServiceRunning() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", false);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public boolean isServiceRunning() {
        return mSettings.getBoolean("service_running", false);
    }

    public boolean isNewStart() {
        // activity last paused more than 10 minutes ago
        return mSettings.getLong("last_seen", 0) < currentTimeInMillis() - 1000*60*10;
    }

    public static long currentTimeInMillis() {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(false);
    }

}

