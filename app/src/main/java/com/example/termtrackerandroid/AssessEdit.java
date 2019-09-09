package com.example.termtrackerandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.TimeZone;

public class AssessEdit extends AppCompatActivity implements View.OnClickListener {

    private Assessments assessment;
    private long courseID;
    private EditText editAssess;
    private EditText editAssessName;
    private EditText editAssessDesc;
    private EditText editAssessDT;
    private DatePickerDialog assessmentDateDialog;
    private TimePickerDialog assessmentTimeDialog;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_assess_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editAssess = findViewById(R.id.editAssessCode);
        editAssessName = findViewById(R.id.editAssessName);
        editAssessDesc = findViewById(R.id.editAssessDesc);
        editAssessDT = findViewById(R.id.editAssessDT);

        Uri assessmentUri = getIntent().getParcelableExtra(DP.ASSESS_CONTENT_TYPE);
        if (assessmentUri == null) {
            setTitle("New Assessment");
            action = Intent.ACTION_INSERT;
            Uri courseUri = getIntent().getParcelableExtra(DP.COURSE_CONTENT_TYPE);
            courseID = Long.parseLong(courseUri.getLastPathSegment());
            assessment = new Assessments();
        }
        else {
            setTitle("Edit Assessment");
            action = Intent.ACTION_EDIT;
            Long assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            assessment = DM.getAssess(this, assessmentId);
            courseID = assessment.courseID;
            fillAssessForm();
        }
        setupDTPickers();
    }

    private void fillAssessForm() {
        if (assessment != null) {
            editAssess.setText(assessment.code);
            editAssessName.setText(assessment.name);
            editAssessDesc.setText(assessment.desc);
            editAssessDT.setText(assessment.datetime);
        }
    }

    private void setupDTPickers() {
        editAssessDT.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        assessmentDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar2 = Calendar.getInstance();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editAssessDT.setText(DateCon.dateFormat.format(newDate.getTime()));
                assessmentTimeDialog = new TimePickerDialog(AssessEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        }
                        else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay > 12) {
                            hourOfDay = hourOfDay - 12;
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String minuteString = Integer.toString(minute);
                        if (minute < 10) {
                            minuteString = "0" + minuteString;
                        }
                        String datetime = editAssessDT.getText().toString() + " " + hourOfDay + ":" + minuteString
                                + " " + AM_PM + " " + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
                        editAssessDT.setText(datetime);
                    }
                }, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), false);
                assessmentTimeDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        editAssessDT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    assessmentDateDialog.show();
                }
            }
        });
    }


    public void saveAssessChanges(View view) {
        getValuesFromFields();
        switch (action) {
            case Intent.ACTION_INSERT:
                DM.insertAssess(this, courseID, assessment.code, assessment.name, assessment.desc,
                        assessment.datetime);
                setResult(RESULT_OK);
                finish();
                break;
            case Intent.ACTION_EDIT:
                assessment.saveChanges(this);
                setResult(RESULT_OK);
                finish();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void getValuesFromFields() {
        assessment.code = editAssess.getText().toString().trim();
        assessment.name = editAssessName.getText().toString().trim();
        assessment.desc = editAssessDesc.getText().toString().trim();
        assessment.datetime = editAssessDT.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        if (view == editAssessDT) {
            assessmentDateDialog.show();
        }
    }
}