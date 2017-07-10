package com.felgueiras.apps.geriatric_helper.Main;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.SubMenu;

import com.felgueiras.apps.geriatric_helper.R;

/**
 * Created by felgueiras on 26/06/2017.
 */

public class ModulesManagement {
    public static void manageModulesPublicArea(PublicAreaActivity context, NavigationView navigationView) {

        // reset to defaults
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation_drawer_public);

        Menu m = navigationView.getMenu();

        // check if modules are active or not
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        // criteria module
        boolean moduleCriteria = SP.getBoolean(context.getResources().getString(R.string.modulePrescription), false);


        if (moduleCriteria)
        {
            SubMenu menuGroup = m.addSubMenu("Módulos");
            // prescription module
            menuGroup.add(0, R.id.prescription, 0, R.string.tab_drug_prescription).setIcon(R.drawable.pill_black);
        }

        // personal area module
//        boolean modulePersonalArea = SP.getBoolean(context.getResources().getString(R.string.modulePrescription), false);
//        if (moduleCriteria)
//        {
//            SubMenu menuGroup = m.addSubMenu("Módulos");
//            // prescription module
//            menuGroup.add(0, R.id.prescription, 0, R.string.tab_drug_prescription);
//        }
    }
}
