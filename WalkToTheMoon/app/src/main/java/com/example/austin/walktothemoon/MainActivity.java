package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");

        TextView textview = (TextView) findViewById(R.id.text_view_steps_count);
        textview.setTypeface(tobiBlack);
        textview = (TextView) findViewById(R.id.text_view_steps_text);
        textview.setTypeface(tobiBlack);
    }

    public void onShopPressed(View v) {
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void onProfilePressed(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
