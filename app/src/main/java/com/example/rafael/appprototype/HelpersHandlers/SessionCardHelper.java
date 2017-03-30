package com.example.rafael.appprototype.HelpersHandlers;

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

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.EvaluationsAll;
import com.example.rafael.appprototype.Main.PrivateAreaActivity;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions.PatientSessionsFragment;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

/**
 * Created by felgueiras on 20/02/2017.
 */

public class SessionCardHelper implements View.OnClickListener {
    private final int position;
    private final Activity context;
    private final Session session;
    private final Fragment fragment;
    private final ImageView overflow;

    public SessionCardHelper(ImageView overflow, int position, Activity context, Session session, Fragment fragment) {
        this.overflow = overflow;
        this.position = position;
        this.context = context;
        this.session = session;
        this.fragment = fragment;
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
        if (fragment instanceof EvaluationsAll)
            inflater.inflate(R.menu.menu_session_card_session_list, popup.getMenu());
        else
        {
            inflater.inflate(R.menu.menu_session_card_patient_profile, popup.getMenu());
        }
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(view, position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

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
                                    session.delete();
                                    // refresh the adapter
                                    if (fragment instanceof PatientSessionsFragment)
                                        ((PatientSessionsFragment) fragment).removeSession(position);
                                    else if (fragment instanceof EvaluationsAll)
                                        ((EvaluationsAll) fragment).removeSession(position);
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
                    Fragment endFragment = new ViewSinglePatientInfo();

                    Bundle args = new Bundle();
                    args.putSerializable(ViewSinglePatientInfo.PATIENT, session.getPatient());
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
