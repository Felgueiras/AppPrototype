package com.felgueiras.apps.geriatrichelper.Main;


import android.support.test.espresso.ViewInteraction;
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
                allOf(withId(R.id.result_quantitative), withText("5.0 (0-5)"),
                        isDisplayed()));
        textView.check(matches(withText("5.0 (0-5)")));

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

        // TODO select by item
        ViewInteraction appCompatButton2 = onView(
                withIndex(allOf(withId(R.id.rightChoice), isDisplayed()), 0
                ));
        appCompatButton2.perform(click());


        ViewInteraction appCompatButton3 = onView(
                withIndex(allOf(withId(R.id.wrongChoice), isDisplayed()), 1
                ));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                withIndex(allOf(withId(R.id.wrongChoice), isDisplayed()), 2
                ));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                withIndex(allOf(withId(R.id.wrongChoice), isDisplayed()), 3
                ));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                withIndex(allOf(withId(R.id.wrongChoice), isDisplayed()), 4
                ));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                withIndex(allOf(withId(R.id.rightChoice), isDisplayed()), 5
                ));
        appCompatButton7.perform(click());

        ViewInteraction appCompatButton8 = onView(
                withIndex(allOf(withId(R.id.rightChoice), isDisplayed()), 6
                ));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                withIndex(allOf(withId(R.id.rightChoice)), 7
                ));
        appCompatButton9.perform(click());

        ViewInteraction appCompatButton10 = onView(
                withIndex(allOf(withId(R.id.wrongChoice), isDisplayed()), 8
                ));
        appCompatButton10.perform(click());

        ViewInteraction appCompatButton11 = onView(
                withIndex(allOf(withId(R.id.wrongChoice), isDisplayed()), 9
                ));
        appCompatButton11.perform(click());

        ViewInteraction appCompatButton12 = onView(
                withIndex(allOf(withId(R.id.rightChoice), isDisplayed()), 10
                ));
        appCompatButton12.perform(click());

        ViewInteraction appCompatButton13 = onView(
                withIndex(allOf(withId(R.id.rightChoice), isDisplayed()), 11
                ));
        appCompatButton13.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.wrongChoice), isDisplayed()));
        appCompatButton14.perform(click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.wrongChoice), isDisplayed()));
        appCompatButton15.perform(click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.rightChoice), isDisplayed()));
        appCompatButton16.perform(click());

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

        ViewInteraction textView = onView(
                allOf(withId(R.id.result_quantitative), withText("6.0 (0-15)"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("6.0 (0-15)")));

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

    @Test
    public void nutritionalTest() {


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

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.nameQuestion), withText("A - Nos últimos três meses houve diminuição da ingesta alimentar devido a perda de apetite, problemas digestivos ou dificuldade para mastigar ou deglutir?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                0)),
                        isDisplayed()));
        appCompatTextView.perform(click());

//        tapRecyclerViewItem(R.id.testQuestions,0);

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText("diminuição grave da ingesta"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.nameQuestion), withText("B - Perda de peso nos últimos 3 meses"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                1)),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatCheckedTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("não sabe informar"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.nameQuestion), withText("C - Mobilidade"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                2)),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatCheckedTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("normal"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.nameQuestion), withText("D - Passou por algum stress psicológico ou doença aguda nos últimos três meses?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                3)),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatCheckedTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("sim"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.nameQuestion), withText("E - Problemas neuropsicológicos"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                4)),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction appCompatCheckedTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("demência ou depressão graves"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.nameQuestion), withText("F - Índice de Massa Corporal (IMC = peso[kg] / estatura [m 2 ])"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                5)),
                        isDisplayed()));
        appCompatTextView6.perform(click());

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

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.area_scales_recycler_view),
                        withParent(allOf(withId(R.id.newSessionLayout),
                                withParent(withId(R.id.current_fragment)))),
                        isDisplayed()));

        // select global assessment
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(R.id.nameQuestion), withText("1 - G - O doente vive na sua própria casa\n(não em instituição geriátrica ou hospital)"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                0)),
                        isDisplayed()));
        appCompatTextView7.perform(click());

        ViewInteraction appCompatCheckedTextView7 = onView(
                allOf(withId(android.R.id.text1), withText("sim"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView7.perform(click());

        ViewInteraction appCompatTextView8 = onView(
                allOf(withId(R.id.nameQuestion), withText("H - Utiliza mais de três medicamentos diferentes por dia?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                1)),
                        isDisplayed()));
        appCompatTextView8.perform(click());

        ViewInteraction appCompatCheckedTextView8 = onView(
                allOf(withId(android.R.id.text1), withText("não"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView8.perform(click());

        ViewInteraction appCompatTextView9 = onView(
                allOf(withId(R.id.nameQuestion), withText("I - Lesões de pele ou escaras?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                2)),
                        isDisplayed()));
        appCompatTextView9.perform(click());

        ViewInteraction appCompatCheckedTextView9 = onView(
                allOf(withId(android.R.id.text1), withText("sim"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView9.perform(click());

        ViewInteraction appCompatTextView10 = onView(
                allOf(withId(R.id.nameQuestion), withText("J - Quantas refeições faz por dia?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                3)),
                        isDisplayed()));
        appCompatTextView10.perform(click());

        ViewInteraction appCompatCheckedTextView10 = onView(
                allOf(withId(android.R.id.text1), withText("uma refeição"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView10.perform(click());

        ViewInteraction appCompatTextView11 = onView(
                allOf(withId(R.id.nameQuestion), withText("5 - K - O doente consome:\n• pelo menos uma porção diária de leite ou derivados (leite, queijo, iogurte)?\n• duas ou mais porções semanais de leguminosas ou ovos?\n• carne, peixe ou aves todos os dias?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                4)),
                        isDisplayed()));
        appCompatTextView11.perform(click());

        ViewInteraction appCompatCheckedTextView11 = onView(
                allOf(withId(android.R.id.text1), withText("três respostas «sim»"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView11.perform(click());

        ViewInteraction appCompatTextView12 = onView(
                allOf(withId(R.id.nameQuestion), withText("6 - L - O doente consome duas ou mais porções diárias de fruta\nou produtos hortícolas?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                5)),
                        isDisplayed()));
        appCompatTextView12.perform(click());

        ViewInteraction appCompatCheckedTextView12 = onView(
                allOf(withId(android.R.id.text1), withText("não"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView12.perform(click());

        ViewInteraction appCompatTextView13 = onView(
                allOf(withId(R.id.nameQuestion),
                        withText("7 - M - Quantos copos de líquidos (água, sumo, café, chá, leite) o\ndoente consome por dia?")));
        appCompatTextView13.perform(click());

        ViewInteraction appCompatCheckedTextView13 = onView(
                allOf(withId(android.R.id.text1), withText("três a cinco copos"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView13.perform(click());

        ViewInteraction appCompatTextView14 = onView(
                allOf(withId(R.id.nameQuestion), withText("N - Modo de se alimentar"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                7)),
                        isDisplayed()));
        appCompatTextView14.perform(click());

        ViewInteraction appCompatCheckedTextView14 = onView(
                allOf(withId(android.R.id.text1), withText("não é capaz de se alimentar sozinho"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView14.perform(click());

        ViewInteraction appCompatTextView15 = onView(
                allOf(withId(R.id.nameQuestion), withText("O - O doente acredita ter algum problema nutricional?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                8)),
                        isDisplayed()));
        appCompatTextView15.perform(click());

        ViewInteraction appCompatCheckedTextView15 = onView(
                allOf(withId(android.R.id.text1), withText("acredita não ter um problema nutricional"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatCheckedTextView15.perform(click());

        ViewInteraction appCompatTextView16 = onView(
                allOf(withId(R.id.nameQuestion), withText("10 - P - Em comparação com outras pessoas da mesma idade,\ncomo considera o doente a sua própria saúde?"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                9)),
                        isDisplayed()));
        appCompatTextView16.perform(click());

        ViewInteraction appCompatCheckedTextView16 = onView(
                allOf(withId(android.R.id.text1), withText("pior"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView16.perform(click());

        ViewInteraction appCompatTextView17 = onView(
                allOf(withId(R.id.nameQuestion), withText("Q - Perímetro braquial (PB) em cm"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                10)),
                        isDisplayed()));
        appCompatTextView17.perform(click());

        ViewInteraction appCompatCheckedTextView17 = onView(
                allOf(withId(android.R.id.text1), withText("PB < 21"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatCheckedTextView17.perform(click());

        ViewInteraction appCompatTextView18 = onView(
                allOf(withId(R.id.nameQuestion), withText("R - Perímetro da perna (PP) em cm"),
                        withParent(childAtPosition(
                                withId(R.id.testQuestions),
                                11)),
                        isDisplayed()));
        appCompatTextView18.perform(click());

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
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("5.0 (0-14)")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.result_quantitative), withText("11.5 (0-30)"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("11.5 (0-30)")));

    }

    public static void tapRecyclerViewItem(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
        onView(withRecyclerView(recyclerViewId).atPosition(position)).perform(click());
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
