package com.ananth.databasesample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anantha on 3/20/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contactsDetails.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_IMAGE = "photo";
    public static final String CONTACTS_NAME = "name";
    public static final String CONTACTS_LOCATION = "location";
    public static final String CONTACTS_EMAIL = "email";
    public static final String CONTACTS_PHONE = "phone";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table contacts "
                + "(id integer primary key,name text,email text,phone text,location text,photo text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);

    }

    public boolean insertContacts(String name, String email,
                                  String phone, String location, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("location", location);
        values.put("photo", photo);
        db.insert("contacts", null, values);
        System.out.println("insert success :111");
        System.out.println("insert photo :" + photo);
        System.out.println("insert phone :" + phone);
        return true;

    }

    public Boolean getSingleContact(String phone) {
        System.out.println("single :" + phone);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where id=" + phone + "",
                null);
        System.out.println("result :" + res);
        if (res.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean getPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("contacts", null, " phone=?",
                new String[]{phone}, null, null, null);
        if (cursor.getCount() > 1) {
            return true;
        } else {
            return false;
        }

    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numofrows = (int) DatabaseUtils.queryNumEntries(db,
                CONTACTS_TABLE_NAME);
        return numofrows;

    }

    public boolean updateContacts(String name, String email,
                                  String phone, String location, String photo) {

        System.out.println("name update :" + name);
        System.out.println("email update :" + email);
        System.out.println("phone phone :" + phone);
        System.out.println("location location :" + location);
        System.out.println("photo location :" + photo);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("location", location);
        values.put("photo", photo);

        db.update("contacts", values, "phone= ? ",
                new String[]{phone});
        return true;

    }

    public ArrayList getAllContacts() {
        ArrayList<HashMap<String, String>> mInfoList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from contacts", null);
        result.moveToFirst();
        while (result.isAfterLast() == false) {
            HashMap<String, String> mMap = new HashMap<>();
            mMap.put("name", result.getString(result
                    .getColumnIndex(CONTACTS_NAME)));
            mMap.put("email", result.getString(result
                    .getColumnIndex(CONTACTS_EMAIL)));
            mMap.put("location", result.getString(result
                    .getColumnIndex(CONTACTS_LOCATION)));
            mMap.put("phone", result.getString(result
                    .getColumnIndex(CONTACTS_PHONE)));
            mMap.put("image", result.getString(result
                    .getColumnIndex(CONTACTS_IMAGE)));
            System.out.println("got phone :" + result.getString(result
                    .getColumnIndex(CONTACTS_PHONE)));

            mInfoList.add(mMap);
            result.moveToNext();
        }
        return mInfoList;

    }

    public Integer deleteContact(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("contacts", "phone=? ",
                new String[]{phone});

    }
}
