package com.journear.app.core.entities;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.journear.app.core.LocalFunctions;
import com.journear.app.core.interfaces.Persistable;
import com.journear.app.core.services.ServiceLocator;
import com.journear.app.core.utils.JnGeocoder;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentNavigableMap;

public class NearbyDevice implements Parcelable, Persistable {
    private String travelPlanId;

    public String getModeOfJourney() {
        return modeOfJourney;
    }

    public void setModeOfJourney(String modeOfJourney) {
        this.modeOfJourney = modeOfJourney;
    }

    private String modeOfJourney;
    private JnGeocodeItem source2;
    private JnGeocodeItem destination2;

    public NearbyDevice() {

    }

    public NearbyDevice(JnGeocodeItem s, JnGeocodeItem d, Time timeOfTravel, UserSkimmed userSkimmed) {
        this.source2 = s;
        this.destination2 = d;
        this.travelTime = timeOfTravel;
        this.owner = userSkimmed;
        getTravelPlanId(); // this will generate the id if there isn't already one
    }

    public NearbyDevice(JnGeocodeItem s, JnGeocodeItem d, Time timeOfTravel, UserSkimmed userSkimmed, boolean preferSameGender, String modeOfJourney) {
        this.source2 = s;
        this.destination2 = d;
        this.travelTime = timeOfTravel;
        this.owner = userSkimmed;
        this.preferSameGender = preferSameGender;
        this.modeOfJourney = modeOfJourney;
        getTravelPlanId(); // this will generate the id if there isn't already one

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
    private UserSkimmed owner = new UserSkimmed();
    private List<UserSkimmed> travellers = new ArrayList<>();

    public UserSkimmed getOwner() {
        return owner;
    }


    public void setOwner(UserSkimmed owner) {
        this.owner = owner;
        getTravelPlanId(); // this will generate the id if there isn't already one
    }

    //Added preferGender boolean in parceable interface fucntions
    protected NearbyDevice(Parcel in) {
        travelPlanId = in.readString();
        owner = new UserSkimmed(in);

        Gson gson = new Gson();
        travellers = Arrays.asList(gson.fromJson(in.readString(), UserSkimmed[].class));

        source2 = JnGeocoder.getJnGeocodeItemById(in.readString());
        destination2 = JnGeocoder.getJnGeocodeItemById(in.readString());
        travelTime = Time.valueOf(in.readString());
        preferSameGender = in.readByte() != 0;
        modeOfJourney = in.readString();
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

    public String getTravelPlanId() {
        if (travelPlanId == null) {
            // if this NearbyDevice ride belongs to the current user, then create the id and set it
            if (this.owner.getUserId().equals(LocalFunctions.getCurrentUser().getUserId())) {
                travelPlanId = StringUtils.leftPad(this.owner.getUserId(), 3) + CurrentTime();
            }
        }
        return travelPlanId;
    }

    public void setTravelPlanId(String travelPlanId) {
        this.travelPlanId = travelPlanId;
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
        dest.writeString(travelPlanId);
        owner.writeToParcel(dest, flags);
        dest.writeString((new Gson()).toJson(travellers.toArray()));
        dest.writeString(source2.id);
        dest.writeString(destination2.id);
        dest.writeString(travelTime.toString());
        dest.writeByte((byte) (preferSameGender ? 1 : 0));
        dest.writeString(modeOfJourney);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof NearbyDevice))
            return false;

        NearbyDevice nd = (NearbyDevice) obj;

        boolean userEq = false;
        if (this.owner == null && nd.owner == null)
            userEq = true;
        else
            userEq = this.owner.isSameAs(nd.owner);

        return userEq && Objects.equals(this.source2, nd.source2)
                && Objects.equals(this.destination2, nd.destination2)
                && Objects.equals(this.travelTime, nd.travelTime)
                && Objects.equals(this.preferSameGender, nd.preferSameGender)
                && StringUtils.equals(this.modeOfJourney, nd.modeOfJourney);
    }

    //Utkarsh : added mode of journey and PreferSameGender
    @Override
    public int hashCode() {
        return Objects.hash(owner, source2, destination2, travelTime, preferSameGender, modeOfJourney);
    }


    public static Time CurrentTime() {
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
        user.setUserId("" + (new Random()).nextInt());
        user.name = "dummy" + user.getUserId();
        user.setGender("M");
        return new NearbyDevice(source, destination, CurrentTime(), user, false, Mode.Any.name());
    }


    public boolean isPreferSameGender() {
        return preferSameGender;
    }

    public void setPreferSameGender(boolean preferSameGender) {
        this.preferSameGender = preferSameGender;
    }

    public boolean getPreferSameGender() {
        return this.preferSameGender;
    }

    private boolean preferSameGender = false;


    public boolean isCompatible(NearbyDevice otherDevice) {
        return this.isGenderCompatible(otherDevice) && isModeCompatible(otherDevice);
    }

    public enum Mode {Any, Walk, Taxi}

    private Mode mode = Mode.Any;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Checks if modes match
     *
     * @param otherDevice the other nearbyDevice to match
     * @return true if mode ok false otherwise
     */
    private boolean isModeCompatible(NearbyDevice otherDevice) {
        return this.checkMode(this.mode, otherDevice.mode);
    }

    /**
     * Checks if modes match
     *
     * @param m1 preferred mode of owner 1
     * @param m2 preferred mode of owner 2
     * @return true if mode ok false otherwise
     */
    public boolean checkMode(Mode m1, Mode m2) {
        return m1 == Mode.Any || m2 == Mode.Any || m1 == m2;
    }

    public boolean isGenderCompatible(NearbyDevice other) {
        return checkGender(this.getOwner().getGender(), this.preferSameGender, other.getOwner().getGender(), other.preferSameGender);
    }

    public static boolean checkGender(String gender1, boolean preferSameGender1, String gender2, boolean preferSameGender2) {
        return preferSameGender1 == false && preferSameGender2 == false
                || preferSameGender1 == false && StringUtils.equalsIgnoreCase(gender2, gender1)
                || StringUtils.equalsIgnoreCase(gender2, gender1) && preferSameGender2 == false
                || StringUtils.equalsIgnoreCase(gender2, gender1);
    }


}