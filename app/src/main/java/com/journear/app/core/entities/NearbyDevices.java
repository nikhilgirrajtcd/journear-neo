package com.journear.app.core.entities;



import android.os.Parcel;
import android.os.Parcelable;

import com.journear.app.core.interfaces.Persistable;

import java.io.Serializable;
import java.sql.Time;

public class NearbyDevices implements Parcelable, Persistable {
        private int id;
        private String source;
        private String destination;
        private Time travelTime;
        private String user_rating;
    private UserSkimmed user = new UserSkimmed();

    public UserSkimmed getUser() {
        return user;
    }

    public void setUser(UserSkimmed user) {
        this.user = user;
    }

        public NearbyDevices(){

        }
        public NearbyDevices(String source,String destination, Time travelTime, String user_rating){
            this.source = source;
            this.destination = destination;
            this.travelTime = travelTime;
            this.user_rating = user_rating;
        }

      public NearbyDevices(String source,String destination, Time travelTime){
            this.source = source;
            this.destination = destination;
            this.travelTime = travelTime;

        }

        public NearbyDevices(int id, String source, String destination, Time travelTime, String user_rating){
            this.id = id;
            this.source = source;
            this.destination = destination;
            this.travelTime = travelTime;
            this.user_rating = user_rating;

        }


    protected NearbyDevices(Parcel in) {

        source = in.readString();
        destination = in.readString();
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

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public Time getTravelTime() {
            return travelTime;
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

        dest.writeString(source);
        dest.writeString(destination);
        dest.writeString(travelTime.toString());

    }
}