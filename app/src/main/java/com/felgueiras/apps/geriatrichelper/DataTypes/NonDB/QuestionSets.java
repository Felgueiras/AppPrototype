package com.felgueiras.apps.geriatrichelper.DataTypes.NonDB;

import com.felgueiras.apps.geriatrichelper.Constants;

import java.util.ArrayList;

/**
 * Created by felgueiras on 02/03/2017.
 */

public class QuestionSets {

    public static QuestionSetNonDB advancedDailyLifeActivities() {
        QuestionSetNonDB questionSet = new QuestionSetNonDB(Constants.set_name_advancedDailyLifeActivities,
                Constants.cga_functional, Constants.set_name_advancedDailyLifeActivities,
                "");
        // short area
        questionSet.setShortName("Atividades avançadas");

        // 1
        QuestionNonDB question = new QuestionNonDB("Colabora? em atividades sócio-recreativas e trabalho?", 0, 1);
        question.setYesOrNo(true);
        questionSet.addQuestion(question);
        // 2
        question = new QuestionNonDB("Utiliza tecnologia? (Ipad)", 1, 0);
        question.setYesOrNo(true);
        questionSet.addQuestion(question);
        // 3
        question = new QuestionNonDB("Viaja?", 1, 0);
        question.setYesOrNo(true);
        questionSet.addQuestion(question);
        // 4
        question = new QuestionNonDB("Pratica exercício físico intenso?", 1, 0);
        question.setYesOrNo(true);
        questionSet.addQuestion(question);

        return questionSet;
    }

    public static QuestionSetNonDB getQuestionSetByName(String setName) {
        switch (setName) {
            case Constants.set_name_advancedDailyLifeActivities:
                return advancedDailyLifeActivities();

        }
        return null;
    }

    /**
     * Get all question sets.
     *
     * @return
     */
    public static ArrayList<QuestionSetNonDB> getAllQuestionSets() {
        ArrayList<QuestionSetNonDB> sets = new ArrayList<>();
        sets.add(advancedDailyLifeActivities());
        return sets;
    }
}
