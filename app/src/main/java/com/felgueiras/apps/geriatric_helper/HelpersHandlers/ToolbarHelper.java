package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.felgueiras.apps.geriatric_helper.Constants;

/**
 * Created by felgueiras on 22/02/2017.
 */

public class ToolbarHelper {

    public static void showBackButton(Activity context) {
//        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Constants.upButton = true;
        Constants.toggle.setDrawerIndicatorEnabled(false);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void hideBackButton(Activity context) {
//        Log.d("Toolbar", "hiding back button");
//        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
////        ((AppCompatActivity) context).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        Constants.toggle.syncState();
        Constants.upButton = false;
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Constants.toggle.setDrawerIndicatorEnabled(true);
    }
}
