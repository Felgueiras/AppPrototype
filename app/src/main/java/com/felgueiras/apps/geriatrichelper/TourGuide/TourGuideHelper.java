package com.felgueiras.apps.geriatrichelper.TourGuide;

import android.app.Activity;
import android.graphics.Color;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;

/**
 * Created by rafae on 08/07/2017.
 */

public class TourGuideHelper {


    public static void runOverlay_ContinueMethod(Activity context, TourGuideStepHelper[] steps) {

        // enter and exit animations
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(600);
        enterAnimation.setFillAfter(true);

        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(600);
        exitAnimation.setFillAfter(true);

        // create steps
        ChainTourGuide[] guides = new ChainTourGuide[steps.length];
        for (int index = 0; index < steps.length; index++) {
            TourGuideStepHelper step = steps[index];
            // create Tooltip
            ToolTip t = new ToolTip()
                    .setTitle(step.getTitle())
                    .setDescription(step.getDescription());
//                    .setGravity(Gravity.BOTTOM | Gravity.LEFT);
            if (step.getGravity() != -1) {
                t.setGravity(step.getGravity());
            }
            ChainTourGuide tourGuide = ChainTourGuide.init(context)
                    .setToolTip(t)
                    .setOverlay(new Overlay()
                            .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                            .setEnterAnimation(enterAnimation)
                            .setExitAnimation(exitAnimation)
                    )
                    .playLater(step.getView());
            guides[index] = tourGuide;
        }

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(guides)
                .setDefaultOverlay(new Overlay()
                        .setEnterAnimation(enterAnimation)
                        .setExitAnimation(exitAnimation)
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();


        ChainTourGuide.init(context).playInSequence(sequence);
    }


}
