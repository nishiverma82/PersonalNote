package com.example.nishchal.personalnote.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class NotesData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesData";
    private static final String TABLE_NAME = "notes_data";
    private static final String NOTE_HEADING = "note_heading";
    private static final String NOTE_CONTENT = "note_content";
    private static final String NOTE_DATETIME = "note_datetime";

    public NotesData(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_QUERY =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        NOTE_HEADING + " TEXT, " +
                        NOTE_CONTENT + " TEXT, " +
                        NOTE_DATETIME + " TEXT PRIMARY KEY);";
        sqLiteDatabase.execSQL(CREATE_QUERY);

    }

    public boolean insertData(String heading, String content, String datetime) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_HEADING, heading);
        contentValues.put(NOTE_CONTENT, content);
        contentValues.put(NOTE_DATETIME, datetime);
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
                 list.add(map);
                 cursor.moveToNext();
             }
             cursor.close();
         }
         db.close();
         return list;
     }

    public Boolean updateItem(String heading, String content, String current_datetime, String previous_datetime) {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(NOTE_HEADING, heading);
         contentValues.put(NOTE_CONTENT, content);
         contentValues.put(NOTE_DATETIME, current_datetime);
         String whereClause = NOTE_DATETIME + "=?";
         String whereArg[] = {previous_datetime};
         long result = db.update(TABLE_NAME, contentValues, whereClause, whereArg);
         db.close();
         return result != -1;
    }

    public Boolean deleteItem(String datetime){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = NOTE_DATETIME + "=?";
        String whereArgs[] = {datetime};
        long result = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
        return  result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
