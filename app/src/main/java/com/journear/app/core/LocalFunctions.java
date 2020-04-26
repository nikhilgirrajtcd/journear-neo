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
import com.journear.app.core.services.ServiceLocator;
import com.journear.app.core.utils.AppConstants;
import com.journear.app.map.MapActivity;
import com.journear.app.map.activities.MapNewActivity;

import org.apache.commons.lang3.StringUtils;


public class LocalFunctions {

    private static User _user;

    @Deprecated
    public static User getCurrentRegisteredUser(Context context) {
        return (User) PersistentStore.getInstance(context).getItem("registeredUser", User.class);
    }

    @Deprecated
    public static StringWrapper getCurrentLoggedInUser(Context context) {
        return (StringWrapper) PersistentStore.getInstance(context).getItem("currentUser", StringWrapper.class);
    }

    public static User getCurrentUser() {
        if (_user == null)
            _user = (User) PersistentStore.getInstance(ServiceLocator.getApplicationContext())
                    .getItem("currentUser", User.class);
        return _user;
    }

    /**
     * Persists the user in local store. Shortcut to PersistentStore.getInstance(context).setItem...
     * @param user
     */
    public static void setCurrentUser(User user) {
        _user = user;
        PersistentStore.getInstance(ServiceLocator.getApplicationContext()).setItem("currentUser", user, true);
    }

    public static void setCurrentJourney(NearbyDevice nd) {
        Context context = ServiceLocator.getApplicationContext();
        PersistentStore.getInstance(context).setItem("CurrentJourneyRequest", nd, true);
    }

    public static void launchMapActivityWithRoute(Context context, double lat1, double long1, double lat2, double long2) {
        Intent intent = new Intent(context, MapNewActivity.class);
        double[] dddddd = {lat1, long1, lat2, long2, 0, 0};
        intent.putExtra(MapActivity.incomingIntentName, dddddd);
        context.startActivity(intent);
    }


    public static void shortToast(String s, Activity activity) {
        Log.i("ShortToast", s);
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    public static boolean requestPermissions(Activity thisActivity) {
        if (!(isLocationPermissionGiven(thisActivity) || isStoragePermissionGiven(thisActivity))) {
            shortToast("Please turn on your location", thisActivity);

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    AppConstants.MY_PERMISSIONS_ALL);
        }
        return true;
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

    /**
     * Returns 1 for Female, 2 for Male and 3 otherwise
     *
     * @param genderString A string {male, female, M, F, Any, Other, Male}
     * @return
     */
    public static int getGenderIndex(String genderString) {
        if (StringUtils.containsIgnoreCase(genderString, "f"))
            return 1;
        else if (StringUtils.containsIgnoreCase(genderString, "m"))
            return 2;
        else
            return 3;
    }

    /**
     * Returns the string Female for 1, Male for 2 and Other for 3
     *
     * @param index
     * @return
     */
    public static String getGenderString(int index) {
        switch (index) {
            default:
                return "Other";
            case 1:
                return "Female";
            case 2:
                return "Male";
        }
    }
}