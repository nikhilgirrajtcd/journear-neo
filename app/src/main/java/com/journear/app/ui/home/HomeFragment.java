package com.journear.app.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.interfaces.JnPeerDiscoveryListener;
import com.journear.app.ui.MainActivity;
import com.journear.app.ui.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements JnPeerDiscoveryListener {

    private static final String LOGTAG = "HomeFragmentTAG";
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private CheckBox filterPreference;
    private MainActivity parentActivity;
    public boolean filterEnabled;

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiP2pManager.WIFI_P2P_STATE_ENABLED)) {
                shortToast("Should do it now!");
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        parentActivity = (MainActivity) getActivity();
        showJourneys();
        addListenerForPreference();
    }

    /**
     * Initialize the list on UI. Add the ndOwnJourneyPlan if not null
     */
    private void showJourneys() {
        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), parentActivity.devicesList, parentActivity.getCollectorListener());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void shortToast(String s) {
        Log.i("ShortToast", s);
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    //Toggle Journey for Preferences
    public void addListenerForPreference() {
        filterPreference = getView().findViewById(R.id.journeyPreferenceCheckbox);
        filterPreference.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HomeFragment.this.filterEnabled = ((CheckBox) v).isChecked();
            }
        });
    }

    @Override
    public void onPeerDiscovered(NearbyDevice obj) {
        try {
            recyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.e(LOGTAG, "Error in updating view with discovered peer.", ex);
        }
    }
}