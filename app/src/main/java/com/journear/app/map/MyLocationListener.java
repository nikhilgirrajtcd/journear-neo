package com.journear.app.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.oscim.core.GeoPoint;



public class MyLocationListener implements LocationListener {

    private LocationManager locationManager ;
    private MyLocationListener locationListener ;
    private GeoPoint onLoadMarker;

    private static final long min_distance_forupdate = 10;
    private static final long min_time_to_update = 2 * 60 * 1000;
    Location location;

    public MyLocationListener(LocationManager locationManager){
        this.locationManager = locationManager;

    }

    public MyLocationListener(){

    }

    public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {

            try {

                locationManager.requestLocationUpdates(provider, min_time_to_update, min_distance_forupdate, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    return location;

                }
            } catch (SecurityException r) {

                Log.d("loc", r.getMessage());
            }
            return location;
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location loc) {

        Log.i(  "Map", "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                + loc.getLongitude());

        System.out.println("Location printed");
        System.out.println(loc.getLongitude());
        onLoadMarker = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }


}