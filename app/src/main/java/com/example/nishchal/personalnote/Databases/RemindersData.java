package com.example.nishchal.personalnote.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class RemindersData extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "RemindersData";
    private static String TABLE_NAME = "reminders_data";
    private static String REMINDER = "reminder";
    private static String ADDITIONAL_NOTE = "additional_note";
    private static String TIME = "time";
    private static String DATE = "date";
    private static String REMINDER_CATEGORY = "reminder_category";
    private static String REMIND_ME = "remind_me";
    private static String REPEAT_TYPE = "repeat_type";
    private static String REMINDER_TYPE = "reminder_type";
    private static String PHONE_NUMBER = "phone_number";
    private static String EMAIL = "email";
    private static String SEND_MESSAGE = "send_message";
    private static String REMINDERS_DATETIME = "reminders_datetime";

    public RemindersData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_QUERY =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        REMINDER + " TEXT, " +
                        ADDITIONAL_NOTE + " TEXT, " +
                        TIME + " TEXT, " +
                        DATE + " TEXT, " +
                        REMINDER_CATEGORY + " TEXT, " +
                        REMIND_ME + " TEXT, " +
                        REPEAT_TYPE + " TEXT, " +
                        REMINDER_TYPE + " TEXT, " +
                        PHONE_NUMBER + " TEXT, " +
                        EMAIL + " TEXT, " +
                        SEND_MESSAGE + " TEXT, " +
                        REMINDERS_DATETIME + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_QUERY);

    }

    public boolean insertData(String reminder, String additional_note, String time, String date, String reminder_category, String remind_me,
                              String repeat_type, String reminder_type, String phone_number, String email, String send_message, String datetime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER, reminder);
        contentValues.put(ADDITIONAL_NOTE, additional_note);
        contentValues.put(TIME, time);
        contentValues.put(DATE, date);
        contentValues.put(REMINDER_CATEGORY, reminder_category);
        contentValues.put(REMIND_ME, remind_me);
        contentValues.put(REPEAT_TYPE, repeat_type);
        contentValues.put(REMINDER_TYPE, reminder_type);
        contentValues.put(PHONE_NUMBER, phone_number);
        contentValues.put(EMAIL, email);
        contentValues.put(SEND_MESSAGE, send_message);
        contentValues.put(REMINDERS_DATETIME, datetime);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;

    }

    public ArrayList<HashMap<String, String>> readData(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + REMINDER + ", " + TIME + ", " + DATE + ", " + REMINDER_CATEGORY + ", " + REPEAT_TYPE + ", " +
                REMINDERS_DATETIME + " FROM " + TABLE_NAME + ";", null);
        if(cursor != null) {
            cursor.moveToFirst();
            for(int i=1; i<=cursor.getCount(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("0", cursor.getString(0));//reminder
                map.put("1", cursor.getString(1));//time
                map.put("2", cursor.getString(2));//date
                map.put("3", cursor.getString(3));//reminder category
                map.put("4", cursor.getString(4));//repeat type
                map.put("5", cursor.getString(5));//datetime
                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public ArrayList<HashMap<String, String>> readByDateTime(String datetime){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor cursor =
                db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + REMINDERS_DATETIME + " = ? ", new String[] {datetime});
        if(cursor != null) {
            cursor.moveToFirst();
            for(int i=1; i<=cursor.getCount(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("0", cursor.getString(0));//reminder
                map.put("1", cursor.getString(1));//additional note
                map.put("2", cursor.getString(2));//time
                map.put("3", cursor.getString(3));//date
                map.put("4", cursor.getString(4));//reminder category
                map.put("5", cursor.getString(5));//remind me
                map.put("6", cursor.getString(6));//repeat type
                map.put("7", cursor.getString(7));//reminder type
                map.put("8", cursor.getString(8));//phone number
                map.put("9", cursor.getString(9));//email
                map.put("10", cursor.getString(10));//send message
                map.put("11", cursor.getString(11));//datetime
                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;

    }

    public Boolean updateItem(String reminder, String additional_note, String time, String date, String reminder_category, String remind_me,String repeat_type,
                              String reminder_type, String phone_number, String email, String send_message, String c_datetime, String pre_datetime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER, reminder);
        contentValues.put(ADDITIONAL_NOTE, additional_note);
        contentValues.put(TIME, time);
        contentValues.put(DATE, date);
        contentValues.put(REMINDER_CATEGORY, reminder_category);
        contentValues.put(REMIND_ME, remind_me);
        contentValues.put(REPEAT_TYPE, repeat_type);
        contentValues.put(REMINDER_TYPE, reminder_type);
        contentValues.put(PHONE_NUMBER, phone_number);
        contentValues.put(EMAIL, email);
        contentValues.put(SEND_MESSAGE, send_message);
        contentValues.put(REMINDERS_DATETIME, c_datetime);
        String whereClause = REMINDERS_DATETIME + "=?";
        String whereArg[] = {pre_datetime};
        long result = db.update(TABLE_NAME, contentValues, whereClause, whereArg);
        db.close();
        return result != -1;
    }

    public Boolean deleteItem(String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = REMINDERS_DATETIME + "=?";
        String whereArgs[] = {datetime};
        long result = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        return  result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
