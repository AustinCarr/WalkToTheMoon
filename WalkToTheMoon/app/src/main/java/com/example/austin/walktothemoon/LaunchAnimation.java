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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class LaunchAnimation extends Activity {

    private UserDataSource datasource;
    private TextView stateView, messageView;
    private boolean resumingActivity = false;
    private int width, height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_animation);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        ImageView rocketImage = (ImageView) findViewById(R.id.image_view_rocket_takeoff);
        final ImageView smoke = (ImageView) findViewById(R.id.image_view_smoke);
        final ImageView rocketSideFuel = (ImageView) findViewById(R.id.image_view_rocket_side_fuel);
        final ImageView rocketSide = (ImageView) findViewById(R.id.image_view_rocket_side);

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (0 - height)) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setStartOffset(1000);
        animation.setDuration(3000);
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

                }, 3000);
            }
        });
        rocketImage.startAnimation(animation);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(4000);
        fadeOut.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                smoke.setVisibility(View.INVISIBLE);
            }
        });
        smoke.startAnimation(fadeOut);

        TranslateAnimation rocketTranslate = new TranslateAnimation((0 - width), (width / 2), 0, 0);
        rocketTranslate.setInterpolator((new DecelerateInterpolator(5f)));
        rocketTranslate.setFillAfter(true);
        rocketTranslate.setStartOffset(6000);
        rocketTranslate.setDuration(2000);
        rocketTranslate.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {

            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {



            }
        });
        rocketSide.startAnimation(rocketTranslate);




        Animation fuelFadeOut = new AlphaAnimation(1, 0);
        fuelFadeOut.setInterpolator(new AccelerateInterpolator());
        fuelFadeOut.setStartOffset(6500);
        fuelFadeOut.setDuration(1000);
        fuelFadeOut.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                rocketSideFuel.setVisibility(View.INVISIBLE);
            }
        });

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fuelFadeOut);
        animationSet.addAnimation(rocketTranslate);

        rocketSideFuel.startAnimation(animationSet);

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
