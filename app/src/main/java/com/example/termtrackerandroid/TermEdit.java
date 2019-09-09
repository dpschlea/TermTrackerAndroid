package com.example.termtrackerandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermEdit extends AppCompatActivity implements View.OnClickListener {

    private static final int MAIN_ACTIVITY_CODE = 1;
    private String action;
    private String termFilter;
    private Terms term;

    private EditText termNameField;
    private EditText termStartDateField;
    private EditText termEndDateField;

    private DatePickerDialog termStartDateDialog;
    private DatePickerDialog termEndDateDialog;
    private SimpleDateFormat dateFormat;

    private DP database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_term_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = new DP();

        termNameField = findViewById(R.id.termNameEditText);
        termStartDateField = findViewById(R.id.termStartEditText);
        termStartDateField.setInputType(InputType.TYPE_NULL);
        termEndDateField = findViewById(R.id.termEndEditText);
        termEndDateField.setInputType(InputType.TYPE_NULL);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DP.TERM_CONTENT_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("Add Term");
        }
        else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Term");
            long termId = Long.parseLong(uri.getLastPathSegment());
            term = DM.getTerm(this, termId);
            fillTermForm(term);
        }
        setupDatePickers();
    }

    private void fillTermForm(Terms term) {
        termNameField.setText(term.name);
        termStartDateField.setText(term.start);
        termEndDateField.setText(term.end);
    }

    private void getTermFromForm() {
        term.name = termNameField.getText().toString().trim();
        term.start = termStartDateField.getText().toString().trim();
        term.end = termEndDateField.getText().toString().trim();
    }

    private void setupDatePickers() {
        termStartDateField.setOnClickListener(this);
        termEndDateField.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        termStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termStartDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        termEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                termEndDateField.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        termStartDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    termStartDateDialog.show();
                }
            }
        });
    }

    public void saveTermChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            term = new Terms();
            getTermFromForm();

            DM.insertTerm(this, term.name, term.start, term.end, term.active);
            Toast.makeText(this, getString(R.string.item_saved), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
        else if (action == Intent.ACTION_EDIT) {
            getTermFromForm();
            term.saveChanges(this);
            Toast.makeText(this, getString(R.string.updated), Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == termStartDateField) {
            termStartDateDialog.show();
        }
        if (view == termEndDateField) {
            termEndDateDialog.show();
        }
    }
}
