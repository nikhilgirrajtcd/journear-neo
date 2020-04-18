package com.journear.app.core.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.journear.app.R;

import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

   private Context context;
   private List<NearbyDevice> devicesList;
    public RecyclerViewAdapter(Context context, List<NearbyDevice> devicesList) {
        this.devicesList = devicesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.nearby_devices,parent, false);

        return new ViewHolder(view,context);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView source;
        public TextView destination;
        public TextView travelTime;
        public TextView userName;
        public TextView user_rating;
        public Button findSource;
        public Button findRoute;
        public Button edit;
        public Button delete;
        public int id;
        public ViewHolder(@NonNull View devicesList , Context ctx) {
            super(devicesList);
            context = ctx;
            userName = devicesList.findViewById(R.id.user_name);
            source = devicesList.findViewById(R.id.source);
            destination = devicesList.findViewById(R.id.destination);
            travelTime = devicesList.findViewById(R.id.travelTime);

        }
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
