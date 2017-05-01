package com.thestudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by joansirma on 4/30/17.
 */

public class LoginActivity extends AppCompatActivity{
    private Button loginbtn,newuserbtn,googlelogin,signout;
    private ProgressBar progressBar;
    private EditText existingEmail, existingPassword;
    private TextInputLayout loginInputEmail, loginInputPassword;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth =FirebaseAuth.getInstance();

        loginInputEmail = (TextInputLayout) findViewById(R.id.login_input_layout_email);
        loginInputPassword = (TextInputLayout) findViewById(R.id.login_input_layout_password);
        progressBar =(ProgressBar)findViewById(R.id.progressBar2);

        existingEmail = (EditText)findViewById(R.id.exisitng_email);
        existingPassword = (EditText)findViewById(R.id.exisitng_password);

        loginbtn = (Button) findViewById(R.id.login_button);
        newuserbtn = (Button) findViewById(R.id.newuser_button);
        googlelogin = (Button) findViewById(R.id.sign_in_button);

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });
        newuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, newUserActivity.class);
                startActivity(intent);
                //finish();


            }

        });

        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (LoginActivity.this, GoogleSignInActivity.class);
                startActivity(intent);
            }
        });



    }

        // validate the form
        public void submitForm() { String email = existingEmail.getText().toString().trim();
        String password = existingPassword.getText().toString().trim();

        if(!checkEmail()) {
            return;
        }
        if(!checkPassword()) {
            return;
        }
        loginInputEmail.setErrorEnabled(false);
        loginInputPassword.setErrorEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, Log a message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                        } else {
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


        }

    private boolean checkEmail() {
        String email = existingEmail.getText().toString().trim();
        if (email.isEmpty()|| !isEmailValid(email)){
            loginInputEmail.setErrorEnabled(true);
            loginInputEmail.setError("Please enter a valid email");
            existingEmail.setError("Required!");
            requestFocus(existingEmail);
            return false;

        }
        loginInputEmail.setErrorEnabled(false);
        return true;

    }

    private boolean checkPassword() {
        String password = existingPassword.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            loginInputPassword.setError(getString(R.string.err_msg_password));
            existingPassword.setError(getString(R.string.err_msg_required));
            requestFocus(existingPassword);
            return false;
        }
        loginInputPassword.setErrorEnabled(false);
        return true;
    }

    private boolean isPasswordValid(String password) {
        //Length of the password needs to be at least 6 char long
        return(password.length()>=6);

    }

    private  boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }



}

