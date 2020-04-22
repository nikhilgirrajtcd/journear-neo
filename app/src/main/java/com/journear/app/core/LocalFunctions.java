package com.journear.app.core;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.entities.User;
import com.journear.app.core.utils.AppConstants;
import com.journear.app.map.MapActivity;


public class LocalFunctions {

    @Deprecated
    public static User getCurrentRegisteredUser(Context context) {

        return (User) PersistentStore.getInstance(context).getItem("registeredUser", User.class);
    }

    @Deprecated
    public static StringWrapper getCurrentLoggedInUser(Context context) {
        return (StringWrapper) PersistentStore.getInstance(context).getItem("currentUser", StringWrapper.class);
    }

    public static User getCurrentUser(Context context) {
        return (User) PersistentStore.getInstance(context).getItem("currentUser", User.class);
    }

    public static void setCurrentUser(Context context, User user) {
        PersistentStore.getInstance(context).setItem("currentUser", user, true);
    }

    public static void setCurrentJourney(NearbyDevice nd, Context context) {
        PersistentStore.getInstance(context).setItem("CurrentJourneyRequest", nd, true);
    }

    public static void launchMapActivityWithRoute(Context context, double lat1, double long1, double lat2, double long2) {
        Intent intent = new Intent(context, MapActivity.class);
        double[] dddddd = {lat1, long1, lat2, long2, 0, 0};
        intent.putExtra(MapActivity.incomingIntentName, dddddd);
        context.startActivity(intent);
    }


    public static void shortToast(String s, Activity activity) {
        Log.i("ShortToast", s);
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

//    public static boolean checkInternetPermission(Activity thisActivity) {
//        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.INTERNET)
//                != PackageManager.PERMISSION_GRANTED) {
//            shortToast("Please turn on your internet", thisActivity);
//
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
//                    Manifest.permission.INTERNET)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(thisActivity,
//                        new String[]{Manifest.permission.INTERNET},
//                        AppConstants.MY_PERMISSIONS_REQUEST_INTERNET);
//
//                // MY_PERMISSIONS_REQUEST_INTERNET is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//            return false;
//        } else
//            return true;
//    }

    public static boolean requestPermissions(Activity thisActivity) {
        if (!(isLocationPermissionGiven(thisActivity) || isStoragePermissionGiven(thisActivity))) {
            shortToast("Please turn on your location", thisActivity);
//            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
//                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
//            } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.MY_PERMISSIONS_ALL);

            // MY_PERMISSIONS_REQUEST_INTERNET is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
        return true;
//        } else
//            return true;
    }

    public static boolean isLocationPermissionGiven(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;

    }

    public static boolean isStoragePermissionGiven(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;

    }


//    public static boolean checkStoragePermission(Activity thisActivity) {
//        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            shortToast("Please turn on your Storage", thisActivity);
//            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions(thisActivity,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        AppConstants.MY_PERMISSIONS_REQUEST_STORAGE);
//
//                // MY_PERMISSIONS_REQUEST_INTERNET is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//            return false;
//        } else
//            return true;
//    }
}