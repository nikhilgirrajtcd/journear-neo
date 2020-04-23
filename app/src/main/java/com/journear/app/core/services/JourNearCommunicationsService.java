package com.journear.app.core.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.journear.app.R;
import com.journear.app.core.PeerFunctions;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.UserSkimmed;
import com.journear.app.ui.MainActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class JourNearCommunicationsService extends Service {
    public static final String SERVICE_INSTANCE = "_journearNeo2";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PLAY = "ACTION_PLAY";

    /**
     * BINDER BEGIN
     **************************************************************/
    // Binder given to clients
    private final IBinder binder = new ServiceActivityBinder();
    private boolean bound = false;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class ServiceActivityBinder extends Binder {
        public JourNearCommunicationsService getService() {
            // Return this instance of LocalService so clients can call public methods
            return JourNearCommunicationsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        bound = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean superResult = super.onUnbind(intent);
        bound = false;
        return true;
    }

    /**
     * BINDER END
     ****************************************************************/

    /*
    P2P related variables
     */
    // Such a Karen function
    private WifiP2pManager getManager() {
        if (_manager == null)
            _manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        return _manager;
    }

    // such an advertisement of digital tv
    private WifiP2pManager.Channel getChannel() {
        if (_channel == null) {
            _channel = getManager().initialize(this, getMainLooper(), null);
        }
        return _channel;
    }

    private WifiP2pManager _manager;
    private WifiP2pManager.Channel _channel;


    public JourNearCommunicationsService() {
    }

    public ArrayList<NearbyDevice> getBufferedResponses() {
        return bufferedResponses;
    }

    ArrayList<NearbyDevice> bufferedResponses = new ArrayList<>();

    private void sendDataToMainActivity(NearbyDevice nd) {
        if (bound) {
            Intent intent = new Intent(JourNearCommunicationsService.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MainActivity.intentDeviceFound, nd);
            startActivity(intent);
        } else {
            bufferedResponses.add(nd);
        }
    }

    private void onNewDeviceFound(NearbyDevice nd) {
        // do more stuff here
        sendDataToMainActivity(nd);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null)
                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        startForegroundService();
                        Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:
                        stopForegroundService();
                        Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PLAY:
                        Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PAUSE:
                        Toast.makeText(getApplicationContext(), "You click Pause button.", Toast.LENGTH_LONG).show();
                        break;
                }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /* Used to build and start foreground service. */
    private void startForegroundService() {

        if (startRecurringLookup()) {
            return;
        }

        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("com.journear", "JourNear peer service");
        } else {

            // Create notification default intent.
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            // Create notification builder.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            // Make notification show big text.
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
            bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
            // Set big text style.
            builder.setStyle(bigTextStyle);

            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
            builder.setLargeIcon(largeIconBitmap);
            // Make the notification max priority.
            builder.setPriority(Notification.PRIORITY_MAX);
            // Make head-up notification.
            builder.setFullScreenIntent(pendingIntent, true);

            // Add Play button intent in notification.
            Intent playIntent = new Intent(this, JourNearCommunicationsService.class);
            playIntent.setAction(ACTION_PLAY);
            PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
            NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
            builder.addAction(playAction);

            // Add Pause button intent in notification.
            Intent pauseIntent = new Intent(this, JourNearCommunicationsService.class);
            pauseIntent.setAction(ACTION_PAUSE);
            PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
            NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
            builder.addAction(prevAction);

            // Build the notification.
            Notification notification = builder.build();

            // Start foreground service.
            startForeground(1, notification);
        }
    }

    private class DiscoveryRunnable implements Runnable {
        Timer timer = new Timer();

        @Override
        public void run() {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    discoverDevices();
                }
            };
            timer.schedule(timerTask, 0L, 30000L);
        }

        public void stop() {
            timer.cancel();
        }
    }

    DiscoveryRunnable discoveryRunnable = new DiscoveryRunnable();


    private boolean startRecurringLookup() {
        discoveryRunnable.run();
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(resultPendingIntent) //intent
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notificationBuilder.build());
        startForeground(1, notification);
    }


    private void stopForegroundService() {
        discoveryRunnable.stop();
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    NearbyDevice ndOwnJourneyPlan = null;

    public void setOwnJourneyInfo(NearbyDevice nd) {
        ndOwnJourneyPlan = nd;
    }

    private WifiP2pDnsSdServiceInfo getWifiP2pDnsSdServiceInfo() {
        Map<String, String> record = new HashMap<String, String>();
        if (ndOwnJourneyPlan == null) {
            ndOwnJourneyPlan = NearbyDevice.getDummy();
        }

        String all = PeerFunctions.getBroadcastString(ndOwnJourneyPlan);
        record.put("a", all);

        return WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
    }

    final HashMap<String, NearbyDevice> discoveredDnsRecords = new HashMap<>();

    WifiP2pManager.DnsSdTxtRecordListener dnsSdResponseRecordListener = new WifiP2pManager.DnsSdTxtRecordListener() {
        @Override
        /* Callback includes:
         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */

        public void onDnsSdTxtRecordAvailable(
                String fullDomain, Map<String, String> record, WifiP2pDevice device) {
            Log.d(TAG_FOREGROUND_SERVICE, "DnsSdTxtRecord available - " + record.toString());
            NearbyDevice nd = new NearbyDevice();
            String[] all = StringUtils.split(record.get("a"), '|');

            UserSkimmed u = new UserSkimmed();
            u.setName(all[0]);
            nd.setUser(u);
            nd.setSource2(all[1]);
            nd.setDestination2(all[2]);
            nd.setTravelTime(all[3]);

            discoveredDnsRecords.put(device.deviceAddress, nd);
        }
    };

    WifiP2pManager.DnsSdServiceResponseListener dnsSdResponseServiceListener = new WifiP2pManager.DnsSdServiceResponseListener() {
        @Override
        public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                            WifiP2pDevice resourceType) {
            shortToast("DNS Service Available");

            if (discoveredDnsRecords.containsKey(resourceType.deviceAddress)) {
                NearbyDevice nd = discoveredDnsRecords.get(resourceType.deviceAddress);

                onNewDeviceFound(nd);
            }
            Log.d(TAG_FOREGROUND_SERVICE, "onBonjourServiceAvailable " + instanceName);
        }
    };


    private void discoverDevices() {
        getManager().clearLocalServices(getChannel(), new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                getManager().addLocalService(getChannel(), getWifiP2pDnsSdServiceInfo(), new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        getManager().setDnsSdResponseListeners(getChannel(), dnsSdResponseServiceListener, dnsSdResponseRecordListener);
                        getManager().clearServiceRequests(getChannel(), new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                getManager().addServiceRequest(getChannel(), WifiP2pDnsSdServiceRequest.newInstance(), new WifiP2pManager.ActionListener() {
                                    @Override
                                    public void onSuccess() {
                                        getManager().discoverPeers(getChannel(), new WifiP2pManager.ActionListener() {
                                            @Override
                                            public void onSuccess() {
                                                getManager().discoverServices(getChannel(), new WifiP2pManager.ActionListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        shortToast("Success - discoverServices");
                                                    }

                                                    @Override
                                                    public void onFailure(int reason) {
                                                        shortToast("F6");
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(int reason) {
                                                shortToast("F5");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(int reason) {
                                        shortToast("F4");
                                    }
                                });
                            }

                            @Override
                            public void onFailure(int reason) {
                                shortToast("F3");
                            }
                        });
                    }

                    @Override
                    public void onFailure(int reason) {
                        shortToast("F2");
                    }
                });
            }

            @Override
            public void onFailure(int reason) {
                shortToast("F1");
            }
        });
    }

    private void shortToast(String s) {
        Log.i("ShortToast", s);
        Toast.makeText(JourNearCommunicationsService.this, s, Toast.LENGTH_SHORT).show();
    }
}
