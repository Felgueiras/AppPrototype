package com.felgueiras.apps.geriatric_helper.Main;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.R;

public class LaunchScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);



        // start AsyncTask
        new FirebaseLoadInitialData().execute("");

    }

    // The definition of our task class
    private class FirebaseLoadInitialData extends AsyncTask<String, Integer, String> {
        private ProgressBar bar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayProgressBar("Downloading...");
        }

        private void displayProgressBar(String s) {
            bar = (ProgressBar) findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE); //View.INVISIBLE, or View.GONE to hide it.

        }

        @Override
        protected String doInBackground(String... params) {
//            String url = params[0];
            // check scales version
            FirebaseHelper.canLeaveLaunchScreen = false;
            FirebaseHelper.initializeAndCheckVersions(getBaseContext());
            FirebaseHelper.downloadScales();
            while (true) {
//                int scalesTotal = FirebaseHelper.getScalesTotal();
//                int scalesCurrent = FirebaseHelper.getScalesCurrent();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (FirebaseHelper.canLeaveLaunchScreen)
                    break;
            }
            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            bar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            bar.setVisibility(View.GONE);

            // go to public area
            Intent intent = new Intent(getBaseContext(), PublicAreaActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
