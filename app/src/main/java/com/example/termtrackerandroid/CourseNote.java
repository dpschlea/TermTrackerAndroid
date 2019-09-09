package com.example.termtrackerandroid;

import android.content.ContentValues;
import android.content.Context;

public class CourseNote {
    public long courseNoteID;
    public long courseID;
    public String text;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DB.COURSE_NOTE_COURSE_ID, courseID);
        values.put(DB.COURSE_NOTE_TEXT, text);
        context.getContentResolver().update(DP.COURSE_NOTES_URI, values, DB.COURSE_NOTES_TABLE_ID
                + " = " + courseNoteID, null);
    }
}