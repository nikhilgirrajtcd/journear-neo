package com.journear.app.map.utils;

import java.io.Serializable;
import java.util.Date;
import com.jjoe64.graphview.series.*;
/**
 * default data point implementation.
 * This stores the x and y values.
 *
 * @author jjoe64
 */
public class DataPoint implements DataPointInterface, Serializable {
    private static final long serialVersionUID=1428263322645L;

    private double x;
    private double y;

    public DataPoint(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public DataPoint(Date x, double y) {
        this.x = x.getTime();
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "["+x+"/"+y+"]";
    }
}
