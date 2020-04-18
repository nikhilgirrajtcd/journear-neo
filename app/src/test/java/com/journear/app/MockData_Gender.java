package com.journear.app;

import java.io.Serializable;
public class MockData_Gender implements Serializable {
    public enum Gender {ANY, MALE, FEMALE}
    private Gender preferredGender = Gender.ANY;
    private Gender userGender = Gender.ANY;

    public MockData_Gender() {
    }

    public MockData_Gender(Gender preferredGender, Gender userGender) {
        this.preferredGender = preferredGender;
        this.userGender = userGender;
    }

    public Gender getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(Gender preferredGender) {
        this.preferredGender = preferredGender;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public static boolean checkGender(Gender prefGender1, Gender prefGender2,
                                      Gender userGender1, Gender userGender2) {
        return prefGender1 == Gender.ANY && prefGender2 == Gender.ANY
                || prefGender1 == Gender.ANY && prefGender2 == userGender1
                || prefGender1 == userGender2 && prefGender2 == Gender.ANY
                || prefGender1 == userGender2 && prefGender2 == userGender1;
    }

    public boolean checkPreferences(MockData_Gender otherPreferences) {
        return checkGender(this.preferredGender, otherPreferences.preferredGender,
                this.userGender, otherPreferences.userGender);
    }


}
