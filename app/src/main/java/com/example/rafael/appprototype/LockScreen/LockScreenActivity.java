package com.example.rafael.appprototype.LockScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.Main.PrivateAreaActivity;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;

public class LockScreenActivity extends AppCompatActivity {
    /**
     * Correct password
     */
    private String correctPassword = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.content_lock_screen);
        setTitle(getResources().getString(R.string.prompt_password));
        final EditText passwordEditText = (EditText) findViewById(R.id.password);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                Log.d("Lock", password.equals(correctPassword) + "");
                if (password.equals(correctPassword)) {
                    passwordEditText.clearFocus();
                    SharedPreferencesHelper.setLockStatus(getApplicationContext(),false);

                    Intent intent = new Intent(LockScreenActivity.this, PrivateAreaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}

