package com.journear.app.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.User;
import com.journear.app.core.services.CommunicationHub;
import com.journear.app.core.services.CommunicationListener;
import com.journear.app.core.services.JnMessage;
import com.journear.app.core.services.JourNearCommunicationsService;
import com.journear.app.core.services.ServiceLocator;
import com.journear.app.ui.home.HomeFragment;
import com.journear.app.ui.share.MessagesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = "MainActivityLogs";
    private AppBarConfiguration mAppBarConfiguration;
    private MenuItem menuItem;
    private TextView badgeCounter;
    int pendingNotifications = 0;
    private MenuItem navNotificationItem;
    private Map<String, Long> discoveryTimes = new HashMap<>();

    public static final String TAG = "MainActivityTag";
    public NearbyDevice ndOwnJourneyPlan;
    private View notificationsImageView;
    long REDISCOVERY_WINDOW = CommunicationHub.MAX_RETRY_COUNT * JourNearCommunicationsService.DISCOVERY_INTERVAL * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        ndOwnJourneyPlan = intent.getParcelableExtra("EXTRA");
        if(intent.getBooleanExtra("fromRatingActivity", false)){
            ndOwnJourneyPlan = null;
            LocalFunctions.setCurrentJourney(ndOwnJourneyPlan);
        }

        // Ask for permissions if user has revoked the permission manually after giving the permission for the first time
        LocalFunctions.requestPermissions(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean loggedIn = checkUserLogon();
        if (!loggedIn) {
            finish();
            return;
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, CreateJourneyActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        navNotificationItem = findViewById(R.id.nav_notification);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_createJourney,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        bindService();
    }

    protected void bindService() {
        Intent intent1 = new Intent(MainActivity.this, JourNearCommunicationsService.class);
        bindService(intent1, serviceConnection, Context.BIND_ADJUST_WITH_ACTIVITY);
        if (communicationsService != null) {
            communicationsService.bound = true;
            processBufferedUpdates();
        }
    }

    JourNearCommunicationsService.ServiceActivityBinder binder = null;
    JourNearCommunicationsService communicationsService = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            binder = (JourNearCommunicationsService.ServiceActivityBinder) service;
            communicationsService = binder.getService();

            processBufferedUpdates();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO: Do something here.
        }
    };

    private void processBufferedUpdates() {
        HomeFragment hf = getHomeFragment();
        if (hf != null && communicationsService != null) {
            communicationsService.setOwnJourneyInfo(ndOwnJourneyPlan);
            for (NearbyDevice obj : communicationsService.getBufferedResponses()) {
                hf.onPeerDiscovered(obj);
            }
        }
    }


    /**
     * Checks if a user is logged in. Sets the left drawer UI.
     *
     * @return true if the a user is logged in, false otherwise.
     */
    private boolean checkUserLogon() {
        Object currentUser = LocalFunctions.getCurrentUser();
        if (currentUser == null) {
            Intent intentToLetUserLogon = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intentToLetUserLogon);
            return false;
        }
        decorateUiForUser();
        return true;
    }

    /**
     * Sets username, avatar and email on the left drawer.
     */
    private void decorateUiForUser() {
        User currentUser = LocalFunctions.getCurrentUser();
        // Todo: Fetch the user details from server over here and user that to set the environment
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userNameTextView);
        navUsername.setText(currentUser.getEmail());
        TextView navName = headerView.findViewById(R.id.side_nav_bar_Name);
        navName.setText(currentUser.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        menuItem = menu.findItem(R.id.nav_notification);
        menuItem.setActionView(R.layout.notification_badge);
        View view = menuItem.getActionView();
        badgeCounter = view.findViewById(R.id.badge_counter);
        notificationsImageView = view.findViewById(R.id.bell_icon_types);
        badgeCounter.setText(String.valueOf(pendingNotifications));

        badgeCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationsFragment();
            }
        });

        notificationsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationsFragment();
            }
        });


        return true;
    }

    public void openNotificationsFragment() {
        FragmentTransaction ft = MainActivity.this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().beginTransaction();
        Fragment sf = new MessagesFragment();
        ft.replace(R.id.nav_host_fragment, sf);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_notification:
                FragmentTransaction ft = this.getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().beginTransaction();
                Fragment sf = new MessagesFragment();
                ft.replace(R.id.nav_host_fragment, sf);
                ft.addToBackStack(null);
                ft.commit();

//                homeFragment.goToFragment(new MessagesFragment());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOGTAG, "Main Activity Paused");
        unbindService();
    }

    private void unbindService() {
        if (communicationsService != null)
            communicationsService.bound = false;
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOGTAG, "Main Activity Resumed");
        bindService();
    }

    @Override
    public void onDestroy() {
        if (serviceConnection != null) {
            try {
                unbindService();
            } catch (Exception ex) {
                Log.e(LOGTAG, "Error while unbinding service connection.");
            }
        }
        super.onDestroy();
    }

    public static final String intentDeviceFound = "DeviceFound";
    public static final String intentJoiningRequest = "NewJoiningRequest";

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(intentDeviceFound)) {
            NearbyDevice nd = intent.getParcelableExtra(intentDeviceFound);
            addDiscoveredNearbyDevice(nd);
            if (homeFragment != null)
                homeFragment.onPeerDiscovered(nd);
            // add device
        }
    }

    public void setHomeFragment(HomeFragment hf) {
        this.homeFragment = hf;
    }

    public HomeFragment getHomeFragment() {
        if (homeFragment == null) {
            NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            for (Fragment frag : nhf.getChildFragmentManager().getFragments()) {
                if (frag.getClass().equals(HomeFragment.class)) {
                    homeFragment = (HomeFragment) frag;
                }
            }
        }
        return homeFragment;
    }

    HomeFragment homeFragment = null;
    public List<NearbyDevice> devicesList = new ArrayList<>();

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Setting the common comms listener for incoming messages
        ServiceLocator.getCommunicationHub().setCommonListener(collectorListener);

        HomeFragment homeFragment = getHomeFragment();
        if (homeFragment != null) {
            addDiscoveredNearbyDevice(ndOwnJourneyPlan);
            homeFragment.onPeerDiscovered(null);
        }
    }

    void addDiscoveredNearbyDevice(NearbyDevice obj) {
        if (obj == null)
            return;

        long discoveryTime = System.currentTimeMillis();

        if (obj.getOwner().isSameAs(LocalFunctions.getCurrentUser())) {// this whole if block is probably not needed since it should be handled in the MainActivity
            ndOwnJourneyPlan = obj;
        }

        // add NearbyDevice obj to the devicesList if the filtering is disabled or the preference matches
        if (!devicesList.contains(obj) && (!homeFragment.filterEnabled || ndOwnJourneyPlan.isCompatible(obj))) {
            devicesList.add(obj);
            updateDiscoveryTimeAndRemoveOld(obj.getOwner().getUserId(), discoveryTime);
            homeFragment.onPeerDiscovered(null);
        }
    }

    private void updateDiscoveryTimeAndRemoveOld(String newOwnerId, long discoveryTime) {
        discoveryTimes.put(newOwnerId, discoveryTime);
//        for(int loopVar = 0; loopVar < discoveryTimes.size(); loopVar++) {
        // milliseconds
        for (String key : discoveryTimes.keySet()) {
            if ((discoveryTime - discoveryTimes.get(key)) > REDISCOVERY_WINDOW) {
                discoveryTimes.remove(key);
                for (int loopVar = devicesList.size() - 1; loopVar >= 0; loopVar--) {
                    if (devicesList.get(loopVar).getOwner().getUserId().equals(newOwnerId)) {
                        devicesList.remove(loopVar);
                    }
                }
            }
        }
    }

    /**
     * Class to collect and cache all communication
     */
    public class CachedComms {

        public boolean isDelivered() {
            return delivered;
        }

        public void setDelivered(boolean delivered) {
            this.delivered = delivered;
        }

        public JnMessage getMessage() {
            return message;
        }

        public NearbyDevice getAssociatedRide() {
            return associatedRide;
        }

        public boolean isExpired() {
            return expired;
        }

        boolean delivered;
        JnMessage message;
        NearbyDevice associatedRide;
        boolean expired = false;

        @Override
        public int hashCode() {
            return Objects.hash(message, associatedRide);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null)
                return false;
            if (obj instanceof CachedComms) {
                CachedComms com = (CachedComms) obj;
                return Objects.equals(this.message.toReconstructableString(), com.message.toReconstructableString())
                        && Objects.equals(this.associatedRide, com.associatedRide);
            }
            return false;
        }

        public CachedComms(JnMessage message, NearbyDevice associatedRide, boolean expired) {
            this.message = message;
            this.associatedRide = associatedRide;
            this.expired = expired;
            this.delivered = false;
        }
    }

    List<CachedComms> cachedCommsList = new ArrayList<>();

    public List<CachedComms> getCachedCommsList() {
        return cachedCommsList;
    }

    public CommunicationListener getCollectorListener() {
        return collectorListener;
    }

    private CommunicationListener collectorListener = new CommunicationListener() {
        @Override
        public void onResponse(JnMessage message, NearbyDevice associatedRide) {
            try {
                CachedComms cachedComms = new CachedComms(message, associatedRide, false);
                if (!cachedCommsList.contains(cachedComms)) {
                    cachedCommsList.add(cachedComms);
                    pendingNotifications = getUnreadCachedCommsCount(cachedCommsList);
                    if (badgeCounter != null)
                        badgeCounter.setText(String.valueOf(pendingNotifications));
                }
            } catch (Exception ex) {
                Log.e(LOGTAG, "Exception in onResponse.", ex);
            }
        }

        @Override
        public void onExpire(JnMessage expiredMessage, NearbyDevice nearbyDevice) {
            try {
                CachedComms cachedComms = new CachedComms(expiredMessage, nearbyDevice, false);
                if (!cachedCommsList.contains(cachedComms)) {
                    cachedCommsList.add(cachedComms);

                    pendingNotifications = getUnreadCachedCommsCount(cachedCommsList);
                    if (badgeCounter != null)
                        badgeCounter.setText(String.valueOf(pendingNotifications));
                }
            } catch (Exception ex) {
                Log.e(LOGTAG, "Exception in onExpire.", ex);
            }
        }
    };

    public static int getUnreadCachedCommsCount(List<CachedComms> l) {
        int _count = 0;
        for (int iIndex = 0; iIndex < l.size(); iIndex++) {
            if (!l.get(0).delivered)
                _count++;
        }
        return _count;
    }
}
