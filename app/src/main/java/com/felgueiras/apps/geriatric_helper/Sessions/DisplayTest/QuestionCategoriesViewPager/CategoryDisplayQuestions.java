package com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.QuestionCategoriesViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionCategory;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.SingleQuestion.RightWrongQuestionHandler;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.QuestionFirebase;
import com.felgueiras.apps.geriatric_helper.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CategoryDisplayQuestions extends RecyclerView.Adapter<CategoryDisplayQuestions.MyViewHolder> {

    private final GeriatricScaleNonDB scaleNonDB;
    private final int categoryIndex;
    private final GeriatricScaleFirebase scaleDB;
    private final QuestionsListAdapter adapter;
    private final TextView categoryTextView;
    private Activity context;


    public CategoryDisplayQuestions(Activity context,
                                    GeriatricScaleNonDB testNonDB, int categoryIndex, GeriatricScaleFirebase test,
                                    QuestionsListAdapter adapter, TextView categoryTextView) {
        this.context = context;
        this.scaleNonDB = testNonDB;
        this.categoryIndex = categoryIndex;
        this.scaleDB = test;
        this.adapter = adapter;
        this.categoryTextView = categoryTextView;
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton right, wrong;
        private final ImageView questionImage;
        public TextView questionTextView;

        public MyViewHolder(View view) {
            super(view);
            questionTextView = (TextView) view.findViewById(R.id.nameQuestion);
            // right and wrong button
            right = (ImageButton) view.findViewById(R.id.rightChoice);
            wrong = (ImageButton) view.findViewById(R.id.wrongChoice);
            questionImage = (ImageView) view.findViewById(R.id.questionImage);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_question_right_wrong_icons, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int questionIndex) {
        final QuestionCategory currentCategory = scaleNonDB.getQuestionsCategories().get(categoryIndex);

        // question not in DB
        final QuestionNonDB currentQuestionNonDB = currentCategory.getQuestions().get(questionIndex);
        // question in DB
        QuestionFirebase questionInDB = null;
        final int questionIdx = QuestionCategory.getQuestionIndex(categoryIndex,
                questionIndex,
                scaleNonDB);
        if (scaleDB != null) {
            String dummyID = scaleDB.getGuid() + "-" + questionIdx;
            questionInDB = FirebaseHelper.getQuestionByID(dummyID);
            if (questionInDB == null) {
                questionInDB = new QuestionFirebase();
                // create question and add to DB
                questionInDB.setGuid(dummyID);
                questionInDB.setDescription(currentQuestionNonDB.getDescription());
                questionInDB.setScaleID(scaleDB.getGuid());
                questionInDB.setYesOrNo(false);
                questionInDB.setRightWrong(true);
                FirebaseHelper.createQuestion(questionInDB);

            }
        }

        // check if all questions were answered
        signalAllQuestionsAnswered();


        /**
         * Set View
         */
        holder.questionTextView.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());

        if (currentQuestionNonDB.getImage() != 0) {
//            holder.questionImage.setImageResource(currentQuestionNonDB.getImage());
            holder.questionImage.setVisibility(View.VISIBLE);

            // display image in full screen when clicking it
            holder.questionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                    View layout = inflater.inflate(R.layout.custom_fullimage_dialog, null);
                    ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
                    image.setImageResource(currentQuestionNonDB.getImage());
                    imageDialog.setView(layout);
                    imageDialog.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });


                    imageDialog.create();
                    imageDialog.show();
                }
            });
        }
        // detect when choice changed

        // if question is already answered
        if (questionInDB != null && questionInDB.isAnswered()) {
            ////system.out.println(questionInDB.toString());
            if (questionInDB.getSelectedRightWrong().equals("right")) {
                holder.right.setImageResource(R.drawable.ic_check_box_black_24dp);
            } else {
                holder.wrong.setImageResource(R.drawable.close_box);
            }
        }


        if (scaleDB != null) {
//            holder.right.setOnClickListener(new RightWrongQuestionHandler(questionInDB, adapter,
//                    scaleNonDB, questionIdx, holder.right, holder.wrong, categoryTextView, currentCategory));
//            holder.wrong.setOnClickListener(new RightWrongQuestionHandler(questionInDB, adapter,
//                    scaleNonDB, questionIdx, holder.right, holder.wrong, categoryTextView,
//                    currentCategory));

            final QuestionFirebase finalQuestionInDB = questionInDB;
            holder.right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // signal this question was answered
                    finalQuestionInDB.setAnswered(true);

                    FirebaseHelper.updateQuestion(finalQuestionInDB);
                    // check if question was answered
                    signalAllQuestionsAnswered();

                    new RightWrongQuestionHandler(finalQuestionInDB, adapter,
                            scaleNonDB, questionIdx, holder.right,
                            holder.wrong, categoryTextView, currentCategory).onClick(v);
                }
            });
            holder.wrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // signal this question was answered

                    finalQuestionInDB.setAnswered(true);
                    FirebaseHelper.updateQuestion(finalQuestionInDB);

                    // check if question was answered
                    signalAllQuestionsAnswered();
                    new RightWrongQuestionHandler(finalQuestionInDB, adapter,
                            scaleNonDB, questionIdx, holder.right, holder.wrong, categoryTextView, currentCategory).onClick(v);
                }
            });
        }


    }

    public void signalAllQuestionsAnswered() {
        if (allQuestionsFromCategoryAnswered()) {
            categoryTextView.setBackgroundResource(R.color.question_answered);
        }
    }

    public boolean allQuestionsFromCategoryAnswered() {
        int numQuestionsAnswered = 0;
        int numQuestionsTotal = scaleNonDB.getQuestionsCategories().get(categoryIndex).getQuestions().size();
        for (int i = 0; i < numQuestionsTotal; i++) {

            int qIdx = QuestionCategory.getQuestionIndex(categoryIndex,
                    i,
                    scaleNonDB);
            String dummyID = scaleDB.getGuid() + "-" + qIdx;
            QuestionFirebase questionInDB = FirebaseHelper.getQuestionByID(dummyID);
            if (questionInDB != null && questionInDB.isAnswered()) numQuestionsAnswered++;

        }
        return numQuestionsAnswered == numQuestionsTotal;
    }


    @Override
    public int getItemCount() {
        /**
         * Number of questions for this category.
         */
        QuestionCategory cat = scaleNonDB.getQuestionsCategories().get(categoryIndex);
        return cat.getQuestions().size();
    }


}