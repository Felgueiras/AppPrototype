package com.felgueiras.apps.geriatrichelper.PersonalAreaAccess;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.felgueiras.apps.geriatrichelper.Firebase.CipherDecipherFiles;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // FIrebase
    private FirebaseAuth auth;

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

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        /**
//         * Check if there's an already registered user.
//         */
//        final String email = SharedPreferencesHelper.getUserEmail(getActivity());
//        View view;
//        if (email != "") {
//            // Inflate the layout for this fragment
//            view = inflater.inflate(R.layout.activity_login_already_registered, container, false);
//
//            // set the title
//            getActivity().setTitle(getResources().getString(R.string.action_log_in));
//
//            TextView userNameTextView = (TextView) view.findViewById(R.id.userName);
//            String userName = SharedPreferencesHelper.getUserName(getActivity());
//            userNameTextView.setText(userName);
//
//
//            mPasswordView = (EditText) view.findViewById(R.id.password);
//            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                        attemptLogin(email, mPasswordView.getText().toString());
//                        return true;
//                    }
//                    return false;
//                }
//            });
//
//            Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
//            mEmailSignInButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    attemptLogin(email, mPasswordView.getText().toString());
//                }
//            });
//
//            mLoginFormView = view.findViewById(R.id.login_form);
//            mProgressView = view.findViewById(R.id.login_progress);
//
////            Button otherAccount = (Button) view.findViewById(R.id.other_account);
////            otherAccount.setOnClickListener(new OnClickListener() {
////                @Override
////                public void onClick(View view) {
////
////                }
////            });
//
//
//        } else {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_firebase, container, false);

        // set the title
        getActivity().setTitle(getResources().getString(R.string.action_log_in));

//        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
//
//        mPasswordView = (EditText) view.findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
//            }
//        });
//
//        mLoginFormView = view.findViewById(R.id.login_form);
//        mProgressView = view.findViewById(R.id.login_progress);
//
//
//        TextView registertext = (TextView) view.findViewById(R.id.register_text);
//        registertext.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * Go to register screen.
//                 */
//                Intent intent = new Intent(getActivity(), RegisterActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });

//        }

        final EditText inputEmail = view.findViewById(R.id.email);
        final EditText inputPassword = view.findViewById(R.id.password);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        Button btnSignup = view.findViewById(R.id.btn_signup);
        Button btnLogin = view.findViewById(R.id.btn_login);
        Button btnReset = view.findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
            }
        });

        // login putton pressed
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // generate cipher
                                    CipherDecipherFiles.getInstance().generateKey(
                                            FirebaseAuth.getInstance().getCurrentUser().getUid(), password,
                                            getActivity());

                                    // load initial list of patients
                                    FirebaseHelper.initializeFirebase();

                                    PatientsManagement.loadInitialPatients(getActivity());

                                    Intent intent = new Intent(getActivity(), PrivateAreaActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });
            }
        });


        return view;
    }


    /**
     * Validate email.
     *
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Validate password.
     *
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return true;
        // return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade_show-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private Boolean validateLogin(String email, String password) {
        return SharedPreferencesHelper.getUserEmail(getActivity()).equals(email) &&
                SharedPreferencesHelper.getUserPassword(getActivity()).equals(password);
    }
}

