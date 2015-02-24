package com.example.austin.walktothemoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class DialogFragment extends android.app.DialogFragment {

    public static final int DIALOG_FROM_CREATE_PROFILE = 0;
    public static final int DIALOG_FROM_EDIT_PROFILE = 1;
    public static final int DIALOG_FROM_SHOP = 2;

    public static final int ID_PHOTO_PICKER_FROM_CAMERA = 0;
    public static final int ID_PHOTO_PICKER_FROM_PICTURES = 1;
    public static final int ID_SHOP_PURCHASE = 0;
    public static final int ID_SHOP_CANCEL_PURCHASE = 1;

    private static final String DIALOG_ID_KEY = "dialog_id";

    public static DialogFragment newInstance(int dialog_id) {
        DialogFragment frag = new DialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ID_KEY, dialog_id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dialog_id = getArguments().getInt(DIALOG_ID_KEY);

        final Activity parent = getActivity();

        AlertDialog.Builder builder;
        DialogInterface.OnClickListener listener;

        switch (dialog_id) {
            case DIALOG_FROM_CREATE_PROFILE:
                builder = new AlertDialog.Builder(parent);
                builder.setTitle("Select profile picture");
                listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ((ChooseProfilePic) parent).onCameraDialogItemSelected(item);
                    }
                };

                builder.setItems(R.array.camera_dialog_items, listener);
                return builder.create();

            case DIALOG_FROM_EDIT_PROFILE:
                builder = new AlertDialog.Builder(parent);
                builder.setTitle("Change profile picture");
                listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ((Profile) parent).onCameraDialogItemSelected(item);
                    }
                };

                builder.setItems(R.array.camera_dialog_items, listener);
                return builder.create();

            case DIALOG_FROM_SHOP:
                builder = new AlertDialog.Builder(parent);
                builder.setTitle("Confirm purchase");
                listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ((Shop) parent).onCameraDialogItemSelected(item);
                    }
                };

                builder.setItems(R.array.shop_dialog_items, listener);
                return builder.create();



            default:
                return null;

        }
    }
}
