package com.felgueiras.apps.geriatrichelper.PhotoVideoHandling;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordVideoActivity extends AppCompatActivity {
    // Activity request codes

    static final int REQUEST_TAKE_PHOTO = 1;
    public static final String SCALE_ID = "SCALE_ID";


    private Uri fileUri; // file url to store image/video


    private VideoView videoPreview;

    private static Activity context;
    private GeriatricScaleFirebase scale;
    private String videoFileName;
    private String mCurrentVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        context = this;

        // get the associated scale
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String scaleID = bundle.getString(SCALE_ID);

        // fetch scale
        scale = FirebaseDatabaseHelper.getScaleByID(scaleID);

        // it there is already an image associated to the scale
        if (scale != null && scale.getVideoPath() != null) {
            Log.d("Firebase", "Already has video, fetching...");
            fetchVideoFirebaseDisplay();
        }

        videoPreview = (VideoView) findViewById(R.id.videoPreview);
        Button btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);


        /*
          Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                verifyVideoPermission();
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    /**
     * Fetch video from firebase and display it.
     */
    private void fetchVideoFirebaseDisplay() {
        // fetch image from Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/videos/" + scale.getVideoPath());


        try {
            final File videoFile = File.createTempFile("video", "mp4");
            storageRef.getFile(videoFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    videoPreview.setVisibility(View.VISIBLE);
//                    videoPreview.setVideoURI(videoUri);
                    videoPreview.setVideoPath(videoFile.getAbsolutePath());

                    // attach MediaController
                    MediaController mediaController = new MediaController(context);
                    mediaController.setAnchorView(videoPreview);
                    videoPreview.setMediaController(mediaController);

                    // start playing
                    videoPreview.start();
                    Log.d("Firebase", "Setting video");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof com.google.firebase.storage.StorageException) {
                        // scale was not found for that language
                        Log.d("Download", "Video does not exist");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void verifyVideoPermission() {
        // Check permission for CAMERA
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAPTURE_VIDEO_OUTPUT)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAPTURE_VIDEO_OUTPUT,
                            Manifest.permission.CAMERA},
                    REQUEST_TAKE_PHOTO);
        } else {
            recordVideo();
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

    static final int REQUEST_VIDEO_CAPTURE = 1;

    /**
     * Record a video
     */
    private void recordVideo() {
        Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (recordVideoIntent.resolveActivity(getPackageManager()) != null) {

            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            Uri videoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider",
                    videoFile);
            recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);

            startActivityForResult(recordVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // if the result is capturing Image
        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                Uri videoUri = intent.getData();
                previewVideo(videoUri);

                // display and upload video to Firebase
                displayAndUploadVideo(videoUri);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Previewing recorded video
     *
     * @param videoUri
     */
    private void previewVideo(Uri videoUri) {
        try {

            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoURI(videoUri);

            // attach MediaController
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoPreview);
            videoPreview.setMediaController(mediaController);

            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a video file where video is saved locally
     *
     * @return
     * @throws IOException
     */
    private File createVideoFile() throws IOException {
        // Create an video file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentVideoPath = video.getAbsolutePath();
        return video;
    }

    /**
     * Display and upload video to Firebase.
     *
     * @param videoUri
     */
    private void displayAndUploadVideo(Uri videoUri) {

        try {
            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoURI(videoUri);

            // attach MediaController
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoPreview);
            videoPreview.setMediaController(mediaController);

            // start playing
            videoPreview.start();

            // TODO compress the video
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bmp.compress(CompressFormat.JPEG, 0, bos);
//            InputStream stream = new ByteArrayInputStream(bos.toByteArray());

            // read video from file
            InputStream stream = null;
            try {
                stream = new FileInputStream(new File(mCurrentVideoPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            final String fileName = videoFileName + ".mp4";


            // upload image to firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/videos/" + fileName);

            UploadTask uploadTask = storageReference.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Firebase", "Video updated successfully");
                    scale.setVideoPath(fileName);
                    FirebaseDatabaseHelper.updateScale(scale);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}