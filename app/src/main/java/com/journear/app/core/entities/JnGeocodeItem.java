package com.journear.app.core.entities;

import com.journear.app.core.utils.JnGeocoder;

import java.io.Serializable;
import java.util.Objects;

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


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof JnGeocodeItem))
            return false;

        JnGeocodeItem j = (JnGeocodeItem) obj;
        return Objects.equals(this.id, j.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
