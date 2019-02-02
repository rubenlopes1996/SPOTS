package com.example.rubenfilipe.spots;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rubenfilipe.spots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordActivity extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    EditText editOldPass, editNewPass;
    String userID;

    //Firebase Stuff
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        userID = user.getUid();
        myRef = database.getReference("Users");
        editOldPass = (EditText) findViewById(R.id.editOldPass);
        editNewPass = (EditText) findViewById(R.id.editNewPass);
        setTitle("Change Password");

        //Autentication
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    userID = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



        findViewById(R.id.passBtnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void saveUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        final String mail = user.getEmail();
        final String oldPassword = editOldPass.getText().toString();
        final String newPassword = editNewPass.getText().toString();

        if(oldPassword.isEmpty()){
            editOldPass.setError("Password is required.");
            editOldPass.requestFocus();
            showErrorMessage(R.string.emptyPassword);
            return;
        }

        if(newPassword.isEmpty()){
            editNewPass.setError("Password is required.");
            editNewPass.requestFocus();
            showErrorMessage(R.string.emptyPassword);
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(mail, oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                showErrorMessage(R.string.successfullEditProfile);
                                finish();
                                startActivity(new Intent(PasswordActivity.this
                                        , GuestDashboardActivity.class));
                            }
                        }
                    });
                }else {
                    showErrorMessage(R.string.invalidCredentials);
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
    public void onBackPressed() {
        startActivity(new Intent(PasswordActivity.this, DashboardActivity.class));

    }

    public void passBtnCancel(View view) {
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

}
