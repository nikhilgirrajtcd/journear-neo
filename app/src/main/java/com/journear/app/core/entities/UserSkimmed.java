package com.journear.app.core.entities;

import com.journear.app.core.interfaces.Persistable;

import org.apache.commons.lang3.StringUtils;

public class UserSkimmed implements Persistable {
    public String name;

    public String gender;

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
}
