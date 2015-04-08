package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ChooseProfilePic extends Activity implements View.OnTouchListener, SpringListener {

    private static double TENSION = 800;
    private static double DAMPER = 20; //friction

    private ImageView mImageToAnimate;
    private SpringSystem mSpringSystem;
    private Spring mSpring;

    private final int REQUEST_CODE_FOR_CAMERA = 0;
    private final int REQUEST_CODE_FOR_CROP = 1;
    private final int REQUEST_CODE_FOR_GALLERY = 2;

    private Uri imageUri;
    private boolean isTakenFromCamera;
    private ImageView profilePicture;

    int width, height, imageWidth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile_pic);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;


        ImageView profilePicBG = (ImageView) findViewById(R.id.image_view_profile_bg);
        ImageView profilePicFG = (ImageView) findViewById(R.id.image_view_profile_pic);
        ImageView speechBubble = (ImageView) findViewById(R.id.button_confirm);
        BitmapDrawable bd=(BitmapDrawable) this.getResources().getDrawable(R.drawable.astronaut);
        imageWidth = bd.getBitmap().getWidth();
        System.out.println(imageWidth);
        //layout.setY(-30f);
        //float screenHeight = layout.getHeight();
        TranslateAnimation animation = new TranslateAnimation(imageWidth, 0, 0, 0) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setDuration(600);
        profilePicBG.startAnimation(animation);
        profilePicFG.startAnimation(animation);
        speechBubble.startAnimation(animation);

        /* variables for changing profile picture */
        profilePicture = (ImageView) findViewById(R.id.image_view_profile_pic);
        isTakenFromCamera = false;

        // Creates Spring Animations

        mImageToAnimate = (ImageView) findViewById(R.id.button_confirm);
        mImageToAnimate.setOnTouchListener(this);

        mSpringSystem = SpringSystem.create();

        mSpring = mSpringSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);
    }

    public void onChangePicture(View v) {
        displayDialog(DialogFragment.DIALOG_FROM_CREATE_PROFILE);
    }

    public void displayDialog(int id) {
        android.app.DialogFragment fragment = DialogFragment.newInstance(id);
        fragment.show(getFragmentManager(),
                getString(R.string.dialog_fragment_tag_photo_picker));
    }

    public void onLookingGoodPressed(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.image_view_profile_pic);
        mImageView.buildDrawingCache();
        Bitmap bmap = mImageView.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    "ProfilePic", MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        ImageView profilePicBG = (ImageView) findViewById(R.id.image_view_profile_bg);
        ImageView profilePicFG = (ImageView) findViewById(R.id.image_view_profile_pic);
        ImageView speechBubble = (ImageView) findViewById(R.id.button_confirm);
        //layout.setY(-30f);
        //float screenHeight = layout.getHeight();
        TranslateAnimation animation = new TranslateAnimation(0, imageWidth, 0, 0) ;
        animation.setInterpolator((new
                AccelerateDecelerateInterpolator()));
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                ChooseProfilePic.super.onBackPressed();
                overridePendingTransition(0,0);
            }
        });
        animation.setDuration(600);
        profilePicBG.startAnimation(animation);
        profilePicFG.startAnimation(animation);
        speechBubble.startAnimation(animation);

    }


    /* Dialog helper methods */

    /* When a button is clicked in the camera dialog*/
    public void onCameraDialogItemSelected(int item) {

        if (item == DialogFragment.ID_PHOTO_PICKER_FROM_CAMERA)
            openCamera();
        else if (item == DialogFragment.ID_PHOTO_PICKER_FROM_PICTURES)
            openGallery();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSpring.setEndValue(0.75f);
                return true;
            case MotionEvent.ACTION_UP:
                mSpring.setEndValue(0f);
                onBackPressed();
                return true;
        }

        return false;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);
        mImageToAnimate.setScaleX(scale);
        mImageToAnimate.setScaleY(scale);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

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
}
