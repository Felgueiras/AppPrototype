package com.felgueiras.apps.geriatrichelper.Main;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.felgueiras.apps.geriatrichelper.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ScalesTests {

    @Rule
    public ActivityTestRule<LaunchScreen> mActivityTestRule = new ActivityTestRule<>(LaunchScreen.class);

    @Test
    public void marchTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation)));
        appCompatButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction radioButton = onView(
                allOf(withText("O idoso requer ajuda mínima de uma pessoa para não cair em superfície plana. A ajuda consiste em toques suaves, contínuos ou intermitentes, para ajudar a manter o equilíbrio e a coordenação"),
                        withParent(allOf(withId(R.id.radioGroup),
                                withParent(withClassName(is("android.widget.RelativeLayout"))))),
                        isDisplayed()));
        radioButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.saveButton),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.session_finish),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative), withText("2.0 (0-5)"),
                        isDisplayed()));
        textView.check(matches(withText("2.0 (0-5)")));

    }

    @Test
    public void katzTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation), isDisplayed()));
        appCompatButton.perform(click());


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));


        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.nameQuestion), withText("1 - Banho"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                0)),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Independente - necessita de ajuda apenas para lavar uma parte do corpo"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.nameQuestion), withText("2 - Vestir"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                1)),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("Dependente - precisa de ajuda para se vestir; não é capaz de se vestir"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.nameQuestion), withText("3 - Utilização da sanita"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                2)),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Dependente - usa urinol ou arrastadeira ou necessita de ajuda para aceder e utilizar a sanita"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.nameQuestion), withText("4 - Transferência"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                3)),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("Independente - não necessita de ajuda para sentar-se ou levantar-se de uma ca-\ndeira nem para entrar ou sair da cama; pode usar ajudas técnicas, p.ex. bengala"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.nameQuestion), withText("5 - Continência"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                4)),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatCheckedTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("Independente - controlo completo da micção e defecação"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.nameQuestion), withText("6 - Alimentação"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                5)),
                        isDisplayed()));
        appCompatTextView6.perform(click());

        ViewInteraction appCompatCheckedTextView6 = onView(
                allOf(withId(android.R.id.text1), withText("Dependente - necessita de ajuda para comer; não come em absoluto ou necessita\nde nutrição entérica / parentérica"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView6.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.saveButton),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.session_finish), withText("Terminar sessão"),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative), withText("3.0 (0-6)"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                3),
                        isDisplayed()));
        textView.check(matches(withText("3.0 (0-6)")));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.close_session), isDisplayed()));
        floatingActionButton.perform(click());

    }

    @Test
    public void lawtonBrodyMale() {


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));


        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("M"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.nameQuestion), withText("1 - Utilização do telefone"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                0)),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("É capaz de marcar bem alguns números familiares"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.nameQuestion), withText("2 - Fazer compras"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                1)),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Necessita de ir acompanhado para fazer qualquer compra"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.nameQuestion), withText("6 - Utilização de meios de transporte"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                5)),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("É capaz de apanhar um taxi, mas não usa outro transporte"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.nameQuestion), withText("7 - Manejo da medicação"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                6)),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatCheckedTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("Toma a medicação se a dose é preparada previamente"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.nameQuestion), withText("8 - Responsabilidade de assuntos financeiros"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                7)),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatCheckedTextView6 = onView(
                allOf(withId(android.R.id.text1), withText("Encarrega-se de assuntos financeiros sozinho"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView6.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.saveButton),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.session_finish),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative), withText("3.0 (0-5)"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                3),
                        isDisplayed()));
        textView.check(matches(withText("3.0 (0-5)")));

    }

    @Test
    public void lawtonBrodyFemale() {


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation), withText("Criar Sessão"), isDisplayed()));
        appCompatButton.perform(click());



        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("F"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.nameQuestion), withText("1 - Utilização do telefone"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                0)),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("É capaz de pedir para telefonar, mas não é capaz de marcar"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.nameQuestion), withText("2 - Fazer compras"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                1)),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Realiza todas as compras necessárias"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.nameQuestion), withText("3 - Preparação de refeições"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                2)),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("Prepara adequadamente as refeições se se fornecem os alimentos"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.nameQuestion), withText("4 - Tarefas domésticas"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                3)),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatCheckedTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("Não participa em nenhuma tarefa doméstica"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                4),
                        isDisplayed()));
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.nameQuestion), withText("5 - Lavagem da roupa"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                4)),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatCheckedTextView6 = onView(
                allOf(withId(android.R.id.text1), withText("Lava sozinho pequenas peças de roupa"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView6.perform(click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.nameQuestion), withText("6 - Utilização de meios de transporte"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                5)),
                        isDisplayed()));
        appCompatTextView6.perform(click());

        ViewInteraction appCompatCheckedTextView7 = onView(
                allOf(withId(android.R.id.text1), withText("Viaja sozinho em transporte público ou conduz o seu próprio carro"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView7.perform(click());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(R.id.nameQuestion), withText("7 - Manejo da medicação"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                6)),
                        isDisplayed()));
        appCompatTextView7.perform(click());

        ViewInteraction appCompatCheckedTextView8 = onView(
                allOf(withId(android.R.id.text1), withText("Toma a medicação se a dose é preparada previamente"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView8.perform(click());

        ViewInteraction appCompatTextView8 = onView(
                allOf(withId(R.id.nameQuestion), withText("8 - Responsabilidade de assuntos financeiros"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                7)),
                        isDisplayed()));
        appCompatTextView8.perform(click());

        ViewInteraction appCompatCheckedTextView9 = onView(
                allOf(withId(android.R.id.text1), withText("Encarrega-se de assuntos financeiros sozinho"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView9.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.saveButton), withText("Guardar"),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.session_finish), withText("Terminar sessão"),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative),
                        isDisplayed()));
        textView.check(matches(withText("5.0 (0-8)")));

    }


    @Test
    public void yesavageTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));


        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        // 0
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(0).
                onChildView(withId(R.id.rightChoice)).perform(click());


        // 1
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(1).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 2
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(2).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 3
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(3).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 4
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(4).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 5
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(5).
                onChildView(withId(R.id.rightChoice)).perform(click());

        // 6
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(6).
                onChildView(withId(R.id.rightChoice)).perform(click());


        // 7
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(7).
                onChildView(withId(R.id.rightChoice)).perform(click());

        // 8
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(8).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 9
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(9).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 10
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(10).
                onChildView(withId(R.id.rightChoice)).perform(click());

        // 11
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(11).
                onChildView(withId(R.id.rightChoice)).perform(click());

        // 12
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(12).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 13
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(13).
                onChildView(withId(R.id.wrongChoice)).perform(click());

        // 14
        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(14).
                onChildView(withId(R.id.rightChoice)).perform(click());

        // save scale
        ViewInteraction appCompatButton17 = onView(
                allOf(withId(R.id.saveButton),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton17.perform(click());

        ViewInteraction appCompatButton18 = onView(
                allOf(withId(R.id.session_finish),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton18.perform(click());

        ViewInteraction appCompatButton19 = onView(
                allOf(withId(android.R.id.button1)));
        appCompatButton19.perform(scrollTo(), click());


        // check result
        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative), withText("6.0 (0-15)"),
                        isDisplayed()));
        textView.check(matches(withText("6.0 (0-15)")));

    }

    @Test
    public void miniMentaltest() {


        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation), withText("Criar Sessão"), isDisplayed()));
        appCompatButton.perform(click());


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("Analfabeto"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton2.perform(scrollTo(), click());

        // category 1

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(1,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(2,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(3,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(4,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(5,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(6,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(7,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(8,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(9,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));


        ViewInteraction appCompatImageButton14 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton14.perform(click());

        // category 2

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(1,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(2,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        ViewInteraction appCompatImageButton25 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton25.perform(click());

        // category 3

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(1,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(2,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(3,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(4,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        ViewInteraction appCompatImageButton20 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton20.perform(click());

        // category 4

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(1,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(2,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

//        onView(allOf(withId(R.id.questions),
//                isDisplayed())).perform(
//                RecyclerViewActions.actionOnItemAtPosition(3,
//                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        ViewInteraction appCompatImageButton89 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton89.perform(click());

        // category 5

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(1,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));


        ViewInteraction appCompatImageButton28 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton28.perform(click());

        // category 6

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));


        ViewInteraction appCompatImageButton30 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton30.perform(click());

        // category 7

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(1,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(2,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        ViewInteraction appCompatImageButton34 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton34.perform(click());

        // category 8

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        ViewInteraction appCompatImageButton36 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton36.perform(click());

        // ctaegory 9

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.rightChoice)));

        ViewInteraction appCompatImageButton38 = onView(
                allOf(withId(R.id.nextCategory), isDisplayed()));
        appCompatImageButton38.perform(click());

        // category 10

        onView(allOf(withId(R.id.questions),
                isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.wrongChoice)));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.saveButton),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.session_finish),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton6.perform(scrollTo(), click());


        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative),
                        isDisplayed()));
        textView.check(matches(withText("19.0 (0-30)")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.result_qualitative),
                        isDisplayed()));
        textView2.check(matches(withText("Resultado dentro do esperado")));

    }


    @Test
    public void nutritionalTest() {

        // TODO handle offscreen

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_acg_evaluation), isDisplayed()));
        appCompatButton.perform(click());


        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));

        // first is triagem
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(0).perform(click());

        nutritionalTriagem();

        nutritionalGlobal();

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.session_finish),
                        withParent(allOf(withId(R.id.finishButton),
                                withParent(withId(R.id.newSessionLayout)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Sim")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative), withText("5.0 (0-14)"),
                        isDisplayed()));
        textView.check(matches(withText("5.0 (0-14)")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.result_quantitative), withText("11.5 (0-30)"),
                        isDisplayed()));
        textView2.check(matches(withText("11.5 (0-30)")));

    }

    private void nutritionalGlobal() {
        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));

        // select global assessment
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(0).perform(click());

        ViewInteraction appCompatCheckedTextView7 = onView(
                allOf(withId(android.R.id.text1), withText("sim"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView7.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(1).perform(click());

        ViewInteraction appCompatCheckedTextView8 = onView(
                allOf(withId(android.R.id.text1), withText("não"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView8.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(2).perform(click());

        ViewInteraction appCompatCheckedTextView9 = onView(
                allOf(withId(android.R.id.text1), withText("sim"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView9.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(3).perform(click());

        ViewInteraction appCompatCheckedTextView10 = onView(
                allOf(withId(android.R.id.text1), withText("uma refeição"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView10.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(4).perform(click());

        ViewInteraction appCompatCheckedTextView11 = onView(
                allOf(withId(android.R.id.text1), withText("três respostas «sim»"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView11.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(5).perform(click());

        ViewInteraction appCompatCheckedTextView12 = onView(
                allOf(withId(android.R.id.text1), withText("não"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView12.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(6).perform(click());


        ViewInteraction appCompatCheckedTextView13 = onView(
                allOf(withId(android.R.id.text1), withText("três a cinco copos"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView13.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(7).perform(click());

        ViewInteraction appCompatCheckedTextView14 = onView(
                allOf(withId(android.R.id.text1), withText("não é capaz de se alimentar sozinho"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView14.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(8).perform(click());

        ViewInteraction appCompatCheckedTextView15 = onView(
                allOf(withId(android.R.id.text1), withText("acredita não ter um problema nutricional"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView15.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(9).perform(click());

        ViewInteraction appCompatCheckedTextView16 = onView(
                allOf(withId(android.R.id.text1), withText("pior"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView16.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(10).perform(click());

        ViewInteraction appCompatCheckedTextView17 = onView(
                allOf(withId(android.R.id.text1), withText("PB < 21"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView17.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(11).perform(click());

        ViewInteraction appCompatCheckedTextView18 = onView(
                allOf(withId(android.R.id.text1), withText("PP > 31"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView18.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.saveButton), withText("Guardar"),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton3.perform(click());
    }

    private void nutritionalTriagem() {
        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("diminuição grave da ingesta"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(1).perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("não sabe informar"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(2).perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("normal"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(3).perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("sim"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(4).perform(click());

        ViewInteraction appCompatCheckedTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("demência ou depressão graves"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView5.perform(click());

        onData(anything()).inAdapterView(withId(R.id.testQuestions)).atPosition(5).perform(click());

        ViewInteraction appCompatCheckedTextView6 = onView(
                allOf(withId(android.R.id.text1), withText("21 < IMC < 23"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView6.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.saveButton),
                        withParent(withId(R.id.saveButtonLayout)),
                        isDisplayed()));
        appCompatButton2.perform(click());
    }

    public static void tapRecyclerViewItem(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
        onView(withRecyclerView(recyclerViewId).atPosition(position)).perform(click());
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {

            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                // check if matches
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}
