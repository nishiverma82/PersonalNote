package com.example.nishchal.personalnote.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class BudgetData extends SQLiteOpenHelper{

    private static String DATABASE_NAME = "BudgetData";
    private static String TABLE_NAME = "budget_data";
    private static String BUDGET_DATE = "budget_date";
    private static String BUDGET_TIME = "budget_time";
    private static String BUDGET_CATEGORY = "budget_category";
    private static String BUDGET_NAME = "budget_name";
    private static String BUDGET_AMOUNT = "budget_amount";
    private static String BUDGET_DATETIME = "budget_datetime";

    public BudgetData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_QUERY =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        BUDGET_DATE + " TEXT, " +
                        BUDGET_TIME + " TEXT, " +
                        BUDGET_CATEGORY + " TEXT, " +
                        BUDGET_NAME + " TEXT, " +
                        BUDGET_AMOUNT + " INT, " +
                        BUDGET_DATETIME + " TEXT PRIMARY KEY);";
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    public boolean insertData(String date, String time, String category, String name, int amount, String datetime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUDGET_DATE, date);
        contentValues.put(BUDGET_TIME, time);
        contentValues.put(BUDGET_CATEGORY, category);
        contentValues.put(BUDGET_NAME, name);
        contentValues.put(BUDGET_AMOUNT, amount);
        contentValues.put(BUDGET_DATETIME, datetime);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;

    }

    //read count of entries for each date and total amount on that date
    public ArrayList<HashMap<String, String>> readByDate(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + BUDGET_DATE + ", COUNT(*), SUM(" + BUDGET_AMOUNT + ") FROM " + TABLE_NAME + " GROUP BY " + BUDGET_DATE + ";", null);
        if(cursor != null) {
            cursor.moveToFirst();
            for(int i=1; i<=cursor.getCount(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("0", cursor.getString(0));
                map.put("1", cursor.getString(1));
                map.put("2", cursor.getString(2));
                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;

    }

    public ArrayList<HashMap<String, String>> readByTime(String date){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor cursor =
                db.rawQuery("SELECT " + BUDGET_TIME + ", " + BUDGET_CATEGORY + ", " + BUDGET_NAME + ", " + BUDGET_AMOUNT + ", " + BUDGET_DATETIME +
                " FROM " + TABLE_NAME +
                " WHERE " + BUDGET_DATE + " = ? ", new String[] {date});
        if(cursor != null) {
            cursor.moveToFirst();
            for(int i=1; i<=cursor.getCount(); i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("0", cursor.getString(0));//time
                map.put("1", cursor.getString(1));//category
                map.put("2", cursor.getString(2));//name
                map.put("3", cursor.getString(3));//amount
                map.put("4", cursor.getString(4));//datetime
                list.add(map);
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public Boolean updateItem(String date, String time, String category, String name, int amount, String datetime, String previous_datetime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUDGET_DATE, date);
        contentValues.put(BUDGET_TIME, time);
        contentValues.put(BUDGET_CATEGORY, category);
        contentValues.put(BUDGET_NAME, name);
        contentValues.put(BUDGET_AMOUNT, amount);
        contentValues.put(BUDGET_DATETIME, datetime);
        String whereClause = BUDGET_DATETIME + "=?";
        String whereArg[] = {previous_datetime};
        long result = db.update(TABLE_NAME, contentValues, whereClause, whereArg);
        db.close();
        return result != -1;
    }

    public Boolean deleteItem(String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = BUDGET_DATETIME + "=?";
        String whereArgs[] = {datetime};
        long result = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        return  result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
