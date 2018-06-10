package com.example.nishchal.personalnote.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DiaryData extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "DiaryData";
    private static String TABLE_NAME = "diary_data";
    private static String DIARY_DATE = "diary_date";
    private static String DIARY_TOPIC = "diary_topic";
    private static String DIARY_CONTENT = "diary_content";
    private static String DIARY_DATETIME = "diary_datetime";


    public DiaryData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_QUERY =
                "CREATE TABLE " + TABLE_NAME + "(" +
                DIARY_DATE + " TEXT, " +
                DIARY_TOPIC + " TEXT, " +
                DIARY_CONTENT + " TEXT, " +
                DIARY_DATETIME + " TEXT PRIMARY KEY);";
        sqLiteDatabase.execSQL(CREATE_QUERY);

    }

    public boolean insertData(String date, String topic, String content, String datetime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_DATE, date);
        contentValues.put(DIARY_TOPIC, topic);
        contentValues.put(DIARY_CONTENT, content);
        contentValues.put(DIARY_DATETIME, datetime);
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

                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;

    }

    public Boolean updateItem(String date, String topic, String content, String current_datetime, String previous_datetime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIARY_DATE, date);
        contentValues.put(DIARY_TOPIC, topic);
        contentValues.put(DIARY_CONTENT, content);
        contentValues.put(DIARY_DATETIME, current_datetime);
        String whereClause = DIARY_DATETIME + "=?";
        String whereArg[] = {previous_datetime};
        long result = db.update(TABLE_NAME, contentValues, whereClause, whereArg);
        db.close();
        return result != -1;
    }

    public Boolean deleteItem(String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = DIARY_DATETIME + "=?";
        String whereArgs[] = {datetime};
        long result = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        return  result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
