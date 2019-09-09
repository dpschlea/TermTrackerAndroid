package com.example.termtrackerandroid;

import android.content.ContentValues;
import android.content.Context;

public class Courses {
    public long courseID;
    public long termID;
    public String name;
    public String desc;
    public String start;
    public String end;
    public CourseStatus status;
    public String mentor;
    public String mentorPhone;
    public String mentorEmail;
    public int notifi;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DB.COURSE_TERM_ID, termID);
        values.put(DB.COURSE_NAME, name);
        values.put(DB.COURSE_DESCRIPTION, desc);
        values.put(DB.COURSE_START, start);
        values.put(DB.COURSE_END, end);
        values.put(DB.COURSE_STATUS, status.toString());
        values.put(DB.COURSE_MENTOR, mentor);
        values.put(DB.COURSE_MENTOR_PHONE, mentorPhone);
        values.put(DB.COURSE_MENTOR_EMAIL, mentorEmail);
        values.put(DB.COURSE_NOTIFICATIONS, notifi);
        context.getContentResolver().update(DP.COURSES_URI, values, DB.COURSES_TABLE_ID
                + " = " + courseID, null);
    }
}