package com.example.austin.walktothemoon;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
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
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("Item1");
        groupList.add("Item2");
        groupList.add("Item3");
        groupList.add("Item4");
        groupList.add("Item5");
        groupList.add("Item6");
    }

    private void createCollection() {

        /* A list of power ups */
        String[] item1 = { "This is a really long description for item 1.  It will expire in 45 days.  Please buy now.  Huge sale.  This is a test. To make the description really long.  Bye now.  GOOOOOD" };
        String[] item2 = { "DESCRIPTION" };
        String[] item3 = { "DESCRIPTION" };
        String[] item4 = { "DESCRIPTION" };
        String[] item5 = { "DESCRIPTION" };
        String[] item6 = { "DESCRIPTION" };

        powerUpCollection = new LinkedHashMap<>();

        /* Add power ups into collection */
        for (String powerUp : groupList) {
            if (powerUp.equals("Item1")) {
                loadChild(item1);
            } else if (powerUp.equals("Item2"))
                loadChild(item2);
            else if (powerUp.equals("Item3"))
                loadChild(item3);
            else if (powerUp.equals("Item4"))
                loadChild(item4);
            else if (powerUp.equals("Item5"))
                loadChild(item5);
            else
                loadChild(item6);

            powerUpCollection.put(powerUp, childList);
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
