package com.example.rafael.appprototype.DataTypes;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.ScoringNonDB;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class holds  definition of all the tests that exist
 */
public class StaticTestDefinition {

    /**
     * Get infos about 'Escala de Katz'
     */
    public static GeriatricTestNonDB escalaDeKatz() {
        GeriatricTestNonDB testeDeKatz = new GeriatricTestNonDB(Constants.test_name_testeDeKatz, Constants.test_type_estadoFuncional, "Atividades de Vida Diária Básicas",
                "sample description");
        // create Scoring
        ScoringNonDB katzScoring = new ScoringNonDB(0, 6, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Dependência total", 0));
        gradings.add(new GradingNonDB("Dependência grave", new int[]{1, 2}));
        gradings.add(new GradingNonDB("Dependência moderada", new int[]{3, 4}));
        gradings.add(new GradingNonDB("Dependência ligeira", 5));
        gradings.add(new GradingNonDB("Independência total", 6));
        // add Gradings to Scoring
        katzScoring.setValues(gradings);
        // add Scoring to Test
        testeDeKatz.setScoring(katzScoring);
        // create Questions

        // BANHO
        QuestionNonDB banho = new QuestionNonDB("Banho", false);
        ArrayList<ChoiceNonDB> ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "necessita de ajuda apenas para lavar uma parte do corpo", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de ajuda para lavar mai que uma parte do corpo", 0));
        banho.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(banho);

        // VESTIR
        QuestionNonDB vestir = new QuestionNonDB("Vestir", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "escolhe a roupa adequada, veste-a e aperta-a; exclui atar os sapatos", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "precisa de ajuda para se vestir;não é capaz de se vestir", 0));
        vestir.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(vestir);

        // UTILIZAÇão da sanita
        QuestionNonDB useToilet = new QuestionNonDB("Utilização da sanita", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "não necessita de ajuda para entrar e sair do wc; usa a sanita, limpa-se e veste-se adequadamente; pode usar urinol pela noite", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "usa urinol ou arrastadeira ou necessita de ajuda para aceder e utilizar a sanita", 0));
        useToilet.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(useToilet);

        // transferencia
        QuestionNonDB transferencia = new QuestionNonDB("Transferência", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "não necessita de ajuda para sentar-se ou levantar-se de uma ca-\n" +
                "deira nem para entrar ou sair da cama; pode usar ajudas técnicas, p.ex. bengala", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de alguma ajuda para se deitar ou levantar da cama/\n" +
                "cadeira; está acamado", 0));
        transferencia.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(transferencia);

        // continencia
        QuestionNonDB continencia = new QuestionNonDB("Continência", false);
        ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "controlo completo da micção e defecação", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "incontinência total ou parcial vesical e/ou fecal; utilização de ene-\n" +
                "mas, algália, urinol ou arrastadeira", 0));
        continencia.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(continencia);

        // continencia
        QuestionNonDB food = new QuestionNonDB("Alimentação", false);
         ChoiceNonDBs = new ArrayList<>();
        ChoiceNonDBs.add(new ChoiceNonDB("Independente", "leva a comida do prato à boca sem ajuda; exclui cortar a carne", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Dependente", "necessita de ajuda para comer; não come em absoluto ou necessita\n" +
                "de nutrição entérica / parentérica", 0));
        food.setChoices(ChoiceNonDBs);
        testeDeKatz.addQuestion(food);

        return testeDeKatz;
    }

    /**
     * Get infos about 'Escala de Lawton & Brody'
     */
    public static GeriatricTestNonDB escalaLawtonBrody() {
        GeriatricTestNonDB escalaLawtonBrody = new GeriatricTestNonDB(Constants.test_name_escalaLawtonBrody,
                Constants.test_type_estadoFuncional, "Atividades Instrumentais de Vida Diária",
                "sample description");
        // create Scoring
        ScoringNonDB lawtonScoring = new ScoringNonDB(0, 8, false);
        lawtonScoring.setDifferentMenWomen(true);
        // create Gradings for men
        ArrayList<GradingNonDB> gradingsMen = new ArrayList<>();
        gradingsMen.add(new GradingNonDB("Dependência total", 0));
        gradingsMen.add(new GradingNonDB("Dependência grave", 1));
        gradingsMen.add(new GradingNonDB("Dependência moderada", new int[]{2, 3}));
        gradingsMen.add(new GradingNonDB("Dependência ligeira", 4));
        gradingsMen.add(new GradingNonDB("Independência total", 5));
        // add Gradings to Scoring
        lawtonScoring.setValues(gradingsMen);

        // create Gradings for women
        ArrayList<GradingNonDB> gradingsWomen = new ArrayList<>();
        gradingsWomen.add(new GradingNonDB("Dependência total", new int[]{0, 1}));
        gradingsWomen.add(new GradingNonDB("Dependência grave", new int[]{2, 3}));
        gradingsWomen.add(new GradingNonDB("Dependência moderada", new int[]{4, 5}));
        gradingsWomen.add(new GradingNonDB("Dependência ligeira", new int[]{6, 7}));
        gradingsWomen.add(new GradingNonDB("Independência total", 8));
        // add Gradings to Scoring
        lawtonScoring.setValues(gradingsWomen);
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
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza todas as compras necessárias \uF063\n" +
                "1 É capaz de apanhar um táxi, mas não usa\n" +
                "independentemente", 1));
        ChoiceNonDBs.add(new ChoiceNonDB("Realiza independentemente pequenas compras", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("Necessita de ir acompanhado para fazer qualquer compra", 0));
        ChoiceNonDBs.add(new ChoiceNonDB("É totalmente incapaz de comprar", 0));
        shopping.setChoices(ChoiceNonDBs);
        escalaLawtonBrody.addQuestion(shopping);

        // Preparar refeições
        QuestionNonDB prepareMeals = new QuestionNonDB("Preparação de refeições", false);
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
        GeriatricTestNonDB marcha = new GeriatricTestNonDB(Constants.test_name_marchaHolden, Constants.test_type_marcha, "",
                "sample description");
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
        marchaScoring.setValues(gradings);
        // add Scoring to Test
        marcha.setScoring(marchaScoring);

        return marcha;
    }


    /**
     * Get infos about 'Escala de Depressão Geriátrica de Yesavage – versão curta'
     */
    public static GeriatricTestNonDB escalaDepressao() {
        GeriatricTestNonDB escalaDepressao = new GeriatricTestNonDB(Constants.test_name_escalaDepressao,
                Constants.test_type_estadoAfetivo, "",
                "Escala utilizada para o rastreio da depressão, avaliando aspectos cognitivos e\n" +
                        "comportamentais tipicamente afectados na depressão do idoso.\n" +
                        "A informação é obtida através de questionário directo ao idoso. Pode ser aplica-\n" +
                        "da por médicos, psicólogos, enfermeiros ou outros profissionais de saúde.\n" +
                        "A escala de Yesavage tem uma versão completa, com 30 questões e uma versão\n" +
                        "curta com 15 questões. A versão curta está validada pelo autor e os seus resul-\n" +
                        "tados são sobreponíveis aos da versão completa, pelo que é a mais utilizada.\n" +
                        "É constituída por 15 questões com resposta dicotómica (Sim ou Não).As respos-\n" +
                        "tas sugestivas de existência de depressão correspondem a 1 ponto.");
        // create Scoring
        ScoringNonDB depressionScoring = new ScoringNonDB(0, 15, false);
        // create Gradings
        ArrayList<GradingNonDB> gradings = new ArrayList<>();
        gradings.add(new GradingNonDB("Sem depressão", new int[]{0, 1, 2, 3, 4, 5}));
        gradings.add(new GradingNonDB("Depressão ligeira", new int[]{6, 7, 8, 9, 10}));
        gradings.add(new GradingNonDB("Depressão grave", new int[]{11, 12, 13, 14, 15}));
        // add Gradings to Scoring
        depressionScoring.setValues(gradings);
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
     * Get access to a test definition by its date
     *
     * @param testName
     * @return
     */
    public static GeriatricTestNonDB getTestByName(String testName) {
        if (Objects.equals(testName, Constants.test_name_testeDeKatz)) {
            return escalaDeKatz();
        } else if (Objects.equals(testName, Constants.test_name_escalaDepressao)) {
            return escalaDepressao();
        } else if (Objects.equals(testName, Constants.test_name_marchaHolden)) {
            return marchaHolden();
        } else if (Objects.equals(testName, Constants.test_name_escalaLawtonBrody)) {
            return escalaLawtonBrody();
        }
        return null;
    }

}
