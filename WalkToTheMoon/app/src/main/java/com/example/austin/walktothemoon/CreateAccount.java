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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
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


public class CreateAccount extends Activity implements View.OnTouchListener, SpringListener {

    private static double TENSION = 800;
    private static double DAMPER = 20; //friction

    private ImageView mImageToAnimate;
    private SpringSystem mSpringSystem;
    private Spring mSpring;

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


        // Creates Spring Animations

        mImageToAnimate = (ImageView) findViewById(R.id.image_view_profile_pic);
        mImageToAnimate.setOnTouchListener(this);

        mSpringSystem = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);
    }

    public void onNextClicked(View v) {
        Intent intent = new Intent(this, ChooseProfilePic.class);
        startActivity(intent);
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
