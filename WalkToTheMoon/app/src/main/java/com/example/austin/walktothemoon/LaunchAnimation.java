package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.FileInputStream;
import java.io.IOException;
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

        final ImageView rocketImage = (ImageView) findViewById(R.id.image_view_rocket_takeoff);
        final ImageView smoke = (ImageView) findViewById(R.id.image_view_smoke);
        final ImageView rocketSideFuel = (ImageView) findViewById(R.id.image_view_rocket_side_fuel);
        final ImageView rocketSide = (ImageView) findViewById(R.id.image_view_rocket_side);
        final ImageView astroHelmet = (ImageView) findViewById(R.id.image_view_launch_face_helmet);
        final ImageView astroFace = (ImageView) findViewById(R.id.image_view_launch_face);

        //Change face to user-chosen face
        try
        {
            FileInputStream fis = openFileInput("ProfilePic");
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            astroFace.setImageBitmap(bmap);
            fis.close();
        } catch (IOException e) {
        }

        //Rocket launches up off screen
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, (0 - height)) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setStartOffset(1000);
        animation.setDuration(3000);
        rocketImage.startAnimation(animation);

        //Smoke fades out to reveal stars
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(4000);
        fadeOut.setDuration(1000);
        smoke.startAnimation(fadeOut);

        //Rocket comes in from the left and stops in middle of screen
        TranslateAnimation rocketTranslate = new TranslateAnimation((0 - width), (width / 2), 0, 0);
        rocketTranslate.setInterpolator((new DecelerateInterpolator(5f)));
        rocketTranslate.setFillAfter(true);
        rocketTranslate.setStartOffset(6000);
        rocketTranslate.setDuration(2000);
        rocketSide.startAnimation(rocketTranslate);

        //Rocket fuel flame fades out
        Animation fuelFadeOut = new AlphaAnimation(1, 0);
        fuelFadeOut.setInterpolator(new AccelerateInterpolator());
        fuelFadeOut.setFillAfter(true);
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
                //smoke.setVisibility(View.VISIBLE);
                rocketSideFuel.setVisibility(View.INVISIBLE);
                
                /*
                Intent intent = new Intent(LaunchAnimation.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                 */


                //Smoke fades in to hide stars
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new AccelerateInterpolator());
                fadeIn.setFillAfter(true);
                fadeIn.setStartOffset(500);
                fadeIn.setDuration(250);
                smoke.startAnimation(fadeIn);
            }
        });

        AnimationSet loseGas = new AnimationSet(true);
        loseGas.addAnimation(fuelFadeOut);
        loseGas.addAnimation(rocketTranslate);

        rocketSideFuel.startAnimation(loseGas);




        //Astronaut pops up from bottom
        TranslateAnimation astroPopUp = new TranslateAnimation(0, 0, height, 0) ;
        astroPopUp.setInterpolator((new AccelerateDecelerateInterpolator()));
        astroPopUp.setFillAfter(true);
        astroPopUp.setStartOffset(8500);
        astroPopUp.setDuration(1000);
        astroPopUp.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {


                /*
                Intent intent = new Intent(LaunchAnimation.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); */


            }
        });
        astroHelmet.startAnimation(astroPopUp);
        astroFace.startAnimation(astroPopUp);

        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");


        /*datasource = new UserDataSource(this);
        datasource.open();
        User user = datasource.getUser();


        stateView = (TextView) findViewById(R.id.text_view_user_location);
        stateView.setTypeface(tobiBlack);
        stateView.setText(String.valueOf(user.getAddressState()));
        stateView.setVisibility(View.VISIBLE);
        datasource.close();

        messageView = (TextView) findViewById(R.id.message);
        messageView.setTypeface(tobiBlack);
        messageView.setVisibility(View.VISIBLE);*/




    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }

    @Override
    protected void onResume() {
        //datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //datasource.close();
        super.onPause();
    }
}
