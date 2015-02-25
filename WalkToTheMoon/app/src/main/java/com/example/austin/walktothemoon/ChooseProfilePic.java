package com.example.austin.walktothemoon;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;


public class ChooseProfilePic extends Activity {

    private final int REQUEST_CODE_FOR_CAMERA = 0;
    private final int REQUEST_CODE_FOR_CROP = 1;
    private final int REQUEST_CODE_FOR_GALLERY = 2;

    private Uri imageUri;
    private boolean isTakenFromCamera;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile_pic);

        /* variables for changing profile picture */
        profilePicture = (ImageView) findViewById(R.id.image_view_profile_pic);
        isTakenFromCamera = false;
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
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
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
