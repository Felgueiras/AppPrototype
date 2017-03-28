package com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;

public class CategoryDisplayQuestions extends RecyclerView.Adapter<CategoryDisplayQuestions.MyViewHolder> {

    private final GeriatricScaleNonDB scaleNonDB;
    private final int categoryIndex;
    private final GeriatricScale scaleDB;
    private final QuestionsListAdapter adapter;
    private Activity context;


    public CategoryDisplayQuestions(Activity context,
                                    GeriatricScaleNonDB testNonDB, int categoryIndex, GeriatricScale test,
                                    QuestionsListAdapter adapter) {
        this.context = context;
        this.scaleNonDB = testNonDB;
        this.categoryIndex = categoryIndex;
        this.scaleDB = test;
        this.adapter = adapter;
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
        QuestionCategory currentCategory = scaleNonDB.getQuestionsCategories().get(categoryIndex);

        // question not in DB
        QuestionNonDB currentQuestionNonDB = currentCategory.getQuestions().get(questionIndex);
        // question in DB
        Question questionInDB;
        int questionIdx = QuestionCategory.getQuestionIndex(categoryIndex,
                questionIndex,
                scaleNonDB);
        String dummyID = scaleDB.getGuid() + "-" + questionIdx;
        questionInDB = Question.getQuestionByID(dummyID);
        if (questionInDB == null) {
            questionInDB = new Question();
            // create question and add to DB
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setScale(scaleDB);
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(true);
            questionInDB.save();
        }


        /**
         * Set View
         */
        holder.questionTextView.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());

        if (currentQuestionNonDB.getImage() != 0) {
            holder.questionImage.setImageResource(currentQuestionNonDB.getImage());
            holder.questionImage.setVisibility(View.VISIBLE);
        }
        // detect when choice changed

        // if question is already answered
        if (questionInDB.isAnswered()) {
            ////system.out.println(questionInDB.toString());
            if (questionInDB.getSelectedRightWrong().equals("right")) {
                holder.right.setImageResource(R.drawable.ic_check_box_black_24dp);
            } else {
                holder.wrong.setImageResource(R.drawable.close_box);
            }
        }


        holder.right.setOnClickListener(new RightWrongQuestionHandler(questionInDB, adapter,
                scaleNonDB, questionIdx, holder.right, holder.wrong));
        holder.wrong.setOnClickListener(new RightWrongQuestionHandler(questionInDB, adapter,
                scaleNonDB, questionIdx, holder.right, holder.wrong));

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
