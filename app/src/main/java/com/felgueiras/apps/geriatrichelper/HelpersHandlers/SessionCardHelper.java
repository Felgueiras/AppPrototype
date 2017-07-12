package com.felgueiras.apps.geriatrichelper.HelpersHandlers;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.Sessions.EvaluationsAllFragment;
import com.felgueiras.apps.geriatrichelper.Sessions.SessionsHistoryMainFragment;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientProfileFragment;
import com.felgueiras.apps.geriatrichelper.R;

/**
 * Created by felgueiras on 20/02/2017.
 */

public class SessionCardHelper implements View.OnClickListener {
    private final int position;
    private final Activity context;
    private final SessionFirebase session;
    private final Fragment fragment;
    private final ImageView overflow;

    public SessionCardHelper(ImageView overflow, int position, Activity context, SessionFirebase session) {
        this.overflow = overflow;
        this.position = position;
        this.context = context;
        this.session = session;
        // get current fragment
        this.fragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
    }

    @Override
    public void onClick(View view) {
        showPopupMenu(overflow, position);
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        /**
         * Inflate menu depending on the fragment.
         */
        if (fragment instanceof SessionsHistoryMainFragment)
            inflater.inflate(R.menu.menu_session_card_session_list, popup.getMenu());
        else {
            inflater.inflate(R.menu.menu_session_card_patient_profile, popup.getMenu());
        }
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(view, position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final View view;
        private final int position;

        public MyMenuItemClickListener(View view, int position) {
            this.view = view;
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.session_erase:
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                    alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
                    alertDialog.setMessage("Deseja eliminar esta Sessão?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Snackbar.make(view, "Sessão eliminada.", Snackbar.LENGTH_SHORT).show();
                                    FirebaseDatabaseHelper.deleteSession(session, context);
                                    // refresh the adapter
                                    if (fragment instanceof SessionsHistoryMainFragment) {
                                        Fragment evaluationsList = fragment.getChildFragmentManager().findFragmentById(R.id.evaluation_history_frame_layout);
                                        ((EvaluationsAllFragment) evaluationsList).removeSession();
                                    }
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Snackbar.make(view, "Ação cancelada", Snackbar.LENGTH_SHORT).show();

                                }
                            });
                    alertDialog.show();
                    return true;
                case R.id.session_go_patient_profile:
                    Fragment endFragment = new PatientProfileFragment();

                    Bundle args = new Bundle();
                    args.putSerializable(PatientProfileFragment.PATIENT, PatientsManagement.getInstance().getPatientFromSession(session, context));
                    ((PrivateAreaActivity) context).replaceFragmentSharedElements(endFragment,
                            args,
                            Constants.tag_view_patient_info_records_from_sessions_list,
                            null);
                    break;
                default:
            }
            return false;
        }
    }
}
