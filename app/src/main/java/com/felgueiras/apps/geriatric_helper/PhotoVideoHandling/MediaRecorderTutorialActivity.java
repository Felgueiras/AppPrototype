package com.felgueiras.apps.geriatric_helper.PhotoVideoHandling;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ToggleButton;

import com.felgueiras.apps.geriatric_helper.R;

import java.io.File;
import java.io.IOException;

public class MediaRecorderTutorialActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String VIDEO_PATH_NAME = "/Pictures/test.3gp";
    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private boolean mInitSuccesful;
    private Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.felgueiras.apps.geriatric_helper.R.layout.media_recorder_recipe);

        context = this;


        // we shall take the video in landscape orientation
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();

//        mMediaRecorder.setPreviewDisplay(surface);
//        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

        SurfaceView mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
//        mMediaRecorder.setCamera(mCamera);

        View mToggleButton = findViewById(R.id.toggleRecordingButton);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked())
                    mMediaRecorder.start();
                else {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    try {
                        initRecorder(mHolder.getSurface());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    static final int REQUEST_TAKE_PHOTO = 1;


    /* Init the MediaRecorder, the order the methods are called is vital to
     * its correct functioning */
    private void initRecorder(Surface surface) throws IOException {
        // Check permission for CAMERA
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAPTURE_VIDEO_OUTPUT)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(context,
                    new String[]{android.Manifest.permission.CAPTURE_VIDEO_OUTPUT,
                            Manifest.permission.CAMERA},
                    REQUEST_TAKE_PHOTO);
            return;
        }


        // It is very important to unlock the camera before doing setCamera
        // or it will results in a black preview
        if (mCamera == null) {
            mCamera = Camera.open(0);
            mCamera.unlock();
        }

        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        File file = new File(Environment.getExternalStorageDirectory(), VIDEO_PATH_NAME);
        // "touch" the file
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null)
                if (!parent.exists())
                    if (!parent.mkdirs())
                        throw new IOException("Cannot create " +
                                "parent directories for file: " + file);

            file.createNewFile();
        }

        mMediaRecorder.setOutputFile(file.getAbsolutePath());

        // No limit. Check the space on disk!
        mMediaRecorder.setMaxDuration(-1);
        mMediaRecorder.setVideoFrameRate(15);

        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // This is thrown if the previous calls are not called with the
            // proper order
            e.printStackTrace();
        }

        mInitSuccesful = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }


    private void shutdown() {
        // Release MediaRecorder and especially the Camera as it's a shared
        // object that can be used by other applications
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mCamera.release();

        // once the objects have been released they can't be reused
        mMediaRecorder = null;
        mCamera = null;
    }
}