package com.felgueiras.apps.geriatrichelper.Main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LaunchScreen extends AppCompatActivity {

    private Context context;

    // Butterknife
    @BindView(R.id.launchScreenLogo)
    ImageView appLogo;
    @BindView(R.id.progressBar)
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch_screen);
        ButterKnife.bind(this);


        // load image with glide
        Glide.with(this).load(R.drawable.app_icon).apply(new RequestOptions().override(184,274)).into(appLogo);


        context = this;

        // check scales version

        // SharedPreferencesHelper.disableTour(this);

        boolean isFirstStart = getSharedPreferences(getString(R.string.sharedPreferencesTag), MODE_PRIVATE)
                .getBoolean("firstStart", true);
        // if first time starting app, user need to be connected
        // to fetch information
        if (isFirstStart) {
            Log.d("FirstStart", "Checking internet connection");
            try {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null) {
                    // no internet connection
                    Log.d("FirstStart", "Showing alert dialog");
                    // inform user that for the first time starting the app he or she
                    // needs to be connected
                    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                    alertDialog.setTitle("Aviso");
                    alertDialog.setMessage("A primeira vez que inicia a app, precisa de estar conectado à Internet." +
                            "Por favor, ligue os seus dados móveis ou Wi-Fi, feche e volte a abrir a aplicação");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                    finish();
                                }
                            });

                    alertDialog.show();
                } else {
                    Log.d("FirstStart", "Not showing alert dialog");

                    // start AsyncTask
                    new FirebaseLoadInitialData().execute("");

                }


            } catch (Exception e) {
//                    return false;
            }
        } else {
            Log.d("FirstStart", "Not first start!!!");

            // start AsyncTask
            new FirebaseLoadInitialData().execute("");
        }


    }

    // The definition of our task class
    private class FirebaseLoadInitialData extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayProgressBar("Downloading...");
        }

        private void displayProgressBar(String s) {
            bar.setVisibility(View.VISIBLE); //View.INVISIBLE, or View.GONE to hide it.
        }

        @Override
        protected String doInBackground(String... params) {

            FirebaseHelper.canLeaveLaunchScreen = false;
            FirebaseRemoteConfig.setupRemoteConfig(context);
            FirebaseHelper.initializeAndCheckVersions(getBaseContext());
//            FirebaseStorageHelper.downloadLanguageResources();
            while (true) {
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
