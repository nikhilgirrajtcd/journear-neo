package com.journear.app.core.entities;

import android.location.Geocoder;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

public class JnGeocodeItem {
    public String id;
    public double latitude = 0.0;
    public double longitude = 0.0;
    public String name;
    public String street;
    public String city;
    public String country;

    @Override
    public String toString() {
        String returnVal = "";
        if (!StringUtils.isEmpty(name)) {
            returnVal = name;

            if (!StringUtils.isEmpty(street)) {
                returnVal += ", " + street;
            }

            if (!StringUtils.isEmpty(city)) {
                returnVal += ", " + city;
            }

            if (!StringUtils.isEmpty(country)) {
                returnVal += ", " + country;
            }
        }
        else
            returnVal = StringUtils.joinWith(", ", name, street, city, country);
        return returnVal;
    }


}
