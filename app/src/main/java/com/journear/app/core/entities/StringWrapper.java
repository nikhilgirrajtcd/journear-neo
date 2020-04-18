package com.journear.app.core.entities;

import com.journear.app.core.interfaces.Persistable;

public class StringWrapper implements Persistable {
        String toWrap;
    public StringWrapper(String s) {
    this.toWrap = s;
    }

    @Override
    public String toString() {
        return toWrap;
    }
}
