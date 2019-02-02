package com.example.rubenfilipe.spots;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rubenfilipe.spots.model.Favorite;
import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.example.rubenfilipe.spots.model.HistorySpot;
import com.example.rubenfilipe.spots.model.Rate;
import com.example.rubenfilipe.spots.model.Spot;
import com.example.rubenfilipe.spots.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private Spinner parkSpinner;
    private List<Spot> spots;
    private List<Spot> spotsFree;
    private List<Spot> allSpots;
    private List<Rate> rates;
    private List<Favorite> favorites;
    private TextView textViewFreeSpots;
    private TextView textViewBusySpots;
    private TextView textViewLastUpdateSpots;
    private TextView textViewCurrentSpotId;

    private TextView txtLatLng;
    private List<Marker> markers;
    private Spot spot;
    private int mHour, mMinute, mSecond;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    private LatLng latLng;
    private Location location;
    double longitude;
    double latitude;
    private Marker myLocation;
    private Polyline polyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        spots = new ArrayList<>();
        spotsFree = new ArrayList<>();
        markers = new ArrayList<>();
        allSpots = new ArrayList<>();
        rates = new ArrayList<>();
        favorites = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Spots");

        setTitle(R.string.dashboardTitle);

        parkSpinner = findViewById(R.id.parkSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.parks_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parkSpinner.setAdapter(adapter);


        TextView txt = findViewById(R.id.textViewDashboardMap);
        textViewFreeSpots = findViewById(R.id.textViewFreeSpots);
        textViewBusySpots = findViewById(R.id.textViewBusySpots);
        textViewLastUpdateSpots = findViewById(R.id.textViewLastUpdatedSpots);
        textViewCurrentSpotId = findViewById(R.id.textViewCurrentSpotId);

        txtLatLng = findViewById(R.id.txtLatLng);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//not working yet

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.userLoggedDashBoardMap);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbar);

        parkSpinner.setOnItemSelectedListener(this);

        //My spot
        FirebaseManager.INSTANCE.getUserCurrentSpot();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        getMyLocation();
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
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
            case R.id.menuLogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("loggedIn").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(DashboardActivity.this, GuestDashboardActivity.class));
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.getUiSettings().setZoomGesturesEnabled(false);//disable zoom
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


    public void getMyLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (myLocation != null) {
                        myLocation.remove();
                        myLocation = null;
                    }
                    if (mMap != null) {
                        myLocation = mMap.addMarker(new MarkerOptions().position(latLng).title("here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                    txtLatLng.setText("" + "LatLng:" + latLng);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String textSelected = parent.getItemAtPosition(position).toString();
        TextView tv2 = findViewById(R.id.textViewDashboardMap);
        tv2.setText("Park selected " + textSelected);

        getParkSpots(textSelected);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        TextView tv = findViewById(R.id.textViewSelectAPark);
        tv.setText("Select a Park");
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private void getParkSpots(String park) {
        final String parkName = park;
        mDatabase.addValueEventListener(new ValueEventListener() {
            //Vai buscar os valores da tabela
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long idPark = 1;
                LatLng coordenatesPark = new LatLng(39.735122, -8.820438);
                switch (parkName) {
                    case "Parque A":
                        idPark = 1;
                        coordenatesPark = new LatLng(39.735122, -8.820438);
                        break;
                    case "Parque B":
                        coordenatesPark = new LatLng(39.733856, -8.821231);
                        idPark = 2;
                        break;
                    case "Parque C":
                        coordenatesPark = new LatLng(39.732870, -8.822175);
                        idPark = 3;
                        break;
                }


                spots.clear();
                allSpots.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = postSnapshot.getValue(Spot.class);

                    if (spot.getParkId() == idPark) {
                        spots.add(spot);
                    }

                    if (spot.getAvailable()) {
                        spotsFree.add(spot);
                    }
                    allSpots.add(spot);
                }

                LatLng latCurrentPosition = new LatLng(latitude, longitude);

                User user = FirebaseManager.INSTANCE.getUser();


                for (Spot spot : allSpots) {
                    LatLng latLngSpot = new LatLng(spot.getLatitude(), spot.getLongitude());
                    if (user != null) {

                    }
                    if (SphericalUtil.computeDistanceBetween(latLngSpot, latCurrentPosition) < 5
                            && !spot.getAvailable() && user.getCurrentSpotId() == -1) {
                        parkQuestion(spot.getId());
                    }

                    if (user != null && user.getCurrentSpotId() != -1 && user.getCurrentSpotId() == spot.getId() && spot.getAvailable()) {
                        leaveQuestion(spot.getId());
                    }


                }

                textViewFreeSpots.setText("Free Spots: " + getAvailableSpots());
                textViewBusySpots.setText("Busy Spots: " + getUnavailableSpots());
                textViewLastUpdateSpots.setText("Last updated at:" + mHour + ":" + mMinute + ":" + mSecond);
                if (FirebaseManager.INSTANCE.getUser() != null) {
                    textViewCurrentSpotId.setText("Parked at: " + FirebaseManager.INSTANCE.getUser().getCurrentSpotId());
                } else {
                    textViewCurrentSpotId.setText("Parked at: -1");

                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenatesPark, 18));
                addSpotsMarker();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rates.clear();
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Rates");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Rate rate = postSnapshot.getValue(Rate.class);
                    rates.add(rate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        favorites.clear();
        mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Favorites");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = FirebaseManager.INSTANCE.getUser();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Favorite fav = postSnapshot.getValue(Favorite.class);

                        if (fav != null && user != null && user.getEmail().equals(fav.getEmail())) {
                            System.out.println("estoirou aqui fav"+ fav.getEmail() );
                            System.out.println("estoirou aqui user "+ user.getEmail());
                            favorites.add(fav);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private int getUnavailableSpots() {
        int busy = 0;
        for (Spot spot : spots) {
            if (!spot.getAvailable()) {
                busy++;
            }
        }
        return busy;
    }

    private void leaveQuestion(final int spotId) {
        //final Spinner parkSpinner = findViewById(R.id.parkSpinner);
        //final Spot spot = FirebaseManager.INSTANCE.getCurrentSpot();

        //if (spot != null) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = FirebaseManager.INSTANCE.getUser();
                int spotIDFromUser = user.getCurrentSpotId();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        FirebaseManager.INSTANCE.leaveSpot();
                        getParkSpots(parkSpinner.getSelectedItem().toString());
                        startActivity(RateSpotActivity.getIntent(DashboardActivity.this, "Spot" + spotIDFromUser));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        if (!isFinishing()) {
            builder.setMessage(R.string.leaveSpotQuestion).setPositiveButton("Yes", dialogClickListener).setTitle("Spot" + spotId)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        //}

    }

    private void parkQuestion(final int spotId) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        FirebaseManager.INSTANCE.setUserCurrentSpot(spotId);
                        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //Date date = new Date();
                        //FirebaseDatabase.getInstance().getReference("HistorySpot").child(dateFormat.format(date)).setValue(new HistorySpot(false, spotId, dateFormat.format(date),theSpot.getParkId()));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        if (!isFinishing()) {
            builder.setMessage(R.string.takeSpotQuestion).setPositiveButton("Yes", dialogClickListener).setTitle("Spot" + spotId)
                    .setNegativeButton("No", dialogClickListener).show();
        }

    }

    private int getAvailableSpots() {
        int free = 0;

        for (Spot spot : spots) {
            if (spot.getAvailable()) {
                free++;
            }
        }

        Calendar mCalendar = Calendar.getInstance();

        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);

        return free;
    }

    private void addSpotsMarker() {
        cleanSpotsMarkers();
        if (mMap != null) {
            float color;
            for (Spot spot : spots) {
                Log.d("Spot do park", spot.toString());
                if (spot.getAvailable()) {
                    //color = BitmapDescriptorFactory.HUE_GREEN;
                    Marker marker = mMap.addMarker(new MarkerOptions().title("Parque").snippet("ESTG").position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("green", 50, 50))));
                    markers.add(marker);
                } else {
                    Marker marker = null;
                    if (FirebaseManager.INSTANCE.getUser().getCurrentSpotId() != -1 && FirebaseManager.INSTANCE.getUser().getCurrentSpotId() == spot.getId()) {
                        marker = mMap.addMarker(new MarkerOptions().title("Parque").snippet("ESTG").position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("current_spot", 75, 75))));
                        markers.add(marker);
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().title("Parque").snippet("ESTG").position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("red", 50, 50))));
                        markers.add(marker);
                    }
                }
            }
        }
    }

    private void cleanSpotsMarkers() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    public void mySpot(View view) {
        final Spinner parkSpinner = findViewById(R.id.parkSpinner);
        final Spot spot = FirebaseManager.INSTANCE.getCurrentSpot();

        if (spot != null) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final User user = FirebaseManager.INSTANCE.getUser();
                    int spotIDFromUser = user.getCurrentSpotId();
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            FirebaseManager.INSTANCE.leaveSpot();
                            getParkSpots(parkSpinner.getSelectedItem().toString());

                            startActivity(RateSpotActivity.getIntent(DashboardActivity.this, "Spot" + spotIDFromUser));
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.leaveSpotQuestion).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            double bestDistance = 9999999;
                            Spot theSpot = null;
                            for (Spot spot : allSpots) {
                                LatLng latCurrentPosition = new LatLng(latitude, longitude);
                                LatLng latLngSpot = new LatLng(spot.getLatitude(), spot.getLongitude());
                                if (SphericalUtil.computeDistanceBetween(latLngSpot, latCurrentPosition) < bestDistance && spot.getAvailable()) {
                                    bestDistance = SphericalUtil.computeDistanceBetween(latLngSpot, latCurrentPosition);
                                    theSpot = spot;
                                }
                            }

                            if (theSpot != null) {
                                FirebaseManager.INSTANCE.changeSpotAvailable("Spot" + theSpot.getId(), false);
                                FirebaseManager.INSTANCE.setUserCurrentSpot(theSpot.getId());

                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();
                                FirebaseDatabase.getInstance().getReference("HistorySpot").child(dateFormat.format(date)).setValue(new HistorySpot(false, theSpot.getId(), dateFormat.format(date), theSpot.getParkId()));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                                builder.setMessage("No spots available!");
                                builder.setNeutralButton(R.string.OK, null);
                                builder.show();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.takeSpotQuestion).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

    }

    public void findMeSpot(View view) {
        User user = FirebaseManager.INSTANCE.getUser();
        Spot theSpot = null;
        LatLng latCurrentPosition = new LatLng(latitude, longitude);
        double bestDistance = 9999999;
        if (user != null) {
            switch (user.getPreference()) {
                case "Distance":
                    for (Spot spot : allSpots) {
                        LatLng latLngSpot = new LatLng(spot.getLatitude(), spot.getLongitude());
                        if (SphericalUtil.computeDistanceBetween(latLngSpot, latCurrentPosition) < bestDistance && spot.getAvailable()) {
                            bestDistance = SphericalUtil.computeDistanceBetween(latLngSpot, latCurrentPosition);
                            theSpot = spot;
                        }
                    }
                    if (theSpot != null) {
                        askFindMeASpot(user, theSpot);
                    } else {
                        noSpots();
                    }
                    break;
                case "Best rating":
                    double bestRate = 0;
                    for (Spot spot : allSpots) {
                        double totalRate = 0;
                        int count = 0;
                        for (Rate rate : rates) {
                            if (rate.getSpotId().equals("Spot" + spot.getId()) && spot.getAvailable()) {
                                totalRate += rate.getValue();
                            }
                            count++;

                        }
                        totalRate /= count;
                        if (totalRate > bestRate) {
                            bestRate = totalRate;
                            theSpot = spot;

                        }
                    }
                    if (theSpot != null) {
                        askFindMeASpot(user, theSpot);
                    } else {
                        noSpots();
                    }
                    break;
                case "My favorites":
                    Random r = new Random();
                    int min = 0;
                    int max = favorites.size() - 1;
                    int i1 = r.nextInt(max - min + 1) + min;
                    int haveFav = 1;
                    if (favorites.size() == 0) {
                        noSpots();
                        haveFav = 0;
                    } else {
                        for (Spot spot : allSpots) {
                            Favorite f = favorites.get(i1);
                            if (("Spot" + spot.getId()).equals(f.getSpotId()) && spot.getAvailable()) {
                                theSpot = spot;
                            }
                        }
                    }

                    if (theSpot == null && haveFav == 1) {
                        noSpots();
                    } else {
                        askFindMeASpot(user, theSpot);
                    }
                    break;
                default:
                    askChangePreference();

            }

        }
    }

    private void noSpots() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setMessage("No spots available!");
        builder.setNeutralButton(R.string.OK, null);
        builder.show();
    }

    public void askFindMeASpot(User user, final Spot theSpot) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        polyline = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(latitude, longitude),
                                        new LatLng(theSpot.getLatitude(), theSpot.getLongitude())));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Preference: " + user.getPreference() + " Spot: Spot" + theSpot.getId() + ", accept the spot?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void askChangePreference() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.preferenceChangeQuestion).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
