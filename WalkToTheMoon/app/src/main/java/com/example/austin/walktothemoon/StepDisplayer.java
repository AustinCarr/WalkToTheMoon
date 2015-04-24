package com.example.austin.walktothemoon;

/**
 * Created by ericmusliner on 4/8/15.
 */

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Counts steps provided by StepDetector and passes the current
 * step count to the activity.
 */
public class StepDisplayer implements StepListener {

    private int mCount = 0;
    private int counter = 0;
    private Context context;
    private PowerupsDataSource datasource;
    PedometerSettings mSettings;

    public StepDisplayer(PedometerSettings settings, Context context) {
        mSettings = settings;
        this.context = context;
        datasource = new PowerupsDataSource(context);
        datasource.open();
        notifyListener();
    }
    public void setSteps(int steps) {
        mCount = steps;
        notifyListener();
    }
    public void onStep() {
        //Modify step count based on boost
        ArrayList<String> activePowerups = datasource.getActivePowerups();
        Calendar c = Calendar.getInstance();
        Date expireDate = null;
        Date curDate = c.getTime();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        int addedSteps = 0; //Steps to add to mCount
        boolean ebActive = false; //Is the energy bar powerup active
        boolean wwActive = false;
        if(activePowerups != null)
        {

            for (int i = 0; i < activePowerups.size(); i++)
            {
                Powerups activePowerup = datasource.getPowerup(activePowerups.get(i));
                //Log.e("Active Powerups", activePowerup.getName());
                try {
                    expireDate = df.parse(activePowerup.getExpirationDate());

                }catch(Exception e){
                    //Do nothing
                }
                if (curDate.after(expireDate) && expireDate != null){
                    activePowerup.setInUse(0);
                    activePowerup.setExpirationDate("");
                    datasource.updatePowerup(activePowerup);
                }
                else if (curDate.before(expireDate) && expireDate != null){
                    //Log.e("here", activePowerup.getName());
                    if (activePowerup.getName().equals("Energy Bar")){
                        ebActive = true;
                        addedSteps++;
                        //Log.e("Modified value", Integer.toString(mCount));
                    }
                    else if (activePowerup.getName().equals("Sneakers")){
                        //Extra bonus step for every 5 steps
                        counter ++;
                        if (counter == 5) {
                            addedSteps += 2;
                            counter = 0;
                        }
                        else
                            addedSteps++;

                    }
                    else if (activePowerup.getName().equals("Rad Sneakers")){
                        //Extra bonus step for every 4 steps
                        counter ++;
                        if (counter == 4) {
                            addedSteps += 2;
                            counter = 0;
                        }
                        else
                            mCount++;

                    }
                    else if (activePowerup.getName().equals("Ultra Rad Sneakers")){
                        //Extra bonus step for every 3 steps
                        counter ++;
                        if (counter == 3) {
                            addedSteps += 2;
                            counter = 0;
                        }
                        else
                            mCount++;

                    }
                    else if (activePowerup.getName().equals("Walking Warrior")){
                        wwActive = true;
                        addedSteps++;

                    }
                    else if (activePowerup.getName().equals("Walkie Talkie")){
                        //Make sure this only happens once!
                        addedSteps++;
                        mCount += 10000000;
                    }
                }
            }
        }
        else {
            addedSteps ++;
        }

        if (ebActive) //Energy Bar active
            addedSteps = addedSteps * 4;

        if (wwActive) //Walking warrior active
            addedSteps = addedSteps * 2;

        mCount += addedSteps;
        notifyListener();
    }
    public void reloadSettings() {
        notifyListener();
    }
    public void passValue() {
    }

    //-----------------------------------------------------
    // Listener

    public interface Listener {
        public void stepsChanged(int value);
        public void passValue();
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }
    public void notifyListener() {
        for (Listener listener : mListeners) {
            listener.stepsChanged((int)mCount);
        }
    }

}
