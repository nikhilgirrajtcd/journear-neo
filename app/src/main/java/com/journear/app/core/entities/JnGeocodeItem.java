package com.journear.app.core.entities;

import java.io.Serializable;

public class JnGeocodeItem implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

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
