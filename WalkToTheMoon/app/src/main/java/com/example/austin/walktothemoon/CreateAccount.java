package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;


public class CreateAccount extends Activity implements View.OnTouchListener, SpringListener {

    private static double TENSION = 800;
    private static double DAMPER = 20; //friction

    private ImageView mImageToAnimate;
    private SpringSystem mSpringSystem;
    private Spring mSpring;

    private boolean resumingActivity = false;

    private Typeface tobiBlack;
    private String list[]={"Alabama","Arkansas‹"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);
        //layout.setY(-30f);
        //float screenHeight = layout.getHeight();
        TranslateAnimation animation = new TranslateAnimation(0, 0, (height * -1), 0) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setDuration(600);
        layout.startAnimation(animation);

        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");

        TextView textview = (TextView) findViewById(R.id.label_ID);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.value_ID);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.label_IssueDate);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.value_IssueDate);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.label_Permissions);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.value_Permissions);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.label_name);
        textview.setTypeface(tobiBlack);

        EditText editText = (EditText) findViewById(R.id.edit_text_name);
        editText.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.label_address);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.text_view_dob);
        textview.setTypeface(tobiBlack);

        //Customized Spinner

        String[] states = getResources().getStringArray(R.array.spinner_states);
        String[] days = getResources().getStringArray(R.array.spinner_day);
        String[] months = getResources().getStringArray(R.array.spinner_month);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_design, states) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
        };

        MySpinnerAdapter<String> dayAdapter = new MySpinnerAdapter<String>(this, R.layout.spinner_design, days);
        MySpinnerAdapter<String> monthAdapter = new MySpinnerAdapter<String>(this, R.layout.spinner_design, months);

        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner daySpinner = (Spinner) findViewById(R.id.spinner_day);
        daySpinner.setAdapter(dayAdapter);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner monthSpinner = (Spinner) findViewById(R.id.spinner_month);
        monthSpinner.setAdapter(monthAdapter);




        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_location);
        spinner.setAdapter(adapter);

        // Creates Spring Animations

        mImageToAnimate = (ImageView) findViewById(R.id.image_view_profile_pic);
        mImageToAnimate.setOnTouchListener(this);

        mSpringSystem = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);
    }

    private class MySpinnerAdapter<String> extends ArrayAdapter<String> {

        public MySpinnerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");
            ((TextView) v).setTypeface(externalFont);

            return v;
        }


        public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
            View v =super.getDropDownView(position, convertView, parent);

            Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");
            ((TextView) v).setTypeface(externalFont);

            return v;
        }
    }

    public void onNextClicked(View v) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);

        TranslateAnimation animation = new TranslateAnimation(0, (0-width), 0, 0) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setDuration(600);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(CreateAccount.this, ChooseProfilePic.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                resumingActivity = true;
                startActivity(intent);
            }
        });
        layout.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(resumingActivity) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);

            TranslateAnimation animation = new TranslateAnimation((0 - width), 0, 0, 0);
            animation.setInterpolator((new
                    AccelerateDecelerateInterpolator()));
            animation.setFillAfter(true);
            animation.setDuration(600);
            layout.startAnimation(animation);
        }
    }

    public void onLaunchPressed(View v) {
        Intent intent = new Intent(this, LaunchAnimation.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSpring.setEndValue(0.75f);
                return true;
            case MotionEvent.ACTION_UP:
                mSpring.setEndValue(0f);
                return true;
        }

        return false;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);
        mImageToAnimate.setScaleX(scale);
        mImageToAnimate.setScaleY(scale);
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        onNextClicked(findViewById(R.id.image_view_profile_pic));
    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {



    }

}
