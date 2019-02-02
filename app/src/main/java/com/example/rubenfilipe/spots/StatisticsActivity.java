package com.example.rubenfilipe.spots;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rubenfilipe.spots.model.AlgorithmDuration;
import com.example.rubenfilipe.spots.model.Favorite;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.example.rubenfilipe.spots.model.Rate;
import com.example.rubenfilipe.spots.model.Spot;
import com.example.rubenfilipe.spots.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity {
    private TextView txtRegisteredUsers, txtAuthenticatedUsers;
    private  List<Rate> spots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.toolbarStatistics);
        setSupportActionBar(toolbar);

        setTitle("Statistics");


        txtRegisteredUsers=findViewById(R.id.textViewRegisteredUsers);
        txtAuthenticatedUsers=findViewById(R.id.textViewAuthenticatedUsers);

        spots=new ArrayList<>();

        getRegisteredUsers();
        getAuthtenticatedUsers();
        getTop5SpotsByRate();
        getTop5FavoritesSpots();
        getAvaregeByAlgoritm("Distance");
        getTop5MustUsedSpots();
    }


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
                            startActivity(new Intent(StatisticsActivity.this, GuestDashboardActivity.class));
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

    public void getRegisteredUsers(){
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    count++;
                }
                txtRegisteredUsers.setText("Registered users: "+count);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAuthtenticatedUsers(){
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if(user.getLoggedIn()){
                        count++;
                    }
                }

                txtAuthenticatedUsers.setText("Authenticated users: "+count);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getTop5SpotsByRate(){
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Spots");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                Double value=0.0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = postSnapshot.getValue(Spot.class);
                    Rate rate = new Rate("Spot"+spot.getId(),0.0);
                    spots.add(rate);
                }

                getRates();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRates(){
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Rates");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count=0;
                //Vai buscar os rates e incrementa ao valor no spot correspondente
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Rate rate = postSnapshot.getValue(Rate.class);
                    for(int i=0;i<spots.size();i++){
                        Rate r=spots.get(i);
                        if(r.getSpotId().equals(rate.getSpotId())){
                            double v=r.getValue();
                            v+=rate.getValue();
                            r.setValue(v);
                            spots.remove(i);
                            spots.add(i, r);
                            count++;
                        }
                    }
                }

                //media
                for(int i=0;i<spots.size();i++) {
                    Rate rate = spots.get(i);
                    rate.setValue(rate.getValue()/count);
                    spots.remove(i);
                    spots.add(i,rate);

                }
                for(int i=0;i<spots.size();i++) {
                    Rate rate = spots.get(i);
                    Log.d("RATES", rate.getSpotId()+" "+rate.getValue());
                }


                Collections.sort(spots, new Comparator<Rate>() {
                    @Override
                    public int compare(Rate o1, Rate o2) {
                        return Double.compare(o2.getValue(),o1.getValue());
                    }
                });

                for(int i=0;i<spots.size();i++) {
                    Rate rate = spots.get(i);
                    Log.d("RATES", rate.getSpotId()+" "+rate.getValue());
                }

                TextView txtViewSpot = findViewById(R.id.textViewSpot1);
                txtViewSpot.setText(spots.get(0).getSpotId());

                txtViewSpot = findViewById(R.id.textViewSpot2);
                txtViewSpot.setText(spots.get(1).getSpotId());

                txtViewSpot = findViewById(R.id.textViewSpot3);
                txtViewSpot.setText(spots.get(2).getSpotId());

                txtViewSpot = findViewById(R.id.textViewSpot4);
                txtViewSpot.setText(spots.get(3).getSpotId());

                txtViewSpot = findViewById(R.id.textViewSpot5);
                txtViewSpot.setText(spots.get(4).getSpotId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTop5FavoritesSpots() {
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Spots");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                Double value = 0.0;
                spots.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = postSnapshot.getValue(Spot.class);
                    Rate rate = new Rate("Spot" + spot.getId(), 0.0);
                    spots.add(rate);
                }

                getFavorites();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFavorites(){

        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Favorites");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Vai buscar os favoritos e incrementa ao valor no spot correspondente
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Favorite favorite = postSnapshot.getValue(Favorite.class);
                    for(int i=0;i<spots.size();i++){
                        Rate r=spots.get(i);
                        if(r.getSpotId().equals(favorite.getSpotId())){
                            double v=r.getValue();
                            v++;
                            r.setValue(v);
                            spots.remove(i);
                            spots.add(i, r);
                        }
                    }
                }

                //Ordena a lista pelo valor
                Collections.sort(spots, new Comparator<Rate>() {
                    @Override
                    public int compare(Rate o1, Rate o2) {
                        return Double.compare(o2.getValue(),o1.getValue());
                    }
                });

                for(int i=0;i<spots.size();i++) {
                    Rate rate = spots.get(i);
                    Log.d("FAVORITOS", rate.getSpotId()+" "+rate.getValue());
                }

                TextView txtViewSpot = findViewById(R.id.textViewFavSpot1);
                txtViewSpot.setText(spots.get(0).getSpotId());

                txtViewSpot = findViewById(R.id.textViewFavSpot2);
                txtViewSpot.setText(spots.get(1).getSpotId());

                txtViewSpot = findViewById(R.id.textViewFavSpot3);
                txtViewSpot.setText(spots.get(2).getSpotId());

                txtViewSpot = findViewById(R.id.textViewFavSpot4);
                txtViewSpot.setText(spots.get(3).getSpotId());

                txtViewSpot = findViewById(R.id.textViewFavSpot5);
                txtViewSpot.setText(spots.get(4).getSpotId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAvaregeByAlgoritm(final String algorithm){
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("DurationByAlgorithm");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<AlgorithmDuration> algDuration=new ArrayList<>();
                algDuration.add(new AlgorithmDuration("(ms)Distance", 0.0));
                algDuration.add(new AlgorithmDuration("(ms)Best Rating", 0.0));
                algDuration.add(new AlgorithmDuration("(ms)My Favorites", 0.0));

                int count=0;
                //Vai buscar as medias e incrementa ao valor no spot correspondente
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AlgorithmDuration algD = postSnapshot.getValue(AlgorithmDuration.class);
                    for(int i=0;i<algDuration.size();i++){
                        AlgorithmDuration r=algDuration.get(i);
                        if(r.getAlgorithm().equals(algD.getAlgorithm())){
                                double v=r.getSeconds();
                                v+=algD.getSeconds();
                                r.setSeconds(v);
                                algDuration.remove(i);
                                algDuration.add(i, r);
                                count++;
                            }
                        }
                    }

                    //media
                    for(int i=0;i<algDuration.size();i++) {
                        AlgorithmDuration r=algDuration.get(i);
                        r.setSeconds(r.getSeconds()/count);
                        algDuration.remove(i);
                        algDuration.add(i,r);

                    }

                TextView txtViewSpot = findViewById(R.id.textViewClosestSpot);
                txtViewSpot.setText(algDuration.get(0).getAlgorithm()+": "+ String.format( "%.2f",algDuration.get(0).getSeconds()));

                txtViewSpot = findViewById(R.id.textViewMyFavoritesSpot);
                txtViewSpot.setText(algDuration.get(2).getAlgorithm()+": "+ String.format( "%.2f",algDuration.get(2).getSeconds()));

                txtViewSpot = findViewById(R.id.textViewRatesSpot);
                txtViewSpot.setText(algDuration.get(1).getAlgorithm()+": "+ String.format( "%.2f",algDuration.get(1).getSeconds()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTop5MustUsedSpots(){

        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Spots");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Vai buscar os spots
                int count=0;
                List<Spot> spotsUsed=new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = postSnapshot.getValue(Spot.class);
                    spotsUsed.add(spot);
                    count++;
                }

                //Ordena a lista pelo valor
                Collections.sort(spotsUsed, new Comparator<Spot>() {
                    @Override
                    public int compare(Spot o1, Spot o2) {
                        return Integer.compare(o2.getCount(),o1.getCount());
                    }
                });



                TextView txtViewSpot = findViewById(R.id.textViewMustUsedSpot1);
                txtViewSpot.setText("Spot"+spotsUsed.get(0).getId());

                txtViewSpot = findViewById(R.id.textViewMustUsedSpot2);
                txtViewSpot.setText("Spot"+spotsUsed.get(1).getId());

                txtViewSpot = findViewById(R.id.textViewMustUsedSpot3);
                txtViewSpot.setText("Spot"+spotsUsed.get(2).getId());

                txtViewSpot = findViewById(R.id.textViewMustUsedSpot4);
                txtViewSpot.setText("Spot"+spotsUsed.get(3).getId());

                txtViewSpot = findViewById(R.id.textViewMustUsedSpot5);
                txtViewSpot.setText("Spot"+spotsUsed.get(4).getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openGraphsActivity(View view) {
        startActivity(new Intent(this, StatisticsParkActivity.class));
    }
}
