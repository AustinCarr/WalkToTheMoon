package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ChooseProfilePic extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile_pic);
    }

    public void onChangePicture(View v) {
        //Intent intent = new Intent(this, ChooseProfilePic.class);
        //startActivity(intent);

    }

    public void onLaunchPressed(View v) {
        Intent intent = new Intent(this, LaunchAnimation.class);
        startActivity(intent);

    }
}
