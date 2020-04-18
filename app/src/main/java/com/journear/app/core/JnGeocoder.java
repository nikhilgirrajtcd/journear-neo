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
    public static ArrayList<JnGeocodeItem> GetGeocodingListForRegion(String region, Context context){
            // check if the data is downloaded, use the function below

            //return dummyData();
            // return (ArrayList<JnGeocodeItem>) PersistentStore.getInstance().getItem("geocodes-" + region);
        return readGeocodingDataCsv(region, context);

    }

    private static ArrayList<JnGeocodeItem> dummyData() {
        ArrayList<JnGeocodeItem> dummyList = new ArrayList<>();
        dummyList.add(new JnGeocodeItem() {{
            city = "Dublin"; country = "Ireland";
            latitude = 1;
            longitude = 2;
            id = "1";
            name = "ABCD";
            street = "Abbey Street";
        }});
        dummyList.add(new JnGeocodeItem() {{
            city = "Dublin"; country = "Ireland";
            latitude = 3;
            longitude = 4;
            id = "2";
            name = "ACDC";
            street = "Nassau Street";
        }});
        dummyList.add(new JnGeocodeItem() {{
            city = "Dublin"; country = "Ireland";
            latitude = 5;
            longitude = 6;
            id = "3";
            name = "YOLO";
            street = "Henry Street";
        }});
        return dummyList;
    }

    private static ArrayList<JnGeocodeItem> readGeocodingDataCsv(final String region, Context context)
    {
        ArrayList<JnGeocodeItem> returnSet = new ArrayList<>();
        String row = "";

        try
        {
            InputStream csvStream = context.getResources().openRawResource(R.raw.geocodes_ie);
            BufferedReader reader = new BufferedReader(new InputStreamReader(csvStream, Charset.forName("UTF-8")));

            reader.readLine(); // skip header
            while( (row = reader.readLine()) != null)
            {
                final String[] cols = row.split(",", -1);
                try {
                    JnGeocodeItem jnGeocodeItem = new JnGeocodeItem() {{
                        id = cols[0];
                        longitude = Double.parseDouble(cols[1]);
                        latitude = Double.parseDouble(cols[2]);
                        name = cols[3];
                        street = cols[4];
                        city = cols[5];
                        country = cols[6];
                    }};
                    returnSet.add(jnGeocodeItem);
                }
                catch (NumberFormatException e)
                {
                    Log.e("JnGeocoder", "Lat/Longitude parsing error in row - " + row, e);
                }
            }

            reader.close();
            csvStream.close();
        }
        catch (Exception ex)
        {
            Log.wtf("JnGeoCoder", "Error reading row from csv - " + row, ex);
        }

        return returnSet;
    }

    // TODO: Nikhil
    public static void DownloadAndStoreGeocodingDataForRegion(String region)
    {
        throw new UnsupportedOperationException("implementation pending");
    }
}
