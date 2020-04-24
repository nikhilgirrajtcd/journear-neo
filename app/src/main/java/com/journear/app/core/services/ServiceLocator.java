package com.journear.app.core.services;

import android.content.Context;

import com.journear.app.core.PersistentStore;
import com.journear.app.core.ServerFunctions;
import com.journear.app.core.utils.JnGeocoder;

public class ServiceLocator {
    private static ServiceLocator _i = null;
    private Context context = null;

    /* Local Instance Store */
    PersistentStore persistentStore;
    JnGeocoder jnGeocoder;
    ServerFunctions serverFunctions;
    private CommunicationHub comHub;

    private ServiceLocator(Context context) {
        this.context = context;
    }

    public static void initialize(Context context) {
        _i = new ServiceLocator(context);
    }

    public static PersistentStore getPersistentStore() {
        if (_i.persistentStore == null)
            _i.persistentStore = PersistentStore.getInstance(_i.context);
        return _i.persistentStore;
    }

    public static Context getApplicationContext() {
        return _i.context;
    }

    public static ServerFunctions getServerFunctions() {
        if (_i.serverFunctions == null)
            _i.serverFunctions = ServerFunctions.getInstance(_i.context);
        return _i.serverFunctions;
    }

    public static CommunicationHub getCommunicationHub() {
        if(_i.comHub == null)
            _i.comHub = new CommunicationHub();
        return _i.comHub;
    }
}
