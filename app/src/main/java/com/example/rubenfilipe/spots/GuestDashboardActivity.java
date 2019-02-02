package com.example.rubenfilipe.spots;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rubenfilipe.spots.model.Spot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GuestDashboardActivity extends FragmentActivity implements OnMapReadyCallback {
    FirebaseAuth mAuth;
    private GoogleMap mMap;
    private int idPark=1;
    private DatabaseReference mDatabase;
    private List<Spot> spots;
    private List<Marker> markers;
    private int mHour, mMinute,mSecond;
    private TextView textViewGuestFreeSpots;
    private TextView textViewGuestBusySpots;
    private TextView textViewGuestLastUpdatedSpots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_dashboard);

        TextView txt=findViewById(R.id.textViewGuestDashboard);

        txt.setText(R.string.guestDashboardTitle);

        mAuth = FirebaseAuth.getInstance();

        spots=new ArrayList<>();
        markers=new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Spots");

        textViewGuestFreeSpots = findViewById(R.id.textViewGuestFreeSpots);
        textViewGuestBusySpots = findViewById(R.id.textViewGuestBusySpots);
        textViewGuestLastUpdatedSpots = findViewById(R.id.textViewGuestLastUpdatedSpots);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.guestDashboardMap);
        mapFragment.getMapAsync(this);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.735122,-8.820438),19));
        mMap.getUiSettings().setAllGesturesEnabled(false);

        mDatabase.addValueEventListener(new ValueEventListener() {
            //Vai buscar os valores da tabela
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                spots.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Spot spot=postSnapshot.getValue(Spot.class);
                    if(spot != null){

                        if(spot.getParkId() == idPark){
                        spots.add(spot);
                    }
                }
                textViewGuestFreeSpots.setText("Free Spots: "+getAvailableSpots());
                textViewGuestBusySpots.setText("Busy Spots: "+getUnavailableSpots());
                textViewGuestLastUpdatedSpots.setText("Last updated at:"+mHour+":"+mMinute+":"+mSecond);
                addSpotsMarker();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onClickLogin(View view) {
        startActivity(new Intent(GuestDashboardActivity.this, LoginActivity.class));
    }

    private void addSpotsMarker(){
        cleanSpotsMarkers();
        if(mMap!=null) {
            float color;
            for (Spot spot : spots) {
                if (spot.getAvailable()) {
                    color = BitmapDescriptorFactory.HUE_GREEN;
                } else {
                    color = BitmapDescriptorFactory.HUE_RED;
                }

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(spot.getLatitude(), spot.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(color)));
                markers.add(marker);
            }
        }
    }

    private int getAvailableSpots() {
        int free = 0;

        for(Spot spot: spots){
            Log.d("Leitura", Boolean.toString(spot.getAvailable()));
            if(spot.getAvailable()){
                free++;
            }
        }

        Calendar mCalendar = Calendar.getInstance();

        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);

        return free;
    }

    private int getUnavailableSpots() {
        int busy = 0;
        for (Spot spot: spots){
            if(!spot.getAvailable()){
                busy++;
            }
        }
        return busy;
    }

    private void cleanSpotsMarkers() {
        for(Marker marker:markers) {
            marker.remove();
        }
        markers.clear();
    }
}
