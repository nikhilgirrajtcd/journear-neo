package com.journear.app.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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
import com.journear.app.core.services.JourNearCommunicationsService;
import com.journear.app.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    public static final String TAG = "MainActivityTag";
    private NearbyDevice ndOwnJourneyPlan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        ndOwnJourneyPlan = intent.getParcelableExtra("EXTRA");


        // Ask for permissions if user has revoked the permission manually after giving the permission for the first time
        LocalFunctions.requestPermissions(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean loggedIn = checkUserLogon();
        if (!loggedIn)
            finish();
        // if needs be check the value of loggedIn and stop further execution from here

//        LocalFunctions.checkLocationPermission(MainActivity.this);
//        LocalFunctions.checkStoragePermission(MainActivity.this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, CreateJourneyActivity.class);
                MainActivity.this.startActivity(myIntent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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
        bindService(intent1, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    JourNearCommunicationsService.ServiceActivityBinder binder = null;
    JourNearCommunicationsService communicationsService = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            binder = (JourNearCommunicationsService.ServiceActivityBinder) service;
            communicationsService = binder.getService();

            HomeFragment hf = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_home);
            communicationsService.setOwnJourneyInfo(ndOwnJourneyPlan);
            for (NearbyDevice obj : communicationsService.getBufferedResponses()) {
                hf.onPeerDiscovered(obj);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO: Do something here.
        }
    };


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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    public void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    public static final String intentDeviceFound = "DeviceFound";
    public static final String intentJoiningRequest = "NewJoiningRequest";

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(intentDeviceFound)) {
            NearbyDevice nd = intent.getParcelableExtra(intentDeviceFound);

            if (homeFragment != null)
                homeFragment.onPeerDiscovered(nd);
            // add device
        }
    }

    public void setHomeFragment(HomeFragment hf) {
        this.homeFragment = hf;
    }

    HomeFragment homeFragment = null;
    public List<NearbyDevice> devicesList = new ArrayList<>();

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NavHostFragment nhf = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        for (Fragment frag : nhf.getChildFragmentManager().getFragments()) {
            if (frag.getClass().equals(HomeFragment.class)) {
                homeFragment = (HomeFragment) frag;
            }
        }

        if (homeFragment != null) {
            addDiscoveredNearbyDevice(ndOwnJourneyPlan);
            homeFragment.onPeerDiscovered(null);
        }
    }

    void addDiscoveredNearbyDevice(NearbyDevice obj) {
        if(obj == null)
            return;

        if (obj.getOwner().isSameAs(LocalFunctions.getCurrentUser())) {// this whole if block is probably not needed since it should be handled in the MainActivity
            ndOwnJourneyPlan = obj;
        }
        // add NearbyDevice obj to the devicesList if the filtering is disabled or the preference matches
        if (!devicesList.contains(obj) && (!homeFragment.filterEnabled || ndOwnJourneyPlan.isCompatible(obj))) {
            devicesList.add(obj);
            homeFragment.onPeerDiscovered(null);
        }
    }
}
