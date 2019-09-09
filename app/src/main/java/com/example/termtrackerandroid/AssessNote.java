package com.example.termtrackerandroid;

import android.content.ContentValues;
import android.content.Context;

public class AssessNote {
    public long assessNoteID;
    public long assessID;
    public String text;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DB.ASSESS_NOTE_ASSESS_ID, assessID);
        values.put(DB.ASSESS_NOTE_TEXT, text);
        context.getContentResolver().update(DP.ASSESS_NOTES_URI, values, DB.ASSESS_NOTES_TABLE_ID
                + " = " + assessNoteID, null);
    }
}
