package com.example.rubenfilipe.spots;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rubenfilipe.spots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.PasswordAuthentication;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private static final String TAG = "ViewDatabase";

    TextView editMail;
    EditText editProfileName, editProfileAge, editProfilePassword;
    Spinner editProfilePreference;
    ImageView imageView;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;
    String userID;

    //Firebase Stuff
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;
    User userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        setTitle("My Profile");

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //editText = (EditText) findViewById(R.id.editTextDisplayName);
        //imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        //textView = (TextView) findViewById(R.id.textViewVerified);
        editMail = (TextView) findViewById(R.id.editMail);
        editProfileName = (EditText) findViewById(R.id.editProfileName);
        editProfileAge = (EditText) findViewById(R.id.editProfileAge);
        editProfilePreference = (Spinner) findViewById(R.id.editProfilePreference);
        //editPass = (EditText) findViewById(R.id.editPass);-------------------USER NOVA PASSWORD
        editProfilePassword = (EditText) findViewById(R.id.editProfilePassword);

        String[] arraySpinner = new String[] {
                "Without preference","Distance","Best rating","My favorites"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        editProfilePreference.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                showData(dataSnapshot);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });*/

        //loadUserInformation();

        findViewById(R.id.editProfileSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        userModel = dataSnapshot.child(userID).getValue(User.class);

        Log.d(TAG, "showData: name" + userModel.getName());
        Log.d(TAG, "showData: age" + userModel.getAge());
        Log.d(TAG, "showData: email" + userModel.getEmail());

        editProfileName.setText(userModel.getName());
        editProfileAge.setText(Integer.toString(userModel.getAge()));
        editMail.setText(userModel.getEmail());
        String preference =userModel.getPreference();

        if(preference==null){
            editProfilePreference.setSelection(0);
        }else{
            switch (preference){
                case "Distance":
                    editProfilePreference.setSelection(1);
                    break;
                case "Best rating":
                    editProfilePreference.setSelection(2);
                    break;
                case "My favorites":
                    editProfilePreference.setSelection(3);
                    break;
            }
        }
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
/*
    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfileActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }*/


    private void saveUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        final String password=editProfilePassword.getText().toString();
        final String displayRealName = editProfileName.getText().toString();
        final String displayAge = editProfileAge.getText().toString();
        //final String displayPassword = editPass.getText().toString();-------------------USER NOVA PASSWORD
        final String displayEmail = editMail.getText().toString();
        //String displayName = editText.getText().toString();Old field
        final String preference = editProfilePreference.getSelectedItem().toString();


        if(password.isEmpty()){
            editProfilePassword.setError("Password is required.");
            editProfilePassword.requestFocus();
            showErrorMessage(R.string.emptyPassword);
            return;
        }


        if(displayRealName.isEmpty()){
            editProfileName.setError("Name is required.");
            editProfileName.requestFocus();
            showErrorMessage(R.string.emptyName);
            return;
        }

        if(displayAge.isEmpty()){
            editProfileAge.setError("Age required");
            editProfileAge.requestFocus();
            showErrorMessage(R.string.emptyAge);
            return;
        }

        if(Integer.parseInt(displayAge)<18 || Integer.parseInt(displayAge)>99){
            editProfileAge.setError("Age not valid.(Age beetween 18 and 99)");
            editProfileAge.requestFocus();
            showErrorMessage(R.string.invalidAge);
            return;
        }



        /*
        AuthCredential credential = EmailAuthProvider.getCredential(displayEmail, oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(displayPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Update Succefully", Toast.LENGTH_LONG).show();
                            }else {
                                //Toast.makeText(ProfileActivity.this, "Update not Succefully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(ProfileActivity.this, "Update not Succefully", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        mAuth.signInWithEmailAndPassword(userModel.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //User user = new User(name, email, Integer.parseInt(age));
                    userModel = new User(displayRealName, displayEmail, Integer.parseInt(displayAge),preference,-1);

                    FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showSuccess(R.string.successfullEditProfile);
                                finish();
                                startActivity(new Intent(ProfileActivity.this
                                        , GuestDashboardActivity.class));
                            }
                        }
                    });
                } else {
                    showErrorMessage(R.string.invalidCredentials);
                }
            }
        });


        /*
        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }*/
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!");
        builder.setMessage(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    private void showSuccess(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success!");
        builder.setMessage(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
/*
    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            //profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboardMenu:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.id.menuLogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid=mAuth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("loggedIn").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(ProfileActivity.this, GuestDashboardActivity.class));
                        }
                    }
                });

                break;
            case R.id.profileID:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.statistics:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, StatisticsActivity.class));
                break;
            case R.id.favorites:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, MyFavoritesActivity.class));
                break;
        }
        return true;
    }

    /*
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }*/

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));

    }

    public void cancelButton(View view) {
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void onClickPassword(View view) {
        finish();
        startActivity(new Intent(this, PasswordActivity.class));
    }
}