package com.journear.app.core;

import android.content.Context;
import android.util.Log;

import com.journear.app.R;
import com.journear.app.core.entities.JnGeocodeItem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;

public class JnGeocoder {
    public static ArrayList<JnGeocodeItem> GetGeocodingListForRegion(String region, Context context) {
        return readGeocodingDataCsv(region, context);

    }

    private static ArrayList<JnGeocodeItem> readGeocodingDataCsv(final String region, Context context) {
        // At the moment the function only returns the data for region "ie", while the region field is not checked

        ArrayList<JnGeocodeItem> returnSet = new ArrayList<>();
        String row = "";

        try {
            InputStream csvStream = context.getResources().openRawResource(R.raw.geocodes_ie_fst);
            BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, Charset.forName("UTF-8")));

            reader.readLine(); // skip header
            while ((row = reader.readLine()) != null) {
                // Split the row, separate first 3 columns, and keep all the others together
                final String[] cols = row.split(",", 4);
                try {
                    JnGeocodeItem jnGeocodeItem = new JnGeocodeItem() {{
                        id = cols[0];
                        longitude = Double.parseDouble(cols[1]);
                        latitude = Double.parseDouble(cols[2]);
                        placeString = cols[3];
                    }};
                    returnSet.add(jnGeocodeItem);
                } catch (NumberFormatException e) {
                    Log.e("JnGeocoder", "Lat/Longitude parsing error in row - " + row, e);
                }
            }

            reader.close();
            csvStream.close();
        } catch (Exception ex) {
            Log.wtf("JnGeoCoder", "Error reading row from csv - " + row, ex);
        }

        return returnSet;
    }

    // TODO: Nikhil
    public static void DownloadAndStoreGeocodingDataForRegion(String region) {
        throw new UnsupportedOperationException("implementation pending");
    }
}
