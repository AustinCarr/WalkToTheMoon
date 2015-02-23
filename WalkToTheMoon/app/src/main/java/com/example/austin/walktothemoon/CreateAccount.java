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
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class CreateAccount extends Activity {

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
    }

    public void onNextClicked(View v) {
        Intent intent = new Intent(this, ChooseProfilePic.class);
        startActivity(intent);
    }
}
