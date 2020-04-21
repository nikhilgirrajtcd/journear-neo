package com.journear.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
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
            if(action.equals(WifiP2pManager.WIFI_P2P_STATE_ENABLED))
            {
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

    // Such a Karen function
    public WifiP2pManager getManager() {
        if (_manager == null)
            _manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        return _manager;
    }

    // such an advertisement of digital tv
    public WifiP2pManager.Channel getChannel() {
        if (_channel == null){
            _channel = getManager().initialize(this, getMainLooper(), null);
        }
        return _channel;
    }

    private WifiP2pManager _manager;
    private WifiP2pManager.Channel _channel;

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

        unregisterAllWifiP2p();
        showJourneys();

        findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                double[] dddddd = {0, 0, 0, 0, 0, 0};
                intent.putExtra(MapActivity.incomingIntentName, dddddd);
                startActivity(intent);
            }
        });
    }

    private void showJourneys() {
        if (ndOwnJourneyPlan != null) {
//            startRegistration();
//            discoverService();
//            startBroadCastAndDiscovery();
            discoverDevices();
            showList();
        }
    }

    private void showList() {

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //if (!devicesList.contains(ndOwnJourneyPlan))
            devicesList.add(ndOwnJourneyPlan);

        // TODO Nikhil Sujit
        // devicesList = some source for the data.

        for (NearbyDevice devices : devicesList) {
            Log.d(TAG, "onCreate: " + devices.getSource());
        }


        recyclerViewAdapter = new RecyclerViewAdapter(this, devicesList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }

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
            Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
            NearbyDevice nd = new NearbyDevice();
            String[] all = StringUtils.split(record.get("a"), '|');

            // sequence = u, s, d, t
            UserSkimmed u = new UserSkimmed();
            u.setUserName(all[0]);
            nd.setUser(u);
            nd.setSource2(all[1]);
            nd.setDestination2(all[2]);
            nd.setTravelTime(all[3]);

            //            nd.setSource2(record.get("s"));
//            nd.setDestination2(record.get("d"));
//            nd.setTravelTime(record.get("t"));
//            UserSkimmed u = new UserSkimmed();
//            u.setUserName(record.get("u"));
//            nd.setUser(u);

            discoveredDnsRecords.put(device.deviceAddress, nd);
        }
    };

    WifiP2pManager.DnsSdServiceResponseListener dnsSdResponseServiceListener = new WifiP2pManager.DnsSdServiceResponseListener() {
        @Override
        public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                            WifiP2pDevice resourceType) {
            shortToast("DNS Service Available");
            // Update the device name with the human-friendly version from
            // the DnsTxtRecord, assuming one arrived.

            // Nikhil - Set deviceName as deviceAddress because we don't have much info available so far!

            if (discoveredDnsRecords.containsKey(resourceType.deviceAddress)) {
                NearbyDevice nd = discoveredDnsRecords.get(resourceType.deviceAddress);

                if (!devicesList.contains(nd))
                    devicesList.add(nd);

                recyclerViewAdapter.notifyItemInserted(devicesList.size());
            }
            Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
        }
    };


    private WifiP2pDnsSdServiceInfo getWifiP2pDnsSdServiceInfo() {
        Map<String, String> record = new HashMap<String, String>();

        String all = StringUtils.joinWith("|", ndOwnJourneyPlan.getUser().userName, ndOwnJourneyPlan.getSource2().id,
                ndOwnJourneyPlan.getDestination2().id, ndOwnJourneyPlan.getTravelTime().toString());

        record.put("a", all);
//        record.put("u", ndOwnJourneyPlan.getUser().userName);
//        record.put("s", ndOwnJourneyPlan.getSource2().id);
//        record.put("d", ndOwnJourneyPlan.getDestination2().id);
//        record.put("t", ndOwnJourneyPlan.getTravelTime().toString());

        return WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
    }

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

    private void unregisterAllWifiP2p() {
        getManager().clearLocalServices(getChannel(), null);
        getManager().clearServiceRequests(getChannel(), null);
    }
}
