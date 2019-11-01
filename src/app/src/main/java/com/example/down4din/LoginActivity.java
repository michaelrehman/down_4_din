package com.example.down4din;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private LinearLayout returningUserButtons, createAccountButtons;
    private boolean isPassVisible;

    private FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        returningUserButtons = findViewById(R.id.returningUser);
        createAccountButtons = findViewById(R.id.createAccount);
        mAuth = FirebaseAuth.getInstance();

        isPassVisible = false;
        //Allows the user to see their password
        //by clcicking the eye icon in the password field
        passwordInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (passwordInput.getRight() -passwordInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if(isPassVisible) {
                            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            isPassVisible = false;
                        }
                        else {
                            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                            isPassVisible = true;
                        }

                        return true;
                    }
                }
                return false;
            }
        });

//        passwordInput.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    if(event.getX() >= (passwordInput.getWidth() - passwordInput
//                            .getCompoundDrawables()[2].getBounds().width())) {
//                        if (passwordInput.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
//                            passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                        } else {
//                            passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }

    public void switchButtons(View v) {
        if (createAccountButtons.getVisibility() == View.GONE) {
            returningUserButtons.setVisibility(View.GONE);
            createAccountButtons.setVisibility(View.VISIBLE);
        } else {
            returningUserButtons.setVisibility(View.VISIBLE);
            createAccountButtons.setVisibility(View.GONE);
        }
    }

    public void createAccount(View v) {
        final String emailValue = emailInput.getText().toString().trim();
        final String passwordValue = passwordInput.getText().toString().trim();
        if (validateInputs(emailValue, passwordValue)) {
            if (passwordValue.length() >= 6) {
                mAuth.createUserWithEmailAndPassword(emailValue.trim(), passwordValue.trim())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Account creation failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Password must be 6 or more characters",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    public void verifyUser(View v) {
        String emailValue = emailInput.getText().toString().trim();
        String passwordValue = passwordInput.getText().toString().trim();
        if (validateInputs(emailValue, passwordValue)) {
            mAuth.signInWithEmailAndPassword(emailValue, passwordValue)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
								startActivity(new Intent(getApplicationContext(), MainActivity.class));
								playSound();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputs(String email, String password) {
        return !(email.equals("") || password.equals(""));
    }

    private void playSound() {
        MediaPlayer mediaPlayer= MediaPlayer.create(LoginActivity.this,R.raw.noisething);
        mediaPlayer.start();
    }
}
