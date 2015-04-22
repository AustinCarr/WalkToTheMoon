package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
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
        final TextView dialogue1 = (TextView) findViewById(R.id.text_view_dialogue1);
        final TextView dialogue2 = (TextView) findViewById(R.id.text_view_dialogue2);
        final ImageView logo = (ImageView) findViewById(R.id.logo);


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

                rocketSide.setX(width);

                Animation fadeIn1 = new AlphaAnimation(0, 1);
                fadeIn1.setInterpolator(new AccelerateInterpolator());
                fadeIn1.setFillAfter(true);
                fadeIn1.setStartOffset(2000);
                fadeIn1.setDuration(500);
                dialogue1.startAnimation(fadeIn1);

                Animation fadeOut1 = new AlphaAnimation(1, 0);
                fadeOut1.setInterpolator(new AccelerateInterpolator());
                fadeOut1.setFillAfter(true);
                fadeOut1.setStartOffset(4500);
                fadeOut1.setDuration(500);
                fadeOut1.setAnimationListener(new Animation.AnimationListener() {
                      @Override
                      public void onAnimationStart(Animation arg0) {
                      }

                      @Override
                      public void onAnimationRepeat(Animation arg0) {
                      }

                      @Override
                      public void onAnimationEnd(Animation arg0) {

                          Animation fadeIn2 = new AlphaAnimation(0, 1);
                          fadeIn2.setInterpolator(new AccelerateInterpolator());
                          fadeIn2.setFillAfter(true);
                          fadeIn2.setStartOffset(3000);
                          fadeIn2.setDuration(1000);
                          dialogue2.startAnimation(fadeIn2);

                          Animation fadeOut2 = new AlphaAnimation(1, 0);
                          fadeOut2.setInterpolator(new AccelerateInterpolator());
                          fadeOut2.setFillAfter(true);
                          fadeOut2.setStartOffset(4500);
                          fadeOut2.setDuration(500);
                          dialogue2.startAnimation(fadeOut2);

                          TranslateAnimation astroPopDown = new TranslateAnimation(0, 0, 0, (height)) ;
                          astroPopDown.setInterpolator((new AccelerateDecelerateInterpolator()));
                          astroPopDown.setFillAfter(true);
                          astroPopDown.setStartOffset(5000);
                          astroPopDown.setDuration(1000);
                          astroHelmet.startAnimation(astroPopDown);
                          astroFace.startAnimation(astroPopDown);

                          Animation fadeOut = new AlphaAnimation(1, 0);
                          fadeOut.setInterpolator(new AccelerateInterpolator());
                          fadeOut.setFillAfter(true);
                          fadeOut.setStartOffset(6000);
                          fadeOut.setDuration(1000);
                          smoke.startAnimation(fadeOut);

                      }
                  });
                dialogue1.startAnimation(fadeOut1);
            }
        });
        astroHelmet.startAnimation(astroPopUp);
        astroFace.startAnimation(astroPopUp);

        //Fade in logo
        Animation fadeInLogo = new AlphaAnimation(0, 1);
        fadeInLogo.setInterpolator(new AccelerateInterpolator());
        fadeInLogo.setFillAfter(true);
        fadeInLogo.setStartOffset(21500);
        fadeInLogo.setDuration(2000);
        fadeInLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                Animation fadeOutLogo = new AlphaAnimation(1, 0);
                fadeOutLogo.setInterpolator(new AccelerateInterpolator());
                fadeOutLogo.setFillAfter(true);
                fadeOutLogo.setStartOffset(3000);
                fadeOutLogo.setDuration(2000);
                fadeOutLogo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(LaunchAnimation.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);

                    }
                });
                logo.startAnimation(fadeOutLogo);
            }
        });
        logo.startAnimation(fadeInLogo);




        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");


        dialogue1.setTypeface(tobiBlack);
        dialogue2.setTypeface(tobiBlack);


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
