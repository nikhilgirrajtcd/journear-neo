package com.journear.app.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.entities.UserSkimmed;
import com.journear.app.core.services.JourNearCommunicationsService;
import com.journear.app.map.LandingActivity;
import com.journear.app.map.MapActivity;
import com.journear.app.ui.adapters.RecyclerViewAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // TXT RECORD properties
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_journearNeo2";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

    public static final int MESSAGE_READ = 0x400 + 1;
    public static final int MY_HANDLE = 0x400 + 2;

    private static final int PERMISSIONS_REQUEST_CODE = 1001;

    static final int SERVER_PORT = 4545;

    private final IntentFilter intentFilter = new IntentFilter();
//    private BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        ndOwnJourneyPlan = intent.getParcelableExtra("EXTRA");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean loggedIn = checkUserLogon();
        if (!loggedIn)
            finish();
        // if needs be check the value of loggedIn and stop further execution from here

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
        StringWrapper currentUser = LocalFunctions.getCurrentLoggedInUser(MainActivity.this);
        // Todo: Fetch the user details from server over here and user that to set the environment
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userNameTextView);
        navUsername.setText(currentUser.toString());
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
