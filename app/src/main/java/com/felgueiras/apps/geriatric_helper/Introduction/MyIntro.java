package com.felgueiras.apps.geriatric_helper.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.felgueiras.apps.geriatric_helper.Main.PublicAreaActivity;
import com.felgueiras.apps.geriatric_helper.R;
import com.github.paolorotolo.appintro.AppIntro;

public class MyIntro extends AppIntro {
    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        //adding the three slides for introduction app you can ad as many you needed
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro1));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro2));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.app_intro3));

        // Show and Hide Skip and Done buttons
        showStatusBar(false);
        showSkipButton(false);


        //Add animation to the intro slider
        setDepthAnimation();
    }

    @Override
    public void onSkipPressed() {
        // Do buildTable here when users click or tap on Skip button.
        Toast.makeText(getApplicationContext(),
                getString(R.string.app_intro_skip), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), PublicAreaActivity.class);
        startActivity(i);
    }

    @Override
    public void onNextPressed() {
        // Do buildTable here when users click or tap on Next button.
    }

    @Override
    public void onDonePressed() {
        // Do buildTable here when users click or tap tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do buildTable here when slide is changed
    }
}