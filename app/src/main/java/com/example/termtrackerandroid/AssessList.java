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
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ASSESS_VIEWER_ACTIVITY_CODE = 11111;
    private static final int ASSESS_EDITOR_ACTIVITY_CODE = 22222;

    private CursorAdapter cursorAdapter;
    private long courseId;
    private Uri courseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_assess_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseUri = getIntent().getParcelableExtra(DP.COURSE_CONTENT_TYPE);
        courseId = Long.parseLong(courseUri.getLastPathSegment());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessList.this, AssessEdit.class);
                intent.putExtra(DP.COURSE_CONTENT_TYPE, courseUri);
                startActivityForResult(intent, ASSESS_EDITOR_ACTIVITY_CODE);
            }
        });
        bindAssessList();
        getLoaderManager().initLoader(0, null, this);
    }

    protected void bindAssessList() {
        String[] from = {DB.ASSESS_CODE, DB.ASSESS_NAME, DB.ASSESS_DATETIME};
        int[] to = {R.id.tvAssessCode, R.id.tvAssessName, R.id.tvAssessDT};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.assessments_list, null, from, to, 0);
        DP database = new DP();
        ListView list = findViewById(R.id.assessListView);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessList.this, AssessView.class);
                Uri uri = Uri.parse(DP.ASSESS_URI + "/" + id);
                intent.putExtra(DP.ASSESS_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESS_VIEWER_ACTIVITY_CODE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DP.ASSESS_URI, DB.ASSESS_COLUMNS,
                DB.ASSESS_COURSE_ID + " = " + this.courseId, null, null);
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