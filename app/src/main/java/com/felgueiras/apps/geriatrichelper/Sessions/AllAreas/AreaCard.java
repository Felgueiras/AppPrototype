package com.felgueiras.apps.geriatrichelper.Sessions.AllAreas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.Sessions.SingleArea.CGAAreaPrivate;
import com.felgueiras.apps.geriatrichelper.Sessions.SingleArea.CGAAreaPublic;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.R;
import com.felgueiras.apps.geriatrichelper.TourGuide.TourGuideHelper;
import com.felgueiras.apps.geriatrichelper.TourGuide.TourGuideStepHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;

import tourguide.tourguide.TourGuide;


/**
 * Create the Card for each of the Tests available
 */
public class AreaCard extends RecyclerView.Adapter<AreaCard.CGACardHolder> {

    /**
     * ID for this Session
     */
    private final SessionFirebase session;

    private Activity context;
    private TourGuide areaCardTourGUide;


    /**
     * Create a View
     */
    public class CGACardHolder extends RecyclerView.ViewHolder implements Serializable {
        private final ImageButton infoButton;
        private final RecyclerView scalesIcons;
        private final ImageView areaIcon;
        public TextView name, type, cgaCompletion;
        public View view;
        public EditText notes;

        public CGACardHolder(View view) {
            super(view);
            name = view.findViewById(R.id.cga_area);
            //type = (TextView) view.findViewById(R.id.testType);
            infoButton = view.findViewById(R.id.area_info);
            areaIcon = view.findViewById(R.id.area_icon);
            cgaCompletion = view.findViewById(R.id.cga_completion);
            scalesIcons = view.findViewById(R.id.area_scales);
            //addNotesButton = (EditText) view.findViewById(R.id.testNotes);
            this.view = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context       current Context
     * @param session
     * @param resuming      true if we are resuming a Session
     * @param patientGender
     */
    public AreaCard(Activity context, SessionFirebase session, boolean resuming, int patientGender) {
        this.context = context;
        this.session = session;
    }

    @Override
    public CGACardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.cga_area_card, parent, false);
        return new CGACardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final CGACardHolder holder, int position) {

        final String area = Constants.cga_areas[position];

        holder.name.setText(area);
        switch (area) {
            case Constants.cga_functional:
                holder.areaIcon.setImageResource(R.drawable.ic_functional);
                break;
            case Constants.cga_afective:
                holder.areaIcon.setImageResource(R.drawable.ic_afective);
                break;
            case Constants.cga_march:
                holder.areaIcon.setImageResource(R.drawable.ic_march);
                break;
            case Constants.cga_cognitive:
                holder.areaIcon.setImageResource(R.drawable.ic_cognitive);
                break;
//            case Constants.cga_mental:
//                holder.areaIcon.setImageResource(R.drawable.ic_mental);
//                break;
            case Constants.cga_nutritional:
                holder.areaIcon.setImageResource(R.drawable.ic_nutritional);
                break;
            case Constants.cga_social:
                holder.areaIcon.setImageResource(R.drawable.ic_people_black_24dp);
                break;
        }
        /**
         * Check if all scales were completed.
         */
        // TODO
//        ArrayList<GeriatricScale> scalesFromArea = session.getScalesFromArea(area);
        ArrayList<GeriatricScaleFirebase> scalesFromArea = new ArrayList<>();
//        boolean allCompleted = true;
//        for (GeriatricScale scale : scalesFromArea) {
//            if (!scale.isCompleted()) {
//                allCompleted = false;
//                break;
//            }
//        }
//        if (!allCompleted) {
//            ViewManager parentView = (ViewManager) holder.cgaCompletion.getParent();
//            parentView.removeView(holder.cgaCompletion);
//        } else {
//            holder.cgaCompletion.setText("Todas as escalas foram preenchidas");
//        }

        /**
         * Tour Guide.
         */
        if (position == 1 && SharedPreferencesHelper.showTour(context)) {

            // create tour guide
            TourGuideStepHelper step1 = new TourGuideStepHelper(holder.name,
                    "Áreas",
                    "A AGG encontra-se dividida por áreas.",
                    Gravity.BOTTOM | Gravity.CENTER);
            TourGuideStepHelper step2 = new TourGuideStepHelper(holder.infoButton,
                    "Info",
                    "Pode clicar aqui para aceder a informações sobre uma área.");
            TourGuideStepHelper step3 = new TourGuideStepHelper(holder.name,
                    "Área",
                    "Clique aqui para selecionar esta área.");
            TourGuideStepHelper[] steps = new TourGuideStepHelper[]{step1, step2, step3};

            TourGuideHelper.runOverlay_ContinueMethod(context, steps);
        }


        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.setTitle(area);
                // add info about this area
                String area_text = null;
                switch (area) {
                    case Constants.cga_functional:
                        area_text = FirebaseRemoteConfig.getString("cga_functional", "");
                        break;
                    case Constants.cga_afective:
                        area_text = FirebaseRemoteConfig.getString("cga_afective", "");
                        break;
                    case Constants.cga_nutritional:
                        area_text = FirebaseRemoteConfig.getString("cga_nutritional", "");
                        break;
                    case Constants.cga_cognitive:
                        area_text = FirebaseRemoteConfig.getString("cga_cognitive", "");
                        break;
                    case Constants.cga_march:
                        area_text = FirebaseRemoteConfig.getString("cga_march", "");
                        break;
                }


                alertDialog.setMessage(area_text);
                alertDialog.show();
            }
        });

        /**
         * For when the CGA area is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedArea = (String) holder.name.getText();
                // Create new fragment and transaction
                FirebaseAuth auth = FirebaseAuth.getInstance();

                // user already logged in
                if (auth.getCurrentUser() != null) {
                    Fragment newFragment = new CGAAreaPrivate();
                    // add arguments
                    Bundle bundle = new Bundle();
                    PatientFirebase patient = PatientsManagement.getInstance().getPatientFromSession(session, context);
                    if (patient != null)
                        bundle.putSerializable(CGAAreaPrivate.PATIENT, patient);

                    bundle.putSerializable(CGAAreaPrivate.SESSION, session);
                    bundle.putString(CGAAreaPrivate.CGA_AREA, selectedArea);
                    newFragment.setArguments(bundle);
                    // setup the transaction
                    FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                    Fragment frag = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                    transaction.remove(frag);
                    transaction.replace(R.id.current_fragment, newFragment);
                    transaction.addToBackStack(Constants.tag_display_single_area_private).commit();
                } else {
                    Fragment newFragment = new CGAAreaPublic();
                    // add arguments
                    Bundle bundle = new Bundle();

                    PatientFirebase patient = PatientsManagement.getInstance().getPatientFromSession(session, context);
                    if (patient != null)
                        bundle.putSerializable(CGAAreaPublic.PATIENT, patient);

                    bundle.putSerializable(CGAAreaPublic.sessionObject, session);
                    bundle.putString(CGAAreaPublic.CGA_AREA, selectedArea);
                    newFragment.setArguments(bundle);
                    // setup the transaction
                    FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                    Fragment frag = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                    transaction.remove(frag);
                    transaction.replace(R.id.current_fragment, newFragment);
                    transaction.addToBackStack(Constants.tag_display_single_area_public).commit();
                }

                // close TourGuide
                if (areaCardTourGUide != null) {
                    areaCardTourGUide.cleanUp();
                }

            }
        });

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showScalesIcon = SP.getBoolean(context.getResources().getString(R.string.areaCardShowScalesIcon), false);
        if (showScalesIcon) {
            /**
             * Display icons for the areas that exist.
             */
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.scalesIcons.setLayoutManager(layoutManager);

            AreaScalesIconsAdapter adapter = new AreaScalesIconsAdapter(context, scalesFromArea, session);
            holder.scalesIcons.setAdapter(adapter);
        } else {
            /**
             * Show completed scales for this area.
             */
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.scalesIcons.setLayoutManager(layoutManager);

            AreaScalesAlreadyCompleted adapter = new AreaScalesAlreadyCompleted(context, scalesFromArea, session);
            holder.scalesIcons.setAdapter(adapter);
        }


    }

    @Override
    public int getItemCount() {
        return Constants.cga_areas.length;
    }


}
