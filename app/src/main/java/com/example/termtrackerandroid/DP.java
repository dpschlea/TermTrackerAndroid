package com.example.termtrackerandroid;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DP extends ContentProvider {

    // Authority/path
    private static final String AUTHORITY = "com.example.termtrackerandroid.DP";
    private static final String TERMS_PATH = "terms";
    private static final String COURSES_PATH = "courses";
    private static final String COURSE_NOTES_PATH = "courseNotes";
    private static final String ASSESS_PATH = "assessments";
    private static final String ASSESS_NOTES_PATH = "assessmentNotes";

    // Paths
    public static final Uri TERMS_URI = Uri.parse("content://" + AUTHORITY + "/" + TERMS_PATH);
    public static final Uri COURSES_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSES_PATH);
    public static final Uri COURSE_NOTES_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_NOTES_PATH);
    public static final Uri ASSESS_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESS_PATH);
    public static final Uri ASSESS_NOTES_URI = Uri.parse("content://" + AUTHORITY + "/" + ASSESS_NOTES_PATH);

    // ID's
    private static final int TERMS = 1;
    private static final int TERMS_ID = 2;
    private static final int COURSES = 3;
    private static final int COURSES_ID = 4;
    private static final int COURSE_NOTES = 5;
    private static final int COURSE_NOTES_ID = 6;
    private static final int ASSESS = 7;
    private static final int ASSESS_ID = 8;
    private static final int ASSESS_NOTES = 9;
    private static final int ASSESS_NOTES_ID = 10;

    // UriMatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TERMS_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERMS_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, COURSES_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSES_PATH + "/#", COURSES_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_NOTES_PATH, COURSE_NOTES);
        uriMatcher.addURI(AUTHORITY, COURSE_NOTES_PATH + "/#", COURSE_NOTES_ID);
        uriMatcher.addURI(AUTHORITY, ASSESS_PATH, ASSESS);
        uriMatcher.addURI(AUTHORITY, ASSESS_PATH + "/#", ASSESS_ID);
        uriMatcher.addURI(AUTHORITY, ASSESS_NOTES_PATH, ASSESS_NOTES);
        uriMatcher.addURI(AUTHORITY, ASSESS_NOTES_PATH + "/#", ASSESS_NOTES_ID);
    }

    public static final String TERM_CONTENT_TYPE = "term";
    public static final String COURSE_CONTENT_TYPE = "course";
    public static final String COURSE_NOTE_CONTENT_TYPE = "courseNote";
    public static final String ASSESS_CONTENT_TYPE = "assessment";
    public static final String ASSESS_NOTE_CONTENT_TYPE = "assessmentNote";

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DB helper = new DB(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return database.query(DB.TABLE_TERMS, DB.TERMS_COLUMNS, selection, null,
                        null, null, DB.TERMS_TABLE_ID + " ASC");
            case COURSES:
                return database.query(DB.TABLE_COURSES, DB.COURSES_COLUMNS, selection, null,
                        null, null, DB.COURSES_TABLE_ID + " ASC");
            case COURSES_ID:
                return database.query(DB.TABLE_COURSES, DB.COURSES_COLUMNS,
                        DB.COURSES_TABLE_ID + "=" + uri.getLastPathSegment(), null,
                        null, null, DB.COURSES_TABLE_ID + " ASC" );
            case COURSE_NOTES:
                return database.query(DB.TABLE_COURSE_NOTES, DB.COURSE_NOTES_COLUMNS, selection, null,
                        null, null, DB.COURSE_NOTES_TABLE_ID + " ASC");
            case ASSESS:
                return database.query(DB.TABLE_ASSESS, DB.ASSESS_COLUMNS, selection, null,
                        null, null, DB.ASSESS_TABLE_ID + " ASC");
            case ASSESS_NOTES:
                return database.query(DB.TABLE_ASSESS_NOTES, DB.ASSESS_NOTES_COLUMNS, selection, null,
                        null, null, DB.ASSESS_NOTES_TABLE_ID + " ASC");
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        switch (uriMatcher.match(uri)) {
            case TERMS:
                id = database.insert(DB.TABLE_TERMS, null, values);
                return Uri.parse(TERMS_PATH + "/" + id);
            case COURSES:
                id = database.insert(DB.TABLE_COURSES, null, values);
                return Uri.parse(COURSES_PATH + "/" + id);
            case COURSE_NOTES:
                id = database.insert(DB.TABLE_COURSE_NOTES, null, values);
                return Uri.parse(COURSE_NOTES_PATH + "/" + id);
            case ASSESS:
                id = database.insert(DB.TABLE_ASSESS, null, values);
                return Uri.parse(ASSESS_PATH + "/" + id);
            case ASSESS_NOTES:
                id = database.insert(DB.TABLE_ASSESS_NOTES, null, values);
                return Uri.parse(ASSESS_NOTES_PATH + "/" + id);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return database.delete(DB.TABLE_TERMS, selection, selectionArgs);
            case COURSES:
                return database.delete(DB.TABLE_COURSES, selection, selectionArgs);
            case COURSE_NOTES:
                return database.delete(DB.TABLE_COURSE_NOTES, selection, selectionArgs);
            case ASSESS:
                return database.delete(DB.TABLE_ASSESS, selection, selectionArgs);
            case ASSESS_NOTES:
                return database.delete(DB.TABLE_ASSESS_NOTES, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return database.update(DB.TABLE_TERMS, values, selection, selectionArgs);
            case COURSES:
                return database.update(DB.TABLE_COURSES, values, selection, selectionArgs);
            case COURSE_NOTES:
                return database.update(DB.TABLE_COURSE_NOTES, values, selection, selectionArgs);
            case ASSESS:
                return database.update(DB.TABLE_ASSESS, values, selection, selectionArgs);
            case ASSESS_NOTES:
                return database.update(DB.TABLE_ASSESS_NOTES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
