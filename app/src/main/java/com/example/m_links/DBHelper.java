package com.example.m_links;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String databaseName = "mlinks";
    private static final String tableAuth = "auth";
    private static final String tableTools = "tools";

    public DBHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateTableAuth = "CREATE TABLE " + tableAuth + "(username TEXT PRIMARY KEY, password TEXT)";
        String queryCreateTableTools = "CREATE TABLE " + tableTools + "(" +
                "id TEXT PRIMARY KEY, title TEXT, description TEXT, " +
                "link TEXT, logo BLOB)";

        // Execute queries
        sqLiteDatabase.execSQL(queryCreateTableAuth);
        sqLiteDatabase.execSQL(queryCreateTableTools);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop existing database
        String queryDropTableAuth = "DROP TABLE IF EXISTS " + tableAuth;
        String queryDropTableTools = "DROP TABLE IF EXISTS " + tableTools;

        // Execute queries
        sqLiteDatabase.execSQL(queryDropTableAuth);
        sqLiteDatabase.execSQL(queryDropTableTools);
        onCreate(sqLiteDatabase);
    }

    public Boolean insertAuth(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = database.insert("auth", null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableAuth + " WHERE username = ?";
        Cursor cursor= database.rawQuery(query, new String[] {username});

        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean checkUsernameAndPassword(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableAuth + " WHERE username = ? AND password = ?";
        Cursor cursor= database.rawQuery(query, new String[] {username, password});

        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
