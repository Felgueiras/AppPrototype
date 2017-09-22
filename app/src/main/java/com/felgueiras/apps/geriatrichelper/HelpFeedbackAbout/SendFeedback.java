package com.felgueiras.apps.geriatrichelper.HelpFeedbackAbout;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.felgueiras.apps.geriatrichelper.R;


public class SendFeedback extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.send_feedback, container, false);

        // set the title
        getActivity().setTitle(getResources().getString(R.string.send_feedback));

        final Button sendFeedback = view.findViewById(R.id.ButtonSendFeedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailWithFeedback();
            }


        });

        return view;
    }

    private void sendEmailWithFeedback() {


        final EditText nameField = view.findViewById(R.id.EditTextName);
        String name = nameField.getText().toString();

        final EditText feedbackField = view.findViewById(R.id.EditTextFeedbackBody);
        String feedback = feedbackField.getText().toString();

        final CheckBox responseCheckbox = view.findViewById(R.id.CheckBoxResponse);
        boolean bRequiresResponse = responseCheckbox.isChecked();

        if (name.isEmpty()) {
            Snackbar.make(view, getResources().getString(R.string.feedback_name_error), Snackbar.LENGTH_SHORT).show();
            return;
        }


        if (feedback.isEmpty()) {
            Snackbar.make(view, getResources().getString(R.string.feedback_message_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        String[] TO = {"rafaelfelgueiras1993@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        if (bRequiresResponse) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Prototype application feedback - requires response");
        } else {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Prototype application feedback");
        }
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }


    }
}
