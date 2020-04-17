package com.journear.app.core.entities;

import android.location.Geocoder;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

public class JnGeocodeItem {
    public String id;
    public double latitude = 0.0;
    public double longitude = 0.0;
    // consolidated String for the name, as anything else is not needed in the whole workflow
    public String placeString;

    @Override
    public String toString() {
        return placeString;
    }


}
