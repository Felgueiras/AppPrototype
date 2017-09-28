package com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.QuestionCategoriesViewPager;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.QuestionCategory;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.SingleQuestion.RightWrongQuestionHandler;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.QuestionFirebase;
import com.felgueiras.apps.geriatrichelper.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CategoryDisplayQuestions extends RecyclerView.Adapter<CategoryDisplayQuestions.MyViewHolder> {

    private final GeriatricScaleNonDB scaleNonDB;
    private final int categoryIndex;
    private final GeriatricScaleFirebase scaleDB;
    private final QuestionsListAdapter adapter;
    private final TextView categoryTextView;
    private Activity context;
    private RecyclerView recyclerView;


    CategoryDisplayQuestions(Activity context,
                             GeriatricScaleNonDB testNonDB, int categoryIndex, GeriatricScaleFirebase test,
                             QuestionsListAdapter adapter, TextView categoryTextView, RecyclerView questionsRecyclerView) {
        this.context = context;
        this.scaleNonDB = testNonDB;
        this.categoryIndex = categoryIndex;
        this.scaleDB = test;
        this.adapter = adapter;
        this.categoryTextView = categoryTextView;
        this.recyclerView = questionsRecyclerView;
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // ButterKnife
        @BindView(R.id.nameQuestion)
        TextView questionTextView;
        @BindView(R.id.rightChoice)
        ImageButton right;
        @BindView(R.id.wrongChoice)
        ImageButton wrong;
        @BindView(R.id.questionImage)
        ImageView questionImage;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_question_right_wrong_icons, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int questionIndexInsideCategory) {
        final QuestionCategory currentCategory = scaleNonDB.getQuestionsCategories().get(categoryIndex);

        // question not in DB
        QuestionNonDB currentQuestionNonDB = currentCategory.getQuestions().get(questionIndexInsideCategory);
        // question in DB
        QuestionFirebase questionInDB = null;
        final int questionIdxGlobal = QuestionCategory.getQuestionIndex(categoryIndex,
                questionIndexInsideCategory, scaleNonDB);
        if (scaleDB != null) {
            String dummyID = scaleDB.getGuid() + "-" + questionIdxGlobal;
            questionInDB = FirebaseDatabaseHelper.getQuestionByID(dummyID);
            if (questionInDB == null) {
                questionInDB = new QuestionFirebase();
                // create question and add to DB
                questionInDB.setGuid(dummyID);
                questionInDB.setDescription(currentQuestionNonDB.getDescription());
                questionInDB.setScaleID(scaleDB.getGuid());
                questionInDB.setYesOrNo(false);
                questionInDB.setRightWrong(true);
                FirebaseDatabaseHelper.createQuestion(questionInDB);
            }
        }

        // check if all questions were answered
        checkAllQuestionsAnswered();


        /*
          Set View
         */
        holder.questionTextView.setText((questionIndexInsideCategory + 1) + " - " + currentQuestionNonDB.getDescription());

        // check if there is an associated image
        if (!currentQuestionNonDB.getImage().equals("")) {
            setImage(holder, currentQuestionNonDB);
        }

        // if question is already answered
        if (questionInDB != null && questionInDB.isAnswered()) {
            ////system.out.println(questionInDB.toString());
            if (questionInDB.getSelectedRightWrong().equals("right")) {
                holder.right.setImageResource(R.drawable.ic_right_selected);
            } else {
                holder.wrong.setImageResource(R.drawable.ic_wrong_selected);
            }
        }
        else
        {
            holder.right.setImageResource(R.drawable.ic_right_unselected);
            holder.wrong.setImageResource(R.drawable.ic_wrong_unselected);
        }

        if (scaleDB != null) {

            final QuestionFirebase finalQuestionInDB = questionInDB;
            View.OnClickListener choiceSelectedClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choiceSelected(finalQuestionInDB, questionIdxGlobal, holder, currentCategory, v);
                }
            };
            holder.right.setOnClickListener(choiceSelectedClickListener);
            holder.wrong.setOnClickListener(choiceSelectedClickListener);
        }

    }

    /**
     * Fetch image from firebase and set it to a question.
     *
     * @param holder
     * @param currentQuestionNonDB
     */
    private void setImage(final MyViewHolder holder, QuestionNonDB currentQuestionNonDB) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                .child("images/" + currentQuestionNonDB.getImage());

        try {
            final File imageFile = File.createTempFile("photoDownloaded", "png");
            storageRef.getFile(imageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    // display image
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
//                    options.inSampleSize = 8;

                    final Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                            options);
                    holder.questionImage.setVisibility(View.VISIBLE);

                    holder.questionImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                            View layout = inflater.inflate(R.layout.custom_fullimage_dialog, null);
                            ImageView image = layout.findViewById(R.id.fullimage);
                            image.setImageBitmap(bitmap);
                            imageDialog.setView(layout);
                            imageDialog.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = imageDialog.create();
                            alertDialog.show();

                            // update image size
                            Display display = context.getWindowManager().getDefaultDisplay();
                            image.getLayoutParams().width = (int) (display.getWidth() * 0.8f);
                            image.getLayoutParams().height = (int) (display.getHeight() * 0.8f);


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof com.google.firebase.storage.StorageException) {
                        // scale was not found for that language
                        Log.d("Download", "Image does not exist");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle choice selection.
     *
     * @param questionDB
     * @param questionIndex
     * @param holder
     * @param currentCategory
     * @param v
     */
    private void choiceSelected(QuestionFirebase questionDB, int questionIndex, MyViewHolder holder, QuestionCategory currentCategory, View v) {
        // signal this question was answered
        questionDB.setAnswered(true);
        FirebaseDatabaseHelper.updateQuestion(questionDB);
        // check if question was answered
        checkAllQuestionsAnswered();
        recyclerView.getLayoutManager().scrollToPosition(questionIndex + 2);

        new RightWrongQuestionHandler(questionDB, adapter,
                scaleNonDB, questionIndex, holder.right,
                holder.wrong, categoryTextView, currentCategory).onClick(v);
    }

    /**
     * Check if all questions were answered.
     */
    private void checkAllQuestionsAnswered() {
        int numQuestionsAnswered = 0;
        int numQuestionsTotal = scaleNonDB.getQuestionsCategories().get(categoryIndex).getQuestions().size();
        for (int i = 0; i < numQuestionsTotal; i++) {

            int qIdx = QuestionCategory.getQuestionIndex(categoryIndex,
                    i,
                    scaleNonDB);
            if (scaleDB != null) {
                String dummyID = scaleDB.getGuid() + "-" + qIdx;
                QuestionFirebase questionInDB = FirebaseDatabaseHelper.getQuestionByID(dummyID);
                if (questionInDB != null && questionInDB.isAnswered()) numQuestionsAnswered++;
            }


        }
        if (numQuestionsAnswered == numQuestionsTotal) {
            categoryTextView.setBackgroundResource(R.color.question_answered);
        }
    }


    @Override
    public int getItemCount() {
        /*
          Number of questions for this category.
         */
        QuestionCategory cat = scaleNonDB.getQuestionsCategories().get(categoryIndex);
        return cat.getQuestions().size();
    }


}
