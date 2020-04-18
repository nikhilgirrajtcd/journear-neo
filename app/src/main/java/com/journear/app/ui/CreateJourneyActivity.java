package com.journear.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.journear.app.R;
import com.journear.app.core.JnGeocoder;
import com.journear.app.core.entities.JnGeocodeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CreateJourneyActivity extends AppCompatActivity {

    private HashMap<String, JnGeocodeItem> mapTextValueToJnGeoCodeItem = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journey);


        final AutoCompleteTextView sourceTextView = findViewById(R.id.acTextView_source);
        final AutoCompleteTextView destinationTextView = findViewById(R.id.acTextView_destination);

        configureAutoCompleteTextViewForSearch(sourceTextView, "ie");
        configureAutoCompleteTextViewForSearch(destinationTextView, "ie");

        findViewById(R.id.btn_CreateJourney).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String source = sourceTextView.getText().toString();
                String destination = destinationTextView.getText().toString();
                Log.i("SELECTION", ""+source);
                Log.i("SELECTION", ""+destination);

                if(mapTextValueToJnGeoCodeItem.containsKey(source) && mapTextValueToJnGeoCodeItem.containsKey(destination))
                {
                    JnGeocodeItem s = mapTextValueToJnGeoCodeItem.get(source);
                    JnGeocodeItem d = mapTextValueToJnGeoCodeItem.get(destination);
                    Log.i("SELECTION", "Source: " + s.latitude + ", " + s.longitude);
                    Log.i("SELECTION", "Destination: " + d.latitude + ", " + d.longitude);
                    Log.i("SELECTION", "Safe to Proceed");
                }

            }
        });
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
