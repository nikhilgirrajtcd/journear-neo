package com.journear.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.journear.app.JourneyViewActivity;
import com.journear.app.R;
import com.journear.app.core.JnGeocoder;
import com.journear.app.core.PersistentStore;
import com.journear.app.core.entities.JnGeocodeItem;
import com.journear.app.core.entities.NearbyDevices;
import com.journear.app.core.entities.UserSkimmed;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CreateJourneyActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private HashMap<String, JnGeocodeItem> mapTextValueToJnGeoCodeItem = new HashMap<>();
    private TextView timeTextView;
    private int minuteOfJourney;
    private int hourOfJourney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_ride);

        final AutoCompleteTextView sourceTextView = findViewById(R.id.acTextView_source);
        final AutoCompleteTextView destinationTextView = findViewById(R.id.acTextView_destination);
        final Calendar cal = Calendar.getInstance();

        timeTextView = findViewById(R.id.btn_setTime);
        setTimeInTextView(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

        timeTextView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           TimePickerDialog tp1 = new TimePickerDialog(CreateJourneyActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                               @Override
                                               public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                   setTimeInTextView(hourOfDay, minute);
                                               }
                                           },
                                                   hourOfJourney, minuteOfJourney, true);
                                           tp1.show();
                                       }
                                   });

        configureAutoCompleteTextViewForSearch(sourceTextView, "ie");
        configureAutoCompleteTextViewForSearch(destinationTextView, "ie");

        findViewById(R.id.btn_CreateJourney).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String source = sourceTextView.getText().toString();
                String destination = destinationTextView.getText().toString();
                Log.i("SELECTION", ""+source);
                Log.i("SELECTION", ""+destination);
                createJourney(v);

                if(mapTextValueToJnGeoCodeItem.containsKey(source) && mapTextValueToJnGeoCodeItem.containsKey(destination))
                {
                    JnGeocodeItem s = mapTextValueToJnGeoCodeItem.get(source);
                    JnGeocodeItem d = mapTextValueToJnGeoCodeItem.get(destination);
                    Log.i("SELECTION", "Source: " + s.latitude + ", " + s.longitude);
                    Log.i("SELECTION", "Destination: " + d.latitude + ", " + d.longitude);
                    Log.i("SELECTION", "Safe to Proceed");
                }

            }

            private void createJourney(View v) {
                NearbyDevices nd = new NearbyDevices();
                List<NearbyDevices> devicesList;

                String source = sourceTextView.getText().toString().trim();
                String Destination =  destinationTextView.getText().toString().trim();
                Time timeOfTravel = Time.valueOf(timeTextView.getText().toString() + ":00");

                nd.setSource(source);
                nd.setDestination(Destination);
                nd.setTravelTime(timeOfTravel);
                nd.setUser((UserSkimmed) PersistentStore.getInstance(CreateJourneyActivity.this).getItem("registeredUser", UserSkimmed.class));
                Snackbar.make(v, "Journey Created",Snackbar.LENGTH_SHORT)
                        .show();

                devicesList = new ArrayList<>();

                devicesList.add(nd);
                PersistentStore.getInstance(CreateJourneyActivity.this).setItem("CurrentJourneyRequest",nd,true);


                final Intent intent = new Intent(CreateJourneyActivity.this, JourneyViewActivity.class);
                intent.putExtra("EXTRA", nd);

                //                Bundle args = new Bundle();
//                args.putSerializable("ARRAYLIST", (Serializable)devicesList);
//
//                intent.putExtra("BUNDLE",args);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(intent);
                        finish();

                    }
                }, 1200);
            }
        });
    }

    private void setTimeInTextView(int hour, int minute) {
        hourOfJourney = hour;
        minuteOfJourney = minute;
        timeTextView.setText(hour + ":" + minute);
    }

    private void configureAutoCompleteTextViewForSearch(AutoCompleteTextView actv, String region){
        ArrayList<JnGeocodeItem> geocodeItems =  JnGeocoder.GetGeocodingListForRegion(region, this);
        // initialize the list for autocomplete for geocoding
        final ArrayAdapter<JnGeocodeItem> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, geocodeItems);

        actv.setAdapter(adapter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppCompatTextView view1 = (AppCompatTextView) view;
                mapTextValueToJnGeoCodeItem.put(view1.getText().toString(), adapter.getItem(position));
            }
        });
    }
}
