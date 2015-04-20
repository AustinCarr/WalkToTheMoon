package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;

public class Profile extends Activity {

    private final int REQUEST_CODE_FOR_CAMERA = 0;
    private final int REQUEST_CODE_FOR_CROP = 1;
    private final int REQUEST_CODE_FOR_GALLERY = 2;

    private Uri imageUri;
    private boolean isTakenFromCamera;
    private ImageView profilePicture;
    private android.app.DialogFragment fragment;

    private UserDataSource datasource;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* variables for changing profile picture */

        ImageView profilePictureView = (ImageView) findViewById(R.id.image_view_profile_pic);
        //ImageView profilePictureHelmetView = (ImageView) findViewById(R.id.image_view_profile_pic_helmet);

        try
        {
            //profilePictureView.setImageResource(R.drawable.licensehelmet);
            //profilePictureView.setBackgroundColor(Color.TRANSPARENT);

            float density = getResources().getDisplayMetrics().density;
            int xDpValue = 25; // margin in dips
            int xPixelValue = (int)(xDpValue * density);

            int yDpValue = 25; // margin in dips
            int YPixelValue = (int)(yDpValue * density);
            //profilePictureHelmetView.getLayoutParams().height = margin;

            FileInputStream fis = openFileInput("ProfilePic");
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            profilePictureView.setImageBitmap(bmap);
            profilePictureView.setX(xPixelValue * -1);
            profilePictureView.setY(YPixelValue);
            fis.close();
        } catch (IOException e) {
            //profilePictureView.setImageResource(R.drawable.camera_icon);
        }

        //db stuff
        datasource = new UserDataSource(this);
        datasource.open();
        user = datasource.getUser();
        //User user = dbHandler.getUser(CreateAccount.user.getLicenseId());
        /*SharedPreferences prefs = getSharedPreferences("WalkToTheMoonPref", MODE_PRIVATE);
        String restoredText = prefs.getString("license_id", null);
        User user = dbHandler.getUser(restoredText);*/

        Typeface tobiBlack;

        tobiBlack = Typeface.createFromAsset(getAssets(), "fonts/TobiBlack.otf");

        TextView textview = (TextView) findViewById(R.id.text_view_steps_count);
        textview.setTypeface(tobiBlack);
        textview.setText(String.valueOf(user.getRealSteps()));

        textview = (TextView) findViewById(R.id.text_view_steps_text);
        textview.setTypeface(tobiBlack);

        textview = (TextView) findViewById(R.id.text_profileName);
        textview.setTypeface(tobiBlack);
        textview.setText(String.valueOf(user.getName()));

        textview = (TextView) findViewById(R.id.text_view_steps_fact);
        textview.setTypeface(tobiBlack);
        textview.setText(NumberFormat.getIntegerInstance().format(478000000 - user.getBoostedSteps()) + " more steps to the moon");
    }

    public void onChangePicture(View view) {
        displayDialog(DialogFragment.DIALOG_FROM_EDIT_PROFILE);
    }

    public void displayDialog(int id) {
        fragment = DialogFragment.newInstance(id);
        fragment.show(getFragmentManager(),
                getString(R.string.dialog_fragment_tag_photo_picker));
    }


        /* Dialog helper methods */

    /* When a button is clicked in the camera dialog*/
    public void onDialogItemSelected(int item) {

        if (item == DialogFragment.ID_PHOTO_PICKER_FROM_CAMERA)
            openCamera();
        else if (item == DialogFragment.ID_PHOTO_PICKER_FROM_PICTURES)
            openGallery();

        /* code to edit name in profile
        else if (item == DialogFragment.ID_SAVE_NAME)
            Toast.makeText(getBaseContext(), "SAVED", Toast.LENGTH_LONG).show();
        else if (item == DialogFragment.ID_CANCEL)
            Toast.makeText(getBaseContext(), "CANCELLED", Toast.LENGTH_LONG).show();
            */
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", true);

        try {
            startActivityForResult(intent, REQUEST_CODE_FOR_CAMERA);
        } catch(ActivityNotFoundException e) {
            e.printStackTrace();
        }

        isTakenFromCamera = true;
    }

    // Crop and resize the image for profile
    private void cropImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, REQUEST_CODE_FOR_CROP);
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_FOR_CAMERA:
                cropImage();
                break;

            case REQUEST_CODE_FOR_CROP:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    profilePicture.setImageBitmap((Bitmap) extras.getParcelable("data"));
                }

                // Delete temporary image taken by camera after crop.
                if (isTakenFromCamera) {
                    File f = new File(imageUri.getPath());
                    if (f.exists())
                        f.delete();

                    isTakenFromCamera = false;
                }
                break;

            case REQUEST_CODE_FOR_GALLERY:
                /* If we don't want to crop the chosen image, use this code

                Uri uri = data.getData();
                String realPath = getRealPathFromURI(uri);

                profilePicture.setImageBitmap(BitmapFactory.decodeFile(realPath));
                */

                imageUri = data.getData();
                cropImage();

                break;
        }
    }

    /* code to edit name in profile
    public void onNameClicked(View view) {
        displayDialog(DialogFragment.DIALOG_FROM_EDIT_PROFILE_NAME);
    }


    public void onSaveClicked(View view) {
        EditText newName = (EditText) findViewById(R.id.new_name);
        user.setName(newName.getText().toString());
        datasource.updateUser(user);

        TextView nameView = (TextView) findViewById(R.id.text_profileName);
        nameView.setText(user.getName());

        datasource.close();

        fragment.dismiss();
    }

    public void onCancelClicked(View view) {

        fragment.dismiss();
    }
    */

    /* If we don't want to crop the chosen image, we will need this method
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = new String[] { android.provider.MediaStore.Images.ImageColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filename = cursor.getString(column_index);
        cursor.close();
        return filename;
    }
    */

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
