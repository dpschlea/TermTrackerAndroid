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

public class AssessNoteView extends AppCompatActivity {

    private static final int ASSESS_NOTE_EDITOR_ACTIVITY_CODE = 11111;

    private long assessNoteID;
    private Uri assessNoteUri;
    private TextView tvAssessNoteText;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_assess_note_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvAssessNoteText = findViewById(R.id.tvAssessNoteText);
        assessNoteUri = getIntent().getParcelableExtra(DP.ASSESS_NOTE_CONTENT_TYPE);
        if (assessNoteUri != null) {
            assessNoteID = Long.parseLong(assessNoteUri.getLastPathSegment());
            setTitle("Assessment Note");
            loadNote();
        }
    }

    private void loadNote() {
        AssessNote assessmentNote = DM.getAssessNote(this, assessNoteID);
        tvAssessNoteText.setText(assessmentNote.text);
        tvAssessNoteText.setMovementMethod(new ScrollingMovementMethod());
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
        getMenuInflater().inflate(R.menu.menu_assess_note, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        AssessNote assessmentNote = DM.getAssessNote(this, assessNoteID);
        Assessments assessment = DM.getAssess(this, assessmentNote.assessID);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareSubject = assessment.code + " " + assessment.name + ": Assessment Note";
        String shareBody = assessmentNote.text;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        shareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.act_delete_assess_note:
                return deleteAssessmentNote();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAssessmentNote() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    DM.deleteAssessNote(AssessNoteView.this, assessNoteID);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(AssessNoteView.this, getString(R.string.note_delete), Toast.LENGTH_SHORT).show();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Delete")
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
        return true;
    }


    public void handleEditNote(View view) {
        Intent intent = new Intent(this, AssessNoteEdit.class);
        intent.putExtra(DP.ASSESS_NOTE_CONTENT_TYPE, assessNoteUri);
        startActivityForResult(intent, ASSESS_NOTE_EDITOR_ACTIVITY_CODE);
    }
}