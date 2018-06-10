package com.example.nishchal.personalnote.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class PassStoreData extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "PassStoreData";
    private static String TABLE_NAME = "pass_store_data";
    private static String PASS_WEB_URL = "pass_web_url";
    private static String PASS_USERNAME_EMAIL = "pass_username_email";
    private static String PASS_PASSWORD = "pass_password";
    private static String PASS_ADDITIONAL_NOTES = "pass_additional_notes";
    private static String PASS_DATETIME = "pass_datetime";

    public PassStoreData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_QUERY =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        PASS_WEB_URL + " TEXT, " +
                        PASS_USERNAME_EMAIL + " TEXT, " +
                        PASS_PASSWORD + " TEXT, " +
                        PASS_ADDITIONAL_NOTES + " TEXT, " +
                        PASS_DATETIME + " TEXT, " +
                        "PRIMARY KEY (" + PASS_WEB_URL + ", " + PASS_USERNAME_EMAIL + "));";
        sqLiteDatabase.execSQL(CREATE_QUERY);

    }

    public boolean insertData(String web_url, String username_email, String pass_password, String pass_additional_notes, String datetime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASS_WEB_URL, web_url);
        contentValues.put(PASS_USERNAME_EMAIL, username_email);
        contentValues.put(PASS_PASSWORD, pass_password);
        contentValues.put(PASS_ADDITIONAL_NOTES, pass_additional_notes);
        contentValues.put(PASS_DATETIME, datetime);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;

    }

    public ArrayList<HashMap<String, String>> readData(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if(cursor != null) {
            cursor.moveToFirst();
            for(int i=1; i<=cursor.getCount(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("0", cursor.getString(0));
                map.put("1", cursor.getString(1));
                map.put("2", cursor.getString(2));
                map.put("3", cursor.getString(3));
                map.put("4", cursor.getString(4));
                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;

    }

    public Boolean updateItem(String web_url, String username_email, String password, String additional_notes, String current_datetime, String previous_datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PASS_WEB_URL, web_url);
        contentValues.put(PASS_USERNAME_EMAIL, username_email);
        contentValues.put(PASS_PASSWORD, password);
        contentValues.put(PASS_ADDITIONAL_NOTES, additional_notes);
        contentValues.put(PASS_DATETIME, current_datetime);
        String whereClause = PASS_DATETIME + "=?";
        String whereArgs[] = {previous_datetime};
        long result = db.update(TABLE_NAME, contentValues, whereClause, whereArgs);
        db.close();
        return result != -1;
    }

    public Boolean deleteItem(String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = PASS_DATETIME + "=?";
        String whereArgs[] = {datetime};
        long result = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        return  result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
