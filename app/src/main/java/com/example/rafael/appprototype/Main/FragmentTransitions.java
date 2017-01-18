package com.example.rafael.appprototype.Main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.rafael.appprototype.R;

/**
 * Created by felgueiras on 18/01/2017.
 */

public class FragmentTransitions {

    /**
     * Replace one fragment by another
     *
     * @param args
     */
    public static void replaceFragment(Activity context, Fragment endFragment, Bundle args, String addToBackStackTag) {

        // get current Fragment
        Fragment startFragment = context.getFragmentManager().findFragmentById(R.id.content_fragment);
        if (args != null) {
            endFragment.setArguments(args);
        }
        // add Exit transition
        /*
        startFragment.setExitTransition(TransitionInflater.from(
                this).inflateTransition(android.R.transition.fade));
        // add Enter transition
        endFragment.setEnterTransition(TransitionInflater.from(this).
                inflateTransition(android.R.transition.fade));
                */
        // Create new transaction and add to back stack
        FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, endFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
        transaction.commit();
    }
}
