package com.journear.app.ui.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.journear.app.map.MyLocationListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.JnGeocodeItem;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.utils.JnGeocoder;
import com.journear.app.ui.CreateJourneyActivity;
import com.journear.app.ui.MainActivity;

import org.oscim.core.GeoPoint;

import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    public Context context;
    private List<NearbyDevice> devicesList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private MyLocationListener myLocationListner;
    LocationManager locationManager;
    GeoPoint onLoadMarker;

    public RecyclerViewAdapter(Context context, List<NearbyDevice> devicesList) {
        this.devicesList = devicesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_devices, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        NearbyDevice devices = devicesList.get(position);
        holder.userName.setText(devices.getUser().getUserName());
        holder.source.setText(devices.getSource2().placeString);
        holder.destination.setText(devices.getDestination2().placeString);
        holder.travelTime.setText(devices.getTravelTime().toString());
    }

    // one for just testing deviceslist.size()
    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView source;
        public TextView destination;
        public TextView travelTime;
        public TextView userName;
        public TextView user_rating;
        public Button findSource;
        public Button findCurrentToSource;
        public Button editJourney;
        public Button delete;
        public int id;

        public ViewHolder(@NonNull View devicesList, Context ctx) {
            super(devicesList);
            context = ctx;
            userName = devicesList.findViewById(R.id.user_name);
            source = devicesList.findViewById(R.id.source);
            destination = devicesList.findViewById(R.id.destination);
            travelTime = devicesList.findViewById(R.id.travelTime);
            findSource = devicesList.findViewById(R.id.Maps);
            findCurrentToSource = devicesList.findViewById(R.id.currentTOSource);
            editJourney = devicesList.findViewById(R.id.editJourney);

            editJourney.setOnClickListener(this);
            findSource.setOnClickListener(this);
            findCurrentToSource.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position;
            position = getAdapterPosition();
            NearbyDevice dev = devicesList.get(position);
            Context context = v.getContext();

            Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            switch (v.getId()) {
                case R.id.currentTOSource:
                    //Taran add functionality
                    findLocation(dev , v);
                    break;

                case R.id.editJourney:
                    editCreatedJourney(dev, v);
                    break;

                case R.id.Maps:
                    Toast.makeText(context, "B" + position, Toast.LENGTH_SHORT).show();

                    JnGeocodeItem source = devicesList.get(position).getSource2();
                    JnGeocodeItem destination = devicesList.get(position).getDestination2();
                    LocalFunctions.launchMapActivityWithRoute(v.getContext(), source.latitude, source.longitude,
                    destination.latitude, destination.longitude);

//                    intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
                    break;
            }
        }
    }

    private void editCreatedJourney(final NearbyDevice device, View view) {

        final Intent intent = new Intent(context, CreateJourneyActivity.class);
        intent.putExtra("EditIntent", device);
        intent.putExtra("Class","RecyclerView");
       view.getContext().startActivity(intent);

    }

    public void findLocation(final NearbyDevice device,View v){
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

        LocalFunctions.launchMapActivityWithRoute(v.getContext(),loc.getLatitude(),loc.getLongitude() ,source.latitude, source.longitude
               );

    }
//
//    @Override
//    public void onClick(View v) {
//
//        int position;
//        position = getAdapterPosition();
//        Item item = itemList.get(position);
//
//        switch (v.getId()) {
//            case R.id.editButton:
//                //edit item
//                editItem(item);
//                break;
//            case R.id.deleteButton:
//                //delete item
//                deleteItem(item.getId());
//                break;
//        }
//
//    }
}
