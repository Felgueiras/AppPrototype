package com.felgueiras.apps.geriatrichelper.HelpFeedbackAbout;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.BuildConfig;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatrichelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutFragment extends Fragment {

    @BindView(R.id.infoText)
    TextView info;
    @BindView(R.id.appVersion)
    TextView appVer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about, container, false);
        getActivity().setTitle(getResources().getString(R.string.about));

        ButterKnife.bind(this, view);

        // set info text
        info.setText(FirebaseRemoteConfig.getString("about_info",
                ""));

        // set app version
        appVer.setText("Versão: " + BuildConfig.VERSION_CODE+"");

        return view;
    }

    @OnClick(R.id.openPDF)
    public void openPDF(View view) {
        Uri uri = Uri.parse("https://www.spmi.pt/docs_nucleos/GERMI_36.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}

