package com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleTest;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.PhotoVideoHandling.RecordVideoActivity;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.PhotoVideoHandling.TakePhotoActivity;


public class ReviewScaleFragment extends Fragment {


    public static String PATIENT = "PATIENT";
    public static String SCALE = "SCALE";
    SessionFirebase session;
    /**
     * GeriatricScale which will be written to the DB.
     */
    private GeriatricScaleFirebase scale;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        scale = (GeriatricScaleFirebase) bundle.getSerializable(SCALE);
        session = FirebaseDatabaseHelper.getSessionFromScale(scale);

        // set the title
        getActivity().setTitle(scale.getShortName());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_scale, container, false);
        // populate the ListView
        ListView testQuestions = view.findViewById(R.id.testQuestions);
        // create the adapter
        QuestionsListAdapter adapter = new QuestionsListAdapter(
                this.getActivity(),
                Scales.getScaleByName(scale.getScaleName()),
                scale, null, getChildFragmentManager(), testQuestions, null);
        testQuestions.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        // if this test allows to take photoDownloaded, inflate another menu
        if (scale.photos()) {
            inflater.inflate(R.menu.menu_scale_photo, menu);
        }
//        else if (scale.isContainsVideo()) {
//            inflater.inflate(R.menu.menu_scale_video, menu);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                /*
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
                 */
                Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(TakePhotoActivity.SCALE_ID, scale.getGuid());
                intent.putExtras(bundle);

                startActivity(intent);
                break;

            case R.id.action_record_video:
                /*
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
                 */
                intent = new Intent(getActivity(), RecordVideoActivity.class);
                bundle = new Bundle();
                bundle.putString(RecordVideoActivity.SCALE_ID, scale.getGuid());
                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }

        return true;
    }


}