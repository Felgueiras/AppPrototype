package com.felgueiras.apps.geriatrichelper.Sessions.ReviewSession;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.QuestionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SessionPDF;
import com.felgueiras.apps.geriatrichelper.Sessions.AllAreas.CGAPublicInfo;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.Patients.PatientsMain;
import com.felgueiras.apps.geriatrichelper.R;
import com.felgueiras.apps.geriatrichelper.TourGuide.TourGuideHelper;
import com.felgueiras.apps.geriatrichelper.TourGuide.TourGuideStepHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tourguide.tourguide.TourGuide;


public class ReviewSingleSessionNoPatient extends Fragment {

    /**
     * Session object
     */
    private SessionFirebase session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";
    private TourGuide finishReviewingSessionGuide;
    private View createPdfView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_review_session_no_patient, menu);
        createPdfView = menu.findItem(R.id.createPDF).getActionView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.createPDF:
                // create a PDF of the session for printing
                new SessionPDF(session).createSessionPdf(getActivity(), false);
                break;
        }
        return true;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_navigation_review_session_no_patient, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (SessionFirebase) args.getSerializable(SESSION);

        getActivity().setTitle(getResources().getString(R.string.evaluation_results));


        /*
          Setup bottom navigation.
         */
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        // disable areas that don't have any scale
        Menu menuNav = bottomNavigationView.getMenu();

        int defaultIndex = -1;

        // check if this session contains scales from this area
        ArrayList<GeriatricScaleFirebase> scalesFromSession = FirebaseDatabaseHelper.getScalesFromSession(session);
        for (int i = 0; i < Constants.cga_areas.length; i++) {
            // current area
            String currentArea = Constants.cga_areas[i];
            // disable area
            menuNav.getItem(i).setEnabled(false);
            for (GeriatricScaleFirebase scale : scalesFromSession) {
                if (scale.getArea().equals(currentArea)) {
                    menuNav.getItem(i).setEnabled(true);
                    if (defaultIndex == -1) {
                        defaultIndex = i;
                        menuNav.getItem(i).setChecked(true);
                    }
                }
            }

        }
        /*
          Default fragment.
         */
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_cga_area);
        if (currentFragment != null)
            transaction.remove(currentFragment);

        bottomNavigationView.getMenu().getItem(defaultIndex).setChecked(true);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_functional, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_afective, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_march, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_cognitive, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_nutritional, session));

        Constants.bottomNavigationReviewSession = defaultIndex;
        transaction.replace(R.id.frame_layout_cga_area, fragments.get(defaultIndex));
        transaction.commit();


        final Map<Integer, Integer> fragmentMapping = new HashMap<>();
        fragmentMapping.put(R.id.cga_functional, 0);
        fragmentMapping.put(R.id.cga_afective, 1);
        fragmentMapping.put(R.id.cga_march, 2);
        fragmentMapping.put(R.id.cga_cognitive, 3);
        fragmentMapping.put(R.id.cga_nutritional, 4);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;

                        Integer selectedIndex = fragmentMapping.get(item.getItemId());

                        if (Constants.bottomNavigationReviewSession != selectedIndex) {
                            Constants.bottomNavigationReviewSession = selectedIndex;
                            fragment = fragments.get(selectedIndex);
                        } else {
                            return true;
                        }


                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_cga_area);
                        if (currentFragment != null)
                            transaction.remove(currentFragment);
                        transaction.replace(R.id.frame_layout_cga_area, fragment);
                        transaction.commit();

                        return true;
                    }
                });


        // close session FAB
        FloatingActionButton closeFAB = view.findViewById(R.id.close_session);
        closeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // reset patient's gender
                Constants.SESSION_GENDER = -1;

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                // check if logged in
                Fragment fragment;
                if (SharedPreferencesHelper.isLoggedIn(getActivity())) {
                    SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                    fragment = new PatientsMain();
                } else {
                    SharedPreferencesHelper.resetPublicSession(getActivity());
                    fragment = new CGAPublicInfo();
                }
                fragmentManager.beginTransaction()
                        .remove(currentFragment)
                        .replace(R.id.current_fragment, fragment)
                        .commit();

                if (SharedPreferencesHelper.showTour(getActivity())) {
                    SharedPreferencesHelper.disableTour(getActivity());
                }
            }
        });

        if (SharedPreferencesHelper.showTour(getActivity())) {
//            finishReviewingSessionGuide = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
//                    .setPointer(new Pointer())
//                    .setToolTip(new ToolTip().setTitle("Fechar").
//                            setDescription("Neste ecrã tem acesso ao resumo da sessão. Pode consultar os resultados" +
//                                    " de cada escala e, se pretender, gerar um documento PDF. " +
//                                    "Quando quiser sair, clique neste botão.")
//                            .setGravity(Gravity.TOP | Gravity.LEFT))
//                    .setOverlay(new Overlay())
//                    .playOn(closeFAB);

            // create tour guide
            TourGuideStepHelper step1 = new TourGuideStepHelper(closeFAB,
                    "Rever sessão",
                    "Neste ecrã tem acesso ao resumo da sessão. Pode consultar os resultados" +
                            " de cada escala e, se pretender, gerar um documento PDF.",
                    Gravity.TOP | Gravity.LEFT);
            TourGuideStepHelper step2 = new TourGuideStepHelper(closeFAB,
                    "Fechar",
                    "Clique neste botão para sair do resumo.");
            TourGuideStepHelper step3 = new TourGuideStepHelper(createPdfView,
                    "PDF",
                    "Ao clicar aqui pode gerar um documento PDF com o resumo da sessão," +
                            " que pode juntar ao processo do doente.",
                    Gravity.BOTTOM | Gravity.LEFT);
            TourGuideStepHelper[] steps = new TourGuideStepHelper[]{step1, step2};

            TourGuideHelper.runOverlay_ContinueMethod(getActivity(), steps);
        }

        // just for testing - update session/scale json to Firebase
        uploadSessionForTesting();


        return view;
    }

    private void uploadSessionForTesting() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        ArrayList<GeriatricScaleFirebase> scalesFromSession = FirebaseDatabaseHelper.getScalesFromSession(session);


        for (int i = 0; i < scalesFromSession.size(); i++) {

            GeriatricScaleFirebase currentScale = scalesFromSession.get(i);

            String jsonArray = gson.toJson(currentScale);

            String fileName = currentScale.getScaleName() + ".json";
            // upload file
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.
                    getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("testing/" + currentScale.getScaleName() + "/" + fileName);

            UploadTask uploadTask = storageReference.putBytes(jsonArray.getBytes());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Upload", "success");
                }
            });

            ArrayList<QuestionFirebase> questionsFromScale = FirebaseDatabaseHelper.getQuestionsFromScale(currentScale);

            for (int questionIndex = 0; questionIndex < questionsFromScale.size(); questionIndex++) {
                QuestionFirebase currentQuestion = questionsFromScale.get(questionIndex);

                jsonArray = gson.toJson(currentQuestion);

                fileName = "question-" + currentQuestion.getDescription() + ".json";
                // upload file
                storage = FirebaseStorage.getInstance();
                storageReference = storage.
                        getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                        .child("testing/" + currentScale.getScaleName() + "/" + fileName);

                uploadTask = storageReference.putBytes(jsonArray.getBytes());
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Upload", "success");
                    }
                });
            }

        }


    }

}

