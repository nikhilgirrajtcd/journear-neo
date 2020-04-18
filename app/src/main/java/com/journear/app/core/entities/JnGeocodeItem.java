package com.journear.app.core.entities;

import android.location.Geocoder;

public class JnGeocodeItem {
    public String id;
    public double latitude = 0.0;
    public double longitude = 0.0;
    public String name;
    public String street;
    public String city;
    public String country;

    @Override
    public String toString()
    {
        return name + ", " + street + "\n" + city + ", " + country;
    }


}
