package com.example.rafael.appprototype.ViewSinglePatient;

import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewPatient extends AppCompatActivity {

    private Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        // get the patient we are checking
        Bundle b = getIntent().getExtras();
        if (b != null) {
            patient = (Patient) b.getSerializable(Constants.patient);
        }
        setTitle(patient.getName());

        TextView patientNameTextView = (TextView) findViewById(R.id.patientName);
        patientNameTextView.setText(patient.getName());

        ImageView patientPhoto = (ImageView) findViewById(R.id.patientPhoto);
        if (patient.getGender() == Constants.MALE) {
            patientPhoto.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.male));
        } else {
            patientPhoto.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.female));
        }


        // create new Tests
        GeriatricTest geriatricTest1 = new GeriatricTest("Escala de Katz", Constants.test_type_estadoFuncional, "Atividades de Vida Diária Básicas", "some description");
        geriatricTest1.setResult(3);

        GeriatricTest geriatricTest2 = new GeriatricTest();
        geriatricTest2.setType("Estado Cogniytivo - Atividades de Vida Diária Básicas");
        geriatricTest2.setTestName("Escala de Katz");
        geriatricTest2.setResult(3);

        // add tests to ArrayList
        ArrayList<GeriatricTest> tests = new ArrayList<>();
        tests.add(geriatricTest1);
        tests.add(geriatricTest2);

        // create new Session
        Session session = new Session();
        // add tests to Session
        session.setGuid("1010101");
        session.setDate(Session.dateToString(Calendar.getInstance().getTime()));
        ArrayList<Session> sessions = new ArrayList<>();
        // add Session to list of Records
        sessions.add(session);
        sessions.add(session);


        // get the ViewPager
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        // create an adapter to be used by the ViewPager
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), sessions);
        vpPager.setAdapter(adapterViewPager);

        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(ViewPatient.this, "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

    }

}
