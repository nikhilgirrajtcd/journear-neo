package com.journear.app.core;

import android.content.Context;

import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.entities.User;

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
}
