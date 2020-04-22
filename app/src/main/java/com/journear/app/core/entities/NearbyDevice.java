package com.journear.app.core.entities;


import android.os.Parcel;
import android.os.Parcelable;

import com.journear.app.core.interfaces.Persistable;
import com.journear.app.core.utils.JnGeocoder;

import java.sql.Time;
import java.util.Objects;
import java.util.Random;

public class NearbyDevice implements Parcelable, Persistable {
    private int id;

    @Deprecated
    private String source;

    @Deprecated
    private String destination;

    private JnGeocodeItem source2;
    private JnGeocodeItem destination2;

    public NearbyDevice(JnGeocodeItem s, JnGeocodeItem d, Time timeOfTravel, UserSkimmed userSkimmed) {
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

    public NearbyDevice() {

    }

    protected NearbyDevice(Parcel in) {
        user = new UserSkimmed();
        user.setName(in.readString());
        source2 = JnGeocoder.getJnGeocodeItemById(in.readString());
        destination2 = JnGeocoder.getJnGeocodeItemById(in.readString());
        travelTime = Time.valueOf(in.readString());
    }

    public static final Creator<NearbyDevice> CREATOR = new Creator<NearbyDevice>() {
        @Override
        public NearbyDevice createFromParcel(Parcel in) {
            return new NearbyDevice(in);
        }

        @Override
        public NearbyDevice[] newArray(int size) {
            return new NearbyDevice[size];
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
        dest.writeString(user.name);
        dest.writeString(source2.id);
        dest.writeString(destination2.id);
        dest.writeString(travelTime.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;

        if (!(obj instanceof NearbyDevice))
            return false;

        NearbyDevice nd = (NearbyDevice) obj;
        boolean userEq = false;
        if(this.user == null && nd.user == null)
            userEq = true;
        else
            userEq = this.user.isSameAs(nd.user);

        return userEq && Objects.equals(this.source2, nd.source2)
                && Objects.equals(this.destination2, nd.destination2)
                && Objects.equals(this.travelTime, nd.travelTime);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(user, source2, destination2, travelTime);
    }


    public static Time CurrentTime()
    {
        return new Time(System.currentTimeMillis());
    }

    public static NearbyDevice getDummy() {
        JnGeocodeItem source = new JnGeocodeItem();
        source.id = "350258";
        source.longitude = -6.3241865;
        source.latitude = 54.2184922;
        source.placeString = "Sheephill";

        JnGeocodeItem destination = new JnGeocodeItem();
        destination.id = "351437";
        destination.longitude = -6.0245976;
        destination.latitude = 54.5086153;
        destination.placeString = "Saintfield Road";

        UserSkimmed user = new UserSkimmed();
        user.name = "dummy" + (new Random()).nextInt();
        user.setGender("M");

        return new NearbyDevice(source, destination, CurrentTime(), user);
    }
}