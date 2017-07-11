package com.felgueiras.apps.geriatric_helper.Sessions.SingleArea;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.ScaleFragment;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.TourGuide.TourGuideHelper;
import com.felgueiras.apps.geriatric_helper.TourGuide.TourGuideStepHelper;

import java.io.Serializable;
import java.util.ArrayList;

import tourguide.tourguide.TourGuide;


/**
 * Create the Card for each of the Tests available
 */
public class ScaleCard extends RecyclerView.Adapter<ScaleCard.ScaleCardHolder> {

    /**
     * ID for this Session
     */
    private final SessionFirebase session;
    private final int patientGender;
    /**
     * CGA area.
     */
    private final String area;
    private final Button finishSessionButton;

    private Activity context;
    private ArrayList<GeriatricScaleNonDB> testsForArea;
    private TourGuide scaleTourGuide;


    /**
     * Create a View
     */
    public static class ScaleCardHolder extends RecyclerView.ViewHolder implements Serializable {
        public final TextView result_quantitative;
        public final EditText notes;
        public final TextView subCategory;
        public TextView name;
        public ImageButton description;
        public TextView result_qualitative;
        public View view;
        public ImageButton addNotesButton;
        public TextView patientProgress;

        public ScaleCardHolder(View view) {
            super(view);
            name = view.findViewById(R.id.scaleName);
            subCategory = view.findViewById(R.id.testSubCategory);
            description = view.findViewById(R.id.scale_info);
            result_qualitative = view.findViewById(R.id.result_qualitative);
            result_quantitative = view.findViewById(R.id.result_quantitative);
            addNotesButton = view.findViewById(R.id.addNotes);
            notes = view.findViewById(R.id.testNotes);
            patientProgress = view.findViewById(R.id.patient_progress);
            this.view = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *  @param context       current Context
     * @param session
     * @param reviewing     true if we are reviewing a Session
     * @param patientGender
     * @param area
     * @param finishSession
     */
    public ScaleCard(Activity context, SessionFirebase session, boolean reviewing, int patientGender,
                     String area, Button finishSession) {
        this.context = context;
        this.session = session;
        this.patientGender = patientGender;
        this.area = area;
        this.finishSessionButton = finishSession;
    }

    @Override
    public ScaleCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.scale_card, parent, false);
        return new ScaleCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final ScaleCardHolder holder, final int position) {
        // get constants
        ViewManager parentView = (ViewManager) holder.result_qualitative.getParent();
        // get scale nonDB
        final GeriatricScaleNonDB scaleNonDB = testsForArea.get(position);


        final GeriatricScaleFirebase currentScale = FirebaseDatabaseHelper.getScaleFromSession(session, scaleNonDB.getScaleName());

        if (position == Constants.tourScalePosition && SharedPreferencesHelper.showTour(context)) {

            if (!currentScale.isCompleted()) {

                TourGuideStepHelper step1 = new TourGuideStepHelper(holder.name,
                        "Escalas",
                        "Dentro de cada área há várias escalas.", Gravity.BOTTOM | Gravity.CENTER);
                TourGuideStepHelper step2 = new TourGuideStepHelper(holder.description,
                        "Info",
                        "Pode clicar aqui para aceder a informações sobre uma escala.", Gravity.BOTTOM | Gravity.CENTER);
                TourGuideStepHelper step3 = new TourGuideStepHelper(holder.notes,
                        "Notas",
                        "Pode adicionar notas sobre uma escala, por exemplo, o motivo para um paciente ter" +
                                " obtido determinada pontuação.", Gravity.BOTTOM | Gravity.CENTER);
                TourGuideStepHelper step4 = new TourGuideStepHelper(holder.name,
                        "Escala",
                        "Selecione esta escala e preencha-a, " +
                                "quando estiver completamente preenchida irá aparecer uma mensagem no ecrã.", Gravity.BOTTOM | Gravity.CENTER);
                TourGuideStepHelper[] steps = new TourGuideStepHelper[]{step1, step2, step3, step4};

                TourGuideHelper.runOverlay_ContinueMethod(context, steps);
            } else {
                TourGuideStepHelper step1 = new TourGuideStepHelper(holder.result_qualitative,
                        "Resultado qualitativo",
                        "", Gravity.BOTTOM | Gravity.CENTER);
                TourGuideStepHelper step2 = new TourGuideStepHelper(holder.result_quantitative,
                        "Resultado quantitativo",
                        "", Gravity.BOTTOM | Gravity.CENTER);
                TourGuideStepHelper step3 = new TourGuideStepHelper(finishSessionButton,
                        "Terminar Sessão",
                        "Depois de preencher as escalas que pretende, é altura de terminar a sessão." +
                                " Clique aqui para terminar a sessão.", Gravity.TOP | Gravity.CENTER);

                TourGuideStepHelper[] steps = new TourGuideStepHelper[]{step1, step2, step3};
                TourGuideHelper.runOverlay_ContinueMethod(context, steps);
            }


        }


//        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);


        holder.name.setText(scaleNonDB.getShortName());

        // display subcategory for functional area scales
        if (area.equals(Constants.cga_functional)) {
            holder.subCategory.setVisibility(View.VISIBLE);
            holder.subCategory.setText(scaleNonDB.getSubCategory());
        }

        final GeriatricScaleFirebase finalCurrentTest = currentScale;

        holder.description.setOnClickListener(new ScaleInfoHelper(context, Scales.getScaleByName(currentScale.getScaleName())));

        // Test was already opened
        if (currentScale != null && currentScale.isAlreadyOpened()) {
            float selected = 1f;
            holder.view.setAlpha(selected);
            // already complete
            if (currentScale.isCompleted()) {
                /**
                 * Qualitative result.
                 */
                // get static scale definition
                GeriatricScaleNonDB scaleDefinition = Scales.getScaleByName(currentScale.getScaleName());
                if (scaleDefinition.getScoring().getName() != null) {
                    // get min value for that category
                    int minValue = Scales.getGradingMin(scaleDefinition, Constants.EDUCATION_LEVEL);
                    if (FirebaseHelper.generateScaleResult(currentScale) < minValue) {
                        holder.result_qualitative.setText("Resultado abaixo do esperado");
                    } else {
                        holder.result_qualitative.setText("Resultado dentro do esperado");
                    }

                } else {
                    // display Scoring to the user
                    GradingNonDB match = Scales.getGradingForScale(currentScale, patientGender);
                    // qualitative result
                    if (match != null)
                        holder.result_qualitative.setText(match.getGrade());
                }


                /**
                 * Quantitative result.
                 */
                String quantitative = "";
                quantitative += currentScale.getResult();
                GeriatricScaleNonDB testNonDB = Scales.getScaleByName(currentScale.getScaleName());
                if (testNonDB.getScoring() != null) {
                    if (!testNonDB.getScoring().isDifferentMenWomen()) {
                        quantitative += " (" + testNonDB.getScoring().getMinScore();
                        quantitative += "-" + testNonDB.getScoring().getMaxScore() + ")";
                    } else {
                        if (patientGender == Constants.MALE) {
                            quantitative += " (" + testNonDB.getScoring().getMinMen();
                            quantitative += "-" + testNonDB.getScoring().getMaxMen() + ")";
                        }
                    }
                } else {
                    quantitative = "";
                }
                holder.result_quantitative.setText(quantitative);
            }
            // still incomplete
            else {
                holder.result_qualitative.setText(testCompletionSelectedIncomplete);
//                parentView.removeView(holder.result_quantitative);
//                parentView.removeView(holder.addNotesButton);
//                parentView.removeView(holder.notes);
            }

        } else {
//            parentView.removeView(holder.result_qualitative);
//            parentView.removeView(holder.result_quantitative);
//            parentView.removeView(holder.addNotesButton);
//            parentView.removeView(holder.notes);
        }


        if (currentScale != null && currentScale.getNotes() != null) {
            parentView.removeView(holder.addNotesButton);
            holder.notes.setText(currentScale.getNotes());
        } else {
            parentView.removeView(holder.notes);
        }


//        holder.addNotesButton.setOnClickListener(new SessionNoteshandler(context, finalCurrentTest, holder, parentView));
//        holder.notes.setOnClickListener(new SessionNoteshandler(context, finalCurrentTest, holder, parentView));

        /**
         * For when the Test is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalCurrentTest.getScaleName().equals(Constants.test_name_mini_nutritional_assessment_global)) {
                    // TODO check if triagem is already answered

                    GeriatricScaleFirebase triagem = FirebaseDatabaseHelper.getScaleFromSession(session,
                            Constants.test_name_mini_nutritional_assessment_triagem);

                }

                if (scaleNonDB.getScoring().isDifferentMenWomen() &&
                        Constants.SESSION_GENDER != Constants.MALE && Constants.SESSION_GENDER != Constants.FEMALE) {
                    checkGender(finalCurrentTest, position);

                } else {
                    openScale(finalCurrentTest, position);
                }


            }
        });

        /**
         * Add a listener for when a note is added.
         */
//        /*
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentScale.setNotes(charSequence.toString());
                FirebaseDatabaseHelper.updateScale(currentScale);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        */

    }

    private void openScale(GeriatricScaleFirebase finalCurrentTest, int position) {
        String selectedTestName = finalCurrentTest.getScaleName();

        // Create new fragment and transaction
        Fragment newFragment = new ScaleFragment();
        // add arguments
        Bundle bundle = new Bundle();
        bundle.putSerializable(ScaleFragment.testObject, Scales.getScaleByName(selectedTestName));
        bundle.putSerializable(ScaleFragment.SCALE, finalCurrentTest);
        bundle.putSerializable(ScaleFragment.CGA_AREA, area);
        bundle.putSerializable(ScaleFragment.patient, PatientsManagement.getInstance().getPatientFromSession(session, context));
        newFragment.setArguments(bundle);
        // setup the transaction
        FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
        transaction.replace(R.id.current_fragment, newFragment);
        transaction.addToBackStack(Constants.tag_display_session_scale).commit();

//        if (position == Constants.tourScalePosition && SharedPreferencesHelper.showTour(context)) {
//            // close TourGuide
//            scaleTourGuide.cleanUp();
//        }
    }

    private void checkGender(final GeriatricScaleFirebase finalCurrentTest, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.select_patient_gender);

        //list of items
        String[] items = new String[]{"M", "F"};
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        if (which == 0)
                            Constants.SESSION_GENDER = Constants.MALE;
                        else
                            Constants.SESSION_GENDER = Constants.FEMALE;
                    }
                });


        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();
                        openScale(finalCurrentTest, position);
                    }
                });


        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    @Override
    public int getItemCount() {
        Log.d("Scales", Scales.scales.size() + "");
        // get the number of tests that exist for this area
        testsForArea = Scales.getScalesForArea(area);
        return testsForArea.size();
    }

}
