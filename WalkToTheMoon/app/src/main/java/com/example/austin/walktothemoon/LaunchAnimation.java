package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class LaunchAnimation extends Activity {

    private UserDataSource datasource;
    private TextView stateView, messageView;
    private boolean resumingActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_animation);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (0 - height/3)) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setDuration(4000);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    public void run() {

                        Intent intent = new Intent(LaunchAnimation.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }

                }, 4000);
            }
        });
        layout.startAnimation(animation);






        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");


        datasource = new UserDataSource(this);
        datasource.open();
        User user = datasource.getUser();


        stateView = (TextView) findViewById(R.id.text_view_user_location);
        stateView.setTypeface(tobiBlack);
        stateView.setText(String.valueOf(user.getAddressState()));
        stateView.setVisibility(View.VISIBLE);
        datasource.close();

        messageView = (TextView) findViewById(R.id.message);
        messageView.setTypeface(tobiBlack);
        messageView.setVisibility(View.VISIBLE);




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
