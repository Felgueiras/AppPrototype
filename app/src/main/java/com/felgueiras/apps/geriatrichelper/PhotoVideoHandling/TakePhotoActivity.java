package com.felgueiras.apps.geriatrichelper.PhotoVideoHandling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakePhotoActivity extends AppCompatActivity {
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    static final int REQUEST_TAKE_PHOTO = 1;

    // directory name to store captured images and videos
    public static final String SCALE_ID = "SCALE_ID";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;

    private String imageFileName;
    private GeriatricScaleFirebase scale;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        // setup views
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);

        activity = this;

        // get the associated scale
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String scaleID = bundle.getString(SCALE_ID);

        // fetch scale
        scale = FirebaseDatabaseHelper.getScaleByID(scaleID);

        // it there is already an image associated to the scale
        if (scale != null && scale.getPhotoPath() != null) {
            // load animation
            progressBar.setVisibility(View.VISIBLE);
            FirebaseStorageHelper.fetchImageFirebaseDisplay(scale, imgPreview, progressBar);
        }

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // close the activity if the device does't have camera
            finish();
        }

        // Capture image button click event
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                verifyPhotoPermission(getApplicationContext());
            }
        });


    }

    /**
     * Verify if there is permission to take photoDownloaded.
     *
     * @param context current context
     */
    public void verifyPhotoPermission(Context context) {
        // Check permission for CAMERA
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_TAKE_PHOTO);
        } else {
            takePicture();
        }
    }

    /**
     * Handle request permission results.
     *
     * @param requestCode requestCode
     * @param permissions permissions as a String
     * @param grantResults permission grant results
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
//                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                        takePicture();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (shouldShowRequestPermissionRationale(CAMERA)) {
//                                showMessageOKCancel("You need to allow access to both the permissions",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermissions(new String[]{CAMERA},
//                                                            REQUEST_CAMERA);
//                                                }
//                                            }
//                                        });
//                                return;
//                            }
//                        }
//                    }
                    }
                }
                break;
        }
    }


    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        Log.d("Result", resultCode + "");
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE || requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {

                // display and update image
                displayAndUploadImage();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Take a picture and upload it to Firebase.
     */
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photoDownloaded should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;

    /**
     * Create an image file where image is saved locally
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Display image from a path to ImageView
     */
    private void displayAndUploadImage() {

        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
//        options.inSampleSize = 8;

        // display it in image view
        final Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath,
                options);
        try {
            imgPreview.setVisibility(View.VISIBLE);
            imgPreview.setImageBitmap(bitmap);
            final String fileName = imageFileName + ".jpg";

            // compress the image
            Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int imageQuality = 20;
            bmp.compress(Bitmap.CompressFormat.JPEG, imageQuality, bos);
            InputStream stream = new ByteArrayInputStream(bos.toByteArray());

            // upload image to firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/images/" + fileName);

            UploadTask uploadTask = storageReference.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Firebase", "Photo updated successfully");
                    scale.setPhotoPath(fileName);
                    FirebaseDatabaseHelper.updateScale(scale);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}