package com.example.termtrackerandroid;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COURSE_VIEWER_ACTIVITY_CODE = 11111;
    private static final int COURSE_EDITOR_ACTIVITY_CODE = 22222;

    private long termID;
    private Uri termUri;
    private Terms term;
    private CustomCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_course_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseList.this, CourseEdit.class);
                intent.putExtra(DP.TERM_CONTENT_TYPE, termUri);
                startActivityForResult(intent, COURSE_EDITOR_ACTIVITY_CODE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(DP.TERM_CONTENT_TYPE);
        loadTermData();
        bindClassList();
        getLoaderManager().initLoader(0, null, this);
    }

    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else {
            termID = Long.parseLong(termUri.getLastPathSegment());
            term = DM.getTerm(this, termID);
            setTitle("Courses");
        }
    }

    private void bindClassList() {
        String[] from = {DB.COURSE_NAME, DB.COURSE_START, DB.COURSE_END, DB.COURSE_STATUS};
        int[] to = {R.id.tvCourseName, R.id.tvCourseStart, R.id.tvCourseEnd, R.id.tvCourseStatus};

        cursorAdapter = new CustomCursorAdapter(this, R.layout.courses_list, null, from, to);
        DP database = new DP();

        ListView list = findViewById(R.id.courseLists);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseList.this, CourseView.class);
                Uri uri = Uri.parse(DP.COURSES_URI + "/" + id);
                intent.putExtra(DP.COURSE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_VIEWER_ACTIVITY_CODE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DP.COURSES_URI, DB.COURSES_COLUMNS, DB.COURSE_TERM_ID + " = " + this.termID, null, null);
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
        loadTermData();
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    public class CustomCursorAdapter extends androidx.cursoradapter.widget.SimpleCursorAdapter {

        public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        @Override
        public void setViewText(TextView view, String text) {
            if (view.getId() == R.id.tvCourseStatus) {
                String status = "";
                switch (text) {
                    case "PLANNED":
                        status = "Planned";
                        break;
                    case "IN_PROGRESS":
                        status = "In Progress";
                        break;
                    case "COMPLETED":
                        status = "Completed";
                        break;
                    case "DROPPED":
                        status = "Dropped";
                        break;
                }
                view.setText("Status: " + status);
            }
            else {
                view.setText(text);
            }
        }
    }
}