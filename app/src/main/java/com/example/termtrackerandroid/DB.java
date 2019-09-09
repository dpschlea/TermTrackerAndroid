package com.example.termtrackerandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "wgu_terms.db";
    private static final int DATABASE_VERSION = 1;

    // Columns and tables info

    // Terms
    public static final String TABLE_TERMS = "terms";
    public static final String TERMS_TABLE_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_ACTIVE = "termActive";
    public static final String TERM_CREATED = "termCreated";
    public static final String[] TERMS_COLUMNS = {TERMS_TABLE_ID, TERM_NAME, TERM_START, TERM_END, TERM_ACTIVE, TERM_CREATED};

    // Courses
    public static final String TABLE_COURSES = "courses";
    public static final String COURSES_TABLE_ID = "_id";
    public static final String COURSE_TERM_ID = "courseTermID";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_DESCRIPTION = "courseDescription";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END = "courseEnd";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_MENTOR = "courseMentor";
    public static final String COURSE_MENTOR_PHONE = "courseMentorPhone";
    public static final String COURSE_MENTOR_EMAIL = "courseMentorEmail";
    public static final String COURSE_NOTIFICATIONS = "courseNotifications";
    public static final String COURSE_CREATED = "courseCreated";
    public static final String[] COURSES_COLUMNS = {COURSES_TABLE_ID, COURSE_TERM_ID, COURSE_NAME, COURSE_DESCRIPTION,
            COURSE_START, COURSE_END, COURSE_STATUS, COURSE_MENTOR, COURSE_MENTOR_PHONE, COURSE_MENTOR_EMAIL,
            COURSE_NOTIFICATIONS, COURSE_CREATED};

    // Course Notes
    public static final String TABLE_COURSE_NOTES = "courseNotes";
    public static final String COURSE_NOTES_TABLE_ID = "_id";
    public static final String COURSE_NOTE_COURSE_ID = "courseNoteCourseID";
    public static final String COURSE_NOTE_TEXT = "courseNoteText";
    public static final String COURSE_NOTE_CREATED = "courseNoteCreated";
    public static final String[] COURSE_NOTES_COLUMNS = {COURSE_NOTES_TABLE_ID, COURSE_NOTE_COURSE_ID, COURSE_NOTE_TEXT, COURSE_NOTE_CREATED};

    // Assessments
    public static final String TABLE_ASSESS = "assessments";
    public static final String ASSESS_TABLE_ID = "_id";
    public static final String ASSESS_COURSE_ID = "assessmentCourseID";
    public static final String ASSESS_CODE = "assessmentCode";
    public static final String ASSESS_NAME = "assessmentName";
    public static final String ASSESS_DESCRIPTION = "assessmentDescription";
    public static final String ASSESS_DATETIME = "assessmentDatetime";
    public static final String ASSESS_NOTIFICATIONS = "assessmentNotifications";
    public static final String ASSESS_CREATED = "assessmentCreated";
    public static final String[] ASSESS_COLUMNS = {ASSESS_TABLE_ID, ASSESS_COURSE_ID, ASSESS_CODE,
            ASSESS_NAME, ASSESS_DESCRIPTION, ASSESS_DATETIME, ASSESS_NOTIFICATIONS, ASSESS_CREATED};

    // Assessment Notes
    public static final String TABLE_ASSESS_NOTES = "assessmentNotes";
    public static final String ASSESS_NOTES_TABLE_ID = "_id";
    public static final String ASSESS_NOTE_ASSESS_ID = "assessmentNoteAssessmentID";
    public static final String ASSESS_NOTE_TEXT = "assessmentNoteText";
    public static final String ASSESS_NOTE_CREATED = "assessmentNoteCreated";
    public static final String[] ASSESS_NOTES_COLUMNS = {ASSESS_NOTES_TABLE_ID, ASSESS_NOTE_ASSESS_ID,
            ASSESS_NOTE_TEXT, ASSESS_NOTE_CREATED};

    // SQLite commands

    // Terms
    private static final String TERMS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TERMS + " (" +
                    TERMS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_START + " DATE, " +
                    TERM_END + " DATE, " +
                    TERM_ACTIVE + " INTEGER, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    // Courses
    private static final String COURSES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + " (" +
                    COURSES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TERM_ID + " INTEGER, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_DESCRIPTION + " TEXT, " +
                    COURSE_START + " DATE, " +
                    COURSE_END + " DATE, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_MENTOR + " TEXT, " +
                    COURSE_MENTOR_PHONE + " TEXT, " +
                    COURSE_MENTOR_EMAIL + " TEXT, " +
                    COURSE_NOTIFICATIONS + " INTEGER, " +
                    COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" + TERMS_TABLE_ID + ")" +
                    ")";

    // Course Notes
    private static final String COURSE_NOTES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_NOTES + " (" +
                    COURSE_NOTES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NOTE_COURSE_ID + " INTEGER, " +
                    COURSE_NOTE_TEXT + " TEXT, " +
                    COURSE_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_NOTE_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Assessments
    private static final String ASSESS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ASSESS + " (" +
                    ASSESS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESS_COURSE_ID + " INTEGER, " +
                    ASSESS_NAME + " TEXT, " +
                    ASSESS_DESCRIPTION + " TEXT, " +
                    ASSESS_CODE + " TEXT, " +
                    ASSESS_DATETIME + " TEXT, " +
                    ASSESS_NOTIFICATIONS + " INTEGER, " +
                    ASSESS_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + ASSESS_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Assessment Notes
    private static final String ASSESS_NOTES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ASSESS_NOTES + " (" +
                    ASSESS_NOTES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESS_NOTE_ASSESS_ID + " INTEGER, " +
                    ASSESS_NOTE_TEXT + " TEXT, " +
                    ASSESS_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + ASSESS_NOTE_ASSESS_ID + ") REFERENCES " + TABLE_ASSESS + "(" + ASSESS_TABLE_ID + ")" +
                    ")";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERMS_TABLE_CREATE);
        db.execSQL(COURSES_TABLE_CREATE);
        db.execSQL(COURSE_NOTES_TABLE_CREATE);
        db.execSQL(ASSESS_TABLE_CREATE);
        db.execSQL(ASSESS_NOTES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESS_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        onCreate(db);
    }
}
