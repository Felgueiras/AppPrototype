package com.felgueiras.apps.geriatric_helper.DataTypes;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ChoiceNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionCategory;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ScoringNonDB;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

/**
 * This class holds  definition of all the scales that exist
 */
public class Scales {

    public static String[] scalesNames = {
            Constants.test_name_barthel_index,
            Constants.test_name_clock_drawing,
            Constants.test_name_escalaDepressaoYesavage,
            Constants.test_name_escalaLawtonBrody,
            Constants.test_name_marchaHolden,
            Constants.test_name_mini_mental_state,
            Constants.test_name_mini_nutritional_assessment_global,
            Constants.test_name_mini_nutritional_assessment_triagem,
            Constants.test_name_valoracionSocioFamiliar,
            Constants.test_name_tinetti,
            Constants.test_name_testeDeKatz,
            Constants.test_name_recursos_sociales,
    };

    /**
     * Get infos about 'escala de resursos sociales de la OARS'
     */
    private static GeriatricScaleNonDB recursosSociales() {
        GeriatricScaleNonDB recursosSociales = new GeriatricScaleNonDB(Constants.test_name_recursos_sociales,
                Constants.cga_social, "",
                "• Proporciona información " +
                        "acerca de cinco áreas:\n" +
                        "\t• estructura familiar y recursos sociales\n" +
                        "\t• recursos económicos\n" +
                        "\t• salud mental\n" +
                        "\t• salud física\n" +
                        "\t• capacidades para la realización de AVD. \n" +
                        "• Evalúa las respuestas en " +
                        "una escala de 6 puntos, que van desde excelentes recursos " +
                        "sociales (1 punto) hasta el deterioro social total " +
                        "(6 puntos).");
        recursosSociales.setShortName("Recursos sociales");
        recursosSociales.setIconName("RS");
        // create Scoring
        ScoringNonDB scoring = new ScoringNonDB(1, 6, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Recursos sociales excelentes",
                1,
                "Relaciones sociales muy satisfactorias al menos una persona le cuidaría indefinidamente"));
        gradings.add(new GradingNonDB("Buenos recursos sociales",
                2,
                "Relaciones sociales satisfactorias y adequadas" +
                        "al menos una persona le cuidaría pero sólo durante un tempo definido"));
        gradings.add(new GradingNonDB("Levemente incapacitado socialmente",
                3,
                "Relaciones sociales insatisfactorias, inadecuadas " +
                        " satisfactorias al menos una persona le cuidaría indefinidamente"));
        gradings.add(new GradingNonDB("Moderadamente incapacitado socialmente",
                4,
                "Relaciones sociales insatisfactorias y escasas" +
                        "al menos una persona le cuidaría pero sólo durante un tempo definido"));
        gradings.add(new GradingNonDB("Gravemente incapacitado socialmente",
                5,
                "Relaciones sociales insatisfactorias, escasas y mala calidad " +
                        "solo se conseguiria ayuda de otra persona de cuando en cuando"));
        gradings.add(new GradingNonDB("Totalmente incapacitado socialmente",
                6,
                "Relaciones sociales insatisfactorias, escasas y mala calidad " +
                        "no se conseguiria ayuda de otra persona nunca"));
        // add Gradings to Scoring
        scoring.setValuesBoth(gradings);
        // add Scoring to Test
        recursosSociales.setScoring(scoring);
        recursosSociales.setSingleQuestion(true);

        return recursosSociales;
    }


    public static GeriatricScaleNonDB hamiltonDepressionScale() {
        GeriatricScaleNonDB hamiltonScale = new GeriatricScaleNonDB(Constants.test_name_hamilton,
                Constants.cga_mental, Constants.cga_afective,
                "• The HAM-D is designed to rate the severity of depression in patients.");
        hamiltonScale.setShortName("HAM-D");
        hamiltonScale.setIconName("HD");
        hamiltonScale.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB hamiltonScoring = new ScoringNonDB(0, 50, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Normal", 0, 7));
        gradings.add(new GradingNonDB("Mild depression", 8, 13));
        gradings.add(new GradingNonDB("Moderate depression", 14, 18));
        gradings.add(new GradingNonDB("Severe depression", 19, 22));
        gradings.add(new GradingNonDB("Very severe depression total", 23, 50));
        // add Gradings to Scoring
        hamiltonScoring.setValuesBoth(gradings);
        // add Scoring to Test
        hamiltonScale.setScoring(hamiltonScoring);
        // create Questions

        // 1
        QuestionNonDB question = new QuestionNonDB("Depressed mood (Gloomy attitude, pessimism about the future,\n" +
                "feeling of sadness, tendency to weep)", false);
        ArrayList<ChoiceNonDB> choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Sadness, etc", 1));
        choices.add(new ChoiceNonDB("Occasional weeping", 2));
        choices.add(new ChoiceNonDB("Frequent weeping", 3));
        choices.add(new ChoiceNonDB("Extreme symptoms", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 2
        question = new QuestionNonDB("Feelings of guilt", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB(" Self-reproach, feels he/she has let people\n" +
                "down", 1));
        choices.add(new ChoiceNonDB("Ideas of guilt", 2));
        choices.add(new ChoiceNonDB("Present illness is a punishment; delusions\n" +
                "of guilt", 3));
        choices.add(new ChoiceNonDB("Hallucinations of guilt", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 3
        question = new QuestionNonDB("Suicide", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Feels life is not worth living", 1));
        choices.add(new ChoiceNonDB("Wishes he/she were dead ", 2));
        choices.add(new ChoiceNonDB("Suicidal ideas or gestures", 3));
        choices.add(new ChoiceNonDB("Attempts at suicide", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 4
        question = new QuestionNonDB("Insomnia - initial (Difficulty in falling asleep)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Occasional", 1));
        choices.add(new ChoiceNonDB("Frequent", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 5
        question = new QuestionNonDB("Insomnia - middle (Complains of being restless and disturbed\n" +
                "during the night. Waking during the night)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Occasional", 1));
        choices.add(new ChoiceNonDB("Frequent", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 6
        question = new QuestionNonDB("Insomnia - delayed (Waking in early hours of the morning and\n" +
                "unable to fall asleep again)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Occasional", 1));
        choices.add(new ChoiceNonDB("Frequent", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 7
        question = new QuestionNonDB("Work and interests", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("No difficulty", 0));
        choices.add(new ChoiceNonDB("Feelings of incapacity, listlessness, indecision\n" +
                "and vacillation", 1));
        choices.add(new ChoiceNonDB(" Loss of interest in hobbies, decreased social\n" +
                "activities", 2));
        choices.add(new ChoiceNonDB("Productivity decreased", 3));
        choices.add(new ChoiceNonDB(" Unable to work. Stopped working because\n" +
                "of present illness only. (Absence from work\n" +
                "after treatment or recovery may rate a lower\n" +
                "score)", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 8
        question = new QuestionNonDB("RETARDATION\n" +
                "(Slowness of thought, speech, and activity;\n" +
                "apathy; stupor.)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Slight retardation at interview", 1));
        choices.add(new ChoiceNonDB("Obvious retardation at interview", 2));
        choices.add(new ChoiceNonDB("Interview difficult", 3));
        choices.add(new ChoiceNonDB("Complete stupor", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 9
        question = new QuestionNonDB("Agitation", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Occasional", 1));
        choices.add(new ChoiceNonDB("Frequent", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 10
        question = new QuestionNonDB("Anxiety - psychic", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("No difficulty", 0));
        choices.add(new ChoiceNonDB("Tension and irritability", 1));
        choices.add(new ChoiceNonDB("Worrying about minor matters", 2));
        choices.add(new ChoiceNonDB("Apprehensive attitude", 3));
        choices.add(new ChoiceNonDB("Fears", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 11
        question = new QuestionNonDB("Anxiety - somatic (Gastrointestinal, indigestion\n" +
                "Cardiovascular, palpitation, Headaches\n" +
                "Respiratory, Genito-urinary, etc.)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Mild", 1));
        choices.add(new ChoiceNonDB("Moderate", 2));
        choices.add(new ChoiceNonDB("Severe", 3));
        choices.add(new ChoiceNonDB("Incapacitating", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 12
        question = new QuestionNonDB("SOMATIC SYMPTOMS -\n" +
                "GASTROINTESTINAL (Loss of appetite , heavy feeling in abdomen;\n" +
                "constipation)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Mild", 1));
        choices.add(new ChoiceNonDB("Severe", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 13
        question = new QuestionNonDB("SOMATIC SYMPTOMS -\n" +
                "General ((Heaviness in limbs, back or head; diffuse\n" +
                "backache; loss of energy and fatiguability))", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Mild", 1));
        choices.add(new ChoiceNonDB("Severe", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 14
        question = new QuestionNonDB("Genital symptoms (Loss of libido, menstrual disturbances)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Mild", 1));
        choices.add(new ChoiceNonDB("Severe", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 15
        question = new QuestionNonDB("HYPOCHONDRIASIS\n", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Not present", 0));
        choices.add(new ChoiceNonDB("Self-absorption (bodily)", 1));
        choices.add(new ChoiceNonDB("Preoccupation with health", 2));
        choices.add(new ChoiceNonDB("Querulous attitude", 3));
        choices.add(new ChoiceNonDB("Hypochondriacal delusions", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 16
        question = new QuestionNonDB("Weight loss", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("No weight loss", 0));
        choices.add(new ChoiceNonDB("Slight", 1));
        choices.add(new ChoiceNonDB("Obvious or severe", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 17
        question = new QuestionNonDB("Insight (Insight must be interpreted in terms of PATIENT’s\n" +
                "understanding and background.)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("No loss", 0));
        choices.add(new ChoiceNonDB("Partial or doubtfull loss", 1));
        choices.add(new ChoiceNonDB("Loss of insight", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 18
        question = new QuestionNonDB("Diurnal variation (Symptoms worse in morning or evening.\n" +
                "Note which it is. ) ", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("No variation", 0));
        choices.add(new ChoiceNonDB("Mild variation; AM ( ) PM ( )", 1));
        choices.add(new ChoiceNonDB("Severe variation; AM ( ) PM ( )", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 19
        question = new QuestionNonDB("DEPERSONALIZATION AND\n" +
                "DEREALIZATION\n" +
                "(feelings of unreality, nihilistic ideas)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Mild", 1));
        choices.add(new ChoiceNonDB("Moderate", 2));
        choices.add(new ChoiceNonDB("Severe", 3));
        choices.add(new ChoiceNonDB("Incapacitating", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 20
        question = new QuestionNonDB("PARANOID SYMPTOMS\n" +
                "(Not with a depressive quality)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("None", 0));
        choices.add(new ChoiceNonDB("Suspicious", 1));
        choices.add(new ChoiceNonDB("Ideas of reference", 2));
        choices.add(new ChoiceNonDB("Delusions of reference and persecution", 3));
        choices.add(new ChoiceNonDB("Hallucinations, persecutory", 4));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);

        // 21
        question = new QuestionNonDB("OBSESSIONAL SYMPTOMS\n" +
                "(Obsessive thoughts and compulsions against\n" +
                "which the PATIENT struggles)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("Absent", 0));
        choices.add(new ChoiceNonDB("Mild", 1));
        choices.add(new ChoiceNonDB("Severe", 2));
        question.setChoices(choices);
        hamiltonScale.addQuestion(question);


        return hamiltonScale;
    }

    /**
     * Get infos about 'Escala de Depressão Geriátrica de Yesavage – versão curta'
     */
    public static GeriatricScaleNonDB escalaDepressaoYesavage() {
        GeriatricScaleNonDB escalaDepressao = new GeriatricScaleNonDB(Constants.test_name_escalaDepressaoYesavage,
                Constants.cga_mental, Constants.cga_afective,

                "• Utilizada para o rastreio da depressão, avaliando aspectos cognitivos e " +
                        "comportamentais tipicamente afectados na depressão do idoso.\n" +
                        "• A informação é obtida através de questionário directo ao idoso.\n" +
//                        "• A escala de Yesavage tem uma versão completa, com 30 questões e uma versão" +
//                        "curta com 15 questões. A versão curta está validada pelo autor e os seus resultados" +
//                        " são sobreponíveis aos da versão completa, pelo que é a mais utilizada.\n" +
                        "• 15 questões com resposta dicotómica (Sim ou Não).\n" +
                        "• As respostas sugestivas de existência de depressão correspondem a 1 ponto.\n" +
                        "• Tempo de aplicação: 6 minutos.");
        // short area
        escalaDepressao.setShortName("Escala Depressão Yesavage (curta)");
        escalaDepressao.setIconName("YS");
        // create Scoring
        ScoringNonDB depressionScoring = new ScoringNonDB(0, 15, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Sem depressão", 0, 5));
        gradings.add(new GradingNonDB("Depressão ligeira", 6, 10));
        gradings.add(new GradingNonDB("Depressão grave", 11, 15));
        // add Gradings to Scoring
        depressionScoring.setValuesBoth(gradings);
        // add Scoring to Test
        escalaDepressao.setScoring(depressionScoring);

        // 1
        QuestionNonDB question = new QuestionNonDB("Está satisfeito com a sua vida?", 0, 1);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 2
        question = new QuestionNonDB("Abandonou muitos dos seus interesses e actividades?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 3
        question = new QuestionNonDB("Sente que a sua vida está vazia?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 4
        question = new QuestionNonDB("Sente-se frequentemente aborrecido?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 5
        question = new QuestionNonDB("Na maior parte do tempo está de bom humor?", 0, 1);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 6
        question = new QuestionNonDB("Tem medo de que algo de mal lhe aconteça?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 7
        question = new QuestionNonDB("Sente-se feliz na maior parte do tempo?", 0, 1);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 8
        question = new QuestionNonDB("Sente-se frequentemente abandonado / desamparado?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 9
        question = new QuestionNonDB("Prefere ficar em casa, a sair e fazer coisas novas?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 10
        question = new QuestionNonDB("Sente que tem mais problemas de memória do que os outros da sua\n" +
                "idade?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 11
        question = new QuestionNonDB("Actualmente, acha que é maravilhoso estar vivo?", 0, 1);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 12
        question = new QuestionNonDB("Sente-se inútil?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 13
        question = new QuestionNonDB("Sente-se cheio de energia?", 0, 1);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 14
        question = new QuestionNonDB("Sente-se sem esperança?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);
        // 15
        question = new QuestionNonDB("Acha que as outras pessoas estão melhores que o Sr./Sra.?", 1, 0);
        question.setYesOrNo(true);
        escalaDepressao.addQuestion(question);

        return escalaDepressao;
    }

    public static GeriatricScaleNonDB advancedDailyLifeActivities() {
        GeriatricScaleNonDB escala = new GeriatricScaleNonDB(Constants.set_name_advancedDailyLifeActivities,
                Constants.cga_functional, Constants.set_name_advancedDailyLifeActivities,
                "");
        // short area
        escala.setShortName("Atividades avançadas");
        escala.setIconName("AA");
//        // create Scoring
//        ScoringNonDB depressionScoring = new ScoringNonDB(0, 15, false);
//        // create Gradings
//        ArrayList<GradingNonDB> gradings = new ArrayList<>();
//        gradings.add(new GradingNonDB("Sem depressão", 0, 5));
//        gradings.add(new GradingNonDB("Depressão ligeira", 6, 10));
//        gradings.add(new GradingNonDB("Depressão grave", 11, 15));
//        // add Gradings to Scoring
//        depressionScoring.setValuesBoth(gradings);
//        // add Scoring to Test
//        escala.setScoring(depressionScoring);

        // 1
        QuestionNonDB question = new QuestionNonDB("Colabora? em atividades sócio-recreativas e trabalho?", 0, 1);
        question.setYesOrNo(true);
        escala.addQuestion(question);
        // 2
        question = new QuestionNonDB("Utiliza tecnologia? (Ipad)", 1, 0);
        question.setYesOrNo(true);
        escala.addQuestion(question);
        // 3
        question = new QuestionNonDB("Viaja?", 1, 0);
        question.setYesOrNo(true);
        escala.addQuestion(question);
        // 4
        question = new QuestionNonDB("Pratica exercício físico intenso?", 1, 0);
        question.setYesOrNo(true);
        escala.addQuestion(question);

        return escala;
    }

    /**
     * Get infos about 'Escala de Katz'
     */
    public static GeriatricScaleNonDB escalaDeKatz() {
        GeriatricScaleNonDB testeDeKatz = new GeriatricScaleNonDB(Constants.test_name_testeDeKatz,
                Constants.cga_functional, "Atividades básicas da vida diária",
                "• Avalia a autonomia para realizar as atividades imprescindíveis à vida;\n" +
                        "• As ABVD são avaliadas na sequência habitual de deterioração/recuperação;\n" +
                        "• Procedimento: observação ou questionário direto ao idoso," +
                        " familiares ou cuidadores;\n" +
                        "• Tempo de aplicação: 5 minutos.");
        testeDeKatz.setShortName("Escala Katz");
        testeDeKatz.setIconName("KZ");
        testeDeKatz.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB katzScoring = new ScoringNonDB(0, 6, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Independência total", 6));
        gradings.add(new GradingNonDB("Dependência ligeira", 5));
        gradings.add(new GradingNonDB("Dependência moderada", 3, 4));
        gradings.add(new GradingNonDB("Dependência grave", 1, 2));
        gradings.add(new GradingNonDB("Dependência total", 0));
        // add Gradings to Scoring
        katzScoring.setValuesBoth(gradings);
        // add Scoring to Test
        testeDeKatz.setScoring(katzScoring);
        // create Questions

        // BANHO
        QuestionNonDB banho = new QuestionNonDB("Banho", false);
        ArrayList<ChoiceNonDB> ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de ajuda para lavar mais que uma parte do corpo", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "necessita de ajuda apenas para lavar uma parte do corpo", 1));
        banho.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(banho);

        // VESTIR
        QuestionNonDB vestir = new QuestionNonDB("Vestir", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "precisa de ajuda para se vestir;não é capaz de se vestir", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "escolhe a roupa adequada, veste-a e aperta-a; exclui atar os sapatos", 1));
        vestir.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(vestir);

        // UTILIZAÇão da sanita
        QuestionNonDB useToilet = new QuestionNonDB("Utilização da sanita", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "usa urinol ou arrastadeira ou necessita de ajuda para aceder e utilizar a sanita", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "não necessita de ajuda para entrar e sair do wc; usa a sanita, limpa-se e veste-se adequadamente; pode usar urinol pela noite", 1));
        useToilet.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(useToilet);

        // transferencia
        QuestionNonDB transferencia = new QuestionNonDB("Transferência", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de alguma ajuda para se deitar ou levantar da cama/\n" +
                "cadeira; está acamado", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "não necessita de ajuda para sentar-se ou levantar-se de uma ca-\n" +
                "deira nem para entrar ou sair da cama; pode usar ajudas técnicas, p.ex. bengala", 1));
        transferencia.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(transferencia);

        // continencia
        QuestionNonDB continencia = new QuestionNonDB("Continência", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "incontinência total ou parcial vesical e/ou fecal; utilização de ene-\n" +
                "mas, algália, urinol ou arrastadeira", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "controlo completo da micção e defecação", 1));
        continencia.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(continencia);

        // alimentação
        QuestionNonDB food = new QuestionNonDB("Alimentação", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de ajuda para comer; não come em absoluto ou necessita\n" +
                "de nutrição entérica / parentérica", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "leva a comida do prato à boca sem ajuda; exclui cortar a carne", 1));
        food.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(food);

        return testeDeKatz;
    }

    /**
     * Get infos about 'Zarit Burden Interview'
     */
    public static GeriatricScaleNonDB zaritBurdenInterview() {
        GeriatricScaleNonDB zaritBurdenInterview = new GeriatricScaleNonDB(Constants.test_name_burden_interview,
                Constants.cga_social, "",
                "• Specially designed to reflect the stresses experienced by" +
                        "caregivers of dementia patients. It can be completed by caregivers themselves or as part of an\n" +
                        "interview. \n" +
                        "• Caregivers are asked to respond to a series of 22 questions about the impact of the" +
                        "PATIENT’s disabilities on their life; \n" +
                        "• For each item, caregivers are to indicate how often they felt that" +
                        "way (never, rarely, sometimes, quite frequently, or nearly always).");
        zaritBurdenInterview.setShortName("Burden Interview");
        zaritBurdenInterview.setIconName("BI");
        zaritBurdenInterview.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB scoring = new ScoringNonDB(0, 88, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Little or no burden", 0, 20));
        gradings.add(new GradingNonDB("Mild to moderate burden", 21, 40));
        gradings.add(new GradingNonDB("Moderate to severe burden", 41, 60));
        gradings.add(new GradingNonDB("Severe burden", 61, 88));
        // add Gradings to Scoring
        scoring.setValuesBoth(gradings);
        // add Scoring to Test
        zaritBurdenInterview.setScoring(scoring);

        // default choices
        ArrayList<ChoiceNonDB> defaultChoices = new ArrayList<>();
        defaultChoices.add(new ChoiceNonDB("1", "Never", 0));
        defaultChoices.add(new ChoiceNonDB("2", "Rarely", 1));
        defaultChoices.add(new ChoiceNonDB("3", "Sometimes", 2));
        defaultChoices.add(new ChoiceNonDB("4", "Quite frequently", 3));
        defaultChoices.add(new ChoiceNonDB("5", "Nearly always", 4));

        ArrayList<ChoiceNonDB> secondaryChoices = new ArrayList<>();
        secondaryChoices.add(new ChoiceNonDB("1", "Not at all", 0));
        secondaryChoices.add(new ChoiceNonDB("2", "A little", 1));
        secondaryChoices.add(new ChoiceNonDB("3", "Moderately", 2));
        secondaryChoices.add(new ChoiceNonDB("4", "Quite a but", 3));
        secondaryChoices.add(new ChoiceNonDB("5", "Extremely always", 4));

        // create Questions
        // 1
        QuestionNonDB question = new QuestionNonDB("Do you feel that your relative asks for more help than he or she needs?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);

        // 2
        question = new QuestionNonDB("Do you feel that, because of the time you spend with your relative, you don't have enough time for\n" +
                "yourself?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 3
        question = new QuestionNonDB("Do you feel stressed between caring for your relative and trying to meet other responsibilities for your\n" +
                "family or work?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 4
        question = new QuestionNonDB("Do you feel embarrassed about your relative's behavior?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 5
        question = new QuestionNonDB("Do you feel angry when you are around your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 6
        question = new QuestionNonDB("Do you feel that your relative currently affects your relationship with other family members?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 7
        question = new QuestionNonDB("Are you afraid about what the future holds for your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 8
        question = new QuestionNonDB("Do you feel that your relative is dependent upon you?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 9
        question = new QuestionNonDB("Do you feel strained when you are around your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 10
        question = new QuestionNonDB("Do you feel that your health has suffered because of your involvement with your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 11
        question = new QuestionNonDB("Do you feel that you don't have as much privacy as you would like, because of your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 12
        question = new QuestionNonDB("Do you feel that your social life has suffered because you are caring for your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 13
        question = new QuestionNonDB("Do you feel uncomfortable having your friends over because of your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 14
        question = new QuestionNonDB("Do you feel that your relative seems to expect you to take care of him or her, as if you were the only one he\n" +
                "or she could depend on?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 15
        question = new QuestionNonDB("Do you feel that you don't have enough money to care for your relative, in addition to the rest of your\n" +
                "expenses?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 16
        question = new QuestionNonDB("Do you feel that you will be unable to take care of your relative much longer?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 17
        question = new QuestionNonDB("Do you feel that you have lost control of your life since your relative's death?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 18
        question = new QuestionNonDB("Do you wish that you could just leave the care of your relative to someone else?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 19
        question = new QuestionNonDB("Do you feel uncertain about what to do about your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 20
        question = new QuestionNonDB("Do you feel that you should be doing more for your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 21
        question = new QuestionNonDB("Do you feel that you could do a better job in caring for your relative?", false);
        question.setChoices(defaultChoices);
        zaritBurdenInterview.addQuestion(question);
        // 22
        question = new QuestionNonDB("Overall, how burdened do you feel in caring for your relative?", false);
        question.setChoices(secondaryChoices);
        zaritBurdenInterview.addQuestion(question);

        return zaritBurdenInterview;
    }


    /**
     * Get infos about 'Barthel index'
     */
    public static GeriatricScaleNonDB barthelIndex() {
        GeriatricScaleNonDB barthelIndex = new GeriatricScaleNonDB(Constants.test_name_barthel_index,
                Constants.cga_functional, "Atividades bBásicas da vida diária",
                "• Es la escala más internacionalmente" +
                        "conocida para la valoración funcional de" +
                        "pacientes con enfermedad cerebrovascular aguda.\n" +
                        "• Su aplicación es fundamental en unidades de rehabilitación " +
                        "y en unidades de media estancia (UME).\n" +
                        "• Evalúa 10 actividades, dando más importancia que " +
                        "el índice de Katz a las puntuaciones de los ítems relacionados " +
                        "con el control de esfínteres y la movilidad;\n" +
                        "• Presenta gran valor predictivo sobre mortalidad," +
                        " ingreso hospitalario, duración de estancia en unidades " +
                        "de rehabilitación y ubicación al alta de pacientes con " +
                        "accidente cerebrovascular.");
        barthelIndex.setShortName("Barthel Index");
        barthelIndex.setIconName("BI");
        barthelIndex.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB scoring = new ScoringNonDB(0, 100, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Dependencia leve", 60, 100));
        gradings.add(new GradingNonDB("Dependencia moderada", 40, 55));
        gradings.add(new GradingNonDB("Dependencia grave", 20, 35));
        gradings.add(new GradingNonDB("Dependencia total", 0, 19));
        // add Gradings to Scoring
        scoring.setValuesBoth(gradings);
        // add Scoring to Test
        barthelIndex.setScoring(scoring);

        // create Questions
        // 1
        QuestionNonDB question = new QuestionNonDB("Feeding", false);
        ArrayList<ChoiceNonDB> choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "Unable", 0));
        choices.add(new ChoiceNonDB("2", "needs help cutting, spreading butter, etc., or requires modified diet", 5));
        choices.add(new ChoiceNonDB("3", "independent", 10));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);

        // 2
        question = new QuestionNonDB("Bathing", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "dependent", 0));
        choices.add(new ChoiceNonDB("2", "independent (or in shower)", 5));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 3
        question = new QuestionNonDB("Grooming", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "needs to help with personal care", 0));
        choices.add(new ChoiceNonDB("2", "independent face/hair/teeth/shaving (implements provided)", 5));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 4
        question = new QuestionNonDB("Dressing", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "dependent", 0));
        choices.add(new ChoiceNonDB("2", "needs help but can do about half unaided", 5));
        choices.add(new ChoiceNonDB("3", "independent (including buttons, zips, laces, etc.)", 10));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 5
        question = new QuestionNonDB("Bowels", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "incontinent (or needs to be given enemas)", 0));
        choices.add(new ChoiceNonDB("2", "occasional accident", 5));
        choices.add(new ChoiceNonDB("3", "continent", 10));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 6
        question = new QuestionNonDB("Bladder", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "incontinent, or catheterized and unable to manage alone", 0));
        choices.add(new ChoiceNonDB("2", "occasional accident", 5));
        choices.add(new ChoiceNonDB("3", "continent", 10));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 7
        question = new QuestionNonDB("Toilet use", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "dependent", 0));
        choices.add(new ChoiceNonDB("2", "needs some help, but can do buildTable alone", 5));
        choices.add(new ChoiceNonDB("3", "independent (on and off, dressing, wiping)", 10));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 8
        question = new QuestionNonDB("Transfers (bed to chair and back)", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "unable, no sitting balance", 0));
        choices.add(new ChoiceNonDB("2", "major help (one or two people, physical), can sit", 5));
        choices.add(new ChoiceNonDB("3", "minor help (verbal or physical)", 10));
        choices.add(new ChoiceNonDB("4", "independent", 15));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 9
        question = new QuestionNonDB("Mobility", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "immobile or < 50 yards", 0));
        choices.add(new ChoiceNonDB("2", "wheelchair independent, including corners, > 50 yards", 5));
        choices.add(new ChoiceNonDB("3", "walks with help of one person (verbal or physical) > 50 yards", 10));
        choices.add(new ChoiceNonDB("4", "independent (but may use any aid; for example, stick) > 50 yards", 15));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);
        // 10
        question = new QuestionNonDB("Stairs", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "unable", 0));
        choices.add(new ChoiceNonDB("2", "needs help (verbal, physical, carrying aid)", 5));
        choices.add(new ChoiceNonDB("3", "independent", 10));
        question.setChoices(choices);
        barthelIndex.addQuestion(question);

        return barthelIndex;
    }


    /**
     * Get infos about 'Escala de Katz'
     */
    public static GeriatricScaleNonDB valoracionSocioFamiliarGijon() {
        GeriatricScaleNonDB valoracionSocioFamiliar = new GeriatricScaleNonDB(Constants.test_name_valoracionSocioFamiliar,
                Constants.cga_social, "",
                "• Se emplea para valorar la situación social y familiar de las personas " +
                        "mayores que viven en domicilio. Su objetivo " +
                        "es detectar situaciones de riesgo y problemas sociales " +
                        "para la puesta en marcha de intervenciones sociales.\n" +
                        "• Evalúa cinco áreas de riesgo social:" +
                        " situación familiar," +
                        " vivienda," +
                        " relaciones y contactos sociales," +
                        "apoyos de la red social" +
                        " y situación económica");
        valoracionSocioFamiliar.setShortName("Valoracion Socio-Familiar");
        valoracionSocioFamiliar.setIconName("VSF");
        valoracionSocioFamiliar.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB valoracionScoring = new ScoringNonDB(5, 25, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Buena/aceptable situación social", 5, 9));
        gradings.add(new GradingNonDB("Existe riesgo social", 10, 14));
        gradings.add(new GradingNonDB("Problema social", 15, 25));
        // add Gradings to Scoring
        valoracionScoring.setValuesBoth(gradings);
        // add Scoring to Test
        valoracionSocioFamiliar.setScoring(valoracionScoring);
        // create Questions

        // Situación familiar
        QuestionNonDB question = new QuestionNonDB("Situación familiar", false);
        ArrayList<ChoiceNonDB> choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "Vive con pareja y/o familia sin conflicto", 1));
        choices.add(new ChoiceNonDB("2", "Vive con pareja de similar edad", 2));
        choices.add(new ChoiceNonDB("3", "Vive con pareja y/o familia y/o otros, pero no pueden" +
                " o no quieren atenderlo", 3));
        choices.add(new ChoiceNonDB("4", "Vive solo, hijos y/o familiares próximos que no cubren todas las " +
                "necesidades", 4));
        choices.add(new ChoiceNonDB("5", "Vive solo, familia lejana, desatendido, sin familia", 5));
        question.setChoices(choices);
        valoracionSocioFamiliar.addQuestion(question);

        // Situación economica
        question = new QuestionNonDB("Situación economica", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "Más de 1,5 veces el salario mínimo", 1));
        choices.add(new ChoiceNonDB("2", "Desde 1.5 veces el salario mínimo hasta el salario mínimo exclusive", 2));
        choices.add(new ChoiceNonDB("3", "Desde el salario mínimo a pensión mínima contributiva", 3));
        choices.add(new ChoiceNonDB("4", "Pensión no contributiva", 4));
        choices.add(new ChoiceNonDB("5", "Sin ingresos o inferiores al apartado anterior", 5));
        question.setChoices(choices);
        valoracionSocioFamiliar.addQuestion(question);

        // Situación economica
        question = new QuestionNonDB("Vivienda", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "Adecuada a necesidades", 1));
        choices.add(new ChoiceNonDB("2", "Barreras arquitectónicas en la vivienda o portal de la casa", 2));
        choices.add(new ChoiceNonDB("3", "Humedades, mala higiene, equipamiento inadecuado", 3));
        choices.add(new ChoiceNonDB("4", "Ausencia ascensor, teléfono", 4));
        choices.add(new ChoiceNonDB("5", "Vivienda inadecuada (vivienda declarada en ruina, ausencia de equipamientos minimos)", 5));
        question.setChoices(choices);
        valoracionSocioFamiliar.addQuestion(question);

        // Relaciones y contactos sociales
        QuestionNonDB relacionesContactos = new QuestionNonDB("Relaciones y contactos sociales", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "Mantiene relaciones sociales fuera del domicilio", 1));
        choices.add(new ChoiceNonDB("2", "Sólo se relaciona con familia/vecinos/otros, sale de casa", 2));
        choices.add(new ChoiceNonDB("3", "Sólo se relaciona con familia, sale de casa", 3));
        choices.add(new ChoiceNonDB("4", "No sale de su domicilio, recibe familia o visitas (>1 por semana)", 4));
        choices.add(new ChoiceNonDB("5", "No sale del domicilio, ni recibe visitas (<1 semana)", 5));
        relacionesContactos.setChoices(choices);
        valoracionSocioFamiliar.addQuestion(relacionesContactos);

        // Apoyos red social
        QuestionNonDB apoyosRedSocial = new QuestionNonDB("Apoyos red social", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "No necesita ningún apoyo", 1));
        choices.add(new ChoiceNonDB("2", "Recibe apoyo de la familia y/o vecinos", 2));
        choices.add(new ChoiceNonDB("3", "Recibe apoyo social formal suficiente (centro de dia, trabajador/a" +
                " familiar, vive en residencia, etc ...)", 3));
        choices.add(new ChoiceNonDB("4", "Tiene soporte social pero es insuficiente", 4));
        choices.add(new ChoiceNonDB("5", "No tiene ningún soporte social y lo necesita", 5));
        apoyosRedSocial.setChoices(choices);
        valoracionSocioFamiliar.addQuestion(apoyosRedSocial);

        return valoracionSocioFamiliar;
    }

    /**
     * Get infos about 'Escala de Lawton & Brody'
     */
    public static GeriatricScaleNonDB escalaLawtonBrody() {
        GeriatricScaleNonDB escalaLawtonBrody = new GeriatricScaleNonDB(Constants.test_name_escalaLawtonBrody,
                Constants.cga_functional, "Atividades Instrumentais de Vida Diária",
                "• Avalia a autonomia para realizar as atividades necessárias para viver\n" +
                        "de forma independente na comunidade\n" +
                        "• Cada AIVD tem vários níveis de dependência (3 a 5);\n" +
                        "• Procedimento: observação ou questionário directo ao idoso,\n" +
                        "familiares ou cuidadores;\n" +
                        "•  Tempo de aplicação: 5 minutos.");
        escalaLawtonBrody.setShortName("Escala Lawton Brody");
        escalaLawtonBrody.setIconName("LB");
        escalaLawtonBrody.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB lawtonScoring = new ScoringNonDB(0, 8, true);
        lawtonScoring.setScoringMen(0, 5);
        // create Gradings for men
        ArrayList<GradingNonDB> gradingsMen = new ArrayList<>();
        gradingsMen.add(new GradingNonDB("Independência total", 5));
        gradingsMen.add(new GradingNonDB("Dependência ligeira", 4));
        gradingsMen.add(new GradingNonDB("Dependência moderada", 2, 3));
        gradingsMen.add(new GradingNonDB("Dependência grave", 1));
        gradingsMen.add(new GradingNonDB("Dependência total", 0));
        // add Gradings to Scoring
        lawtonScoring.setValuesMen(gradingsMen);

        // create Gradings for women
        ArrayList<GradingNonDB> gradingsWomen = new ArrayList<>();
        gradingsWomen.add(new GradingNonDB("Independência total", 8));
        gradingsWomen.add(new GradingNonDB("Dependência ligeira", 6, 7));
        gradingsWomen.add(new GradingNonDB("Dependência moderada", 4, 5));
        gradingsWomen.add(new GradingNonDB("Dependência grave", 2, 3));
        gradingsWomen.add(new GradingNonDB("Dependência total", 0, 1));
        // add Gradings to Scoring
        lawtonScoring.setValuesWomen(gradingsWomen);
        // add Scoring to Test
        escalaLawtonBrody.setScoring(lawtonScoring);
        // create Questions

        // TELEFONE
        QuestionNonDB telefone = new QuestionNonDB("Utilização do telefone", false);
        ArrayList<ChoiceNonDB> ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Utiliza o telefone por iniciativa própria", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("É capaz de marcar bem alguns números\n" +
                "familiares", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("É capaz de pedir para telefonar, mas não é\n" +
                "capaz de marcar", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Não é capaz de usar o telefone", 0));
        telefone.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(telefone);

        // FAZER COMPRAS
        QuestionNonDB shopping = new QuestionNonDB("Fazer compras", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza todas as compras necessárias", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza independentemente pequenas compras", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Necessita de ir acompanhado para fazer qualquer compra", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("É totalmente incapaz de comprar", 0));
        shopping.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(shopping);

        // Preparar refeições
        QuestionNonDB prepareMeals = new QuestionNonDB("Preparação de refeições", false);
        prepareMeals.setOnlyForWomen(true);

        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Organiza, prepara e serve as refeições\n" +
                "sozinho e adequadamente", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Prepara adequadamente as refeições se se\n" +
                "fornecem os alimentos", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Prepara, aquece e serve as refeições, mas\n" +
                "não segue uma dieta adequada", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Necessita que lhe preparem e sirvam as\n" +
                "refeições", 0));
        prepareMeals.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(prepareMeals);

        // Tarefas domésticas
        QuestionNonDB domesticChores = new QuestionNonDB("Tarefas domésticas", false);
        domesticChores.setOnlyForWomen(true);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Mantém a casa sozinho ou com ajuda\n" +
                "ocasional (trabalhos pesados)", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza tarefas ligeiras, como lavar pratos ou\n" +
                "fazer a cama", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza tarefas ligeiras, mas não pode manter\n" +
                "um nível adequado de limpeza", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Necessita de ajuda em todas as tarefas\n" +
                "domésticas", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Não participa em nenhuma tarefa doméstica", 0));
        domesticChores.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(domesticChores);

        // lavagem da roupa
        QuestionNonDB washingClothes = new QuestionNonDB("Lavagem da roupa", false);
        washingClothes.setOnlyForWomen(true);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Lava sozinho toda a sua roupa", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Lava sozinho pequenas peças de roupa", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("A lavagem da roupa tem de ser feita por\n" +
                "terceiros", 0));
        washingClothes.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(washingClothes);

        // meios de transporte
        QuestionNonDB transportation = new QuestionNonDB("Utilização de meios de transporte", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Viaja sozinho em transporte público ou conduz o seu próprio carro", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("É capaz de apanhar um t+axi, mas não usa outro transporte", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Viaja em transportes públicos quando vai acomoanhado", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Só utiliza o táxi ou o automóvel com ajuda de terceiros", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Não viaja", 0));
        transportation.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(transportation);

        // manejo da medicação
        QuestionNonDB medication = new QuestionNonDB("Manejo da medicação", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("É capaz de tomar a medicação à hora e dose corretas", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Toma a medicação se a dose é preparada previamente", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Não é capaz de administrar a sua medicação", 0));
        medication.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(medication);

        // responsabilidade de assuntos financeiros
        QuestionNonDB financial = new QuestionNonDB("Responsabilidade de assuntos financeiros", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Encarrega-se de assuntos financeiros sozinho", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza as compras diárias, mas necessita de ajuda em grandes compras e no banco", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Incapaz de manusear o dinheiro", 0));
        financial.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(financial);

        return escalaLawtonBrody;
    }


    public static GeriatricScaleNonDB clockDrawing() {
        GeriatricScaleNonDB clockDrawing = new GeriatricScaleNonDB(Constants.test_name_clock_drawing,
                Constants.cga_mental, Constants.cga_cognitive,
                "• Se trata de un test sencillo que valora el funcionamiento " +
                        "cognitivo global, principalmente la apraxia " +
                        "constructiva, la ejecución motora, la atención, la comprensión" +
                        "y el conocimiento numérico\n" +
                        "• Procedimento: \n" +
                        "Step 1: Give PATIENT a sheet of paper with a large (relative to the size of handwritten\n" +
                        "numbers) predrawn circle on it. Indicate the top of the page.\n" +
                        "Step 2: Instruct PATIENT to draw numbers in the circle to make the circle look like the face\n" +
                        "of a clock and then draw the hands of the clock to read \"10 after 11.\" \n" +
                        "•  Higher scores reflect a greater number of errors and more impairment. A score of ≥3 represents\n" +
                        "a cognitive deficit, while a score of 1 or 2 is considered normal. ");
        clockDrawing.setShortName("Clock Drawing");
        clockDrawing.setIconName("CD");
        // create Scoring
        ScoringNonDB clockDrawingScoring = new ScoringNonDB(1, 6, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Perfect", 1, "No errors in the task"));
        gradings.add(new GradingNonDB("Minor visuospatial errors ", 2, "a) Mildly impaired spacing of times\n" +
                "b) Draws times outside circle\n" +
                "c) Turns page while writing so that some\n" +
                "numbers appear upside down\n" +
                "d) Draws in lines (spokes) to orient spacing "));
        gradings.add(new GradingNonDB("Inaccurate representation of 10 after 11\n" +
                "when visuospatial organization is perfect\n" +
                "or shows only minor deviations ", 3, "a) Minute hand points to 10\n" +
                "b) Writes \"10 after 11\"\n" +
                "c) Unable to make any denotation of time "));
        gradings.add(new GradingNonDB("Moderate visuospatial disorganization of\n" +
                "times such that accurate denotation of 10\n" +
                "after 11 is impossible ", 4, "a) Moderately poor spacing\n" +
                "b) Omits numbers\n" +
                "c) Perseveration: repeats circle or continues\n" +
                "on past 12 to 13, 14, 15, etc.\n" +
                "d) Right-left reversal: numbers drawn\n" +
                "counterclockwise\n" +
                "e) Dysgraphia: unable to write numbers\n" +
                "accurately"));
        gradings.add(new GradingNonDB("Severe level of disorganization as\n" +
                "described in scoring of 4 ", 5, "See examples for scoring of 4 "));
        gradings.add(new GradingNonDB("No reasonable representation of a clock ", 6, "a) No attempt at all\n" +
                "b) No semblance of a clock at all\n" +
                "c) Writes a word or name "));
        // add Gradings to Scoring
        clockDrawingScoring.setValuesBoth(gradings);
        // add Scoring to Test
        clockDrawing.setScoring(clockDrawingScoring);
        clockDrawing.setSingleQuestion(true);

        return clockDrawing;
    }


    /**
     * Get infos about 'Marcha'
     */
    public static GeriatricScaleNonDB marchaHolden() {
        GeriatricScaleNonDB marcha = new GeriatricScaleNonDB(Constants.test_name_marchaHolden,
                Constants.cga_functional, "Marcha",
                "• Avalia a autonomia na marcha de acordo com o tipo de ajuda física" +
                        "ou supervisão necessárias, em função do tipo de superfície (plana," +
                        "inclinada, escadas).\n" +
                        "• São estabelecidas 6 categorias, tentando-se classificar o idoso na " +
                        "categoria que mais se aproxima da sua capacidade para a marcha\n" +
                        "• Procedimento: observação ou questionário direto ao idoso,\n" +
                        "familiares ou cuidadores;\n" +
                        "• Tempo de aplicação: 3-5 minutos.");
        marcha.setShortName("Marcha");
        marcha.setIconName("M");
        marcha.setContainsPhoto(true);

        marcha.setSingleQuestion(true);
        // create Scoring
        ScoringNonDB marchaScoring = new ScoringNonDB(0, 5, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Marcha independente", 5, "O idoso é capaz de andar independentemente em superfícies planas, inclinadas ou escadas"));
        gradings.add(new GradingNonDB("Marcha independente (superfície plana)", 4, "O idoso é capaz de andar de forma independente em " +
                "superfícies planas, mas requer supervisão ou ajuda física " +
                "para superar escadas, superfícies inclinadas ou terrenos " +
                "não planos"));
        gradings.add(new GradingNonDB("Marcha dependente com supervisão", 3, "O idoso é capaz de andar de forma independente em " +
                "superfícies planas sem ajuda, mas para a sua segurança " +
                "requer supervisão de uma pessoa."));
        gradings.add(new GradingNonDB("Marcha dependente - Nível II", 2, "O idoso requer ajuda mínima de uma pessoa para não " +
                "cair em superfície plana. A ajuda consiste em " +
                "toques suaves, contínuos ou intermitentes, para ajudar a " +
                "manter o equilíbrio e a coordenação"));
        gradings.add(new GradingNonDB("Marcha dependente - Nível I", 1, "O idoso necessita de grande ajuda de uma pessoa para " +
                "andar e evitar quedas. Esta ajuda é constante, sendo " +
                "necessária para suportar o peso do corpo ou para manter " +
                "o equilíbrio ou a coordenação"));
        gradings.add(new GradingNonDB("Marcha ineficaz", 0, "O idoso não é capaz de caminhar, caminha apenas em " +
                "barras paralelas ou requer ajuda física ou supervisão"));
        // add Gradings to Scoring
        marchaScoring.setValuesBoth(gradings);
        // add Scoring to Test
        marcha.setScoring(marchaScoring);
        marcha.setSingleQuestion(true);

        return marcha;
    }


    /**
     * Mini nutritional assessment evaluation.
     *
     * @return
     */
    public static GeriatricScaleNonDB miniNutritionalAssessmentTriagem() {
        GeriatricScaleNonDB nutritionalAssessment = new GeriatricScaleNonDB(Constants.test_name_mini_nutritional_assessment_triagem,
                Constants.cga_nutritional, "",
                "• Deteta presença/risco malnutrição sem recurso a parâmetros analíticos;\n" +
                        "• Procedimento: questionário direto ao idoso ou a familiares/\n" +
                        "cuidadores (excluindo as questões sobre a auto-percepção);\n" +
                        "• Primeira parte (Triagem) é constituída por 6 questões;\n" +
                        "• Caso não seja possível determinar o IMC (p.ex. doentes acamados) pode-se " +
                        "em alternativa usar o perímetro da perna – se PP < 31 cm corresponde a 0 " +
                        "pontos; se PP ≥ 31 cm corresponde a 3 pontos;\n" +
                        "• Se triagem com malnutrição/risco realiza-se segunda parte do questionário;\n" +
                        "• Segunda parte (Avaliação Global) é constituída por 12 questões;\n" +
                        "• Soma da pontuação permite identificar 3 categorias: estado nutricional " +
                        "normal, sob risco de malnutrição, malnutrição;");
        // short area
        nutritionalAssessment.setShortName("Nutritional assessment - triagem");
        nutritionalAssessment.setIconName("NA-T");
        nutritionalAssessment.setMultipleChoice(true);
//        // create Scoring
//        ScoringNonDB scoring = new ScoringNonDB(0, 30, false);
//        // create Gradings
//        ArrayList<GradingNonDB> gradings = new ArrayList<>();
//        gradings.add(new GradingNonDB("Estado nutricional normal", 24, 30));
//        gradings.add(new GradingNonDB("Sob risco de desnutrição", 17, 23));
//        gradings.add(new GradingNonDB("Desnutrido", 0, 16));// create Scoring
        ScoringNonDB scoring = new ScoringNonDB(0, 14, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Estado nutricional normal", 12, 14));
        gradings.add(new GradingNonDB("Sob risco de desnutrição", 8, 11));
        gradings.add(new GradingNonDB("Desnutrido", 0, 7));
        // add Gradings to Scoring
        scoring.setValuesBoth(gradings);
        // add Scoring to Test
        nutritionalAssessment.setScoring(scoring);

        /*
        Responda à secção “triagem”, preenchendo as caixas com os
        números adequados. Some os números da secção “triagem”.
        Se a pontuação obtida for igual ou menor que 11, continue o preenchimento
        do questionário para obter a pontuação indicadora de desnutrição.
         */

        /**
         * Triagem.
         */
        // 1
        QuestionNonDB question = new QuestionNonDB("A - Nos últimos três meses houve diminuição da ingesta\n" +
                "alimentar devido a perda de apetite, problemas digestivos\n" +
                "ou dificuldade para mastigar ou deglutir?", false);
        ArrayList<ChoiceNonDB> choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("diminuição grave da ingesta", 0));
        choiceNonDBs.add(new ChoiceNonDB("diminuição moderada da ingesta", 1));
        choiceNonDBs.add(new ChoiceNonDB("sem diminuição da ingesta", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        // 2
        question = new QuestionNonDB("B - Perda de peso nos últimos 3 meses", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("superior a três quilos", 0));
        choiceNonDBs.add(new ChoiceNonDB("não sabe informar", 1));
        choiceNonDBs.add(new ChoiceNonDB("entre um e três quilos", 2));
        choiceNonDBs.add(new ChoiceNonDB("sem perda de peso", 3));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        // 3
        question = new QuestionNonDB("C - Mobilidade", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("restrito ao leito ou à cadeira de rodas", 0));
        choiceNonDBs.add(new ChoiceNonDB("deambula mas não é capaz de sair de casa", 1));
        choiceNonDBs.add(new ChoiceNonDB("normal", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        // 4
        question = new QuestionNonDB("D - Passou por algum stress psicológico ou doença aguda nos\n" +
                "últimos três meses?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("sim", 0));
        choiceNonDBs.add(new ChoiceNonDB("não", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        // 5
        question = new QuestionNonDB("E - Problemas neuropsicológicos", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("demência ou depressão graves", 0));
        choiceNonDBs.add(new ChoiceNonDB("demência ligeira", 1));
        choiceNonDBs.add(new ChoiceNonDB("sem problemas psicológicos", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        // 6
        question = new QuestionNonDB("F - Índice de Massa Corporal (IMC = peso[kg] / estatura [m 2 ])", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("IMC < 19", 0));
        choiceNonDBs.add(new ChoiceNonDB("19 < IMC < 21", 1));
        choiceNonDBs.add(new ChoiceNonDB("21 < IMC < 23", 2));
        choiceNonDBs.add(new ChoiceNonDB("IMC > 23", 3));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        return nutritionalAssessment;
    }


    public static GeriatricScaleNonDB tinettiScale() {
        GeriatricScaleNonDB tinettiScale = new GeriatricScaleNonDB(Constants.test_name_tinetti,
                Constants.cga_functional, "Avaliação do risco de queda",
                "• Avalia o equilíbrio estático e dinâmico do idoso\n" +
                        "• A primeira parte é composta por 9 questões, nas quais o paciente deve estar sentado" +
                        "numa cadeira sem braços\n" +
                        "• Na segunda parte o sujeito faz um percurso de 3m, na sua passada normal" +
                        " e volta com passos mais rápidos até à cadeira, deverá utilizar os seus " +
                        " auxiliares de marcha habituais");
        // short area
        tinettiScale.setShortName("Tinetti");
        tinettiScale.setIconName("TI");
        tinettiScale.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB scoring = new ScoringNonDB(0, 28, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("TODO", 0, 28));
        // add Gradings to Scoring
        scoring.setValuesBoth(gradings);
        // add Scoring to Test
        tinettiScale.setScoring(scoring);


        /**
         * Equilíbrio estático.
         */
        // 1
        QuestionNonDB question = new QuestionNonDB("Equilíbrio sentado", false);
        ArrayList<ChoiceNonDB> choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Inclina-se ou desliza na cadeira", 0));
        choiceNonDBs.add(new ChoiceNonDB("Inclina-se ligeiramente ou aumenta a distância" +
                " das nádegas ao encosto da cadeira", 1));
        choiceNonDBs.add(new ChoiceNonDB("Estável, seguro", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 2
        question = new QuestionNonDB("Levantar-se", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Incapaz sem ajuda ou perde equilíbrio", 0));
        choiceNonDBs.add(new ChoiceNonDB("Capaz, mas utiliza os braços para ajudar ou" +
                " faz excessiva flexão do tronco ou não consegue à primeira tentativa", 1));
        choiceNonDBs.add(new ChoiceNonDB("Capaz na primeira tentativa sem usar os braços", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 3
        question = new QuestionNonDB("Equilíbrio imediato (primeiros 5 segundos)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Instável (cambaleante, move os pés, macadas" +
                "oscilações do tronco, tenta agarrar algo para suportar-se)", 0));
        choiceNonDBs.add(new ChoiceNonDB("Estável, mas utiliza auxiliar de marcha para suportar-se", 1));
        choiceNonDBs.add(new ChoiceNonDB("Estável sem qualquer tipo de ajudas", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 4
        question = new QuestionNonDB("Equilíbrio em pé com os pés paralelos", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Instável", 0));
        choiceNonDBs.add(new ChoiceNonDB("Estável mas alargando a base de sustentação (calcanhares afastados" +
                "10 cm) ou recorrendo a auxiliar de marcha para apoio", 1));
        choiceNonDBs.add(new ChoiceNonDB("Pés próximos e sem ajudas", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 5
        question = new QuestionNonDB("Pequenos desequilíbrios na mesma posição (sujeito de pé com os pés próximos, o observador" +
                " empurra-o levemente com a palma da mão, 3 vezes ao nível do esterno)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Começa a cair", 0));
        choiceNonDBs.add(new ChoiceNonDB("Vacilante, agarra-se, mas estabiliza", 1));
        choiceNonDBs.add(new ChoiceNonDB("Estável", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 6
        question = new QuestionNonDB("Fechar os olhos na mesma posição", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Instável", 0));
        choiceNonDBs.add(new ChoiceNonDB("Estável", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 7
        question = new QuestionNonDB("Volta de 360º (2 vezes)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Instável (agarra-se, vacila)", 0));
        choiceNonDBs.add(new ChoiceNonDB("Estável, mas dá passos descontínuos", 1));
        choiceNonDBs.add(new ChoiceNonDB("Estável e passos contínuos", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 8
        question = new QuestionNonDB("Apoio unipodal (aguenta pelo menos 5 segundos de forma estável)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Não consegue ou tenta segurar-se a qualquer objeto", 0));
        choiceNonDBs.add(new ChoiceNonDB("Aguenta 5 segundos de forma estável", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 9
        question = new QuestionNonDB("Sentar-se", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Pouco seguro ou cai na cadeira ou calcula mal a distância", 0));
        choiceNonDBs.add(new ChoiceNonDB("Usa os braços ou movimento não harmonioso", 1));
        choiceNonDBs.add(new ChoiceNonDB("Seguro, movimento harmonioso", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        /**
         * Equilíbrio dinâmico - marcha.
         */
        // 10
        question = new QuestionNonDB("Início da marcha (imediatamente após o sinal de partida)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Hesitação ou múltiplas tentativas para iniciar", 0));
        choiceNonDBs.add(new ChoiceNonDB("Sem hesitação", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 11
        question = new QuestionNonDB("Largura do passo (pé direito)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Não ultrapassa à frente do pé em apoio", 0));
        choiceNonDBs.add(new ChoiceNonDB("Ultrapassa o pé esquerdo em apoio", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 12
        question = new QuestionNonDB("Altura do passo (pé direito)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("O pé direito não perde completamente o contacto com o solo", 0));
        choiceNonDBs.add(new ChoiceNonDB("O pé direito eleva-se completamente do solo", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 13
        question = new QuestionNonDB("Largura do passo (pé esquerdo)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Não ultrapassa à frente do pé em apoio", 0));
        choiceNonDBs.add(new ChoiceNonDB("Ultrapassa o pé direito em apoio", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 14
        question = new QuestionNonDB("Altura do passo (pé esquerdo)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("O pé esquerdoesquerdo não perde totalmente o contacto com o solo", 0));
        choiceNonDBs.add(new ChoiceNonDB("O pé direito eleva-se totalmente do solo", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 15
        question = new QuestionNonDB("Simetria do passo", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Comprimento do passo aparentemente assimétrico", 0));
        choiceNonDBs.add(new ChoiceNonDB("Comprimento do passo aparentemente simétrico", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 16
        question = new QuestionNonDB("Continuidade do passo", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Pára ou dá passos descontínuos", 0));
        choiceNonDBs.add(new ChoiceNonDB("Passos contínuos", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 17
        question = new QuestionNonDB("Percurso de 3m (previmente marcado)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Desvia-se da linha amrcada", 0));
        choiceNonDBs.add(new ChoiceNonDB("Desvia-se ligeiramente ou utiliza auxiliar de marcha", 1));
        choiceNonDBs.add(new ChoiceNonDB("Sem desvios e sem ajudas", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 18
        question = new QuestionNonDB("Estabilidade do tronco", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Nítida oscilação ou utiliza auxiliar de marcha", 0));
        choiceNonDBs.add(new ChoiceNonDB("Sem oscilação mas com flexão dos joelhos ou coluna ou " +
                "afasta os braços do tronco enquanto caminha", 1));
        choiceNonDBs.add(new ChoiceNonDB("Sem oscilação, sem flexão, não utliza os braços, nem" +
                "auxiliares de marcha", 2));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        // 19
        question = new QuestionNonDB("Base de sustentação durante a marcha ", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("Calcanhares muito afastados", 0));
        choiceNonDBs.add(new ChoiceNonDB("Calcanhares próximos, quase se tocam", 1));
        question.setChoices(choiceNonDBs);
        tinettiScale.addQuestion(question);

        return tinettiScale;
    }


    public static GeriatricScaleNonDB miniNutritionalAssessmentGlobal() {
        GeriatricScaleNonDB nutritionalAssessment = new GeriatricScaleNonDB(Constants.test_name_mini_nutritional_assessment_global,
                Constants.cga_nutritional, "",
                "• Deteta presença/risco malnutrição sem recurso a parâmetros analíticos;\n" +
                        "• Procedimento: questionário direto ao idoso ou a familiares/\n" +
                        "cuidadores (excluindo as questões sobre a auto-percepção);\n" +
                        "• Primeira parte (Triagem) é constituída por 6 questões;\n" +
                        "• Caso não seja possível determinar o IMC (p.ex. doentes acamados) pode-se " +
                        "em alternativa usar o perímetro da perna – se PP < 31 cm corresponde a 0 " +
                        "pontos; se PP ≥ 31 cm corresponde a 3 pontos;\n" +
                        "• Se triagem com malnutrição/risco realiza-se segunda parte do questionário;\n" +
                        "• Segunda parte (Avaliação Global) é constituída por 12 questões;\n" +
                        "• Soma da pontuação permite identificar 3 categorias: estado nutricional " +
                        "normal, sob risco de malnutrição, malnutrição;");
        // short area
        nutritionalAssessment.setShortName("Nutritional assessment - avaliação global");
        nutritionalAssessment.setIconName("NA-G");
        nutritionalAssessment.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB scoring = new ScoringNonDB(0, 30, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Estado nutricional normal", 24, 30));
        gradings.add(new GradingNonDB("Sob risco de desnutrição", 17, 23));
        gradings.add(new GradingNonDB("Desnutrido", 0, 16));// create Scoring

        // add Gradings to Scoring
        scoring.setValuesBoth(gradings);
        // add Scoring to Test
        nutritionalAssessment.setScoring(scoring);


        /**
         * Avaliação Global.
         */
        // 7
        QuestionNonDB question = new QuestionNonDB("G - O doente vive na sua própria casa\n" +
                "(não em instituição geriátrica ou hospital)", false);
        ArrayList<ChoiceNonDB> choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("sim", 1));
        choices.add(new ChoiceNonDB("não", 0));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 8
        question = new QuestionNonDB("H - Utiliza mais de três medicamentos diferentes por dia?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("sim", 0));
        choices.add(new ChoiceNonDB("não", 1));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 9
        question = new QuestionNonDB("I - Lesões de pele ou escaras?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("sim", 0));
        choices.add(new ChoiceNonDB("não", 1));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 10
        question = new QuestionNonDB("J - Quantas refeições faz por dia?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("uma refeição", 0));
        choices.add(new ChoiceNonDB("duas refeições", 1));
        choices.add(new ChoiceNonDB("três refeições", 2));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 11
        question = new QuestionNonDB("K O doente consome:\n" +
                "• pelo menos uma porção diária de leite ou derivados (leite, queijo, iogurte)?\n" +
                "• duas ou mais porções semanais de leguminosas ou ovos?\n" +
                "• carne, peixe ou aves todos os dias?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("nenhuma ou uma resposta «sim»", 0));
        choices.add(new ChoiceNonDB("duas respostas «sim»", 0.5));
        choices.add(new ChoiceNonDB("três respostas «sim»", 1.0));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 12
        question = new QuestionNonDB("L - O doente consome duas ou mais porções diárias de fruta\n" +
                "ou produtos hortícolas?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("não", 0));
        choices.add(new ChoiceNonDB("sim", 1));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 13
        question = new QuestionNonDB("M - Quantos copos de líquidos (água, sumo, café, chá, leite) o\n" +
                "doente consome por dia?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("menos de três copos", 0));
        choices.add(new ChoiceNonDB("três a cinco copos", 0.5));
        choices.add(new ChoiceNonDB("mais de cinco copos", 1));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 14
        question = new QuestionNonDB("N - Modo de se alimentar", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("não é capaz de se alimentar sozinho", 0));
        choices.add(new ChoiceNonDB("alimenta-se sozinho, porém com dificuldade", 1));
        choices.add(new ChoiceNonDB("alimenta-se sozinho sem dificuldade", 2));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 15
        question = new QuestionNonDB("O - O doente acredita ter algum problema nutricional?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("acredita estar desnutrido", 0));
        choices.add(new ChoiceNonDB("não sabe dizer", 1));
        choices.add(new ChoiceNonDB("acredita não ter um problema nutricional", 2));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 16
        question = new QuestionNonDB("P - Em comparação com outras pessoas da mesma idade,\n" +
                "como considera o doente a sua própria saúde?", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("pior", 0));
        choices.add(new ChoiceNonDB("não sabe", 0.5));
        choices.add(new ChoiceNonDB("igual", 1));
        choices.add(new ChoiceNonDB("melhor", 2));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 17
        question = new QuestionNonDB("Q - Perímetro braquial (PB) em cm", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("PB < 21", 0));
        choices.add(new ChoiceNonDB("21 < PB < 22", 0.5));
        choices.add(new ChoiceNonDB("PB > 22", 1));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);

        // 18
        question = new QuestionNonDB("R - Perímetro da perna (PP) em cm", false);
        choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("PP < 31", 0));
        choices.add(new ChoiceNonDB("PP > 31", 1));
        question.setChoices(choices);
        nutritionalAssessment.addQuestion(question);


        return nutritionalAssessment;
    }

    /**
     * Get infos about 'Mini mental state examination (Folstein)'
     */
    public static GeriatricScaleNonDB mentalStateFolstein() {
        GeriatricScaleNonDB mentalState = new GeriatricScaleNonDB(Constants.test_name_mini_mental_state,
                Constants.cga_mental, Constants.cga_cognitive,
                "• Permite fazer uma avaliação sumária das funções cognitivas.\n" +
                        "• Avalia a orientação, memória imediata " +
                        "e recente, capacidade de atenção e cálculo, linguagem e capacidade construtiva.\n" +
                        "• Procedimento: questionário directo ao idoso.\n" +
                        "• A interpretação da pontuação final depende do nível educacional do idoso.\n" +
                        "• Tempo de aplicação: 5-10 minutos.");
        // short area
        mentalState.setShortName("Mini-Mental State Examination");
        mentalState.setIconName("MMSE");
        mentalState.setMultipleCategories(true);

        // create Scoring
        ScoringNonDB mentalStateScoring = new ScoringNonDB(0, 30, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Escolaridade superior a 11 anos", 23, 30));
        gradings.add(new GradingNonDB("1 a 11 anos de escolaridade", 16, 22));
        gradings.add(new GradingNonDB("Analfabetos", 0, 15));
        // add Gradings to Scoring
        mentalStateScoring.setValuesBoth(gradings);
        // add Scoring to Test
        mentalState.setScoring(mentalStateScoring);

        // Orientação
        QuestionCategory category = new QuestionCategory("Orientação");
        category.setDescription("");
        // 1
        QuestionNonDB question = new QuestionNonDB("Em que ano estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 2
        question = new QuestionNonDB("Em que mês estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 3
        question = new QuestionNonDB("Em que dia do mês estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 4
        question = new QuestionNonDB("Em que dia da semana estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 5
        question = new QuestionNonDB("Em que estação do ano estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 6
        question = new QuestionNonDB("Em que país estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 7
        question = new QuestionNonDB("Em que distrito vive");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 8
        question = new QuestionNonDB("Em que terra vive?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 9
        question = new QuestionNonDB("Em que casa estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);

        // 10
        question = new QuestionNonDB("Em que andar estamos?");
        question.setRightWrong(true);
        category.addQuestion(question);


        mentalState.addQuestionCategory(category);


        // Retenção
        category = new QuestionCategory("Retenção");
        category.setDescription("“Vou dizer três palavras; queria que as repetisse, mas só depois de eu as dizer todas; procure ficar a sabê-las de cor”.");
        // 11
        question = new QuestionNonDB("Pêra");
        question.setRightWrong(true);
        category.addQuestion(question);
        // 12
        question = new QuestionNonDB("Gato");
        question.setRightWrong(true);
        category.addQuestion(question);
        // 13
        question = new QuestionNonDB("Bola");
        question.setRightWrong(true);
        category.addQuestion(question);
        // add category to the test
        mentalState.addQuestionCategory(category);

        // Atenção e cálculo
        category = new QuestionCategory("Atenção e cálculo");
        category.setDescription("“Agora peço-lhe que me diga quantos são 30 menos 3 e depois ao número encontrado volta a tirar 3 e repete assim\n" +
                "até eu lhe dizer para parar”\n" +
                "Se der uma errada mas depois continuar a subtrair bem, " +
                "consideram-se as seguintes como correctas. Parar ao fim de 5 respostas");
        // 14
        question = new QuestionNonDB("27", 1, 0);
        question.setRightWrong(true);
        category.addQuestion(question);
        // 15
        question = new QuestionNonDB("24", 1, 0);
        question.setRightWrong(true);
        category.addQuestion(question);
        // 16
        question = new QuestionNonDB("21", 1, 0);
        question.setRightWrong(true);
        category.addQuestion(question);
        // 17
        question = new QuestionNonDB("18", 1, 0);
        question.setRightWrong(true);
        category.addQuestion(question);
        // 18
        question = new QuestionNonDB("15", 1, 0);
        question.setRightWrong(true);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);


        // Evocação
        category = new QuestionCategory("Evocação");
        category.setDescription("“Veja se consegue dizer as três palavras que pedi há pouco para decorar”.");

        // 19
        question = new QuestionNonDB("Pêra", 0, 1);
        category.addQuestion(question);
        // 20
        question = new QuestionNonDB("Gato", 1, 0);
        category.addQuestion(question);
        // 21
        question = new QuestionNonDB("Bola", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);


        // Linguagem a
        category = new QuestionCategory("Linguagem a");
        category.setDescription("“Como se chama isto? Mostrar os objectos:");
        // 21
        question = new QuestionNonDB("Relógio", 0, 1);
//        question.setImage(R.drawable.folstein_watch);
        category.addQuestion(question);
        // 23
        question = new QuestionNonDB("Lápis", 1, 0);
//        question.setImage(R.drawable.folstein_pencil);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);

        // Linguagem b
        category = new QuestionCategory("Linguagem b");
        category.setDescription("Repita a frase que eu vou dizer: O RATO ROEU A ROLHA");
        // 24
        question = new QuestionNonDB("Repetiu bem a frase", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);

        // Linguagem c
        category = new QuestionCategory("Linguagem c");
        category.setDescription("Quando eu lhe der esta folha de papel, pegue nela com a mão direita, dobre-a ao meio e ponha sobre a mesa”; dar\n" +
                "a folha segurando com as duas mãos.");
        // 25
        question = new QuestionNonDB("Pega com a mão direita", 0, 1);
        category.addQuestion(question);
        // 26
        question = new QuestionNonDB("Dobra ao meio", 0, 1);
        category.addQuestion(question);
        // 27
        question = new QuestionNonDB("Coloca onde deve", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);

        // Linguagem d
        category = new QuestionCategory("Linguagem d");
        category.setDescription("Leia o que está neste cartão e faça o que lá diz”. Mostrar um cartão com a frase bem legível, “FECHE OS OLHOS”;\n" +
                "sendo analfabeto lê-se a frase.");
        // 28
        question = new QuestionNonDB("Feche os olhos", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);


        // Linguagem e
        category = new QuestionCategory("Linguagem e");
        category.setDescription("“Escreva uma frase inteira aqui”. Deve ter sujeito e verbo e fazer sentido; os erros gramaticais não prejudicam a\n" +
                "pontuação.");
        // 29
        question = new QuestionNonDB("Escreveu uma frase inteira", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);

        // Capacidade construtiva
        category = new QuestionCategory("Capacidade construtiva");
        category.setDescription("Deve copiar um desenho. Dois pentágonos parcialmente sobrepostos; cada um deve ficar com 5 lados, dois dos quais\n" +
                "intersectados. Não valorizar tremor ou rotação");
        // 30
        question = new QuestionNonDB("Copiou bem o desenho", 0, 1);
//        question.setImage(R.drawable.folstein_pentagons);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);

        return mentalState;
    }


    public static GeriatricScaleNonDB setTest() {
        GeriatricScaleNonDB setTest = new GeriatricScaleNonDB(Constants.test_name_set_set,
                Constants.cga_mental, Constants.cga_cognitive,
                "• El Set-test fue introducido por Isaacs y Akhtar en\n" +
                        "1972  y propuesto\n" +
                        "como ayuda en el diagnóstico de la demencia en el\n" +
                        "anciano por Isaacs y Kennie en 1973. \n" +
                        "•  Explora la fluencia verbal, la denominación por categorías y la memoria\n" +
                        "semántica. Es una prueba breve y generalmente\n" +
                        "bien aceptada por los pacientes, con gran utilidad en\n" +
                        "pacientes analfabetos o con déficit sensoriales.\n" +
                        "• Se le pide al paciente que diga tantos nombres como\n" +
                        "pueda recordar de cada una de cuatro categorías (set):\n" +
                        "colores, animales, frutas y ciudades. Se obtiene 1 punto\n" +
                        "por cada ítem correcto, con un máximo de 10 ítems\n" +
                        "puntuables en cada set. " +
                        "•  El tiempo máximo de que dispone " +
                        "el paciente por categoría es de un minuto.\n" +
                        "•  Las" +
                        "repeticiones o los nombres que no correspondan a la\n" +
                        "categoría pedida no puntúan, aunque es interesante\n" +
                        "anotar todas las respuestas para el seguimiento evolutivo.\n" +
                        "• La puntuación oscila entre 0 y 40 puntos, considerando " +
                        "el resultado normal para adultos de 29 o más\n" +
                        "aciertos, y de 27 o más si se trata de ancianos.");
        // short area
        setTest.setShortName("Set Test");
        setTest.setIconName("ST");
        // create Scoringleve
        ScoringNonDB setTestScoring = new ScoringNonDB(0, 40, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Normal (ancianos)", 27, 40));
        gradings.add(new GradingNonDB("Anormal", 0, 26));
        // add Gradings to Scoring
        setTestScoring.setValuesBoth(gradings);
        // add Scoring to Test
        setTest.setScoring(setTestScoring);

        /**
         * Questions - text input as answer.
         */
        String[] questions = new String[]{
                "Cores",
                "Animais",
                "Frutas",
                "Cidades"
        };
        QuestionNonDB question;
        for (String questionText : questions) {
            question = new QuestionNonDB(questionText);
            question.setRightWrong(false);
            question.setNumerical(false);
            question.setMultipleTextInput(true);
            setTest.addQuestion(question);
        }
        /**
         * Errors / repetitions
         */
        question = new QuestionNonDB("Erros/repetições");
        question.setRightWrong(false);
        question.setNumerical(true);
        question.setNumericalMax(40);
        setTest.addQuestion(question);
        /**
         * Pontuação total
         */
        question = new QuestionNonDB("Pontuação total");
        question.setRightWrong(false);
        question.setNumerical(true);
        setTest.addQuestion(question);

        return setTest;
    }

    public static GeriatricScaleNonDB shortPortableMentalStatus() {
        GeriatricScaleNonDB shortPortableMentalStatus = new GeriatricScaleNonDB(Constants.test_name_short_portable_mental_status,
                Constants.cga_mental, Constants.cga_cognitive,
                "• Permite fazer uma avaliação sumária das funções cognitivas.\n" +
                        "•  Se trata de un test sencillo, breve y de aplicación" +
                        "rápida que explora orientación témporo-espacial," +
                        "memoria reciente y remota, información sobre hechos" +
                        "recientes, capacidad de concentración y de cálculo" +
                        "• Se acepta un error más en ancianos que no han\n" +
                        "recibido educación primaria y un error menos en aquellos\n" +
                        "que han realizado estudios superiores. Su principal\n" +
                        "problema es que no detecta pequeños cambios en\n" +
                        "la evolución.");
        // short area
        shortPortableMentalStatus.setShortName("SPMSQ");
        shortPortableMentalStatus.setIconName("SPMSQ");
        // create Scoringleve
        ScoringNonDB mentalStateScoring = new ScoringNonDB(0, 10, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Normal", 8, 10));
        gradings.add(new GradingNonDB("Deterioro mental leve ", 6, 7));
        gradings.add(new GradingNonDB("Deterioro mental moderado ", 3, 5));
        gradings.add(new GradingNonDB("Deterioro mental severo", 0, 2));
        // add Gradings to Scoring
        mentalStateScoring.setValuesBoth(gradings);
        // add Scoring to Test
        shortPortableMentalStatus.setScoring(mentalStateScoring);

        String[] questions = new String[]{
                "Qual a data de hoje? (dia, mês e ano)",
                "Qual o dia da semana?",
                "Qual é o nome deste lugar?",
                "Qual é o seu número de telefone? (caso não tenha " +
                        "telefone, perguntar qual é a sua morada)",
                "Que idade tem?",
                "Qual a sua data de nascimento?",
                "Como se chama o Presidente da República?",
                "Quem era o Presidente antes do atual?",
                "Qual o nome e apelidos da sua mãe?",
                "Em que ano nasceu?"
        };
        QuestionNonDB question;
        for (String questionText : questions) {
            question = new QuestionNonDB(questionText);
            shortPortableMentalStatus.addQuestion(question);
        }

        return shortPortableMentalStatus;
    }


    /**
     * Get access to a test definition by its date
     *
     * @param scaleName
     * @return
     */
    public static GeriatricScaleNonDB getScaleByName(String scaleName) {
        for (GeriatricScaleNonDB scale : scales) {
            if (scale.getScaleName().equals(scaleName))
                return scale;
        }
//        switch (scaleName) {
//            case Constants.test_name_testeDeKatz:
//                return escalaDeKatz();
//            case Constants.test_name_escalaDepressaoYesavage:
//                return escalaDepressaoYesavage();
//            case Constants.test_name_marchaHolden:
//                return marchaHolden();
//            case Constants.test_name_escalaLawtonBrody:
//                return escalaLawtonBrody();
//            case Constants.test_name_mini_mental_state:
//                return mentalStateFolstein();
//            case Constants.test_name_mini_nutritional_assessment_triagem:
//                return miniNutritionalAssessmentTriagem();
//            case Constants.test_name_mini_nutritional_assessment_global:
//                return miniNutritionalAssessmentGlobal();
//            case Constants.test_name_recursos_sociales:
//                return recursosSociales();
//            case Constants.test_name_valoracionSocioFamiliar:
//                return valoracionSocioFamiliarGijon();
//            case Constants.test_name_burden_interview:
//                return zaritBurdenInterview();
//            case Constants.test_name_barthel_index:
//                return barthelIndex();
//            case Constants.test_name_short_portable_mental_status:
//                return shortPortableMentalStatus();
//            case Constants.test_name_clock_drawing:
//                return clockDrawing();
//            case Constants.test_name_set_set:
//                return setTest();
//            case Constants.test_name_hamilton:
//                return hamiltonDepressionScale();
//            case Constants.test_name_tinetti:
//                return tinettiScale();
//            case Constants.set_name_advancedDailyLifeActivities:
//                return advancedDailyLifeActivities();
//        }
        return null;
    }

    /**
     * Get the short area for a given test.
     *
     * @param testName
     */
    public static String getShortName(String testName) {
        return getScaleByName(testName).getShortName();
    }

    /**
     * Get Scoring for a test.
     *
     * @param scale
     * @param gender
     * @return
     */
    public static GradingNonDB getGradingForScale(GeriatricScaleFirebase scale, int gender) {

        double testResult = FirebaseHelper.generateScaleResult(scale);
        ScoringNonDB scoring = getScaleByName(scale.getScaleName()).getScoring();
        GradingNonDB match;
        // check if it's different for men and women
        if (scoring == null)
            return null;
        if (scoring.isDifferentMenWomen()) {
            if (gender == Constants.MALE) {
                match = scoring.getGrading(testResult, Constants.MALE);
            } else {
                match = scoring.getGrading(testResult, Constants.FEMALE);
            }
        } else {
            match = scoring.getGrading(testResult, Constants.BOTH);
        }
        return match;
    }

    public static int getGradingIndex(GeriatricScaleFirebase scale, int gender) {


        double testResult = FirebaseHelper.generateScaleResult(scale);
        ScoringNonDB scoring = getScaleByName(scale.getScaleName()).getScoring();
        int match;
        // check if it's different for men and women
        if (scoring.isDifferentMenWomen()) {
            if (gender == Constants.MALE) {
                match = scoring.getGradingIndex(testResult, Constants.MALE);
            } else {
                match = scoring.getGradingIndex(testResult, Constants.FEMALE);
            }
        } else {
            match = scoring.getGradingIndex(testResult, Constants.BOTH);
        }

        return match;
    }

    public static GradingNonDB getGradingForTestWithoutGenerating(GeriatricScaleFirebase test, int gender) {
        double testResult = test.getResult();
        ScoringNonDB scoring = getScaleByName(test.getScaleName()).getScoring();
        GradingNonDB match;
        // question sets don't have scoring
        if (scoring == null)
            return null;
        // check if it's different for men and women
        if (scoring.isDifferentMenWomen()) {
            if (gender == Constants.MALE) {
                match = scoring.getGrading(testResult, Constants.MALE);
            } else {
                match = scoring.getGrading(testResult, Constants.FEMALE);
            }

        } else {
            match = scoring.getGrading(testResult, Constants.BOTH);
        }
        return match;
    }


    /**
     * Available scales - area set up dinamically at startup.
     */
    public static ArrayList<GeriatricScaleNonDB> scales = new ArrayList<>();


    /**
     * Get the scales that belong to a certain CGA area.
     *
     * @param area
     * @return
     */
    public static ArrayList<GeriatricScaleNonDB> getScalesForArea(String area) {
        ArrayList<GeriatricScaleNonDB> testsForArea = new ArrayList<>();
        for (GeriatricScaleNonDB test : scales) {
            if (test.getArea().equals(area)) {
                testsForArea.add(test);
            }
        }
        return testsForArea;
    }


}
