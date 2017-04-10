package com.felgueiras.apps.geriatric_helper.PDFCreation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.felgueiras.apps.geriatric_helper.R;

public class CreatePDF extends AppCompatActivity {

    private static final String TAG = "PDF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);

        SelfNoteFragment noteFragment = SelfNoteFragment.newInstance();

        android.support.v4.app.FragmentTransaction fragTransaction= this.getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.container, noteFragment);
        fragTransaction.commit();
    }


}
