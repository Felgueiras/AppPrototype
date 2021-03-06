package com.felgueiras.apps.geriatrichelper.Main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.felgueiras.apps.geriatrichelper.R;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.ToolbarHelper;

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

        ToolbarHelper.showBackButton(context);

        // get current Fragment
        Fragment startFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
        if (args != null) {
            endFragment.setArguments(args);
        }
        // add Exit transition
        /*
        startFragment.setExitTransition(TransitionInflater.from(
                context).inflateTransition(android.R.transition.fade));
        // add Enter transition
        endFragment.setEnterTransition(TransitionInflater.from(context).
                inflateTransition(android.R.transition.fade));
                */

        // Create new transaction and add to back stack
        FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
        Fragment currentFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
        if(currentFragment!=null)
            transaction.remove(currentFragment);
        transaction.replace(R.id.current_fragment, endFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
        transaction.commit();
    }
}
