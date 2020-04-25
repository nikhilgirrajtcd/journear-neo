package com.journear.app.map.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.journear.app.R;
import com.journear.app.map.fragments.Dialog;
import com.journear.app.map.map.Destination;
import com.journear.app.map.map.MapHandler;
import com.journear.app.map.map.Navigator;
import com.journear.app.map.map.Tracking;
import com.journear.app.map.navigator.NaviEngine;
import com.journear.app.map.navigator.NaviText;
import com.journear.app.map.utils.SetStatusBarColor;
import com.journear.app.map.utils.Variable;
import com.villoren.android.kalmanlocationmanager.lib.KalmanLocationManager;
import com.villoren.android.kalmanlocationmanager.lib.KalmanLocationManager.UseProvider;

import org.oscim.android.MapView;
import org.oscim.core.GeoPoint;

import java.io.File;
import java.lang.reflect.Array;

/**
 * This file is part of PocketMaps
 * <p/>
 * Created by GuoJunjun <junjunguo.com> on July 04, 2015.
 */
public class MapNewActivity extends Activity implements LocationListener {
    enum PermissionStatus { Enabled, Disabled, Requesting, Unknown };
    private MapView mapView;
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;
    private static Location mCurrentLocation;
    private static boolean mapAlive = false;
    private Location mLastLocation;
    private MapActions mapActions;
    private LocationManager locationManager;
    private KalmanLocationManager kalmanLocationManager;
    private PermissionStatus locationListenerStatus = PermissionStatus.Unknown;
    private String lastProvider;
    /**
     * Request location updates with the highest possible frequency on gps.
     * Typically, this means one update per second for gps.
     */
    private static final long GPS_TIME = 1000;
    /**
     * For the network provider, which gives locations with less accuracy (less reliable),
     * request updates every 5 seconds.
     */
    private static final long NET_TIME = 5000;
    /**
     * For the filter-time argument we use a "real" value: the predictions are triggered by a timer.
     * Lets say we want ~25 updates (estimates) per second = update each 40 millis (to make the movement fluent).
     */
    private static final long FILTER_TIME = 40;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NaviText.initTextList(this);
        lastProvider = null;
        setContentView(R.layout.activity_map);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        kalmanLocationManager = new KalmanLocationManager(this);
        kalmanLocationManager.setMaxPredictTime(10000);
        Variable.getVariable().setContext(getApplicationContext());
        mapView = new MapView(this);
        mapView.setClickable(true);
        MapHandler.getMapHandler()
                .init(mapView, "europe_ireland-and-northern-ireland", new File("/storage/emulated/0/Download/graphhopper/maps"));
        try
        {
            File mapFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "/graphhopper/maps/");
            File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            if (requestForPermission()) {
                //File[] dirFiles = f.listFiles();
                mapFolder.setReadable(true);
                mapFolder.setWritable(true);
                File[] dirFiles = mapFolder.listFiles();
                //Log.i("message",dirFiles.toString());


            }
            String currentArea = "europe_ireland-and-northern-ireland";
            Boolean n = new File(mapFolder.getAbsolutePath(),
                    currentArea + "-gh").exists();
            //logUser(Variable.getVariable().getMapsFolder().toString());

          MapHandler.getMapHandler().loadMap(new File(mapFolder.getAbsolutePath(),
                currentArea + "-gh"), this);
          log("came herer");
          getIntent().putExtra("com.journear.app.map.activities.MapNewActivity.SELECTNEWMAP", false);
        }
        catch (Exception e)
        {
          logUser("Map file seems corrupt!\nPlease try to re-download.");
          log("Error while loading map!");
            log(e.toString());
          e.printStackTrace();
          finish();
          Intent intent = new Intent(this, MainActivity.class);
          intent.putExtra("com.journear.app.map.activities.MapNewActivity.SELECTNEWMAP", true);
          startActivity(intent);
          return;
        }
        customMapView();
        checkGpsAvailability();
        ensureLastLocationInit();
        updateCurrentLocation(null);
        mapAlive = true;
    }




    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }
    public void ensureLocationListener(boolean showMsgEverytime)
    {
      if (locationListenerStatus == PermissionStatus.Disabled) { return; }
      if (locationListenerStatus != PermissionStatus.Enabled)
      {
        boolean f_loc = Permission.checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, this);
        if (!f_loc)
        {
          if (locationListenerStatus == PermissionStatus.Requesting)
          {
            locationListenerStatus = PermissionStatus.Disabled;
            return;
          }
          locationListenerStatus = PermissionStatus.Requesting;
          String[] permissions = new String[2];
          permissions[0] = android.Manifest.permission.ACCESS_FINE_LOCATION;
          permissions[1] = android.Manifest.permission.ACCESS_COARSE_LOCATION;
          Permission.startRequest(permissions, false, this);
          return;
        }
      }
      try
      {
        if (Variable.getVariable().isSmoothON()) {
          locationManager.removeUpdates(this);
          kalmanLocationManager.requestLocationUpdates(UseProvider.GPS, FILTER_TIME, GPS_TIME, NET_TIME, this, false);
          lastProvider = KalmanLocationManager.KALMAN_PROVIDER;
          logUser("LocationProvider: " + lastProvider);
        } else {
          kalmanLocationManager.removeUpdates(this);
          Criteria criteria = new Criteria();
          criteria.setAccuracy(Criteria.ACCURACY_FINE);
          String provider = locationManager.getBestProvider(criteria, true);
          if (provider == null) {
            lastProvider = null;
            locationManager.removeUpdates(this);
            logUser("LocationProvider is off!");
            return;
          } else if (provider.equals(lastProvider)) {
            if (showMsgEverytime) {
              logUser("LocationProvider: " + provider);
            }
            return;
          }
          locationManager.removeUpdates(this);
          lastProvider = provider;
          locationManager.requestLocationUpdates(provider, 3000, 5, this);
          logUser("LocationProvider: " + provider);
        }
        locationListenerStatus = PermissionStatus.Enabled;
      }
      catch (SecurityException ex)
      {
        logUser("Location_Service not allowed by user!");
      }
    }

    /**
     * inject and inflate activity map content to map activity context and bring it to front
     */
    private void customMapView() {
        ViewGroup inclusionViewGroup = (ViewGroup) findViewById(R.id.custom_map_view_layout);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_map_content, null);
        inclusionViewGroup.addView(inflate);

        inclusionViewGroup.getParent().bringChildToFront(inclusionViewGroup);
        new SetStatusBarColor().setSystemBarColor(findViewById(R.id.statusBarBackgroundMap),
                getResources().getColor(R.color.my_primary_dark_transparent), this);
        mapActions = new MapActions(this, mapView);
    }

    /**
     * check if GPS enabled and if not send user to the GSP settings
     */
    private void checkGpsAvailability() {
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Dialog.showGpsSelector(this);
        }
    }

    /**
     * Updates the users location based on the location
     *
     * @param location Location
     */
    private void updateCurrentLocation(Location location) {
        if (location != null) {
            mCurrentLocation = location;
        } else if (mLastLocation != null && mCurrentLocation == null) {
            mCurrentLocation = mLastLocation;
        }
        if (mCurrentLocation != null) {
            GeoPoint mcLatLong = new GeoPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            if (Tracking.getTracking(getApplicationContext()).isTracking()) {
                MapHandler.getMapHandler().addTrackPoint(this, mcLatLong);
                Tracking.getTracking(getApplicationContext()).addPoint(mCurrentLocation, mapActions.getAppSettings());
            }
            if (NaviEngine.getNaviEngine().isNavigating())
            {
              NaviEngine.getNaviEngine().updatePosition(this, mCurrentLocation);
            }
            MapHandler.getMapHandler().setCustomPoint(this, mcLatLong);
            mapActions.showPositionBtn.setImageResource(R.drawable.ic_my_location_white_24dp);
        } else {
            mapActions.showPositionBtn.setImageResource(R.drawable.ic_location_searching_white_24dp);
        }
    }
    
    public MapActions getMapActions() { return mapActions; }

    @Override public void onBackPressed() {
        boolean back = mapActions.homeBackKeyPressed();
        if (back) {
            moveTaskToBack(true);
        }
        // if false do nothing
    }

    @Override protected void onStart() {
        super.onStart();
    }

    @Override public void onResume() {
        super.onResume();
        mapView.onResume();
        ensureLocationListener(true);
        ensureLastLocationInit();
    }

    @Override protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override protected void onStop() {
        super.onStop();
        // Remove location updates is not needed for tracking
        if (!Tracking.getTracking(getApplicationContext()).isTracking()) {
          locationManager.removeUpdates(this);
          kalmanLocationManager.removeUpdates(this);
          lastProvider = null;
        }
        if (mCurrentLocation != null) {
            GeoPoint geoPoint = mapView.map().getMapPosition().getGeoPoint();
            Variable.getVariable().setLastLocation(geoPoint);
        }
        if (mapView != null) Variable.getVariable().setLastZoomLevel(mapView.map().getMapPosition().getZoomLevel());
        Variable.getVariable().saveVariables(Variable.VarType.Base);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mapAlive = false;
        locationManager.removeUpdates(this);
        kalmanLocationManager.removeUpdates(this);
        lastProvider = null;
        mapView.onDestroy();
        if (MapHandler.getMapHandler().getHopper() != null) MapHandler.getMapHandler().getHopper().close();
        MapHandler.getMapHandler().setHopper(null);
        Navigator.getNavigator().setOn(false);
        MapHandler.reset();
        Destination.getDestination().setStartPoint(null, null);
        Destination.getDestination().setEndPoint(null, null);
        System.gc();
    }

    /**
     * @return my currentLocation
     */
    public static Location getmCurrentLocation() {
        return mCurrentLocation;
    }

    private void ensureLastLocationInit()
    {
      if (mLastLocation != null) { return; }
      try
      {
        Location lonet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lonet != null) { mLastLocation = lonet; return; }
      }
      catch (SecurityException|IllegalArgumentException e)
      {
        log("NET-Location is not supported: " + e.getMessage());
      }
      try
      {
        Location logps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);        
        if (logps != null) { mLastLocation = logps; return; }
      }
      catch (SecurityException|IllegalArgumentException e)
      {
        log("GPS-Location is not supported: " + e.getMessage());
      }
    }

    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override public void onLocationChanged(Location location) {
        updateCurrentLocation(location);
    }

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override public void onProviderEnabled(String provider) {
        logUser("LocationService is turned on!!");
    }

    @Override public void onProviderDisabled(String provider) {
        logUser("LocationService is turned off!!");
    }
    
    /** Map was startet and until now not stopped! **/
    public static boolean isMapAlive() { return mapAlive; }
    public static void isMapAlive_preFinish() { mapAlive = false; }

    /**
     * send message to logcat
     *
     * @param str
     */
    private void log(String str) {
        Log.i(this.getClass().getName(), str);
    }
    
    private void logUser(String str) {
      Log.i(this.getClass().getName(), str);
      try
      {
        Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
      }
      catch (Exception e) { e.printStackTrace(); }
    }
}
