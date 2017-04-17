package com.felgueiras.apps.geriatric_helper.PersonalAreaAccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.felgueiras.apps.geriatric_helper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUser extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

//    @InjectView(R.id.input_name)
//    EditText _nameText;
//    @InjectView(R.id.input_email)
//    EditText _emailText;
//    @InjectView(R.id.input_password)
//    EditText _passwordText;
//    @InjectView(R.id.input_password_second)
//    EditText _reenterPasswordText;
//    @InjectView(R.id.btn_signup)
//    Button _signupButton;


    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        TextView btnSignIn = (TextView) findViewById(R.id.sign_in_button);
        Button btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RegisterUser.this, PrivateAreaActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterUser.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterUser.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d("Firebase",task.getException()+"");
                                } else {
                                    startActivity(new Intent(RegisterUser.this, PrivateAreaActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register_user);
//        ButterKnife.inject(this);
//
//        _signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signup();
//            }
//        });
//
////        _loginLink.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                // Finish the registration screen and return to the Login activity
////                finish();
////            }
////        });
//    }

//    public void signup() {
//        Log.d(TAG, "Signup");
//
//        if (!validate()) {
//            onSignupFailed();
//            return;
//        }
//
//        _signupButton.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(RegisterUser.this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Registando-se...");
//        progressDialog.show();
//
//        final String name = _nameText.getText().toString();
//        final String email = _emailText.getText().toString();
//        final String password = _passwordText.getText().toString();
//        final String passwordSecond = _reenterPasswordText.getText().toString();
//
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//
//                        // erase all sessions without patients just to avoid errors
//                        for (Session sess : Session.getAllSessions()) {
//                            if (sess.getPatient() == null) {
//                                sess.delete();
//                            }
//                        }
//
//                        // Register user to the app
//                        SharedPreferencesHelper.registerUser(getBaseContext(), name, email, password);
//                        SharedPreferencesHelper.login(getBaseContext());
//                        Intent intent = new Intent(getBaseContext(), PrivateAreaActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                }, 3000);
//    }
//
//
//    public void onSignupSuccess() {
//        _signupButton.setEnabled(true);
//        setResult(RESULT_OK, null);
//        finish();
//    }
//
//    public void onSignupFailed() {
////        Toast.makeText(getBaseContext(), "Registo falhou", Toast.LENGTH_LONG).show();
//
//        _signupButton.setEnabled(true);
//    }
//
//    public boolean validate() {
//        boolean valid = true;
//
//        String name = _nameText.getText().toString();
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//        String passwordSecond = _reenterPasswordText.getText().toString();
//
//        if (name.isEmpty() || name.length() < 3) {
//            _nameText.setError("Deve ter pelo menos 3 caracteres");
//            valid = false;
//        } else {
//            _nameText.setError(null);
//        }
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("Introduza um email válido (ex: manuel_fernandes@gmail.com )");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            _passwordText.setError("Deve ter entre 4 a 10 carateres alfanuméricos");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
//
//        // validate the "second password"
//        if (passwordSecond.isEmpty() || passwordSecond.length() < 4 || passwordSecond.length() > 10) {
//            _reenterPasswordText.setError("Deve ter entre 4 a 10 carateres alfanuméricos");
//            valid = false;
//        } else if (!passwordSecond.equals(password)) {
//            _reenterPasswordText.setError("As palavras passe não são iguais");
//            valid = false;
//        } else {
//            _reenterPasswordText.setError(null);
//        }
//
//        return valid;
//    }
}
