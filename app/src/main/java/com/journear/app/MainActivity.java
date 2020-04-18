package com.journear.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.journear.app.core.PersistentStore;
import com.journear.app.core.entities.NearbyDevices;
import com.journear.app.core.entities.RecyclerViewAdapter;
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.interfaces.Persistable;
import com.journear.app.ui.CreateJourneyActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<NearbyDevices> devicesList = new ArrayList<>();

    private NearbyDevices ndOwnJourneyPlan;
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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
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


        devicesList.add(ndOwnJourneyPlan);

        // TODO Nikhil Sujit
        // devicesList = some source for the data.

        for (NearbyDevices devices : devicesList) {

            Log.d(TAG, "onCreate: " + devices.getSource());

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, devicesList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }

    private boolean checkUserLogon() {
        Persistable currentUser = PersistentStore.getInstance(MainActivity.this).getItem("currentUser", StringWrapper.class);
        if (currentUser == null) {
            Intent intentToLetUserLogon = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intentToLetUserLogon);
            return false;
        }
        decorateUiForUser();
        return true;
    }

    private void decorateUiForUser() {
        StringWrapper currentUser = (StringWrapper) PersistentStore.getInstance(MainActivity.this).getItem("currentUser", StringWrapper.class);
        // Todo: Fetch the user details from server over here and user that to set the environment
//        TextView tv =findViewById(R.id.nav_view).findViewById(R.id.textView);
//        tv.setText(currentUser.toString());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userNameTextView);
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


    final HashMap<String, NearbyDevices> discoveredDnsRecords = new HashMap<>();

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
            NearbyDevices nd = new NearbyDevices();
//            nd.setSource(record.get("source"));
//            nd.setDestination(record.get("destination"));
            nd.setTravelTime(record.get("travelTime"));
//            nd.setUser_rating(record.get("rating"));

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
                NearbyDevices nd = discoveredDnsRecords.get(resourceType.deviceAddress);

                devicesList.add(nd);
                recyclerViewAdapter.notifyItemInserted(devicesList.size());
            }
            Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
        }
    };

    private void startRegistration() {
        WifiP2pManager.ActionListener addLocalServiceActionListener = new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
                shortToast("Success - Add local service!");
            }

            @Override
            public void onFailure(int code) {
                shortToast("Failed - Add local service! Code - " + code);
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        };

        WifiP2pDnsSdServiceInfo serviceInfo = getWifiP2pDnsSdServiceInfo();

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        shortToast("Adding Local Service");
        getManager().addLocalService(getChannel(), serviceInfo, addLocalServiceActionListener);
    }

    private WifiP2pDnsSdServiceInfo getWifiP2pDnsSdServiceInfo() {
        Map<String, String> record = new HashMap<String, String>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");

        // TODO - Create a shorted format to send this info
        // 1. Add ID of source and destination instead of name.
        // 2. Use smaller names for the keys below - maybe instead of the word 'destination', use 'd' xD
        // 3.
//        record.put("destination", ndOwnJourneyPlan.getDestination());
//        record.put("source", ndOwnJourneyPlan.getSource());
        record.put("travelTime", ndOwnJourneyPlan.getTravelTime().toString());
//        record.put("rating", ndOwnJourneyPlan.getUser_rating());


        return WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
    }

    private void discoverService() {
        getManager().setDnsSdResponseListeners(getChannel(), dnsSdResponseServiceListener, dnsSdResponseRecordListener);
    }

    private void startBroadCastAndDiscovery() {
        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        getManager().addServiceRequest(getChannel(),
                serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        // Success!
                        shortToast("Success - addServiceRequest");
                    }

                    @Override
                    public void onFailure(int code) {
                        shortToast("Failure - addServiceRequest. Code - " + code);

                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    }
                });

        getManager().discoverServices(getChannel(), new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Success!
                shortToast("Success - discoverServices");
            }

            @Override
            public void onFailure(int code) {
                shortToast("Failure - discoverServices. Code - " + code);

                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                if (code == WifiP2pManager.P2P_UNSUPPORTED) {
                    Log.d(TAG, "P2P isn't supported on this device.");
                }
            }
        });

        getManager().discoverPeers(getChannel(), new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                shortToast("Peer discovery success.");
            }

            @Override
            public void onFailure(int reason) {
                shortToast("Peer discovery failure.");
            }
        });
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
