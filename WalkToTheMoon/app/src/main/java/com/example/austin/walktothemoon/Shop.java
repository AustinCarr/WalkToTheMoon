package com.example.austin.walktothemoon;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Shop extends Activity {

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> powerUpCollection;
    ExpandableListView expListView;

    private PowerupsDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.items_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, powerUpCollection);
        expListView.setAdapter(expListAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        expListView.setMinimumWidth(width);

        //setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });

        //db stuff
        datasource = new PowerupsDataSource(this);
        datasource.open();
        String[] powerupNames = {"powerup1", "powerup2"};
        Powerups powerup;
        for (int i = 0; i < powerupNames.length; i++) {
            powerup = datasource.getPowerup(powerupNames[i]);
            //set fields accordingly for that powerup
        }

        Typeface tobiBlack;
        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");

        TextView shop = (TextView) findViewById(R.id.text_view_shop);
        shop.setTypeface(tobiBlack);

        TextView steps = (TextView) findViewById(R.id.text_view_steps_count);
        steps.setTypeface(tobiBlack);

        TextView stepsText = (TextView) findViewById(R.id.text_view_steps_text);
        stepsText.setTypeface(tobiBlack);

    }

    private void createGroupList() {

        String[] items = getResources().getStringArray(R.array.power_up_names);

        groupList = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            groupList.add(items[i]);
        }
    }

    private void createCollection() {

        String[] descriptions = getResources().getStringArray(R.array.power_up_descriptions);

        powerUpCollection = new LinkedHashMap<>();

        String[] temp = {"DESCRIPTION"};
        for (int i = 0; i < groupList.size(); i++) {
            temp[0] = descriptions[i];
            loadChild(temp);

            powerUpCollection.put(groupList.get(i), childList);
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    /**
     * Do something when one of the buttons in the dialog is clicked
     * @param item - the button clicked
     */
    public void onDialogItemSelected(int item) {

        if (item == DialogFragment.ID_SHOP_PURCHASE)
            Toast.makeText(getBaseContext(), "PURCHASED", Toast.LENGTH_LONG).show();
        else if (item == DialogFragment.ID_SHOP_CANCEL_PURCHASE)
            Toast.makeText(getBaseContext(), "CANCELLED", Toast.LENGTH_LONG).show();
    }


    /**
     * When buy button is clicked, show the buy dialog
     */
    public void onBuyClicked(View view) {
        displayDialog(DialogFragment.DIALOG_FROM_SHOP);
    }
    public void displayDialog(int id) {
        android.app.DialogFragment fragment = DialogFragment.newInstance(id);
        fragment.show(getFragmentManager(),
                getString(R.string.dialog_fragment_tag_photo_picker));
    }


}
