package com.felgueiras.apps.geriatrichelper;


import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by felgueiras on 29/03/2017.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }

    private static AtomicBoolean isRunningTest;

    public static synchronized boolean isRunningTest() {
        if (null == isRunningTest) {
            boolean istest;

            try {
                Class.forName("com.felgueiras.apps.geriatrichelper.Main.ScalesTests");
                istest = true;
            } catch (ClassNotFoundException e) {
                istest = false;
            }

            isRunningTest = new AtomicBoolean(istest);
            Log.d("Test", isRunningTest() + "");
        }

        return isRunningTest.get();
    }

}