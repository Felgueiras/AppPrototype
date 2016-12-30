package com.example.rafael.appprototype;

/**
 * Created by rafael on 24-09-2016.
 */
public class Constants {
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int BOTH = 2;

    public static String patient = "patientObject";
    public static String test = "TEST";

    /**
     * Test types
     */
    public static final String test_type_estadoFuncional = "Estado Funcional";
    public static final String test_type_marcha = "Marcha";
    public static final String test_type_estadoAfetivo = "Estado Afetivo";
    public static final String test_type_estadoCognitivo = "Estado Cognitivo";
    public static String test_type_alimentation = "alimentation";

    /**
     * Test names
     */
    public static final String test_name_testeDeKatz = "Escala de Katz";
    public static final String test_name_escalaLawtonBrody = "Escala de Lawton & Brody";
    public static final String test_name_marchaHolden = "Classificaçao Funcional da Marcha de Holden";
    public static final String test_name_escalaDepressaoYesavage = "Escala de Depressão Geriátrica de Yesavage – versão curta";
    public static final String test_name_mini_mental_state = "Mini mental state examination (Folstein)";
    public static final String test_name_mini_nutritional_assessment = "Mini nutritional assessment";

    public static final String[] allTests = new String[]{
            test_name_mini_nutritional_assessment,
            test_name_escalaDepressaoYesavage,
            test_name_testeDeKatz, test_name_escalaLawtonBrody,
            test_name_marchaHolden};
    /**
     * Back stack tags.
     */
    public static final String tag_view_patien_info_records = "viewPatientInfoRecords";
    // create new Session
    public static final String tag_create_new_session_for_patient = "createNewSessionForPatient";
    public static final String tag_display_session_test = "displaySessionTest";
    // review Evaluation
    public static final String tag_view_sessions_history = "viewSessionsHistory";
    public static final String tag_review_session = "reviewSession";
    public static final String tag_review_test = "reviewTest";

    /**
     * Fragment identifiers
     */
    public static final String fragment_show_patients = "showPatients";
    public static final String fragment_sessions = "sessions";
    public static final String fragment_drug_prescription = "drugPrescription";
    public static boolean selectPatient = false;
    public static String create_patient = "createPatient";
    /**
     * Current Session ID.
     */
    public static String sessionID = null;
    public static boolean pickingPatient = false;
    public static String create_session = "createSession";
    public static String userName = "userName";
    public static int vpPatientsPage = 0;
    public static int vpPrescriptionPage = 0;
}
