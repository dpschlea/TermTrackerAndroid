package com.example.termtrackerandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CourseNoteEdit extends AppCompatActivity {

    private long courseID;
    private Uri courseUri;
    private long courseNoteID;
    private Uri courseNoteUri;
    private CourseNote courseNote;
    private EditText noteField;
    private String act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_course_note_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noteField = findViewById(R.id.editCourseNoteText);
        courseNoteUri = getIntent().getParcelableExtra(DP.COURSE_NOTE_CONTENT_TYPE);

        if (courseNoteUri == null) {
            setTitle("New Note");
            courseUri = getIntent().getParcelableExtra(DP.COURSE_CONTENT_TYPE);
            courseID = Long.parseLong(courseUri.getLastPathSegment());
            act = Intent.ACTION_INSERT;
        }
        else {
            setTitle("Edit");
            courseNoteID = Long.parseLong(courseNoteUri.getLastPathSegment());
            courseNote = DM.getCourseNote(this, courseNoteID);
            courseID = courseNote.courseID;
            noteField.setText(courseNote.text);
            act = Intent.ACTION_EDIT;
        }
    }

    public void saveCourseNote(View view) {
        if (act == Intent.ACTION_INSERT) {
            DM.insertCourseNote(this, courseID, noteField.getText().toString().trim());
            setResult(RESULT_OK);
            finish();
        }
        if (act == Intent.ACTION_EDIT) {
            courseNote.text = noteField.getText().toString().trim();
            courseNote.saveChanges(this);
            setResult(RESULT_OK);
            finish();
        }
    }
}