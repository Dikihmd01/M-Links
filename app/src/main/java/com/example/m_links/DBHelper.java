package com.example.m_links;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DBHelper extends SQLiteOpenHelper {
    private static final String databaseName = "mlinks";
    private static final String tableAuth = "auth";
    private static final String tableTools = "tools";

    public DBHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateTableAuth = "CREATE TABLE " + tableAuth + "(username TEXT PRIMARY KEY, password TEXT, is_admin INTEGER)";
        String queryCreateTableTools = "CREATE TABLE " + tableTools + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, " +
                "link TEXT, logo BLOB, is_accepted INTEGER)";

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

    public void acceptData(Integer id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "UPDATE tools SET is_accepted=1 WHERE _id=" + id;
        database.execSQL(query);
    }

    public Boolean insertData(String title, String description, String link, byte[] logo) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("link", link);
        contentValues.put("logo", logo);
        contentValues.put("is_accepted", 0);
        long result = database.insert("tools", null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean updateDate(String title, String description, String link, Integer id, byte[] logo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("link", link);
        contentValues.put("logo", logo);

        long result = database.update("tools", contentValues, "_id=" + id, null);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean deleteData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        long result = database.delete("tools", "_id=" + id, null);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean insertAdmin(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("is_admin", 1);

        long result = database.insert("auth", null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean insertAuth(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("is_admin", 0);

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

    public Boolean checkLink(String link) {
        return URLUtil.isValidUrl(link) && Patterns.WEB_URL.matcher(link).matches();
    }

    public Cursor viewData() {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableTools;
        Cursor cursor = database.rawQuery(query, null);

        return cursor;
    }
}
