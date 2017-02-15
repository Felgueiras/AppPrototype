package com.example.rafael.appprototype.DataTypes;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.ScoringNonDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class holds  definition of all the tests that exist
 */
public class Scales {

    /**
     * Get infos about 'escala de resursos sociales de la OARS'
     */
    private static GeriatricTestNonDB recursosSociales() {
        GeriatricTestNonDB recursosSociales = new GeriatricTestNonDB(Constants.test_name_recursos_sociales,
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

    /**
     * Get infos about 'Escala de Katz'
     */
    public static GeriatricTestNonDB escalaDeKatz() {
        GeriatricTestNonDB testeDeKatz = new GeriatricTestNonDB(Constants.test_name_testeDeKatz,
                Constants.cga_functional, "Atividades de Vida Diária Básicas",
                "• Avalia a autonomia para realizar as atividades imprescindíveis à vida;\n" +
                        "• As ABVD são avaliadas na sequência habitual de deterioração/recuperação;\n" +
                        "• Procedimento: observação ou questionário direto ao idoso," +
                        " familiares ou cuidadores;\n" +
                        "• Tempo de aplicação: 5 minutos.");
        testeDeKatz.setShortName("Escala Katz");
        testeDeKatz.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB katzScoring = new ScoringNonDB(0, 6, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Dependência total", 0));
        gradings.add(new GradingNonDB("Dependência grave", 1, 2));
        gradings.add(new GradingNonDB("Dependência moderada", 3, 4));
        gradings.add(new GradingNonDB("Dependência ligeira", 5));
        gradings.add(new GradingNonDB("Independência total", 6));
        // add Gradings to Scoring
        katzScoring.setValuesBoth(gradings);
        // add Scoring to Test
        testeDeKatz.setScoring(katzScoring);
        // create Questions

        // BANHO
        QuestionNonDB banho = new QuestionNonDB("Banho", false);
        ArrayList<ChoiceNonDB> ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de ajuda para lavar mai que uma parte do corpo", 0));
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
    public static GeriatricTestNonDB zaritBurdenInterview() {
        GeriatricTestNonDB zaritBurdenInterview = new GeriatricTestNonDB(Constants.test_name_burden_interview,
                Constants.cga_social, "",
                "• Specially designed to reflect the stresses experienced by" +
                        "caregivers of dementia patients. It can be completed by caregivers themselves or as part of an\n" +
                        "interview. \n" +
                        "• Caregivers are asked to respond to a series of 22 questions about the impact of the" +
                        "patient’s disabilities on their life; \n" +
                        "• For each item, caregivers are to indicate how often they felt that" +
                        "way (never, rarely, sometimes, quite frequently, or nearly always).");
        zaritBurdenInterview.setShortName("Burden Interview");
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
    public static GeriatricTestNonDB barthelIndex() {
        GeriatricTestNonDB barthelIndex = new GeriatricTestNonDB(Constants.test_name_barthel_index,
                Constants.cga_functional, "",
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
        barthelIndex.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB scoring = new ScoringNonDB(0, 100, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Dependencia total", 0, 19));
        gradings.add(new GradingNonDB("Dependencia grave", 20, 35));
        gradings.add(new GradingNonDB("Dependencia moderada", 40, 55));
        gradings.add(new GradingNonDB("Dependencia leve", 60, 100));
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
        choices.add(new ChoiceNonDB("2", "needs some help, but can do something alone", 5));
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
    public static GeriatricTestNonDB valoracionSocioFamiliarGijon() {
        GeriatricTestNonDB valoracionSocioFamiliar = new GeriatricTestNonDB(Constants.test_name_valoracionSocioFamiliar,
                Constants.cga_social, "",
                "Creada a finales de los años noventa, se emplea\n" +
                        "para valorar la situación social y familiar de las personas\n" +
                        "mayores que viven en domicilio. Su objetivo\n" +
                        "es detectar situaciones de riesgo y problemas sociales\n" +
                        "para la puesta en marcha de intervenciones\n" +
                        "sociales. Evalúa cinco áreas de riesgo social: situación familiar, vivienda, relaciones y contactos sociales,\n" +
                        "apoyos de la red social y situación económica. La\n" +
                        "puntuación oscila entre 0 y 20, indicando mayor puntuación\n" +
                        "peor situación social." +
                        "¿Qué preguntas deberíamos hacer como geriatras\n" +
                        "a un paciente para conocer su situación social?:\n" +
                        "— ¿Soltero, casado o viudo?\n" +
                        "— ¿Tiene hijos?; en caso afirmativo, ¿cuántos?,\n" +
                        "¿viven en la misma ciudad?\n" +
                        "— ¿Con quién vive?\n" +
                        "— ¿Tiene contactos con familiares, amigos o vecinos?\n" +
                        "¿Con qué frecuencia?\n" +
                        "— ¿Cómo es el domicilio donde vive?\n" +
                        "— ¿Tiene ascensor el edificio donde vive?\n" +
                        "— ¿Precisa algún tipo de ayuda para su autocuidado?\n" +
                        "— ¿Quién es la principal persona que le ayuda o le\n" +
                        "cuida?, ¿tiene esa persona algún problema de\n" +
                        "salud?\n" +
                        "— ¿Recibe algún tipo de ayuda formal?");
        valoracionSocioFamiliar.setShortName("Valoracion SocioFamiliar");
        valoracionSocioFamiliar.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB valoracionScoring = new ScoringNonDB(3, 15, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Situación social buena", 3, 7));
        gradings.add(new GradingNonDB("Situación intermedia", 8, 9));
        gradings.add(new GradingNonDB("Deterioro social severo", 10, 15));
        // add Gradings to Scoring
        valoracionScoring.setValuesBoth(gradings);
        // add Scoring to Test
        valoracionSocioFamiliar.setScoring(valoracionScoring);
        // create Questions

        // Situación familiar
        QuestionNonDB situaciónFamiliar = new QuestionNonDB("Situación familiar", false);
        ArrayList<ChoiceNonDB> choices = new ArrayList<>();
        choices.add(new ChoiceNonDB("1", "Vive con pareja y/o familia sin conflicto", 1));
        choices.add(new ChoiceNonDB("2", "Vive con pareja de similar edad", 2));
        choices.add(new ChoiceNonDB("3", "Vive con pareja y/o familia y/o otros, pero no pueden" +
                " o no quieren atenderlo", 3));
        choices.add(new ChoiceNonDB("4", "Vive solo, hijos y/o familiares próximos que no cubren todas las " +
                "necesidades", 4));
        choices.add(new ChoiceNonDB("5", "Vive solo, familia lejana, desatendido, sin familia", 5));
        situaciónFamiliar.setChoices(choices);
        valoracionSocioFamiliar.addQuestion(situaciónFamiliar);

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
    public static GeriatricTestNonDB escalaLawtonBrody() {
        GeriatricTestNonDB escalaLawtonBrody = new GeriatricTestNonDB(Constants.test_name_escalaLawtonBrody,
                Constants.cga_functional, "Atividades Instrumentais de Vida Diária",
                "• Avalia a autonomia para realizar as atividades necessárias para viver\n" +
                        "de forma independente na comunidade\n" +
                        "• Cada AIVD tem vários níveis de dependência (3 a 5);\n" +
                        "• Procedimento: observação ou questionário directo ao idoso,\n" +
                        "familiares ou cuidadores;\n" +
                        "•  Tempo de aplicação: 5 minutos.");
        escalaLawtonBrody.setShortName("Escala Lawton Brody");
        escalaLawtonBrody.setMultipleChoice(true);
        // create Scoring
        ScoringNonDB lawtonScoring = new ScoringNonDB(0, 8, true);
        lawtonScoring.setScoringMen(0, 5);
        // create Gradings for men
        ArrayList<GradingNonDB> gradingsMen = new ArrayList<>();
        gradingsMen.add(new GradingNonDB("Dependência total", 0));
        gradingsMen.add(new GradingNonDB("Dependência grave", 1));
        gradingsMen.add(new GradingNonDB("Dependência moderada", 2, 3));
        gradingsMen.add(new GradingNonDB("Dependência ligeira", 4));
        gradingsMen.add(new GradingNonDB("Independência total", 5));
        // add Gradings to Scoring
        lawtonScoring.setValuesMen(gradingsMen);

        // create Gradings for women
        ArrayList<GradingNonDB> gradingsWomen = new ArrayList<>();
        gradingsWomen.add(new GradingNonDB("Dependência total", 0, 1));
        gradingsWomen.add(new GradingNonDB("Dependência grave", 2, 3));
        gradingsWomen.add(new GradingNonDB("Dependência moderada", 4, 5));
        gradingsWomen.add(new GradingNonDB("Dependência ligeira", 6, 7));
        gradingsWomen.add(new GradingNonDB("Independência total", 8));
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


    /**
     * Get infos about 'Marcha'
     */
    public static GeriatricTestNonDB marchaHolden() {
        GeriatricTestNonDB marcha = new GeriatricTestNonDB(Constants.test_name_marchaHolden,
                Constants.cga_functional, "",
                "• Avalia a autonomia na marcha de acordo com o tipo de ajuda física" +
                        "ou supervisão necessárias, em função do tipo de superfície (plana," +
                        "inclinada, escadas).\n" +
                        "• São estabelecidas 6 categorias, tentando-se classificar o idoso na " +
                        "categoria que mais se aproxima da sua capacidade para a marcha\n" +
                        "• Procedimento: observação ou questionário direto ao idoso,\n" +
                        "familiares ou cuidadores;\n" +
                        "• Tempo de aplicação: 3-5 minutos.");
        marcha.setShortName("Marcha");
        // create Scoring
        ScoringNonDB marchaScoring = new ScoringNonDB(0, 5, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Marcha ineficaz", 0, "O idoso não é capaz de caminhar, caminha apenas em\n" +
                "barras paralelas ou requer ajuda física ou supervisã"));
        gradings.add(new GradingNonDB("Marcha dependente - Nível I", 1, "O idoso necessita de grande ajuda de uma pessoa para\n" +
                "andar e evitar quedas. Esta ajuda é constante, sendo\n" +
                "necessária para suportar o peso do corpo ou para manter\n" +
                "o equilíbrio ou a coordenação"));
        gradings.add(new GradingNonDB("Marcha dependente - Nível II", 2, "O idoso requer ajuda mínima de uma pessoa para não\n" +
                "cair na test_type_marcha em superfície plana. A ajuda consiste em\n" +
                "toques suaves, contínuos ou intermitentes, para ajudar a\n" +
                "manter o equilíbrio e a coordenação"));
        gradings.add(new GradingNonDB("Marcha dependente com supervisão", 3, "O idoso é capaz de andar de forma independente em\n" +
                "superfícies planas sem ajuda, mas para a sua segurança\n" +
                "requer supervisão de uma pessoa."));
        gradings.add(new GradingNonDB("Marcha independente (superfície plana)", 4, "O idoso é capaz de andar de forma independente em\n" +
                "superfícies planas, mas requer supervisão ou ajuda física\n" +
                "para superar escadas, superfícies inclinadas ou terrenos\n" +
                "não planos"));
        gradings.add(new GradingNonDB("Marcha independente", 5, "O idoso é capaz de andar independentemente em superfícies planas, inclinadas ou escadas"));
        // add Gradings to Scoring
        marchaScoring.setValuesBoth(gradings);
        // add Scoring to Test
        marcha.setScoring(marchaScoring);
        marcha.setSingleQuestion(true);

        return marcha;
    }


    /**
     * Get infos about 'Escala de Depressão Geriátrica de Yesavage – versão curta'
     */
    public static GeriatricTestNonDB escalaDepressaoYesavage() {
        GeriatricTestNonDB escalaDepressao = new GeriatricTestNonDB(Constants.test_name_escalaDepressaoYesavage,
                Constants.cga_afetivo, "",
                "• Utilizada para o rastreio da depressão, avaliando aspectos cognitivos e " +
                        "comportamentais tipicamente afectados na depressão do idoso.\n" +
                        "• A informação é obtida através de questionário directo ao idoso.\n" +
                        "• A escala de Yesavage tem uma versão completa, com 30 questões e uma versão" +
                        "curta com 15 questões. A versão curta está validada pelo autor e os seus resultados" +
                        " são sobreponíveis aos da versão completa, pelo que é a mais utilizada.\n" +
                        "• 15 questões com resposta dicotómica (Sim ou Não).\n" +
                        "• As respostas sugestivas de existência de depressão correspondem a 1 ponto.\n" +
                        "• Tempo de aplicação: 6 minutos.");
        // short area
        escalaDepressao.setShortName("Escala Depressão Yesavage (curta)");
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
        escalaDepressao.addQuestion(question);
        // 2
        question = new QuestionNonDB("Abandonou muitos dos seus interesses e actividades?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 3
        question = new QuestionNonDB("Sente que a sua vida está vazia?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 4
        question = new QuestionNonDB("Sente-se frequentemente aborrecido?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 5
        question = new QuestionNonDB("Na maior parte do tempo está de bom humor?", 0, 1);
        escalaDepressao.addQuestion(question);
        // 6
        question = new QuestionNonDB("Tem medo de que algo de mal lhe aconteça?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 7
        question = new QuestionNonDB("Sente-se feliz na maior parte do tempo?", 0, 1);
        escalaDepressao.addQuestion(question);
        // 8
        question = new QuestionNonDB("Sente-se frequentemente abandonado / desamparado?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 9
        question = new QuestionNonDB("Prefere ficar em casa, a sair e fazer coisas novas?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 10
        question = new QuestionNonDB("Sente que tem mais problemas de memória do que os outros da sua\n" +
                "idade?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 11
        question = new QuestionNonDB("Sente que tem mais problemas de memória do que os outros da sua\n" +
                "idade?", 0, 1);
        escalaDepressao.addQuestion(question);
        // 12
        question = new QuestionNonDB("Sente-se inútil?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 13
        question = new QuestionNonDB("Sente-se cheio de energia?", 0, 1);
        escalaDepressao.addQuestion(question);
        // 14
        question = new QuestionNonDB("Sente-se sem esperança?", 1, 0);
        escalaDepressao.addQuestion(question);
        // 15
        question = new QuestionNonDB("Acha que as outras pessoas estão melhores que o Sr./Sra.?", 1, 0);
        escalaDepressao.addQuestion(question);

        return escalaDepressao;
    }


    /**
     * Mini nutritional assessment evaluation.
     *
     * @return
     */
    public static GeriatricTestNonDB miniNutritionalAssessment() {
        GeriatricTestNonDB nutritionalAssessment = new GeriatricTestNonDB(Constants.test_name_mini_nutritional_assessment,
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
        nutritionalAssessment.setShortName("Nutritional assessment");
        // create Scoring
        ScoringNonDB mentalStateScoring = new ScoringNonDB(0, 30, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Desnutrido", 0, 16));
        gradings.add(new GradingNonDB("Sob risco de desnutrição",
                17, 23));
        gradings.add(new GradingNonDB("Estado nutricional normal",
                24, 30));
        // add Gradings to Scoring
        mentalStateScoring.setValuesBoth(gradings);
        // add Scoring to Test
        nutritionalAssessment.setScoring(mentalStateScoring);

        // Questions + Choices
        QuestionNonDB question = new QuestionNonDB("A - Nos últimos três meses houve diminuição da ingesta\n" +
                "alimentar devido a perda de apetite, problemas digestivos\n" +
                "ou dificuldade para mastigar ou deglutir?", false);
        ArrayList<ChoiceNonDB> choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("diminuição grave da ingesta", 0));
        choiceNonDBs.add(new ChoiceNonDB("diminuição moderada da ingesta", 1));
        choiceNonDBs.add(new ChoiceNonDB("sem diminuição da ingesta", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("B - Perda de peso nos últimos 3 meses", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("superior a três quilos", 0));
        choiceNonDBs.add(new ChoiceNonDB("não sabe informar", 1));
        choiceNonDBs.add(new ChoiceNonDB("entre um e três quilos", 2));
        choiceNonDBs.add(new ChoiceNonDB("sem perda de peso", 3));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("C - Mobilidade", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("restrito ao leito ou à cadeira de rodas", 0));
        choiceNonDBs.add(new ChoiceNonDB("deambula mas não é capaz de sair de casa", 1));
        choiceNonDBs.add(new ChoiceNonDB("normal", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("D - Passou por algum stress psicológico ou doença aguda nos\n" +
                "últimos três meses?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("sim", 0));
        choiceNonDBs.add(new ChoiceNonDB("não", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("E - Problemas neuropsicológicos", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("demência ou depressão graves", 0));
        choiceNonDBs.add(new ChoiceNonDB("demência ligeira", 1));
        choiceNonDBs.add(new ChoiceNonDB("sem problemas psicológicos", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("F - Índice de Massa Corporal (IMC = peso[kg] / estatura [m 2 ])", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("IMC < 19", 0));
        choiceNonDBs.add(new ChoiceNonDB("19 < IMC < 21", 1));
        choiceNonDBs.add(new ChoiceNonDB("21 < IMC < 23", 2));
        choiceNonDBs.add(new ChoiceNonDB("IMC > 23", 3));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        /**
         * Avaliação Global.
         */
        question = new QuestionNonDB("G - O doente vive na sua própria casa\n" +
                "(não em instituição geriátrica ou hospital)", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("sim", 1));
        choiceNonDBs.add(new ChoiceNonDB("não", 0));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("H - Utiliza mais de três medicamentos diferentes por dia?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("sim", 0));
        choiceNonDBs.add(new ChoiceNonDB("não", 1));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("I - Lesões de pele ou escaras?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("sim", 0));
        choiceNonDBs.add(new ChoiceNonDB("não", 1));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("J - Quantas refeições faz por dia?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("uma refeição", 0));
        choiceNonDBs.add(new ChoiceNonDB("duas refeições", 1));
        choiceNonDBs.add(new ChoiceNonDB("três refeições", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);


        // TODO question k

        question = new QuestionNonDB("L - O doente consome duas ou mais porções diárias de fruta\n" +
                "ou produtos hortícolas?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("não", 0));
        choiceNonDBs.add(new ChoiceNonDB("sim", 1));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("M - Quantos copos de líquidos (água, sumo, café, chá, leite) o\n" +
                "doente consome por dia?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("menos de três copos", 0));
        choiceNonDBs.add(new ChoiceNonDB("três a cinco copos", 0.5));
        choiceNonDBs.add(new ChoiceNonDB("mais de cinco copos", 1));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("N - Modo de se alimentar", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("não é capaz de se alimentar sozinho", 0));
        choiceNonDBs.add(new ChoiceNonDB("alimenta-se sozinho, porém com dificuldade", 1));
        choiceNonDBs.add(new ChoiceNonDB("alimenta-se sozinho sem dificuldade", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("O - O doente acredita ter algum problema nutricional?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("acredita estar desnutrido", 0));
        choiceNonDBs.add(new ChoiceNonDB("não sabe dizer", 1));
        choiceNonDBs.add(new ChoiceNonDB("acredita não ter um problema nutricional", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("P - Em comparação com outras pessoas da mesma idade,\n" +
                "como considera o doente a sua própria saúde?", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("pior", 0));
        choiceNonDBs.add(new ChoiceNonDB("não sabe", 0.5));
        choiceNonDBs.add(new ChoiceNonDB("igual", 1));
        choiceNonDBs.add(new ChoiceNonDB("melhor", 2));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("Q - Perímetro braquial (PB) em cm", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("PB < 21", 0));
        choiceNonDBs.add(new ChoiceNonDB("21 < PB < 22", 0.5));
        choiceNonDBs.add(new ChoiceNonDB("PB > 22", 1));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);

        question = new QuestionNonDB("R - Perímetro da perna (PP) em cm", false);
        choiceNonDBs = new ArrayList<>();
        choiceNonDBs.add(new ChoiceNonDB("PP < 31", 0));
        choiceNonDBs.add(new ChoiceNonDB("PP > 31", 1));
        question.setChoices(choiceNonDBs);
        nutritionalAssessment.addQuestion(question);


        return nutritionalAssessment;
    }

    /**
     * Get infos about 'Mini mental state examination (Folstein)'
     */
    public static GeriatricTestNonDB mentalStateFolstein() {
        GeriatricTestNonDB mentalState = new GeriatricTestNonDB(Constants.test_name_mini_mental_state,
                Constants.cga_cognitivo, "",
                "• Permite fazer uma avaliação sumária das funções cognitivas.\n" +
                        "• Avalia a orientação, memória imediata " +
                        "e recente, capacidade de atenção e cálculo, linguagem e capacidade construtiva.\n" +
                        "• Procedimento: questionário directo ao idoso.\n" +
                        "• A interpretação da pontuação final depende do nível educacional do idoso.\n" +
                        "• Tempo de aplicação: 5-10 minutos.");
        // short area
        mentalState.setShortName("Mini-Mental State Examination");
        // create Scoring
        ScoringNonDB mentalStateScoring = new ScoringNonDB(0, 30, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Analfabetos", 0, 15));
        gradings.add(new GradingNonDB("1 a 11 anos de escolaridade", 16, 22));
        gradings.add(new GradingNonDB("Escolaridade superior a 11 anos", 23, 30));
        // add Gradings to Scoring
        mentalStateScoring.setValuesBoth(gradings);
        // add Scoring to Test
        mentalState.setScoring(mentalStateScoring);

        // Orientação
        QuestionCategory category = new QuestionCategory("Orientação");
        category.setDescription("");
        // 1
        QuestionNonDB question = new QuestionNonDB("Em que ano estamos?");
        category.addQuestion(question);

        // 2
        question = new QuestionNonDB("Em que mês estamos?");
        category.addQuestion(question);

        // 3
        question = new QuestionNonDB("Em que dia do mês estamos?");
        category.addQuestion(question);

        // 4
        question = new QuestionNonDB("Em que dia da semana estamos?");
        category.addQuestion(question);

        // 5
        question = new QuestionNonDB("Em que estação do ano estamos?");
        category.addQuestion(question);

        // 6
        question = new QuestionNonDB("Em que país estamos?");
        category.addQuestion(question);

        // 7
        question = new QuestionNonDB("Em que distrito vive");
        category.addQuestion(question);

        // 8
        question = new QuestionNonDB("Em que terra vive?");
        category.addQuestion(question);

        // 9
        question = new QuestionNonDB("Em que casa estamos?");
        category.addQuestion(question);

        // 10
        question = new QuestionNonDB("Em que andar estamos?");
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);


        // Retenção
        category = new QuestionCategory("Retenção");
        category.setDescription("“Vou dizer três palavras; queria que as repetisse, mas só depois de eu as dizer todas; procure ficar a sabê-las de cor”.");
        // 11
        question = new QuestionNonDB("Pêra");
        category.addQuestion(question);
        // 12
        question = new QuestionNonDB("Gato");
        category.addQuestion(question);
        // 13
        question = new QuestionNonDB("Bola");
        category.addQuestion(question);
        // add category to the test
        mentalState.addQuestionCategory(category);


        // Atenção e cálculo
        category = new QuestionCategory("Atenção e cálculo");
        category.setDescription("“Agora peço-lhe que me diga quantos são 30 menos 3 e depois ao número encontrado volta a tirar 3 e repete assim\n" +
                "até eu lhe dizer para parar”");
        // 14
        question = new QuestionNonDB("27", 1, 0);
        category.addQuestion(question);
        // 15
        question = new QuestionNonDB("24", 1, 0);
        category.addQuestion(question);
        // 16
        question = new QuestionNonDB("21", 1, 0);
        category.addQuestion(question);
        // 17
        question = new QuestionNonDB("18", 1, 0);
        category.addQuestion(question);
        // 18
        question = new QuestionNonDB("15", 1, 0);
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


        // Linguagem
        category = new QuestionCategory("Linguagem");
        category.setDescription("");
        // 21
        question = new QuestionNonDB("Relógio", 0, 1);
        category.addQuestion(question);
        // 23
        question = new QuestionNonDB("Lápis", 1, 0);
        category.addQuestion(question);
        // 24
        question = new QuestionNonDB("Repita a frase que eu vou dizer: O RATO ROEU A ROLHA", 0, 1);
        category.addQuestion(question);
        // 25
        question = new QuestionNonDB("Pega com a mão direita", 0, 1);
        category.addQuestion(question);
        // 26
        question = new QuestionNonDB("Dobra ao meio", 0, 1);
        category.addQuestion(question);
        // 27
        question = new QuestionNonDB("Coloca onde deve", 0, 1);
        category.addQuestion(question);
        // 28
        question = new QuestionNonDB("Feche os olhos", 0, 1);
        category.addQuestion(question);
        // 29
        question = new QuestionNonDB("“Escreva uma frase inteira aqui”", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);

        // Capacidade construtiva
        category = new QuestionCategory("Capacidade construtiva");
        // 30
        question = new QuestionNonDB("Deve copiar um desenho. Dois pentágonos parcialmente sobrepostos; cada um deve ficar com 5 lados, dois dos quais\n" +
                "intersectados. Não valorizar tremor ou rotação", 0, 1);
        category.addQuestion(question);
        mentalState.addQuestionCategory(category);
        mentalState.setMultipleCategories(true);

        return mentalState;
    }


    public static GeriatricTestNonDB shortPortableMentalStatus() {
        GeriatricTestNonDB shortPortableMentalStatus = new GeriatricTestNonDB(Constants.test_name_short_portable_mental_status,
                Constants.cga_cognitivo, "",
                "• Permite fazer uma avaliação sumária das funções cognitivas.\n" +
                        "Se trata de un test sencillo, breve y de aplicación\n" +
                        "rápida que explora orientación témporo-espacial,\n" +
                        "memoria reciente y remota, información sobre hechos\n" +
                        "recientes, capacidad de concentración y de cálculo\n" +
                        "(J Am Geriatr. Soc. 1975; 23: 433-441). Presenta una\n" +
                        "sensibilidad del 68%, especificidad del 96%, valor predictivo\n" +
                        "positivo del 92%, valor predictivo negativo del\n" +
                        "82%. Se puntúan los errores. En función de la puntuación\n" +
                        "obtenemos (6, 9):\n" +
                        "— De 0 a 2 errores: no deterioro.\n" +
                        "— De 3 a 4 errores: deterioro leve de la capacidad\n" +
                        "intelectual.\n" +
                        "— De 5 a 7 errores: moderado deterioro.\n" +
                        "— De 8 a 10 errores: grave deterioro." +
                        "Se acepta un error más en ancianos que no han\n" +
                        "recibido educación primaria y un error menos en aquellos\n" +
                        "que han realizado estudios superiores. Su principal\n" +
                        "problema es que no detecta pequeños cambios en\n" +
                        "la evolución.");
        // short area
        shortPortableMentalStatus.setShortName("SPMSQ");
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
     * @param testName
     * @return
     */
    public static GeriatricTestNonDB getTestByName(String testName) {
        if (Objects.equals(testName, Constants.test_name_testeDeKatz)) {
            return escalaDeKatz();
        } else if (Objects.equals(testName, Constants.test_name_escalaDepressaoYesavage)) {
            return escalaDepressaoYesavage();
        } else if (Objects.equals(testName, Constants.test_name_marchaHolden)) {
            return marchaHolden();
        } else if (Objects.equals(testName, Constants.test_name_escalaLawtonBrody)) {
            return escalaLawtonBrody();
        } else if (Objects.equals(testName, Constants.test_name_mini_mental_state)) {
            return mentalStateFolstein();
        } else if (Objects.equals(testName, Constants.test_name_mini_nutritional_assessment)) {
            return miniNutritionalAssessment();
        } else if (Objects.equals(testName, Constants.test_name_recursos_sociales)) {
            return recursosSociales();
        } else if (Objects.equals(testName, Constants.test_name_valoracionSocioFamiliar)) {
            return valoracionSocioFamiliarGijon();
        } else if (Objects.equals(testName, Constants.test_name_burden_interview)) {
            return zaritBurdenInterview();
        } else if (Objects.equals(testName, Constants.test_name_barthel_index)) {
            return barthelIndex();
        } else if (Objects.equals(testName, Constants.test_name_short_portable_mental_status)) {
            return shortPortableMentalStatus();
        }
        return null;
    }

    /**
     * Get the short area for a given test.
     *
     * @param testName
     */
    public static String getShortName(String testName) {
        return getTestByName(testName).getShortName();
    }

    /**
     * Get Scoring for a test.
     *
     * @param test
     * @param gender
     * @return
     */
    public static GradingNonDB getGradingForTest(GeriatricTest test, int gender) {


        double testResult = test.generateTestResult();
        System.out.println("Test result is " + testResult);
        ScoringNonDB scoring = getTestByName(test.getTestName()).getScoring();
        System.out.println("Scoring is " + scoring.getMinScore());
        GradingNonDB match;
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

    public static GradingNonDB getGradingForTestWithoutGenerating(GeriatricTest test, int gender) {
        double testResult = test.getResult();
        ScoringNonDB scoring = getTestByName(test.getTestName()).getScoring();
        GradingNonDB match;
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
     * Get all tests definitions.
     *
     * @return
     */
    public static ArrayList<GeriatricTestNonDB> getAllTests() {
        ArrayList<GeriatricTestNonDB> tests = new ArrayList<>();
        tests.add(escalaDeKatz());
        tests.add(escalaDepressaoYesavage());
        tests.add(escalaLawtonBrody());
        tests.add(marchaHolden());
        tests.add(mentalStateFolstein());
        tests.add(miniNutritionalAssessment());
        tests.add(recursosSociales());
        tests.add(valoracionSocioFamiliarGijon());
        tests.add(zaritBurdenInterview());
        tests.add(barthelIndex());
        tests.add(shortPortableMentalStatus());
        return tests;
    }

    /**
     * Get the scales that belong to a certain CGA area.
     *
     * @param area
     * @return
     */
    public static ArrayList<GeriatricTestNonDB> getTestsForArea(String area) {
        ArrayList<GeriatricTestNonDB> testsForArea = new ArrayList<>();
        for (GeriatricTestNonDB test : getAllTests()) {
            if (test.getArea().equals(area)) {
                testsForArea.add(test);
            }
        }
        return testsForArea;
    }

    public static ArrayList<GeriatricTest> getTestsForArea(List<GeriatricTest> scales, String area) {
        ArrayList<GeriatricTest> testsForArea = new ArrayList<>();
        for (GeriatricTest test : scales) {
            if (Scales.getTestByName(test.getTestName()).getArea().equals(area)) {
                testsForArea.add(test);
            }
        }
        return testsForArea;
    }


}
