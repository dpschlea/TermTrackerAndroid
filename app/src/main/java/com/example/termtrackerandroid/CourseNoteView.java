package com.example.termtrackerandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

public class CourseNoteView extends AppCompatActivity {

    private static final int COURSE_NOTE_EDITOR_ACTIVITY_CODE = 11111;

    private long courseNoteID;
    private Uri courseNoteUri;
    private TextView tvCourseNoteText;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_course_note_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCourseNoteText = findViewById(R.id.tvCourseNoteText);
        courseNoteUri = getIntent().getParcelableExtra(DP.COURSE_NOTE_CONTENT_TYPE);

        if (courseNoteUri != null) {
            courseNoteID = Long.parseLong(courseNoteUri.getLastPathSegment());
            setTitle("View Note");
            loadNote();
        }
    }

    private void loadNote() {
        CourseNote courseNote = DM.getCourseNote(this, courseNoteID);
        tvCourseNoteText.setText(courseNote.text);
        tvCourseNoteText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadNote();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_note, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        CourseNote courseNote = DM.getCourseNote(this, courseNoteID);
        Courses course = DM.getCourse(this, courseNote.courseID);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareSubject = course.name + ": Course Note";
        String shareBody = courseNote.text;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.act_delete_course_note:
                return deleteCourseNote();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteCourseNote() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    DM.deleteCourseNote(CourseNoteView.this, courseNoteID);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(CourseNoteView.this, getString(R.string.note_delete), Toast.LENGTH_SHORT).show();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm delete")
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
        return true;
    }
    
    public void handleEditNote(View view) {
        Intent intent = new Intent(this, CourseNoteEdit.class);
        intent.putExtra(DP.COURSE_NOTE_CONTENT_TYPE, courseNoteUri);
        startActivityForResult(intent, COURSE_NOTE_EDITOR_ACTIVITY_CODE);
    }
}