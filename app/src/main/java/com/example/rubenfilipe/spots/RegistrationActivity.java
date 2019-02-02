package com.example.rubenfilipe.spots;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.example.rubenfilipe.spots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private EditText editTextEmail, editTextPassword, editTextName, editTextAge, editTextPasswordVerification;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextPasswordVerification = (EditText) findViewById(R.id.editTextPasswordVerification);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString();
        final String passwordVerification = editTextPasswordVerification.getText().toString();
        final String age = editTextAge.getText().toString();

        if (email.isEmpty()) {
            showErrorMessage(R.string.emptyFields);
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorMessage(R.string.invalidEmail);
            editTextEmail.setError("Invalid email. The email should be of type email@domain.com");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showErrorMessage(R.string.emptyFields);
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            showErrorMessage(R.string.invalidLenghtPassword);
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        if (passwordVerification.isEmpty()) {
            showErrorMessage(R.string.emptyFields);
            editTextPasswordVerification.setError("Password verification is required");
            editTextPasswordVerification.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            showErrorMessage(R.string.emptyFields);
            editTextAge.setError("Age is required");
            editTextAge.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            showErrorMessage(R.string.emptyFields);
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (!password.equals(passwordVerification)) {
            showErrorMessage(R.string.passwordMismatch);
            editTextPasswordVerification.setError("Password and password validation should match");
            editTextPasswordVerification.requestFocus();
            return;
        }

        if (Integer.parseInt(age) > 99 || Integer.parseInt(age) < 18) {
            showErrorMessage(R.string.invalidAge);
            editTextAge.setError("Please introduce a valid age. Age should be between 18-99");
            editTextAge.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(name, email, Integer.parseInt(age), null, -1);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration sucefully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    finish();
                    startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        showErrorMessage(R.string.emailAlreadyUsed);
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!");
        builder.setMessage(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
