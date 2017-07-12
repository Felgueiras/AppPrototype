package com.felgueiras.apps.geriatrichelper.PersonalAreaAccess;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.felgueiras.apps.geriatrichelper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.R;

public class PromptSyncBackend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_sync_backend);

        final Button yesButton = (Button) findViewById(R.id.yes);
        Button noButton = (Button) findViewById(R.id.no);

        final Activity activity = this;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
                if (v.getId() == yesButton.getId()) {
                    // yes
                    PatientsManagement.loadInitialPatients(activity);
                    sharedPreferences.edit().putBoolean(activity.getResources().getString(R.string.syncBackEnd), true).apply();

                } else {
                    // no
                    sharedPreferences.edit().putBoolean(activity.getResources().getString(R.string.syncBackEnd), false).apply();
                }

                Intent intent = new Intent(activity, PrivateAreaActivity.class);
                startActivity(intent);
                activity.finish();
            }
        };

        yesButton.setOnClickListener(clickListener);
        noButton.setOnClickListener(clickListener);


    }
}
