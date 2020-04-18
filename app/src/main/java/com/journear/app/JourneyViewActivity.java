package com.journear.app;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.journear.app.R;
import com.journear.app.core.entities.NearbyDevices;
import com.journear.app.core.entities.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class JourneyViewActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<NearbyDevices> devicesList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        Intent intent = getIntent();
        NearbyDevices nd = intent.getParcelableExtra("EXTRA");
//                Bundle args = intent.getBundleExtra("BUNDLE");
//        ArrayList<NearbyDevices> devicesList1 = (ArrayList<NearbyDevices>) intent.getSerializableExtra("ARRAYLIST");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        devicesList = new ArrayList<>();

        devicesList.add(nd);

        //TODO Nikhil Sujit
        // devicesList = some source for the data.

        for (NearbyDevices devices : devicesList) {

            Log.d(TAG, "onCreate: " + devices.getSource());

        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, devicesList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();



    }

}
