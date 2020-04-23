package com.journear.app.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.User;
import com.journear.app.core.services.JourNearCommunicationsService;
import com.journear.app.ui.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<NearbyDevice> devicesList = new ArrayList<>();

    private NearbyDevice ndOwnJourneyPlan;
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiP2pManager.WIFI_P2P_STATE_ENABLED)) {
                shortToast("Should do it now!");
            }
        }
    };

    public static final String TAG = "MainActivityTag";


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

        findViewById(R.id.nav_host_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJourneys();
            }
        });
        showJourneys();

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

            if (ndOwnJourneyPlan != null) {
                communicationsService.setOwnJourneyInfo(ndOwnJourneyPlan);
                 ArrayList<NearbyDevice> initList = communicationsService.getBufferedResponses();

//                 for(NearbyDevice device : initList){
//
//                     if(ndOwnJourneyPlan.isGenderCompatible(device) ){
//                         devicesList.add(device);
//                     }
//                 }
//                 if(devicesList.size() == 0){
//                     AlertDialog.Builder builderPreference = new AlertDialog.Builder(MainActivity.this);
//                     builderPreference.setMessage("No Journey Found for your Preference, However Other journies are available");
//                     builderPreference.setCancelable(true);
//
//                     builderPreference.setPositiveButton(
//                             "Show",
//                             new DialogInterface.OnClickListener() {
//                                 public void onClick(DialogInterface dialog, int id) {
//                                     devicesList.addAll(communicationsService.getBufferedResponses());
//                                     dialog.cancel();
//                                 }
//                             });
//
//                     builderPreference.setNegativeButton(
//                             "Cancel",
//                             new DialogInterface.OnClickListener() {
//                                 public void onClick(DialogInterface dialog, int id) {
//                                     dialog.cancel();
//                                 }
//                             });
//
//                     AlertDialog alert11 = builderPreference.create();
//                     alert11.show();

//                 }
                // service bind complete
                devicesList.addAll(communicationsService.getBufferedResponses());
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO: Do something here.
        }
    };


    /**
     * Initialize the list on UI. Add the ndOwnJourneyPlan if not null
     */
    private void showJourneys() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this, devicesList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();


        // If the list does not already contain the current ride, Add it.
        if (ndOwnJourneyPlan != null && !devicesList.contains(ndOwnJourneyPlan)) {

            if (devicesList.add(ndOwnJourneyPlan)) {
                int position = devicesList.indexOf(ndOwnJourneyPlan);
                recyclerViewAdapter.notifyItemInserted(position);
            }
        }
    }

    /**
     * Checks if a user is logged in. Sets the left drawer UI.
     * @return true if the a user is logged in, false otherwise.
     */
    private boolean checkUserLogon() {
        Object currentUser = LocalFunctions.getCurrentLoggedInUser(MainActivity.this);
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
        User currentUser = LocalFunctions.getCurrentUser(MainActivity.this);
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


    private void shortToast(String s) {
        Log.i("ShortToast", s);
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
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
        super.onDestroy();
        unbindService(serviceConnection);
    }

    public static final String intentDeviceFound = "DeviceFound";
    public static final String intentJoiningRequest = "NewJoiningRequest";

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(intentDeviceFound)) {
            NearbyDevice nd = intent.getParcelableExtra(intentDeviceFound);
            if (!devicesList.contains(nd))
                devicesList.add(nd);

            recyclerViewAdapter.notifyItemInserted(devicesList.size());
            // add device
        }

        if (intent.hasExtra(intentJoiningRequest)) {

        }
    }

    private void selectRideToJoin() {

    }

}
