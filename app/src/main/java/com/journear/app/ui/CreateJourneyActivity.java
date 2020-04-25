package com.journear.app.ui;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.snackbar.Snackbar;
import com.journear.app.R;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.entities.JnGeocodeItem;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.entities.UserSkimmed;
import com.journear.app.core.utils.JnGeocoder;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;

public class CreateJourneyActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private HashMap<String, JnGeocodeItem> mapTextValueToJnGeoCodeItem = new HashMap<>();
    private TextView timeTextView;
    private int minuteOfJourney;
    private int hourOfJourney;
    private boolean genderPreference;
    private RadioGroup journeyMode;
    private NearbyDevice editDevice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String className = getIntent().getStringExtra("Class");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_ride);

        if(className != null ) {

            editDevice = intent.getParcelableExtra("EditIntent");
            setInputValues(editDevice);
        }


        AutoCompleteTextView sourceTextView = findViewById(R.id.acTextView_source);
        AutoCompleteTextView destinationTextView = findViewById(R.id.acTextView_destination);


        Calendar cal = Calendar.getInstance();
        if(className != null ) {

            editDevice = intent.getParcelableExtra("EditIntent");
            setInputValues(editDevice);
        }


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

                MediaPlayer.create(v.getContext(),R.raw.click);
                createJourney(v);


            }

            private void createJourney(View v) {
                NearbyDevice nd = getCurrentInput();
                Snackbar.make(v, "Journey Created", Snackbar.LENGTH_SHORT).show();

                LocalFunctions.setCurrentJourney(nd, CreateJourneyActivity.this);

                final Intent intent = new Intent(CreateJourneyActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("EXTRA", nd);

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

    private NearbyDevice getCurrentInput() {

        final AutoCompleteTextView sourceTextView = findViewById(R.id.acTextView_source);
        final AutoCompleteTextView destinationTextView = findViewById(R.id.acTextView_destination);

        String source = sourceTextView.getText().toString();
        String destination = destinationTextView.getText().toString();
        final Switch genderPrefSwitch = findViewById(R.id.genderPreference);

        boolean genderPreferenceSwitch = genderPrefSwitch.isChecked();
        RadioGroup editModeJourney = findViewById(R.id.editModeJourney);
        RadioButton checkedBtn = findViewById(editModeJourney.getCheckedRadioButtonId());
        String journeyMode =  checkedBtn.getText().toString();


        if (mapTextValueToJnGeoCodeItem.containsKey(source) && mapTextValueToJnGeoCodeItem.containsKey(destination)) {
            JnGeocodeItem s = mapTextValueToJnGeoCodeItem.get(source);
            JnGeocodeItem d = mapTextValueToJnGeoCodeItem.get(destination);
            Time timeOfTravel = Time.valueOf(timeTextView.getText().toString() + ":00");
            UserSkimmed userSkimmed = LocalFunctions.getCurrentUser();
            NearbyDevice currentInput = new NearbyDevice(s, d, timeOfTravel, userSkimmed, genderPreferenceSwitch,journeyMode);

            Log.i("SELECTION", "Source: " + s.latitude + ", " + s.longitude);
            Log.i("SELECTION", "" + source);
            Log.i("SELECTION", "Destination: " + d.latitude + ", " + d.longitude);
            Log.i("SELECTION", "" + destination);
            Log.i("SELECTION", "Safe to Proceed");

            return currentInput;
        }
        return null;
    }

    private void setTimeInTextView(int hour, int minute) {
        hourOfJourney = hour;
        minuteOfJourney = minute;
        String timeToDisplay = StringUtils.leftPad("" + hour, 2, '0') + ":" + StringUtils.leftPad(minute + "", 2, '0');
        timeTextView.setText(timeToDisplay);
    }

    private void configureAutoCompleteTextViewForSearch(AutoCompleteTextView actv, String region) {
        ArrayList<JnGeocodeItem> geocodeItems = JnGeocoder.GetGeocodingListForRegion(region, this);
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

    private void setInputValues(NearbyDevice nd){

        ((AutoCompleteTextView)findViewById(R.id.acTextView_source)).setText(nd.getSource2().placeString);
        mapTextValueToJnGeoCodeItem.put(nd.getSource2().placeString, nd.getSource2());
        ((AutoCompleteTextView)findViewById(R.id.acTextView_destination)).setText(nd.getDestination2().placeString);
        mapTextValueToJnGeoCodeItem.put(nd.getDestination2().placeString, nd.getDestination2());

        ((TextView)findViewById(R.id.btn_setTime)).setText(nd.getTravelTime().toString());

        //TODO set time
    }
}
