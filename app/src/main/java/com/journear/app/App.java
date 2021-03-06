package com.journear.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.journear.app.core.services.CommunicationHub;
import com.journear.app.core.services.JourNearCommunicationsService;
import com.journear.app.core.services.ServiceLocator;
import com.journear.app.core.utils.JnGeocoder;

public class App extends Application {

    public final String LOGTAG = "JournearApp";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOGTAG, "onCreate App Class");
        initialize();
        startCommunicationService();
    }

    private void initialize() {
        ServiceLocator.initialize(App.this);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                JnGeocoder.GetGeocodingListForRegion("ie", getApplicationContext());
            }
        };
        r.run();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopCommunicationService();
    }

    private void startCommunicationService() {
        try {
            Intent intent = new Intent(App.this, JourNearCommunicationsService.class);
            intent.setAction(JourNearCommunicationsService.ACTION_START_FOREGROUND_SERVICE);
            startService(intent);
        } catch (Exception ex) {
            Log.e(LOGTAG, "Can't initialize peer discovery.");
        }
    }

    private void stopCommunicationService() {
        try {
            Intent intent = new Intent(App.this, JourNearCommunicationsService.class);
            intent.setAction(JourNearCommunicationsService.ACTION_STOP_FOREGROUND_SERVICE);
            startService(intent);
        } catch (Exception ex) {
            Log.e(LOGTAG, "Can't stop peer discovery service.");
        }
    }
}
