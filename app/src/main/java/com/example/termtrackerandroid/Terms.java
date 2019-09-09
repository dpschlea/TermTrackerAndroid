package com.example.termtrackerandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class Terms {
    public long termID;
    public String name;
    public String start;
    public String end;
    public int active;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DB.TERM_NAME, name);
        values.put(DB.TERM_START, start);
        values.put(DB.TERM_END, end);
        values.put(DB.TERM_ACTIVE, active);
        context.getContentResolver().update(DP.TERMS_URI, values, DB.TERMS_TABLE_ID
                + " = " + termID, null);
    }

    public long getClassCount(Context context) {
        Cursor cursor = context.getContentResolver().query(DP.COURSES_URI, DB.COURSES_COLUMNS,
                DB.COURSE_TERM_ID + " = " + this.termID, null, null);
        int numRows = cursor.getCount();
        return numRows;
    }

    public void deactivate(Context context) {
        this.active = 0;
        saveChanges(context);
    }

    public void activate(Context context) {
        this.active = 1;
        saveChanges(context);
    }
}