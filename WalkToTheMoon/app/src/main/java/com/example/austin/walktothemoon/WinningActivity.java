package com.example.austin.walktothemoon;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class WinningActivity extends Activity {

    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        message = (TextView) findViewById(R.id.text_view_congrats);

        Typeface tobiBlack;
        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");
        message.setTypeface(tobiBlack);
    }



}
