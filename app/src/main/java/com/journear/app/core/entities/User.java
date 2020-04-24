package com.journear.app.core.entities;

import com.journear.app.core.interfaces.Persistable;

public class User extends UserSkimmed implements Persistable {

    public String email;
    public String password;
    public String phoneValue;
    public String dobValue;

    public User(){

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phoneValue;
    }

    public void setPhoneValue(String phoneValue) {
        this.phoneValue = phoneValue;
    }

    public String getDob() {
        return dobValue;
    }

    public void setDobValue(String dobValue) {
        this.dobValue = dobValue;
    }





}
