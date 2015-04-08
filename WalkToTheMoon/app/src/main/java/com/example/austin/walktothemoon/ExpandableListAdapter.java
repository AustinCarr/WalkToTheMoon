package com.example.austin.walktothemoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Mariya on 2/23/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> powerUpCollections;
    private List<String> powerUp;

    private int stepsTaken;

    private UserDataSource datasource;

    public ExpandableListAdapter(Activity context, List<String> powerUp,
                                 Map<String, List<String>> powerUpCollections) {
        this.context = context;
        this.powerUpCollections = powerUpCollections;
        this.powerUp = powerUp;

        datasource = new UserDataSource(context);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return powerUpCollections.get(powerUp.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String child = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        Typeface tobiBlack;
        tobiBlack = Typeface.createFromAsset(context.getAssets(), "fonts/TobiBlack.otf");

        TextView item = (TextView) convertView.findViewById(R.id.text_view_description);
        item.setTypeface(tobiBlack);

        String[] powerUpPrices = context.getResources().getStringArray(R.array.power_up_prices);

        // Get number of steps from database!!
        datasource.open();
        User user = datasource.getUser();
        stepsTaken = user.getBoostedSteps();
        datasource.close();

        Button buyButton = (Button) convertView.findViewById(R.id.button_buy);
        buyButton.setTypeface(tobiBlack);


        if (stepsTaken < Integer.parseInt(powerUpPrices[groupPosition])) {
            buyButton.setBackgroundColor(Color.argb(0, 0, 0, 0));
            buyButton.setTextColor(Color.rgb(84, 157, 136));
            buyButton.setEnabled(false);
        }
        else if (stepsTaken >= Integer.parseInt(powerUpPrices[groupPosition])) {
            buyButton.setBackgroundResource(R.drawable.buy_button);
            buyButton.setTextColor(Color.WHITE);
            buyButton.setEnabled(true);
        }

        item.setText(child);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return powerUpCollections.get(powerUp.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return powerUp.get(groupPosition);
    }

    public int getGroupCount() {
        return powerUp.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        String[] powerUpPrices = context.getResources().getStringArray(R.array.power_up_prices);
        TypedArray icons = context.getResources().obtainTypedArray(R.array.power_up_icons);

        String itemName = (String) getGroup(groupPosition);
        String itemPrice = powerUpPrices[groupPosition];

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shop_item,
                    null);
        }

        Typeface tobiBlack;
        tobiBlack = Typeface.createFromAsset(context.getAssets(), "fonts/TobiBlack.otf");

        TextView item = (TextView) convertView.findViewById(R.id.text_view_item_name);
        item.setTypeface(tobiBlack);
        item.setText(itemName);


        TextView price = (TextView) convertView.findViewById(R.id.text_view_item_price);
        price.setTypeface(tobiBlack);
        price.setText(itemPrice + "\nsteps");

        ImageView icon = (ImageView) convertView.findViewById(R.id.image_view_item_pic);
        icon.setImageResource(icons.getResourceId(groupPosition, -1));

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}