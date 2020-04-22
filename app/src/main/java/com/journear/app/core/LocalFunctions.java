package com.journear.app.core;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.journear.app.R;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.entities.User;
import com.journear.app.map.MapActivity;
import com.journear.app.ui.MainActivity;

import org.oscim.core.GeoPoint;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;

import java.util.List;
import java.util.Optional;

public class LocalFunctions {


    public static User getCurrentRegisteredUser(Context context)
    {
        return (User) PersistentStore.getInstance(context).getItem("registeredUser", User.class);
    }

    public static StringWrapper getCurrentLoggedInUser(Context context)
    {
        return (StringWrapper) PersistentStore.getInstance(context).getItem("currentUser", StringWrapper.class);
    }

    public static void setCurrentJourney(NearbyDevice nd, Context context)
    {
        PersistentStore.getInstance(context).setItem("CurrentJourneyRequest", nd, true);
    }

    public static void launchMapActivityWithRoute(Context context, double lat1, double long1, double lat2, double long2)
    {
        Intent intent = new Intent(context, MapActivity.class);
        double[] dddddd = {lat1, long1, lat2, long2, 0, 0};
        intent.putExtra(MapActivity.incomingIntentName, dddddd);
        context.startActivity(intent);
    }



}
