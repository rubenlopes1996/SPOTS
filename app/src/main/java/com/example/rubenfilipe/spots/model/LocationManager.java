package com.example.rubenfilipe.spots.model;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public enum LocationManager {
    INSTANCE;
    private LocationListener locationListener;
    private Double latitude;
    private Double longitude;

    LocationManager() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    longitude=location.getLongitude();
                    latitude=location.getLatitude();

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
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
