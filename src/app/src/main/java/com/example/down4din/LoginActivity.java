package com.example.down4din;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private LinearLayout returningUserButtons, createAccountButtons;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        returningUserButtons = findViewById(R.id.returningUser);
        createAccountButtons = findViewById(R.id.createAccount);

        mAuth = FirebaseAuth.getInstance();
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
        String emailValue = emailInput.getText().toString();
        Log.i("VALUE", emailValue);
        String passwordValue = passwordInput.getText().toString();
        mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void verifyUser(View v) {
        String emailValue = emailInput.getText().toString();
        String passwordValue = passwordInput.getText().toString();
        mAuth.signInWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
