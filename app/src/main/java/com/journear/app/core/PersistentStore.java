package com.journear.app.core;

import com.journear.app.core.entities.DuplicateItemException;
import com.journear.app.core.interfaces.Persistable;

public class PersistentStore {
    static PersistentStore singleton = null;
    public static PersistentStore getInstance() {
        if(singleton == null)
            singleton = new PersistentStore();
        return singleton;
    }

    // Todo: Akshay
    public Persistable getItem(String key, Persistable type) {
        throw new UnsupportedOperationException("implementation pending");
    }

    // Todo: Akshay - AndroidX Jetpack
    public void setItem(String key, Persistable item, boolean replace) throws DuplicateItemException {
        // jsonString = serializeObject(item);
        // fieldName = key
        throw new UnsupportedOperationException("implementation pending");
    }
}