package com.felgueiras.apps.geriatric_helper.LockScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatric_helper.R;

public class LockScreenActivity extends AppCompatActivity {
    /**
     * Correct password
     */
    private String correctPassword;

    TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = s.toString();
            if (password.equals(correctPassword)) {
                passwordEditText.clearFocus();
                SharedPreferencesHelper.setLockStatus(getApplicationContext(), false);

                Intent intent = new Intent(LockScreenActivity.this, PrivateAreaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.content_lock_screen);
        setTitle(getResources().getString(R.string.prompt_password));

        /**
         * Set views
         */
        passwordEditText = (EditText) findViewById(R.id.password);
        correctPassword = SharedPreferencesHelper.getUserPassword(this);

        // fect the correct password
        passwordEditText.addTextChangedListener(passwordTextWatcher);
    }

}

