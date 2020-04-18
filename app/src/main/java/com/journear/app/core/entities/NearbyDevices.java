package com.journear.app.core.entities;


import android.os.Parcel;
import android.os.Parcelable;

import com.journear.app.core.interfaces.Persistable;
import com.journear.app.core.utils.JnGeocoder;

import java.sql.Time;

public class NearbyDevices implements Parcelable, Persistable {
    private int id;

    @Deprecated
    private String source;

    @Deprecated
    private String destination;

    private JnGeocodeItem source2;
    private JnGeocodeItem destination2;

    public NearbyDevices(JnGeocodeItem s, JnGeocodeItem d, Time timeOfTravel, UserSkimmed userSkimmed) {
        this.source2 = s;
        this.destination2 = d;
        this.travelTime = timeOfTravel;
        this.user = userSkimmed;
    }

    public JnGeocodeItem getSource2() {
        return source2;
    }

    public void setSource2(JnGeocodeItem source2) {
        this.source2 = source2;
    }

    public void setSource2(String sourceId) {
        this.source2 = JnGeocoder.getJnGeocodeItemById(sourceId);
    }

    public JnGeocodeItem getDestination2() {
        return destination2;
    }

    public void setDestination2(JnGeocodeItem destination2) {
        this.destination2 = destination2;
    }

    public void setDestination2(String destinationId) {
        this.destination2 = JnGeocoder.getJnGeocodeItemById(destinationId);
    }

    private Time travelTime;
    private String user_rating;
    private UserSkimmed user = new UserSkimmed();

    public UserSkimmed getUser() {
        return user;
    }

    public void setUser(UserSkimmed user) {
        this.user = user;
    }

    public NearbyDevices() {

    }

    public NearbyDevices(String source, String destination, Time travelTime, String user_rating) {
        this.source = source;
        this.destination = destination;
        this.travelTime = travelTime;
        this.user_rating = user_rating;
    }

    public NearbyDevices(String source, String destination, Time travelTime) {
        this.source = source;
        this.destination = destination;
        this.travelTime = travelTime;

    }

    public NearbyDevices(int id, String source, String destination, Time travelTime, String user_rating) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.travelTime = travelTime;
        this.user_rating = user_rating;

    }


    protected NearbyDevices(Parcel in) {
        user = new UserSkimmed();
        user.setUserName(in.readString());
        source2 = JnGeocoder.getJnGeocodeItemById(in.readString());
        destination2 = JnGeocoder.getJnGeocodeItemById(in.readString());
        travelTime = Time.valueOf(in.readString());
    }

    public static final Creator<NearbyDevices> CREATOR = new Creator<NearbyDevices>() {
        @Override
        public NearbyDevices createFromParcel(Parcel in) {
            return new NearbyDevices(in);
        }

        @Override
        public NearbyDevices[] newArray(int size) {
            return new NearbyDevices[size];
        }
    };



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Deprecated
    public String getSource() {
        return source;
    }

    @Deprecated
    public void setSource(String source) {
        this.source = source;
    }

    @Deprecated
    public String getDestination() {
        return destination;
    }

    @Deprecated
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Time getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        setTravelTime(Time.valueOf(travelTime));
    }

    public void setTravelTime(Time travelTime) {
        this.travelTime = travelTime;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user.userName);
        dest.writeString(source2.id);
        dest.writeString(destination2.id);
        dest.writeString(travelTime.toString());

    }
}