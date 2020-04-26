package com.journear.app.ui.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.journear.app.core.entities.UserSkimmed;
import com.journear.app.core.services.CommunicationHub;
import com.journear.app.core.services.CommunicationListener;
import com.journear.app.core.services.JnMessage;
import com.journear.app.core.services.JnMessageSet;
import com.journear.app.core.services.ServiceLocator;
import com.journear.app.map.MyLocationListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.JnGeocodeItem;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.ui.CreateJourneyActivity;
import com.journear.app.ui.tools.ToolsFragment;

import java.util.List;

import static com.journear.app.R.drawable.card_edge;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    public Context context;
    private List<NearbyDevice> devicesList;
    private MyLocationListener myLocationListner;
    LocationManager locationManager;
    CommunicationListener communicationListener;
    private String LOGTAG = "RecyclerViewActivityLog";
    private View view;


    public RecyclerViewAdapter(Context context, List<NearbyDevice> devicesList, CommunicationListener communicationListener) {
        this.devicesList = devicesList;
        this.context = context;
        this.communicationListener = communicationListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_devices, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.discoveryRelativeLayout);
        TableRow tableRow = (TableRow) view.findViewById(R.id.btnTableRow);

        NearbyDevice devices = devicesList.get(position);
        holder.userName.setText(devices.getOwner().getName());
        holder.source.setText(devices.getSource2().placeString);
        holder.destination.setText(devices.getDestination2().placeString);
        holder.travelTime.setText(devices.getTravelTime().toString());
        holder.modeJourney.setText(devices.getModeOfJourney());

        if(devices.getOwner().getUserId().equalsIgnoreCase(LocalFunctions.getCurrentUser().getUserId())){
            holder.editJourney.setVisibility(View.VISIBLE);
            holder.joinRideButton.setVisibility(View.GONE);
            relativeLayout.setBackground(ContextCompat.getDrawable(context, card_edge));

        }


        if (devices.getPreferSameGender()) {
            holder.genderPreferenceTextView.setText(devices.getOwner().getGender());
        } else {
            holder.genderPreferenceTextView.setText(R.string.NoPreference);
        }


    }

    // one for just testing deviceslist.size()
    public int getItemCount() {
        return devicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView source;
        public TextView destination;
        public TextView travelTime;
        public TextView userName;
        public TextView modeJourney;
        public TextView genderPreferenceTextView;
        public TextView user_rating;
        public Switch genderPreference;
        public Button findSource;
        public Button findCurrentToSource;
        public Button joinRideButton;

        public Button editJourney;
        public Button delete;
        public int id;
        final MediaPlayer mp = new MediaPlayer();

        public ViewHolder(@NonNull View devicesList, Context ctx) {
            super(devicesList);
            context = ctx;
            userName = devicesList.findViewById(R.id.user_name);
            source = devicesList.findViewById(R.id.source);
            destination = devicesList.findViewById(R.id.destination);
            travelTime = devicesList.findViewById(R.id.travelTime);
            modeJourney = devicesList.findViewById(R.id.preferenceTextView);
            genderPreference = devicesList.findViewById(R.id.genderPreference);
            genderPreferenceTextView = devicesList.findViewById(R.id.genderPreferenceTextView);

            findSource = devicesList.findViewById(R.id.Maps);
            findCurrentToSource = devicesList.findViewById(R.id.currentTOSource);
            editJourney = devicesList.findViewById(R.id.editJourney);
            joinRideButton = devicesList.findViewById(R.id.Join);


            editJourney.setOnClickListener(this);
            findSource.setOnClickListener(this);
            findCurrentToSource.setOnClickListener(this);
            joinRideButton.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            MediaPlayer.create(v.getContext(),R.raw.click).start();

            final int position;
            position = getAdapterPosition();
            NearbyDevice dev = devicesList.get(position);
            Context context = v.getContext();

//            Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
            switch (v.getId()) {
                case R.id.currentTOSource:
                    Toast.makeText(context, "Navigating to start point." + position, Toast.LENGTH_SHORT).show();

                    findLocation(dev, v);
                    break;

                case R.id.editJourney:
                    editCreatedJourney(dev, v);
                    break;

                case R.id.Maps:
                    Toast.makeText(context, "Opening the journey route." + position, Toast.LENGTH_SHORT).show();

                    JnGeocodeItem source = devicesList.get(position).getSource2();
                    JnGeocodeItem destination = devicesList.get(position).getDestination2();
                    LocalFunctions.launchMapActivityWithRoute(v.getContext(), source.latitude, source.longitude,
                            destination.latitude, destination.longitude);

                    break;
                case R.id.Join:
                    Toast.makeText(context, "Sending request..." + position, Toast.LENGTH_SHORT).show();

                    ServiceLocator.getCommunicationHub().sendMessage(devicesList.get(position),
                            JnMessageSet.RequestToJoin, communicationListener);
                    break;
            }
        }
    }

    private void editCreatedJourney(final NearbyDevice device, View view) {


        final Intent intent = new Intent(context, CreateJourneyActivity.class);
        intent.putExtra("EditIntent", device);
        intent.putExtra("Class", "RecyclerView");
        view.getContext().startActivity(intent);

    }

    public void findLocation(final NearbyDevice device, View v) {
        myLocationListner = new MyLocationListener();
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager
                .GPS_PROVIDER, 5000, 10, myLocationListner);

        myLocationListner = new MyLocationListener(locationManager);
        Location loc = myLocationListner.getLocation(LocationManager.NETWORK_PROVIDER);
        JnGeocodeItem source = device.getSource2();
        LocalFunctions.launchMapActivityWithRoute(v.getContext(),
                loc.getLatitude(), loc.getLongitude(),
                source.latitude, source.longitude);

    }

}
