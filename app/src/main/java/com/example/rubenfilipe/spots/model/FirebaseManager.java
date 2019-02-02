package com.example.rubenfilipe.spots.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rubenfilipe.spots.DashboardActivity;
import com.example.rubenfilipe.spots.GuestDashboardActivity;
import com.example.rubenfilipe.spots.ProfileActivity;
import com.example.rubenfilipe.spots.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public enum FirebaseManager {
    INSTANCE;

    private User user;
    private Spot spot=null;
    private FirebaseAuth mAuth;

    FirebaseManager() {
        this.mAuth=FirebaseAuth.getInstance();

    }

    public void removeUserByEmail(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().delete();
                }
            }
        });
    }

    public void logout() {
        mAuth.signOut();
    }

    public void getUserCurrentSpot() {
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Users");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                    Log.d("Leitura", user.toString());

                    getSpot(user.getCurrentSpotId());
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }


    public void getSpot(final int spotId){
        DatabaseReference mDatabase=FirebaseDatabase.
                getInstance().
                getReference("Spots");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spot = dataSnapshot.child("Spot"+spotId).getValue(Spot.class);
                if(spot==null){
                    spot=null;
                }
                else{
                    Log.d("Leitura","Eu não sou null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public User getUser(){
        return user;
    }
    public Spot getCurrentSpot() {
        return spot;
    }

    public void leaveSpot() {
       spot.setAvailable(true);
       FirebaseDatabase.getInstance().getReference("Spots")
                .child("Spot"+user.getCurrentSpotId())
                .setValue(spot)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("Leitura", "spot livre");
                    }
                }
            });

        user.setCurrentSpotId(-1);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Leitura", "User leave the spot");
                            spot=null;
                        }
                    }
                });
       }


    public void setUserCurrentSpot(int spotId) {
        user.setCurrentSpotId(spotId);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Leitura", "User occuped the spot");
                            getSpot(user.getCurrentSpotId());
                        }
                    }
                });

    }

    public void rateSpot(String spotId, String rating) {
        Double rateValue=Double.parseDouble(rating);
        Rate rate= new Rate(spotId, rateValue);

        FirebaseDatabase.getInstance().getReference("Rates").child(md5(new Date().toString()))
                .setValue(rate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Leitura", "A rate created");
                        }
                    }
                });
    }


    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public void changeSpotAvailable(String spotId, boolean available) {
        FirebaseDatabase.getInstance().getReference("Spots").child(spotId)
                .child("available").setValue(available)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    public void changeUserCurrentSpotId(int spotId) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("currentSpotId").setValue(spotId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    public void setUserPreference(String arg1) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .child("preference").setValue(arg1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    public void takeSpot(String arg2) {
        FirebaseDatabase.getInstance().getReference("Spots").child(arg2)
                .child("available").setValue(false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    public void setSpotLocation(String spotId, double lat, double longi) {
        FirebaseDatabase.getInstance().getReference("Spots").child(spotId)
                .child("latitude").setValue(lat)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });

        FirebaseDatabase.getInstance().getReference("Spots").child(spotId)
                .child("longitude").setValue(longi)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });}

    public void addToFavorites(final String spotId, final String email, final String uid) {
        /*Favorite favorite= new Favorite(spotId, email);

        FirebaseDatabase.getInstance().getReference("Favorites").child(md5(new Date().toString()))
                .setValue(favorite)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Leitura", "Favorite created");
                        }
                    }
                });*/


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Favorites");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.hasChild(spotId + uid)) {
                    FirebaseDatabase.getInstance().getReference("Favorites").child(spotId + uid).child("spotId").setValue(spotId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });
                    FirebaseDatabase.getInstance().getReference("Favorites").child(spotId + uid).child("email").setValue(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Leitura", "A favorite spot added");
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void clearMyFavorites(final String email) {
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Favorites");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Favorite fav = postSnapshot.getValue(Favorite.class);
                        if (fav.getEmail() != null && fav.getEmail().equals(email)) {
                            postSnapshot.getRef().removeValue();
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void logoutAllUsers() {
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Users");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String uid=postSnapshot.getKey();
                        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("loggedIn").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    
                                }
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
