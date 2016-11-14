package com.example.rafael.appprototype;

/**
 * Created by rafael on 24-09-2016.
 */
public class Constants {
    public static final int MALE = 0;

    public static String patient = "patientObject";
    public static String test = "TEST";

    /**
     * Test types
     */
    public static final String test_type_estadoFuncional = "Estado Funcional";
    public static final String test_type_marcha = "Marcha";
    public static final String test_type_estadoAfetivo = "Estado Afetivo";
    /**
     * Test names
     */
    public static final String test_name_testeDeKatz = "Escala de Katz";
    public static final String test_name_escalaLawtonBrody = "Escala de Lawton & Brody";
    public static final String test_name_marchaHolden = "Classificaçao Funcional da Marcha de Holden";
    public static final String test_name_escalaDepressao = "Escala de Depressão Geriátrica de Yesavage – versão curta";
    /**
     * Back stack tags.
     */
    public static final String tag_view_patien_info_records = "viewPatientInfoRecords";
    public static final String tag_display_session_test = "displaySessionTest";
    public static final String tag_create_new_session_for_patient = "createNewSessionForPatient";
    public static final String tag_review_session = "reviewSession";
    public static final String tag_view_sessions_history = "viewSessionsHistory";
    /**
     * Fragment identifiers
     */
    public static final String fragment_show_patients = "showPatients";


    public static final String fragment_sessions = "sessions";
    public static boolean selectPatient = false;
    public static final String fragment_drug_prescription = "drugPrescription";
    public static String create_patient = "createPatient";
    /**
     * Current Session ID.
     */
    public static String sessionID = null;
    public static boolean pickingPatient = false;
}
