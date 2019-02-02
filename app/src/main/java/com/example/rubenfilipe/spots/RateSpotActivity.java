package com.example.rubenfilipe.spots;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.rubenfilipe.spots.model.Favorite;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.example.rubenfilipe.spots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateSpotActivity extends AppCompatActivity {

    private static final String SPOTID = "SPOTID";
    private String spotId;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_spot);

        spotId = getIntent().getStringExtra(SPOTID);

        Log.d("Leitura spot to rate", spotId);

        ratingBar=findViewById(R.id.ratingBar);

        ratingBar.setRating(Float.parseFloat("3.0"));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Favorites");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=FirebaseManager.INSTANCE.getUser();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Favorite fav = postSnapshot.getValue(Favorite.class);
                    if(fav!=null && user!=null) {
                        if (user.getEmail().equals(fav.getEmail())) {
                            Button button = findViewById(R.id.AddToFavorites);
                            button.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void save(View view) {
        FirebaseManager.INSTANCE.rateSpot(spotId, Float.toString(ratingBar.getRating()));

        startActivity(new Intent(this, DashboardActivity.class));
    }

    public static Intent getIntent(Context context, String spotId) {
        Intent intent = new Intent(context, RateSpotActivity.class);
        intent.putExtra(SPOTID, spotId);
        return intent;
    }

    public void favorites(View view) {
        FirebaseManager.INSTANCE.addToFavorites(spotId, FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getUid());
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!");
        builder.setMessage(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    private void showSuccessMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success!");
        builder.setMessage(message);
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }
}
