package com.example.rafael.appprototype.LockScreen;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

public class LockScreenFragment extends Fragment {
    /**
     * Correct password
     */
    private String correctPassword = "1234";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_lock_screen, container, false);
        getActivity().setTitle(getResources().getString(R.string.prompt_password));
        EditText password = (EditText) myInflatedView.findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                Log.d("Lock",password.equals(correctPassword)+"");
                if (password.equals(correctPassword)) {
                    ((MainActivity) getActivity()).resumeAfterLock();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return myInflatedView;
    }

}

