package com.example.abanoub.onlinenotebook;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abanoub.onlinenotebook.widget.WidgetProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signIn;
    Button signUp;
    TextView signInText;
    TextView signUpText;
    LinearLayout signInLinear;
    LinearLayout signUpLinear;
    FirebaseAuth firebaseAuth;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitleEnabled(true);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signup);
        signInText = (TextView) findViewById(R.id.signInText);
        signUpText = (TextView) findViewById(R.id.signUpText);
        signInLinear= (LinearLayout) findViewById(R.id.signInLinear);
        signUpLinear= (LinearLayout) findViewById(R.id.signUpLinear);

        firebaseAuth = FirebaseAuth.getInstance();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(password.getText())==false) {
                    signIn.setEnabled(true);
                    signUp.setEnabled(true);
                } else {
                    signIn.setEnabled(false);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(email.getText())==false) {
                    signIn.setEnabled(true);
                    signUp.setEnabled(true);
                } else {
                    signIn.setEnabled(false);
                    signUp.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(SignInActivity.this)) {
                    if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText()))
                        Toast.makeText(SignInActivity.this, R.string.wrong_email_password, Toast.LENGTH_SHORT).show();
                    else {
                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignInActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else
                    Toast.makeText(SignInActivity.this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(SignInActivity.this)) {
                    if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText()))
                        Toast.makeText(SignInActivity.this, R.string.wrong_email_password, Toast.LENGTH_SHORT).show();
                    else {
                        Log.d("onClick: ", email.getText().toString());
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignInActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else
                    Toast.makeText(SignInActivity.this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInLinear.setVisibility(View.VISIBLE);
                signUpLinear.setVisibility(View.GONE);
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInLinear.setVisibility(View.GONE);
                signUpLinear.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent=new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //exit app
    }
}