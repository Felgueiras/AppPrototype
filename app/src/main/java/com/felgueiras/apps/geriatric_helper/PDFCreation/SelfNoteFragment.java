package com.felgueiras.apps.geriatric_helper.PDFCreation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.HelpersHandlers.PdfHelper;
import com.felgueiras.apps.geriatric_helper.R;

public class SelfNoteFragment extends Fragment {


    private View mRootView;

    public SelfNoteFragment() {
        // Required empty public constructor
    }

    public static SelfNoteFragment newInstance() {
        SelfNoteFragment fragment = new SelfNoteFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_self_note, container, false);

        PdfHelper.createSamplePdf(getActivity());

        return mRootView;
    }
}