package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ReviewScale extends RecyclerView.Adapter<ReviewScale.TestCardHolder> {

    /**
     * Session for the Tests.
     */
    private final Session session;
    /**
     * Patient for this Session
     */
    private final Patient patient;
    private final String area;
    private final boolean comparePrevious;
    /**
     * Context.
     */
    private Context context;
    /**
     * Name of Test being displayed.
     */
    private String testName;


    /**
     * Default constructor for the RV adapter.
     *
     * @param context
     * @param session
     * @param area
     * @param comparePrevious
     */
    public ReviewScale(Context context, Session session, String area, boolean comparePrevious) {
        this.context = context;
        this.session = session;
        this.patient = session.getPatient();
        this.area = area;
        this.comparePrevious = comparePrevious;
    }


    /**
     * CGACardHolder class.
     */
    public class TestCardHolder extends RecyclerView.ViewHolder {
        private final TextView result_qualitative;
        public TextView name, type, testGrading;
        public EditText notes;
        /**
         * Test card view.
         */
        public View testCard;
        public TextView result_quantitative;

        /**
         * Create a CGACardHolder from a View
         *
         * @param view
         */
        public TestCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            //type = (TextView) view.findViewById(R.id.testType);
            result_qualitative = (TextView) view.findViewById(R.id.result_qualitative);
            result_quantitative = (TextView) view.findViewById(R.id.result_quantitative);
            testCard = view;
            notes = (EditText) view.findViewById(R.id.testNotes);

        }
    }


    /**
     * Create a CGACardHolder from a View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the Test CardView
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.scale_card_review, parent, false);
        return new TestCardHolder(testCard);
    }

    /**
     * Display the data at a certain position
     *
     * @param holder   data to be displayed
     * @param position position to "put" the data
     */
    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {

        String testCompletionResult = context.getResources().getString(R.string.test_result);
        // get current test
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        final GeriatricTest currentScale = testsFromSession.get(position);
        testName = currentScale.getScaleName();


        // access a given Test from the DB
        holder.name.setText(currentScale.getShortName());

        // display Scoring to the user
        GradingNonDB match = Scales.getGradingForScale(
                currentScale,
                patient.getGender());
        holder.result_qualitative.setText(match.getGrade());

        if (comparePrevious) {
            comparePreviousSessions(currentScale, holder);
        }

        // quantitative result
        String quantitative = "";
        quantitative += currentScale.getResult();
        GeriatricTestNonDB testNonDB = Scales.getTestByName(currentScale.getScaleName());
        if (!testNonDB.getScoring().isDifferentMenWomen()) {
            quantitative += " (" + testNonDB.getScoring().getMinScore();
            quantitative += "-" + testNonDB.getScoring().getMaxScore() + ")";
        } else {
            if (patient.getGender() == Constants.MALE) {
                quantitative += " (" + testNonDB.getScoring().getMinMen();
                quantitative += "-" + testNonDB.getScoring().getMaxMen() + ")";
            }
        }
        holder.result_quantitative.setText(quantitative);

        if (currentScale.hasNotes()) {
            holder.notes.setText(currentScale.getNotes());
        }


        /**
         * Add a listener for when a note is added.
         */
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentScale.setNotes(charSequence.toString());
                currentScale.save();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // for when the test is selected
        holder.testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Fragment newFragment = new ReviewSingleTestFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReviewSingleTestFragment.testDBobject, currentScale);
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = ((PrivateArea) context).getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_review_test).commit();
            }
        });

    }

    /**
     * Compare the current result with the results from previous sessions.
     *
     * @param currentScale
     * @param holder
     */
    private void comparePreviousSessions(GeriatricTest currentScale, TestCardHolder holder) {
        double currentScaleResult = currentScale.getResult();
        System.out.println("Current result " + currentScaleResult);
        // access this test from previous session that had this test
        String scaleName = currentScale.getScaleName();
        Session currentSession = currentScale.getSession();

        // get all the instances of that Scale for this Patient
        ArrayList<GeriatricTest> scaleInstances = new ArrayList<>();
        ArrayList<Session> patientSessions = patient.getSessionsFromPatient();
        //patientSessions.remove(currentSession);

        // sort by date ascending
        Collections.sort(patientSessions, new Comparator<Session>() {
            public int compare(Session o1, Session o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        // get index current session
        int sessionIndex = patientSessions.indexOf(currentSession);
        // discard sessions with higher indexes, consider only the ones with smaller


        // get instances for that test
        for (int i = sessionIndex - 1; i >= 0; i--) {
            Session previousSession = patientSessions.get(i);
            List<GeriatricTest> scalesfromSession = previousSession.getTestsFromSession();
            for (GeriatricTest previousScale : scalesfromSession) {
                if (previousScale.getScaleName().equals(scaleName)) {
                    scaleInstances.add(previousScale);
                    System.out.println("FOUND match");
                    // compare grade from previous session to current session
                    double previousScaleResult = previousScale.getResult();
                    System.out.println(previousScaleResult + "-" + currentScaleResult);
                    GradingNonDB previousGrading = Scales.getGradingForScale(
                            previousScale,
                            patient.getGender());
                    GradingNonDB currentGrading = Scales.getGradingForScale(
                            currentScale,
                            patient.getGender());
                    System.out.println(previousGrading.getGrade() + "-" + currentGrading.getGrade());
                    // check if has gotten worse or not
                    int index1 = Scales.getGradingIndex(previousScale,
                            patient.getGender());
                    int index2 = Scales.getGradingIndex(currentScale,
                            patient.getGender());
                    System.out.println(index1 + "-" + index2);
                    if (index2 > index1) {
                        // patient got worse
                        ViewStub stubInfo = ((ViewStub) holder.itemView.findViewById(R.id.stub_info)); // get the reference of ViewStub
                        if (stubInfo != null) {
                            // only inflate once
                            View inflated = stubInfo.inflate();
                            TextView patientProgress = (TextView) inflated.findViewById(R.id.patient_progress);
                            ImageButton moreInfo = (ImageButton) inflated.findViewById(R.id.more_info);
                            patientProgress.setText(R.string.evolution_negative);
                            // display more info
                            moreInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                    alertDialog.setTitle("Informação");
                                    alertDialog.setMessage(context.getResources().getString(R.string.info_procedure_patient_worse));
                                    alertDialog.show();
                                }
                            });
                        }

                    } else if (index1 > index2) {
                        // patient got better
                        //holder.patientProgress.setText(R.string.evolution_positive);
                    } else {
                        //holder.patientProgress.setText(R.string.evolution_neutral);
                    }

                    return;
                }
            }
        }
    }


    @Override
    public int getItemCount() {

        List<GeriatricTest> allScales = session.getTestsFromSession();
        // get scales for this area
        return Scales.getTestsForArea(allScales, area).size();
    }

}
