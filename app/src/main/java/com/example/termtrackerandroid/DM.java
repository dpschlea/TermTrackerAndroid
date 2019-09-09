package com.example.termtrackerandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DM {

    // Terms
    public static Uri insertTerm(Context context, String termName, String termStart, String termEnd, int termActive) {
        ContentValues values = new ContentValues();
        values.put(DB.TERM_NAME, termName);
        values.put(DB.TERM_START, termStart);
        values.put(DB.TERM_END, termEnd);
        values.put(DB.TERM_ACTIVE, termActive);
        Uri termUri = context.getContentResolver().insert(DP.TERMS_URI, values);
        return termUri;
    }

    public static Terms getTerm(Context context, long termID) {
        Cursor cursor = context.getContentResolver().query(DP.TERMS_URI, DB.TERMS_COLUMNS,
                DB.TERMS_TABLE_ID + " = " + termID, null, null);
        cursor.moveToFirst();
        String termName = cursor.getString(cursor.getColumnIndex(DB.TERM_NAME));
        String termStart = cursor.getString(cursor.getColumnIndex(DB.TERM_START));
        String termEnd = cursor.getString(cursor.getColumnIndex(DB.TERM_END));
        int termActive = cursor.getInt(cursor.getColumnIndex(DB.TERM_ACTIVE));

        Terms t = new Terms();
        t.termID = termID;
        t.name = termName;
        t.start = termStart;
        t.end = termEnd;
        t.active = termActive;
        return t;
    }

    // Courses
    public static Uri insertCourse(Context context, long termId, String courseName, String courseStart, String courseEnd,
                                   String courseMentor, String courseMentorPhone, String courseMentorEmail, CourseStatus status) {
        ContentValues values = new ContentValues();
        values.put(DB.COURSE_TERM_ID, termId);
        values.put(DB.COURSE_NAME, courseName);
        values.put(DB.COURSE_START, courseStart);
        values.put(DB.COURSE_END, courseEnd);
        values.put(DB.COURSE_MENTOR, courseMentor);
        values.put(DB.COURSE_MENTOR_PHONE, courseMentorPhone);
        values.put(DB.COURSE_MENTOR_EMAIL, courseMentorEmail);
        values.put(DB.COURSE_STATUS, status.toString());
        Uri courseUri = context.getContentResolver().insert(DP.COURSES_URI, values);
        return courseUri;
    }

    public static Courses getCourse(Context context, long courseID) {
        Cursor cursor = context.getContentResolver().query(DP.COURSES_URI, DB.COURSES_COLUMNS,
                DB.COURSES_TABLE_ID + " = " + courseID, null, null);
        cursor.moveToFirst();
        Long termID = cursor.getLong(cursor.getColumnIndex(DB.COURSE_TERM_ID));
        String courseName = cursor.getString(cursor.getColumnIndex(DB.COURSE_NAME));
        String courseDesc = cursor.getString(cursor.getColumnIndex(DB.COURSE_DESCRIPTION));
        String courseStart = cursor.getString(cursor.getColumnIndex(DB.COURSE_START));
        String courseEnd = cursor.getString(cursor.getColumnIndex(DB.COURSE_END));
        CourseStatus courseStatus = CourseStatus.valueOf(cursor.getString(cursor.getColumnIndex(DB.COURSE_STATUS)));
        String Mentor = cursor.getString(cursor.getColumnIndex(DB.COURSE_MENTOR));
        String MentorPhone = cursor.getString(cursor.getColumnIndex(DB.COURSE_MENTOR_PHONE));
        String MentorEmail = cursor.getString(cursor.getColumnIndex(DB.COURSE_MENTOR_EMAIL));
        int courseNotifi = (cursor.getInt(cursor.getColumnIndex(DB.COURSE_NOTIFICATIONS)));

        Courses c = new Courses();
        c.courseID = courseID;
        c.termID = termID;
        c.name = courseName;
        c.desc = courseDesc;
        c.start = courseStart;
        c.end = courseEnd;
        c.status = courseStatus;
        c.mentor = Mentor;
        c.mentorPhone = MentorPhone;
        c.mentorEmail = MentorEmail;
        c.notifi = courseNotifi;
        return c;
    }

    public static boolean deleteCourse(Context context, long courseId) {
        Cursor notesCursor = context.getContentResolver().query(DP.COURSE_NOTES_URI,
                DB.COURSE_NOTES_COLUMNS, DB.COURSE_NOTE_COURSE_ID + " = " + courseId,
                null, null);
        while (notesCursor.moveToNext()) {
            deleteCourseNote(context, notesCursor.getLong(notesCursor.getColumnIndex(DB.COURSE_NOTES_TABLE_ID)));
        }
        context.getContentResolver().delete(DP.COURSES_URI, DB.COURSES_TABLE_ID + " = "
                + courseId, null);
        return true;
    }

    // Course Notes
    public static Uri insertCourseNote(Context context, long courseId, String text) {
        ContentValues values = new ContentValues();
        values.put(DB.COURSE_NOTE_COURSE_ID, courseId);
        values.put(DB.COURSE_NOTE_TEXT, text);
        Uri courseNoteUri = context.getContentResolver().insert(DP.COURSE_NOTES_URI, values);
        return courseNoteUri;
    }

    public static CourseNote getCourseNote(Context context, long courseNoteId) {
        Cursor cursor = context.getContentResolver().query(DP.COURSE_NOTES_URI, DB.COURSE_NOTES_COLUMNS,
                DB.COURSE_NOTES_TABLE_ID + " = " + courseNoteId, null, null);
        cursor.moveToFirst();
        long courseId = cursor.getLong(cursor.getColumnIndex(DB.COURSE_NOTE_COURSE_ID));
        String text = cursor.getString(cursor.getColumnIndex(DB.COURSE_NOTE_TEXT));

        CourseNote c = new CourseNote();
        c.courseNoteID = courseNoteId;
        c.courseID = courseId;
        c.text = text;
        return c;
    }

    public static boolean deleteCourseNote(Context context, long courseNoteId) {
        context.getContentResolver().delete(DP.COURSE_NOTES_URI, DB.COURSE_NOTES_TABLE_ID + " = " + courseNoteId, null);
        return true;
    }

    // Assessments
    public static Uri insertAssess(Context context, long courseId, String code, String name, String description, String datetime) {
        ContentValues values = new ContentValues();
        values.put(DB.ASSESS_COURSE_ID, courseId);
        values.put(DB.ASSESS_CODE, code);
        values.put(DB.ASSESS_NAME, name);
        values.put(DB.ASSESS_DESCRIPTION, description);
        values.put(DB.ASSESS_DATETIME, datetime);
        Uri assessmentUri = context.getContentResolver().insert(DP.ASSESS_URI, values);
        return assessmentUri;
    }

    public static Assessments getAssess(Context context, long assessID) {
        Cursor cursor = context.getContentResolver().query(DP.ASSESS_URI, DB.ASSESS_COLUMNS,
                DB.ASSESS_TABLE_ID + " = " + assessID, null, null);
        cursor.moveToFirst();
        long courseID = cursor.getLong(cursor.getColumnIndex(DB.ASSESS_COURSE_ID));
        String name = cursor.getString(cursor.getColumnIndex(DB.ASSESS_NAME));
        String desc = cursor.getString(cursor.getColumnIndex(DB.ASSESS_DESCRIPTION));
        String code = cursor.getString(cursor.getColumnIndex(DB.ASSESS_CODE));
        String datetime = cursor.getString(cursor.getColumnIndex(DB.ASSESS_DATETIME));
        int notifi = cursor.getInt(cursor.getColumnIndex(DB.ASSESS_NOTIFICATIONS));

        Assessments a = new Assessments();
        a.assessID = assessID;
        a.courseID = courseID;
        a.name = name;
        a.desc = desc;
        a.code = code;
        a.datetime = datetime;
        a.notifi = notifi;
        return a;
    }

    public static boolean deleteAssess(Context context, long assessmentId) {
        Cursor notesCursor = context.getContentResolver().query(DP.ASSESS_NOTES_URI,
                DB.ASSESS_NOTES_COLUMNS, DB.ASSESS_NOTE_ASSESS_ID + " = " +
                        assessmentId, null, null);
        while (notesCursor.moveToNext()) {
            deleteAssessNote(context, notesCursor.getLong(notesCursor.getColumnIndex(DB.ASSESS_NOTES_TABLE_ID)));
        }
        context.getContentResolver().delete(DP.ASSESS_URI, DB.ASSESS_TABLE_ID
                + " = " + assessmentId, null);
        return true;
    }

    // Assessment Notes
    public static Uri insertAssessNote(Context context, long assessmentId, String text) {
        ContentValues values = new ContentValues();
        values.put(DB.ASSESS_NOTE_ASSESS_ID, assessmentId);
        values.put(DB.ASSESS_NOTE_TEXT, text);
        Uri assessmentNoteUri = context.getContentResolver().insert(DP.ASSESS_NOTES_URI, values);
        return assessmentNoteUri;
    }

    public static AssessNote getAssessNote(Context context, long assessmentNoteId) {
        Cursor cursor = context.getContentResolver().query(DP.ASSESS_NOTES_URI,
                DB.ASSESS_NOTES_COLUMNS, DB.ASSESS_NOTES_TABLE_ID + " = "
                        + assessmentNoteId, null, null);
        cursor.moveToFirst();
        long assessmentId = cursor.getLong(cursor.getColumnIndex(DB.ASSESS_NOTE_ASSESS_ID));
        String text = cursor.getString(cursor.getColumnIndex(DB.ASSESS_NOTE_TEXT));

        AssessNote a = new AssessNote();
        a.assessNoteID = assessmentNoteId;
        a.assessID = assessmentId;
        a.text = text;
        return a;
    }

    public static boolean deleteAssessNote(Context context, long assessmentNoteId) {
        context.getContentResolver().delete(DP.ASSESS_NOTES_URI, DB.ASSESS_NOTES_TABLE_ID
                + " = " + assessmentNoteId, null);
        return true;
    }
}