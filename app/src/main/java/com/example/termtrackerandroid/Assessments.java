package com.example.termtrackerandroid;

import android.content.ContentValues;
import android.content.Context;

public class Assessments {
    public long assessID;
    public long courseID;
    public String code;
    public String name;
    public String desc;
    public String datetime;
    public int notifi;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DB.ASSESS_COURSE_ID, courseID);
        values.put(DB.ASSESS_CODE, code);
        values.put(DB.ASSESS_NAME, name);
        values.put(DB.ASSESS_DESCRIPTION, desc);
        values.put(DB.ASSESS_DATETIME, datetime);
        values.put(DB.ASSESS_NOTIFICATIONS, notifi);
        context.getContentResolver().update(DP.ASSESS_URI, values, DB.ASSESS_TABLE_ID
                + " = " + assessID, null);
    }
}
