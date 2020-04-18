
package com.journear.app.core;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.journear.app.core.db.SqliteDbHelper;
import com.journear.app.core.entities.StringWrapper;
import com.journear.app.core.interfaces.Persistable;

public class PersistentStore {
    static PersistentStore singleton = null;
    private  Gson gson;
    SqliteDbHelper sqliteDbHelper;
    private static Context creatingContext ;


    public Gson getGson() {
        if(this.gson == null){
            gson = new Gson();
        }
        return gson;
    }

    public static PersistentStore getInstance(Context context) {
        if(singleton == null) {
            creatingContext = context;
            singleton = new PersistentStore();

        }
        return singleton;
    }

    private SqliteDbHelper getSqliteDbHelper() {
        if(sqliteDbHelper == null)
            sqliteDbHelper = new SqliteDbHelper(creatingContext);
        return sqliteDbHelper;
    }
    // Todo: Akshay
    public Persistable getItem(String key, Class type) {
        String jsonString = getSqliteDbHelper().selectFromKvStore(key);
        return (Persistable) getGson().fromJson(jsonString, type);
    }
    public void setItem(String key, String item, boolean replace) {
        setItem(key, new StringWrapper(item), replace);
    }

    // Todo: Akshay - AndroidX Jetpack
    public void setItem(String key, Persistable item, boolean replace){ /*throws DuplicateItemException */

        String jsonString = getGson().toJson(item);
        //String insertQuery = "INSERT INTO JnKvStore (FieldName, Content) VALUES (" + key + ", \"" + jsonString + "\")";

        ContentValues contentValues = new ContentValues();
        contentValues.put("FieldName", key);
        contentValues.put("Class", "class1");
        contentValues.put("Content", jsonString);


        getSqliteDbHelper().insertInKvStore(contentValues);
    }
}