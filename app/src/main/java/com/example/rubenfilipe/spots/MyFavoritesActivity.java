package com.example.rubenfilipe.spots;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class MyFavoritesActivity extends AppCompatActivity {
    private ArrayList<String> favorites;
    private ListView listViewFavorites;
    private ArrayAdapter<String> adapter;
    private TextView textViewMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);
        Toolbar toolbar = findViewById(R.id.toolbarFavorites);
        setSupportActionBar(toolbar);

        textViewMsg=findViewById(R.id.textViewMsg);
        listViewFavorites=findViewById(R.id.myFavorites);
        favorites=new ArrayList<>();
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                favorites);
        listViewFavorites.setAdapter(adapter);
        getMyFavorites();

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
                            startActivity(new Intent(MyFavoritesActivity.this, GuestDashboardActivity.class));
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


    private void getMyFavorites(){
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Favorites");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = FirebaseManager.INSTANCE.getUser();
                if (dataSnapshot.exists()) {
                    textViewMsg.setVisibility(View.INVISIBLE);
                    listViewFavorites.setVisibility(View.VISIBLE);
                    favorites.clear();
                    for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Favorite fav = postSnapshot.getValue(Favorite.class);
                        if (fav != null && user != null) {
                            if (user.getEmail().equals(fav.getEmail())) {
                                favorites.add(fav.getSpotId());
                                adapter.notifyDataSetChanged();
                            }

                            listViewFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                                    final int positionToRemove = position;

                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            final User user = FirebaseManager.INSTANCE.getUser();
                                            int spotIDFromUser = user.getCurrentSpotId();
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    final String userUid = FirebaseAuth.getInstance().getUid();
                                                    String f = favorites.get(position);
                                                    FirebaseDatabase.getInstance().getReference("Favorites").child(f+""+userUid).
                                                            addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    dataSnapshot.getRef().removeValue();
                                                                    //favorites.remove(position);
                                                                    showSuccess(R.string.removedFavorites);
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                   break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    break;
                                            }
                                        }
                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MyFavoritesActivity.this);
                                    if (!isFinishing()) {
                                        builder.setMessage(R.string.removeFavoritesQuestion).setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();

                                    }
                                }

                            });

                        }
                    }
                    }else{
                        textViewMsg.setText(R.string.noFavorites);
                        textViewMsg.setVisibility(View.VISIBLE);
                        listViewFavorites.setVisibility(View.INVISIBLE);
                    }

                    if (favorites.size() == 0) {
                        textViewMsg.setText(R.string.noFavorites);
                        textViewMsg.setVisibility(View.VISIBLE);
                        listViewFavorites.setVisibility(View.INVISIBLE);
                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void showSuccess(int message) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setNeutralButton(R.string.OK, null);
            builder.show();
        }
    }
}

