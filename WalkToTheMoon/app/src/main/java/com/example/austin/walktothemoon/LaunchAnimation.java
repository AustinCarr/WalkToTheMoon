package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class LaunchAnimation extends Activity {

    private UserDataSource datasource;
    private TextView stateView, messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_animation);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

                Intent intent = new Intent(LaunchAnimation.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }

        }, 1000);


        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");


        datasource = new UserDataSource(this);
        datasource.open();
        User user = datasource.getUser();

        stateView = (TextView) findViewById(R.id.text_view_user_location);
        stateView.setTypeface(tobiBlack);
        stateView.setText(String.valueOf(user.getAddressState()));
        datasource.close();

        messageView = (TextView) findViewById(R.id.message);
        messageView.setTypeface(tobiBlack);




    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
