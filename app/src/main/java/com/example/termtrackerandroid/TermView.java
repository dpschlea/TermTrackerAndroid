package com.example.termtrackerandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class TermView extends AppCompatActivity {

    private static final int TERM_EDITOR_ACTIVITY_CODE = 11111;
    private static final int COURSE_LIST_ACTIVITY_CODE = 22222;

    private Uri termUri;
    private Terms term;

    private CursorAdapter cursorAdapter;

    private TextView tv_title;
    private TextView tv_start;
    private TextView tv_end;
    private Menu menu;

    private long termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_term_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(DP.TERM_CONTENT_TYPE);
        findElements();
        loadTermData();
    }

    private void findElements() {
        tv_title = findViewById(R.id.tvTermViewTermTitle);
        tv_start = findViewById(R.id.tvTermViewStartDate);
        tv_end = findViewById(R.id.tvTermViewEndDate);
    }

    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else {
            termID = Long.parseLong(termUri.getLastPathSegment());
            term = DM.getTerm(this, termID);

            setTitle("View Term");
            tv_title.setText(term.name);
            tv_start.setText(term.start);
            tv_end.setText(term.end);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term, menu);
        this.menu = menu;
        showAppropriateMenuOptions();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.act_mark_term_active:
                return markTermActive();
            case R.id.act_edit_term:
                Intent intent = new Intent(this, TermEdit.class);
                Uri uri = Uri.parse(DP.TERMS_URI + "/" + term.termID);
                intent.putExtra(DP.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);
                break;
            case R.id.act_delete_term:
                return deleteTerm();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean markTermActive() {
        Cursor cursor = getContentResolver().query(DP.TERMS_URI, null, null, null, null);
        ArrayList<Terms> termList = new ArrayList<>();
        while (cursor.moveToNext()) {
            termList.add(DM.getTerm(this, cursor.getLong(cursor.getColumnIndex(DB.TERMS_TABLE_ID))));
        }

        for (Terms term : termList) {
            term.deactivate(this);
        }

        this.term.activate(this);
        showAppropriateMenuOptions();

        Toast.makeText(TermView.this, getString(R.string.activate_term), Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean deleteTerm() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    long classCount = term.getClassCount(TermView.this);
                    if (classCount == 0) {
                        getContentResolver().delete(DP.TERMS_URI, DB.TERMS_TABLE_ID + " = " + termID, null);

                        Toast.makeText(TermView.this, getString(R.string.delete_term), Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else {
                        Toast.makeText(TermView.this, getString(R.string.contains_courses), Toast.LENGTH_SHORT).show();
                    }
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

    public void showAppropriateMenuOptions() {
        if (term.active == 1) {
            menu.findItem(R.id.act_mark_term_active).setVisible(false);
        }
    }

    public void openClassList(View view) {
        Intent intent = new Intent(this, CourseList.class);
        intent.putExtra(DP.TERM_CONTENT_TYPE, termUri);
        startActivityForResult(intent, COURSE_LIST_ACTIVITY_CODE);
    }
}