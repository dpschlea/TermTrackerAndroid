package com.example.termtrackerandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AssessNoteEdit extends AppCompatActivity {

        private long assessmentNoteId;
        private Uri assessmentNoteUri;
        private AssessNote assessmentNote;
        private long assessmentId;
        private Uri assessmentUri;
        private EditText assessmentNoteTextField;
        private String action;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.a_assess_note_edit);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            assessmentNoteTextField = findViewById(R.id.editAssessNoteText);
            assessmentNoteUri = getIntent().getParcelableExtra(DP.ASSESS_NOTE_CONTENT_TYPE);
            if (assessmentNoteUri == null) {
                setTitle("Enter note");
                assessmentUri = getIntent().getParcelableExtra(DP.ASSESS_CONTENT_TYPE);
                assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
                action = Intent.ACTION_INSERT;
            }
            else {
                setTitle("Edit Note");
                assessmentNoteId = Long.parseLong(assessmentNoteUri.getLastPathSegment());
                assessmentNote = DM.getAssessNote(this, assessmentNoteId);
                assessmentId = assessmentNote.assessID;
                assessmentNoteTextField.setText(assessmentNote.text);
                action = Intent.ACTION_EDIT;
            }
        }

        public void saveAssessNote(View view) {
            if (action == Intent.ACTION_INSERT) {
                DM.insertAssessNote(this, assessmentId, assessmentNoteTextField.getText().toString().trim());
                setResult(RESULT_OK);
                finish();
            }
            if (action == Intent.ACTION_EDIT) {
                assessmentNote.text = assessmentNoteTextField.getText().toString().trim();
                assessmentNote.saveChanges(this);
                setResult(RESULT_OK);
                finish();
            }
        }
}
