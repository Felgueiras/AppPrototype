package com.felgueiras.apps.geriatrichelper.TourGuide;

import android.view.View;

/**
 * Created by rafae on 08/07/2017.
 */

public class TourGuideStepHelper {
    private final View view;
    private final String title;
    private final String description;
    private final int gravity;

    public TourGuideStepHelper(View view, String title, String description, int gravity) {
        this.view = view;
        this.title = title;
        this.description = description;
        this.gravity = gravity;

    }

    public TourGuideStepHelper(View view, String title, String description) {
        this.view = view;
        this.title = title;
        this.description = description;
        this.gravity = -1;
    }

    public View getView() {
        return view;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getGravity() {
        return gravity;
    }
}