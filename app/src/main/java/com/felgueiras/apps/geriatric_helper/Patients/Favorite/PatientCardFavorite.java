package com.felgueiras.apps.geriatric_helper.Patients.Favorite;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Sessions.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatric_helper.Patients.SinglePatient.PatientProfileFragment;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

/**
 * Created by rafael on 03-10-2016.
 */
public class PatientCardFavorite extends BaseAdapter {
    private final PatientsFavoriteFragment fragment;
    /**
     * All the Patients
     */
    private ArrayList<PatientFirebase> patients;
    Activity context;

    public PatientCardFavorite(Activity context, ArrayList<PatientFirebase> patients, PatientsFavoriteFragment patientsFavoriteFragment) {
        this.context = context;
        this.patients = patients;
        this.fragment = patientsFavoriteFragment;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final PatientFirebase patient = patients.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.patient_card_grid_2, null);

        final TextView name = (TextView) card.findViewById(R.id.patientName);
        final TextView nameAbbreviation = (TextView) card.findViewById(R.id.patientNameAbbreviation);
        final ImageView overflow = (ImageView) card.findViewById(R.id.overflow);
        overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(overflow, position);
            }
        });


        name.setText(patient.getName());
        nameAbbreviation.setText(patient.getName().charAt(0) + "");

        View.OnClickListener clickListener = new View.OnClickListener() {
            public String patientTransitionName;

            @Override
            public void onClick(View v) {


                Fragment endFragment = new PatientProfileFragment();
                    /*
                    endFragment.setSharedElementReturnTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));


                    endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));
                    */


//                    patientTransitionName = holder.questionTextView.getTransitionName();
                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(PatientProfileFragment.PATIENT, patient);
                ((PrivateAreaActivity) context).replaceFragmentSharedElements(endFragment, args, Constants.tag_view_patient_info_records,
                        name);

            }
        };

        name.setOnClickListener(clickListener);
        nameAbbreviation.setOnClickListener(clickListener);


        return card;
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        /**
         * Inflate menu depending on the fragment.
         */

        inflater.inflate(R.menu.menu_patient_favorite, popup.getMenu());
        popup.setOnMenuItemClickListener(new FavoritePatientClickListener(view, position));
        popup.show();
    }

    class FavoritePatientClickListener implements PopupMenu.OnMenuItemClickListener {

        private final View view;
        private final int position;

        public FavoritePatientClickListener(View view, int position) {
            this.view = view;
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.newSession:
                    Bundle args = new Bundle();
                    args.putSerializable(CGAPrivate.PATIENT, patients.get(position));
                    SharedPreferencesHelper.unlockSessionCreation(context);
                    FragmentTransitions.replaceFragment(context, new CGAPrivate(), args, Constants.tag_create_session_from_favorites);
                    context.setTitle(context.getResources().getString(R.string.cga));
                    break;
                case R.id.removeFavorite:
                    fragment.removePatientFromFavorites(patients.get(position));
                    break;
                default:
            }
            return false;
        }
    }


    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}