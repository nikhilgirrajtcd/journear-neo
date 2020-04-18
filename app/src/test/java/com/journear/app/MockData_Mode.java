package com.journear.app;

import java.io.Serializable;

public class MockData_Mode implements Serializable {
    public enum Mode {ANY, WALK, TAXI}
    private Mode mode = Mode.ANY;
    public MockData_Mode(){

    }
    public MockData_Mode(Mode mode){
        this.mode = mode;
    }
    public Mode getMode() {
        return mode;
    }
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    /**
     * Checks if modes match
     *
     * @param m1 preferred mode of user 1
     * @param m2 preferred mode of user 2
     * @return true if mode ok false otherwise
     */
    public static boolean checkMode(Mode m1, Mode m2) {
        return m1 == Mode.ANY || m2 == Mode.ANY || m1 == m2;
    }
    public boolean checkPreferences(MockData_Mode otherPreferences) {
        return checkMode(this.mode, otherPreferences.mode);
    }
}
