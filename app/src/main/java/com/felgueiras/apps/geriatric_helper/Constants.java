package com.felgueiras.apps.geriatric_helper;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;

import com.felgueiras.apps.geriatric_helper.Firebase.ChoiceFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.QuestionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rafael on 24-09-2016.
 */
public class Constants {
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int BOTH = 2;
    public static final String SCALE = "SCALE";
    public static final String AREA = "AREA";
    public static final String ALL_AREAS = "ALL_AREAS";
    public static int SESSION_GENDER;

    public static String patient = "patientObject";
    public static String test = "TEST";

    /**
     * Test names
     */
    public static final String test_name_testeDeKatz = "Escala de Katz";
    public static final String test_name_escalaLawtonBrody = "Escala de Lawton & Brody";
    public static final String test_name_marchaHolden = "Classificaçao Funcional da Marcha de Holden";
    public static final String test_name_recursos_sociales = "Recursos sociales";
    public static final String test_name_escalaDepressaoYesavage = "Escala de Depressão Geriátrica de Yesavage – versão curta";
    public static final String test_name_mini_mental_state = "Mini mental state examination (Folstein)";
    public static final String test_name_mini_nutritional_assessment_triagem = "Mini nutritional assessment - triagem";
    public static final String test_name_mini_nutritional_assessment_global = "Mini nutritional assessment - avaliação global";
    public static final String test_name_valoracionSocioFamiliar = "Valoración Socio-Familiar de Gijón";
    public static final String test_name_burden_interview = "Zarit Burden Interview";
    public static final String test_name_barthel_index = "Barthel Index";
    public static final String test_name_short_portable_mental_status = "Short Portable Mental Status Questionnaire";
    public static final String test_name_clock_drawing = "Clock drawing test";
    public static final String test_name_set_set = "Set Test de Isaacs";
    public static final String test_name_hamilton = "Hamilton Depression Rating Scale";
    public static final String test_name_tinetti = "Escala de Tinetti";

    /**
     * Question sets.
     */
    public static final String set_name_advancedDailyLifeActivities = "Atividades avançadas da vida diária";


    /**
     * Back stack tags.
     */
    public static final String tag_view_patient_info_records = "tag_view_patient_info_records";
    public static final String tag_create_patient = "tag_create_patient";
    public static final String tag_create_session_from_favorites = "tag_create_session_from_favorites";
    public static final String tag_patient_progress = "tag_patient_progress";
    public static final String tag_progress_detail = "tag_progress_detail";
    public static final String tag_help_topic = "tag_help_topic";
    public static final String tag_cga_public = "tag_cga_public";
    public static final String tag_display_session_scale_shortcut = "tag_display_session_scale_shortcut";
    public static final String tag_view_patient_info_records_from_sessions_list = "tag_view_patient_info_records_from_sessions_list";


    /**
     * CGA Guide.
     */
    public static final String tag_guide_area = "tag_guide_area";


    // create new Session
    public static final String tag_create_session_with_patient = "tag_create_session_with_patient";
    public static final String tag_create_session_no_patient = "tag_create_session_no_patient";
    public static final String tag_display_session_scale = "tag_display_session_scale";
    // review Evaluation
    public static final String tag_view_sessions_history = "tag_view_sessions_history";
    public static final String tag_review_session_from_sessions_list = "tag_review_session_from_sessions_list";
    public static final String tag_review_session_public = "tag_review_session_public";
    public static final String tag_review_session_from_patient_profile = "tag_review_session_from_patient_profile";
    public static final String tag_review_test = "tag_review_test";
    public static final String tag_display_single_area_public = "tag_display_single_area_public";
    public static final String tag_display_single_area_private = "tag_display_single_area_private";
    // drug prescription
    public static final String tag_view_drug_info = "tag_view_drug_info";
    public static final String tag_pick_patient = "tag_pick_patient";


    /**
     * Fragment identifiers
     */
    public static final String fragment_show_patients = "showPatients";
    public static final String fragment_sessions = "sessions";
    public static final String fragment_drug_prescription = "drugPrescription";
    /**
     * Current Session ID.
     */
    public static String SESSION_ID = null;
    public static String userName = "userName";
    public static int vpPatientsPage = 0;
    public static int vpPrescriptionPage = 0;
    public static String first_run = "first_run";
    public static String area_private = "private";
    public static String area_public = "public";
    public static String area = "area";
    public static String logged_in = "logged_in";


    /**
     * CGA areas.
     */
    public static final String cga_clinical = "Avaliação clínica";
    public static final String cga_afective = "Estado afetivo";
    public static final String cga_cognitive = "Estado cognitivo";
    public static final String cga_nutritional = "Estado nutricional";
    public static final String cga_functional = "Estado funcional";
    public static final String cga_mental = "Estado mental";
    public static final String cga_social = "Situação social";
    public static String[] cga_areas = new String[]{
            cga_mental,
//            cga_clinical,
            cga_functional,
            cga_nutritional,
            cga_social};

    /**
     * Clinical evaluation guidelines.
     */
    public static final String clinical_evaluation_tips = "\uF0A1Ambiente calmo, aquecido e bem iluminado;\n" +
            "\uF0A1Sentado ou deitado, estando o médico ao nível do idoso (evitar sentimento de inferioridade);\n" +
            "\uF0A1Não gritar!–o idoso sofre de presbiacusia e não ouve sons de alta frequência;\n" +
            "\uF0A1Interrogatório claro,objetivo eaberto(o idoso pode demorar a perceber);\n" +
            "\uF0A1É necessário mais tempoe mais atenção, tendo em consideração que o idoso pode não se queixar por:\n" +
            "oDéficessensoriais, cognitivos e funcionais (muitas vezes confundidos com demência);\n" +
            "oMedoda hospitalização/institucionalização e preocupação com os custos;\n" +
            "oTendência a menosprezarsintomas que acha serem devidos ao envelhecimento normal;\n" +
            "oVergonhae pudor / negaçãoda velhice." +
            "\uF0A1Expressão semiológica fraca:\n" +
            "oExpressa-se de forma desordenada e difícil de entender;\n" +
            "oConfunde sintomatologia crónica com aguda;\n" +
            "\uF0A1Os idosos queixam-se do que os incomoda mais, mas que não é necessariamente o mais importante e correspondente à situação clínica subjacente;\n" +
            "\uF0A1Cada idoso dá uma importância diferente aos seus problemas e às suas limitações.";
    public static final String clinical_evaluation_what_to_do = "\uF0A1Pedir ao doente para descrever como é um dia típico: o doente deve falar livremente do que o preocupa;\n" +
            "\uF0A1Impedir, numa primeira abordagem, que sejam os familiaresa responder às perguntas;\n" +
            "\uF0A1Obter informação adicional junto dos familiarese cuidadores. Podem ser necessárias várias entrevistas;\n" +
            "\uF0A1Obter informação detalhada:\n" +
            "oHistória médica prévia (pluripatologianão significa ter tudo exaustivamente estudado);\n" +
            "oCuidados de saúde habituais (estilo de vida, dieta, hábitos, PNV);\n" +
            "oHistória farmacológica e medicação habitual;\n" +
            "oHistória social e profissional (religião, habitação, interesses, hobbies,…).";
    public static final String[] clinical_evaluation_fields =
            new String[]{"Medir TA sentado/deitado/ortostatismo",
                    "DesidrataçãoVsenrugamentopele–região esternal",
                    "Sons cardíacos menos audíveis e S4 presente",
                    "Sopros sistólicos em metade idosos, alt. deg. V. Aort.",
                    "Endurecimento art. periférico –Carótidas e radial",
                    "Sopro abdominal-> prev.de estenose da art. renal",
                    "Palp. abd.com almofada no pescoço –Cifose/relax. musc.",
                    "Alt. musculoesqueléticas freq. (artroses/deformações)",
                    "Postura em flexãoe marcha em passos curtos",
                    "Dismetriados membrose atrofia dos interósseos",
                    "Força e tónus musculardiminuídos",
                    "Tendência paratremor(pode ser fisiológico)",
                    "Diminuição reflexosmusculotendinosos",
                    "Pupilas mais pequenas/irregulares, menos reativas luz",
                    "Menos sensibilidade vibratória distal/propriocetiva",
                    "Aumento do limiar à dor e temperatura"};

    public static boolean discard_session = false;
    /**
     * Store the screen size of this device.
     */
    public static int screenSize;


    /**
     * Help screen.
     */
    public static final String help_topic_cga = "Avaliação Geriátrica Global";
    public static final String help_topic_functionalities = "Funcionalidades";
    public static final String help_topic_personal_area = "Área pessoal";
    public static final String help_topic_patients = "Pacientes";
    public static final String help_topic_sessions = "Sessões";
    public static final String help_topic_prescriptions = "Prescrições";
    public static final String help_topic_cga_guide = "Guia da AGG";

    public static String[] help_topics = new String[]{
            help_topic_cga,
            help_topic_functionalities,
            help_topic_personal_area,
            help_topic_patients,
            help_topic_sessions,
            help_topic_prescriptions,
            help_topic_cga_guide
    };


    /**
     * GSON files with JSONArrays.
     */
    public static final String filePatients = "patients.txt";
    public static final String fileSessions = "sessions.txt";
    public static final String fileScales = "scales.txt";
    public static final String fileQuestions = "questions.txt";
    public static final String fileChoices = "choices.txt";
    public static ActionBarDrawerToggle toggle;
    public static boolean upButton;

    /**
     * Bundles - save scroll positions.
     */
    public static Bundle patientsListBundle;
    public static Bundle drugsListBundle;
    public static HashMap<String, Bundle> patientsSessionsBundle = new HashMap<>();
    public static Bundle sessionsListBundle;
    public static int sessionsGridViewIndex;
    public static String create_session_permitted = "create_session_permitted";
    public static final String tag_create_session_with_patient_from_session = "tag_create_session_with_patient_from_session";
    public static final String tag_review_scale_from_progress = "tag_review_scale_from_progress";
    public static int progressPage = 0;
    public static final String tag_create_session_pick_patient_start = "tag_create_session_pick_patient_start";
    public static final String tag_create_patient_session_start = "tag_create_patient_session_start";
    public static final String tag_guide_scale = "tag_guide_scale";
    public static final String fragment_main_private = "fragment_main_private";
    public static final String tag_home_page_selected_page = "tag_home_page_selected_page";
    /**
     * Positions for the navigation drawer.
     */
    public static int menu_positions_home_page = 0;
    public static int menu_positions_patients = 1;
    public static int menu_positions_sessions = 2;
    public static int menu_positions_prescription = 3;
    public static int menu_positions_help = 5;
    public static int menu_positions_cga_guide = 6;
    public static int menu_positions_settings = 7;
    public static final String more_info_clicked = "more_info_clicked";
    public static ArrayList<String> allDrugs = new ArrayList<>();
    public static int bottomNavigationPatientProgress;
    public static int bottomNavigationReviewSession;
    public static String tag_add_prescription_to_patient = "tag_add_prescription_to_patient";
    public static FirebaseDatabase firebaseInstance;
    public static ArrayList<GeriatricScaleFirebase> publicScales = new ArrayList<>();

    /**
     * Hold the current public session.
     */
    public static SessionFirebase publicSession;
    public static int testNumber;
    public static ArrayList<ChoiceFirebase> publicChoices = new ArrayList<>();
    public static ArrayList<QuestionFirebase> publicQuestions = new ArrayList<>();
    public static int patientProfileBottomNavigation = 0;
}
