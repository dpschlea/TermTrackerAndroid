package com.example.termtrackerandroid;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseNoteList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COURSE_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int COURSE_NOTE_VIEWER_ACTIVITY_CODE = 22222;

    private long courseID;
    private Uri courseUri;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_course_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseUri = getIntent().getParcelableExtra(DP.COURSE_CONTENT_TYPE);
        courseID = Long.parseLong(courseUri.getLastPathSegment());
        bindCourseNoteList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseNoteList.this, CourseNoteEdit.class);
                intent.putExtra(DP.COURSE_CONTENT_TYPE, courseUri);
                startActivityForResult(intent, COURSE_NOTE_EDITOR_ACTIVITY_CODE);
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    private void bindCourseNoteList() {
        String[] from = {DB.COURSE_NOTE_TEXT};
        int[] to = {R.id.tvCourseNoteText};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.courses_notes, null, from, to, 0);
        DP database = new DP();

        ListView list = findViewById(R.id.courseNoteListView);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseNoteList.this, CourseNoteView.class);
                Uri uri = Uri.parse(DP.COURSE_NOTES_URI + "/" + id);
                intent.putExtra(DP.COURSE_NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_NOTE_VIEWER_ACTIVITY_CODE);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DP.COURSE_NOTES_URI, DB.COURSE_NOTES_COLUMNS, DB.COURSE_NOTE_COURSE_ID + " = " + this.courseID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }
}
