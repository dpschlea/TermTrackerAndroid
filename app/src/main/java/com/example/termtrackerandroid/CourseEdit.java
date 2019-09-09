package com.example.termtrackerandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class CourseEdit extends AppCompatActivity implements View.OnClickListener {

    private String action;
    private Uri courseUri;
    private Uri termUri;
    private Courses course;

    private EditText editCourseName;
    private EditText editCourseStart;
    private EditText editCourseEnd;
    private EditText editMentorName;
    private EditText editMentorPhone;
    private EditText editMentorEmail;
    private DatePickerDialog courseStartDialog;
    private DatePickerDialog courseEndDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_course_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(DP.COURSE_CONTENT_TYPE);
        termUri = intent.getParcelableExtra(DP.TERM_CONTENT_TYPE);

        if (courseUri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("Add Course");
        }
        else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Course");
            long classId = Long.parseLong(courseUri.getLastPathSegment());
            course = DM.getCourse(this, classId);
            fillCourseForm(course);
        }
        setupDatePickers();
    }

    private void findViews() {
        editCourseName = findViewById(R.id.editCourseName);
        editCourseStart = findViewById(R.id.editCourseStart);
        editCourseEnd = findViewById(R.id.editCourseEnd);
        editMentorName = findViewById(R.id.editMentorName);
        editMentorPhone = findViewById(R.id.editMentorPhone);
        editMentorEmail = findViewById(R.id.editMentorEmail);
    }

    private void fillCourseForm(Courses course) {
        editCourseName.setText(course.name);
        editCourseStart.setText(course.start);
        editCourseEnd.setText(course.end);
        editMentorName.setText(course.mentor);
        editMentorPhone.setText(course.mentorPhone);
        editMentorEmail.setText(course.mentorEmail);
    }

    private void setupDatePickers() {
        editCourseStart.setOnClickListener(this);
        editCourseEnd.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        courseStartDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editCourseStart.setText(DateCon.dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        courseEndDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editCourseEnd.setText(DateCon.dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        editCourseStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    courseStartDialog.show();
                }
            }
        });

        editCourseEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    courseEndDialog.show();
                }
            }
        });
    }

    public void saveCourseChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            long termId = Long.parseLong(termUri.getLastPathSegment());
            DM.insertCourse(this, termId,
                    editCourseName.getText().toString().trim(),
                    editCourseStart.getText().toString().trim(),
                    editCourseEnd.getText().toString().trim(),
                    editMentorName.getText().toString().trim(),
                    editMentorPhone.getText().toString().trim(),
                    editMentorEmail.getText().toString().trim(),
                    CourseStatus.PLANNED);
        }
        else if (action == Intent.ACTION_EDIT) {
            course.name = editCourseName.getText().toString().trim();
            course.start = editCourseStart.getText().toString().trim();
            course.end = editCourseEnd.getText().toString().trim();
            course.mentor = editMentorName.getText().toString().trim();
            course.mentorPhone = editMentorPhone.getText().toString().trim();
            course.mentorEmail = editMentorEmail.getText().toString().trim();
            course.saveChanges(this);
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == editCourseStart) {
            courseStartDialog.show();
        }
        if (view == editCourseEnd) {
            courseEndDialog.show();
        }
    }
}