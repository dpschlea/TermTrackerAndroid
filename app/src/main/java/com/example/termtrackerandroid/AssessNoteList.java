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

public class AssessNoteList  extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ASSESS_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    private static final int ASSESS_NOTE_VIEWER_ACTIVITY_CODE = 22222;

    private long assessID;
    private Uri assessUri;
    private CursorAdapter cursorAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_assess_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assessUri = getIntent().getParcelableExtra(DP.ASSESS_CONTENT_TYPE);
        assessID = Long.parseLong(assessUri.getLastPathSegment());
        bindAssessNoteList();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessNoteList.this, AssessNoteEdit.class);
                intent.putExtra(DP.ASSESS_CONTENT_TYPE, assessUri);
                startActivityForResult(intent, ASSESS_NOTE_EDITOR_ACTIVITY_CODE);
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    private void bindAssessNoteList() {
        String[] from = {DB.ASSESS_NOTE_TEXT};
        int[] to = {R.id.tvAssessNoteText};
        cursorAdapt = new SimpleCursorAdapter(this, R.layout.assessments_notes, null, from, to, 0);
        DP database = new DP();
        ListView list = findViewById(R.id.assessNoteListView);
        list.setAdapter(cursorAdapt);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessNoteList.this, AssessNoteView.class);
                Uri uri = Uri.parse(DP.ASSESS_NOTES_URI + "/" + id);
                intent.putExtra(DP.ASSESS_NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESS_NOTE_VIEWER_ACTIVITY_CODE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DP.ASSESS_NOTES_URI, DB.ASSESS_NOTES_COLUMNS, DB.ASSESS_NOTE_ASSESS_ID + " = " + this.assessID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapt.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapt.swapCursor(null);
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
