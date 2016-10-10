package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.NewSessionFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.SessionsHistoryTab.SessionsHistoryFragment;
import com.example.rafael.appprototype.ViewPatientsTab.ViewPatientsFragment;

/**
 * Handle the selection of an item from the NaviagtionDrawer
 */
public class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
    private final String[] drawerPages;
    private final FragmentManager fragmentManager;
    private final MainActivity context;
    private final ListView drawerList;
    private final DrawerLayout drawerLayout;

    public DrawerItemClickListener(String[] drawerPages, FragmentManager fragmentManager,
                                   MainActivity mainActivity, ListView drawerList,
                                   DrawerLayout drawerLayout) {
        this.drawerPages = drawerPages;
        this.fragmentManager = fragmentManager;
        this.context = mainActivity;
        this.drawerList = drawerList;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Check which action to perform
        String selectedPage = drawerPages[position];
        Fragment fragment = null;
        if (selectedPage == context.getResources().getString(R.string.patients_history)) {
            fragment = new SessionsHistoryFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            context.setTitle(context.getResources().getString(R.string.tab_patients));
        } else if (selectedPage == context.getResources().getString(R.string.create_new_session)) {
            fragment = new NewSessionFragment();
            context.setTitle(context.getResources().getString(R.string.tab_new_session));
        } else if (selectedPage == context.getResources().getString(R.string.my_patients)) {
            fragment = new ViewPatientsFragment();
                /*
                Bundle args = new Bundle();
                args.putInt(ArticleFragment.ARG_PLANET_NUMBER, position);
                fragment.setArguments(args);
                */
            context.setTitle(context.getResources().getString(R.string.tab_my_patients));
        }


        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();


        // Highlight the selected item, update the date, and close the drawer
        drawerList.setItemChecked(position, true);
        // setTitle(drawerPages[position]);
        drawerLayout.closeDrawer(drawerList);
    }
}