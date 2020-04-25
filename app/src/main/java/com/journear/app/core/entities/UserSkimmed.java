package com.journear.app.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.journear.app.core.interfaces.Persistable;

import org.apache.commons.lang3.StringUtils;

public class UserSkimmed implements Persistable, Parcelable {
    public String getUserId() {
        return userId;
    }

    private String userId;
    private String rating;

    public String name;

    public String gender;

    public UserSkimmed(){}

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    protected UserSkimmed(Parcel in) {
        userId = in.readString();
        name = in.readString();
        gender = in.readString();
        rating = in.readString();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static final Creator<UserSkimmed> CREATOR = new Creator<UserSkimmed>() {
        @Override
        public UserSkimmed createFromParcel(Parcel in) {
            return new UserSkimmed(in);
        }

        @Override
        public UserSkimmed[] newArray(int size) {
            return new UserSkimmed[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSameAs(UserSkimmed user)
    {
        if(user == null || StringUtils.isEmpty(user.name) || StringUtils.isEmpty(this.name))
            return false;
        return this.name.equals(user.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeString(rating);
    }
}
