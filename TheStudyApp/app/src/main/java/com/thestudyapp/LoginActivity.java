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
import android.widget.TextView;
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
    private TextView newusertxt;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth =FirebaseAuth.getInstance();

        loginInputEmail = (TextInputLayout) findViewById(R.id.input_email_layout);
        loginInputPassword = (TextInputLayout) findViewById(R.id.input_password_layout);
        progressBar =(ProgressBar)findViewById(R.id.progressBar);

        existingEmail = (EditText)findViewById(R.id.email);
        existingPassword = (EditText)findViewById(R.id.password);

        loginbtn = (Button) findViewById(R.id.btn_login);
        newusertxt = (TextView) findViewById(R.id.link_signup);
        googlelogin = (Button) findViewById(R.id.btn_google);

        loginbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });
        newusertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, newUserActivity.class);
                startActivity(intent);
                finish();


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









//package com.thestudyapp;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//
//import android.content.Intent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.firebase.client.Firebase;
//import com.google.android.gms.common.api.GoogleApiActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import butterknife.ButterKnife;
//
//import static com.thestudyapp.R.id.progressBar;
//
//public class LoginActivity extends AppCompatActivity {
//    private static final String TAG = "LoginActivity";
//   // private static final int REQUEST_SIGNUP = 0;
//
//    private Button _loginButton,_googleButton;
//    private TextView _signupLink;
//    private EditText _emailText, _passwordText;
//    private ProgressBar _progressbar;
//    private TextInputLayout _emailTextLayout, _passTextLayout;
//    // Firebase Objects
//    private FirebaseAuth auth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private String mCustomToken;
//
//
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        ButterKnife.bind(this);
//        auth = FirebaseAuth.getInstance();
//
//
//        // Buttons
//        _loginButton = (Button) this.findViewById(R.id.btn_login);
//        _googleButton = (Button) this.findViewById(R.id.btn_google);
//        //Views
//        _signupLink = (TextView) this.findViewById(R.id.link_signup);
//        //Texts
//        _emailText = (EditText) this.findViewById(R.id.email);
//        _passwordText = (EditText) this.findViewById(R.id.password);
//        _emailTextLayout = (TextInputLayout) this.findViewById(R.id.input_email_layout);
//        _passTextLayout = (TextInputLayout) this.findViewById(R.id.input_password_layout);
//        //Progress Bar
//        _progressbar = (ProgressBar) this.findViewById(progressBar);
//
//
//
//        //get current user
//       // FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
//                    finish();
//                }
//            }
//        };
//
//
//
//        _loginButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                login();
//            }
//        });
//
//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, newUserActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//        _googleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, GoogleApiActivity.class);
//                startActivity(intent);
//
//            }
//
//        });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(mAuthListener);
//        FirebaseUser currentUser = auth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            ((TextView) findViewById(R.id.link_signup)).setText(
//                    "User ID: " + user.getUid());
//        } else {
//            ((TextView) findViewById(R.id.link_signup)).setText(
//                    "Error: sign in failed.");
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            auth.removeAuthStateListener(mAuthListener);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        _progressbar.setVisibility(View.GONE);
//    }
//
//
//
//    public void login() {
//        String email = _emailText.getText().toString().trim();
//        String password = _passwordText.getText().toString().trim();
//
//        //authenticate user
//        auth.signInWithCustomToken(mCustomToken)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCustomToken:success");
//                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//    }
//
//
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (requestCode == REQUEST_SIGNUP) {
////            if (resultCode == RESULT_OK) {
////
////                // TODO: Implement successful signup logic here
////                // By default we just finish the Activity and log them in automatically
////                //authetenticate user
////                String email = _emailText.getText().toString().trim();
////                String password = _passwordText.getText().toString().trim();
////                this.finish();
////            }
////        }
////    }
//
//    @Override
//    public void onBackPressed() {
//        // Disable going back to the MainActivity
//        moveTaskToBack(true);
//    }
//
//    public void onLoginSuccess() {
//        _loginButton.setEnabled(true);
//        Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
//
//        finish();
//    }
//
//    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//
//        _loginButton.setEnabled(true);
//    }
//
//    public boolean validate() {
//        boolean valid = true;
//
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
//            _passwordText.setError("please enter a valid password with at least  characters");
//            valid = false;
//        } else {
//            _passwordText.setError(null);
//        }
//
//        return valid;
//    }
//
//
//}
