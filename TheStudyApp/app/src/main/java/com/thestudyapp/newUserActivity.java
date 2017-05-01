package com.thestudyapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by joansirma on 5/1/17.
 */

public class newUserActivity extends AppCompatActivity{
    private static final String TAG = "newUserActivity";
    private Button existingAccountBtn, newloginBtn;
    private ProgressBar progressBar2;
    private FirebaseAuth auth;
    private EditText signupEmail, signupPass, signupConfirmPass;
    private TextInputLayout signupEmailLayout, signupPassLayout, signupConfirmPassLayout;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        auth = FirebaseAuth.getInstance();

        signupEmailLayout = (TextInputLayout) findViewById(R.id.signup_input_layout_email);
        signupPassLayout = (TextInputLayout) findViewById(R.id.signup_input_layout_password);
        signupConfirmPassLayout = (TextInputLayout) findViewById(R.id.signup_confirm_input_layout_password);

        progressBar2 = (ProgressBar) findViewById(R.id.signup_progressbar);

        signupEmail = (EditText) findViewById(R.id.signup_email);
        signupPass = (EditText) findViewById(R.id.signup_password);
        signupConfirmPass = (EditText) findViewById(R.id.signup_comfirm_password);

        newloginBtn = (Button) findViewById(R.id.createnewuser_btn);
        existingAccountBtn = (Button) findViewById(R.id.exisitingaccount_btn);

        // when "Create Account" is clicked
        newloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_newuser);
                submitForm();
            }
        });

       existingAccountBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //user already has an account
               Intent intent = new Intent(newUserActivity.this, LoginActivity.class);
               startActivity(intent);

           }
       });



    }

    private void submitForm() {

        String email = signupEmail.getText().toString().trim();
        String password = signupPass.getText().toString().trim();

        if(!checkEmail()) {
            return;
        }
        if(!checkPassword()) {
            return;
        }
        signupEmailLayout.setErrorEnabled(false);
        signupPassLayout.setErrorEnabled(false);

        progressBar2.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(newUserActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUserWithEmail:onComplete:" + task.isSuccessful());
                        progressBar2.setVisibility(View.GONE);
                        // If sign in fails, Log the message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.d(TAG,"Authentication failed." + task.getException());

                        } else {
                            startActivity(new Intent(newUserActivity.this, HomePageActivity.class));
                            finish();
                        }
                    }
                });
        Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();
    }

    private boolean checkEmail() {
        String email = signupEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            signupEmailLayout.setErrorEnabled(true);
            signupEmailLayout.setError(getString(R.string.err_msg_email));
            signupEmail.setError(getString(R.string.err_msg_required));
            requestFocus(signupEmail);
            return false;
        }
        signupEmailLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {

        String password = signupPass.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            signupPassLayout.setError(getString(R.string.err_msg_password));
            signupPassLayout.setError(getString(R.string.err_msg_required));
            requestFocus(signupPass);
            return false;
        }
        signupPassLayout.setErrorEnabled(false);
        return true;
    }

    private static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isPasswordValid(String password){
        return (password.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar2.setVisibility(View.GONE);
    }
}
