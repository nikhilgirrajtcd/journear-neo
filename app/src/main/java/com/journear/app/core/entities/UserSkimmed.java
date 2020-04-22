package com.journear.app.core.entities;

import com.journear.app.core.interfaces.Persistable;

import org.apache.commons.lang3.StringUtils;

public class UserSkimmed implements Persistable {
    public String userName;

    public String gender;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSameAs(UserSkimmed user)
    {
        if(user == null || StringUtils.isEmpty(user.userName) || StringUtils.isEmpty(this.userName))
            return false;
        return this.userName.equals(user.userName);
    }
}
