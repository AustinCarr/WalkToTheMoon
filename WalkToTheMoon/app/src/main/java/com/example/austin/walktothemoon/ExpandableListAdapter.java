package com.example.austin.walktothemoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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

    public ExpandableListAdapter(Activity context, List<String> powerUp,
                                 Map<String, List<String>> powerUpCollections) {
        this.context = context;
        this.powerUpCollections = powerUpCollections;
        this.powerUp = powerUp;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return powerUpCollections.get(powerUp.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.text_view_description);


        item.setText(laptop);
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

        String itemName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shop_item,
                    null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.text_view_item_name);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(itemName);

        /* THIS IS WHERE YOU SET THE PRICE OF EACH ONE */
        TextView price = (TextView) convertView.findViewById(R.id.text_view_item_price);
        price.setTypeface(null, Typeface.BOLD);
        price.setText("4000 steps");

        ImageView icon = (ImageView) convertView.findViewById(R.id.image_view_item_pic);
        /* SET THE IMAGE SOMEHOW */

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}