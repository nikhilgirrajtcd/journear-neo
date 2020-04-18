package com.journear.app.core.utils;

import android.content.Context;
import android.util.Log;

import com.journear.app.R;
import com.journear.app.core.entities.JnGeocodeItem;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.nustaq.serialization.FSTConfiguration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class JnGeocoder {


    public static ArrayList<JnGeocodeItem> GetGeocodingListForRegion(String region, Context context) {
        return readGeocodingDataCsv(region, context);
    }

    private static ArrayList<JnGeocodeItem> readGeocodingDataCsv(final String region, Context context) {
        // At the moment the function only returns the data for region "ie", while the region field is not checked
        ArrayList<JnGeocodeItem> returnSet = new ArrayList<>();

        try {
            InputStream deserializeStream = context.getResources().openRawResource(R.raw.geocodes_ie_fst);
            returnSet = (ArrayList<JnGeocodeItem>) SerializerHelper.deserialize(deserializeStream, JnGeocodeItem.class);
            System.out.println("Deserialized: " + returnSet.size());
        } catch (Exception ex) {
            Log.wtf("JnGeoCoder", "Error in deserializing data.", ex);
        }

        return returnSet;
    }

    // run once
    private static ArrayList<JnGeocodeItem> readGeocodingDataCsv(String filePath) {
        // At the moment the function only returns the data for region "ie", while the
        // region field is not checked
        if (StringUtils.isEmpty(filePath)) {
            filePath = "C:\\Work\\Projects\\ASE\\JournearGeocoder\\geocodeHelper\\data\\in\\ireland-and-northern-ireland-latest.csv";
        }
        final ArrayList<JnGeocodeItem> returnSet = new ArrayList<>();
        String row = "";

        try {
            InputStream csvStream = new FileInputStream(filePath);
            final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(csvStream, Charset.forName("UTF-8")));
            reader.readLine(); // skip header
            JnGeocodeItem jnGeocodeItem;
            while ((row = reader.readLine()) != null) {
                // Split the row, separate first 3 columns, and keep all the others together

                final String[] cols = row.split(",", 4);
                try {
                    jnGeocodeItem = new JnGeocodeItem();
                    jnGeocodeItem.id = cols[0];
                    jnGeocodeItem.longitude = Double.parseDouble(cols[1]);
                    jnGeocodeItem.latitude = Double.parseDouble(cols[2]);
                    jnGeocodeItem.placeString = StringUtils.strip(cols[3], ",");
                    returnSet.add(jnGeocodeItem);
                } catch (final NumberFormatException e) {
                    System.err.println("Lat/Longitude parsing error in row - " + row);
                    System.err.println(e);
                }
            }

            reader.close();
            csvStream.close();
        } catch (final Exception ex) {
            System.err.println("Error reading row from csv - " + row);
            System.err.println(ex);
        }
        System.out.println("Objects: " + returnSet.size());

        return returnSet;
    }

    // TODO: Nikhil
    public static void DownloadAndStoreGeocodingDataForRegion(String region) {
        throw new UnsupportedOperationException("implementation pending");
    }
}
