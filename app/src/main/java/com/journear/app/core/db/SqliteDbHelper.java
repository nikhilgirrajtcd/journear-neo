package com.journear.app.core.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SqliteDbHelper extends SQLiteOpenHelper {
    static String name = "database";
    static int version = 1;



    public SqliteDbHelper(Context context) {
        super(context, name, null, version);


    }

    public void registerUser(ContentValues contentValues) {
        getWritableDatabase().insert("user", "", contentValues);
    }

    public boolean isLoginValid(String username, String password) {
        String sql = "Select count(*) from user where username='" + username + "' and password='" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();

        if (l == 1) {
            return true;

        } else {
            return false;
        }



    }

    public void insertInKvStore(ContentValues contentValues){
        getWritableDatabase().insert("KvStore","", contentValues);

    }
    public String selectFromKvStore(String key){
        try{
            String query = "SELECT `Content` FROM KvStore WHERE `FieldName` = '" + key + "'";
            SQLiteStatement statement = getReadableDatabase().compileStatement(query);
            String val = statement.simpleQueryForString();
            return val;
        } catch (SQLiteDoneException donex){
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String kvStoreCreateTableQuery = "CREATE TABLE if not exists `KvStore` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `FieldName` TEXT, `Class` TEXT, `Content` TEXT)";
        db.execSQL(kvStoreCreateTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
