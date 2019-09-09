package com.example.termtrackerandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

public class CourseView extends AppCompatActivity {

    private static final int COURSE_NOTE_LIST_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_LIST_ACTIVITY_CODE = 22222;
    private static final int COURSE_EDITOR_ACTIVITY_CODE = 33333;

    private Menu menu;
    private Uri courseUri;
    private long courseID;
    private Courses course;

    private TextView tvCourseName;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_course_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(DP.COURSE_CONTENT_TYPE);
        courseID = Long.parseLong(courseUri.getLastPathSegment());
        course = DM.getCourse(this, courseID);

        setStatusLabel();
        findElements();
    }

    private void setStatusLabel() {
        tvStatus = findViewById(R.id.tvStatus);
        String status = "";
        switch (course.status.toString()) {
            case "PLANNED":
                status = "Planned";
                break;
            case "IN_PROGRESS":
                status = "In Progress";
                break;
            case "COMPLETED":
                status = "Completed";
                break;
            case "DROPPED":
                status = "Dropped";
                break;
        }
        tvStatus.setText("Status: " + status);
    }

    private void findElements() {
        tvCourseName = findViewById(R.id.tvCourseName);
        tvCourseName.setText(course.name);
        tvStart = findViewById(R.id.tvCourseStart);
        tvStart.setText(course.start);
        tvEnd = findViewById(R.id.tvCourseEnd);
        tvEnd.setText(course.end);
    }

    private void updateElements() {
        course = DM.getCourse(this, courseID);
        tvCourseName.setText(course.name);
        tvStart.setText(course.start);
        tvEnd.setText(course.end);
    }

    public void openClassNotesList(View view) {
        Intent intent = new Intent(CourseView.this, CourseNoteList.class);
        Uri uri = Uri.parse(DP.COURSES_URI + "/" + courseID);
        intent.putExtra(DP.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, COURSE_NOTE_LIST_ACTIVITY_CODE);
    }

    public void openAssess(View view) {
        Intent intent = new Intent(CourseView.this, AssessList.class);
        Uri uri = Uri.parse(DP.COURSES_URI + "/" + courseID);
        intent.putExtra(DP.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, ASSESSMENT_LIST_ACTIVITY_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        this.menu = menu;
        showAppMenuOptions();
        return true;
    }

    private void showAppMenuOptions() {
        SharedPreferences sharedPref = getSharedPreferences(Notifications.courseNotifAlert, Context.MODE_PRIVATE);
        menu.findItem(R.id.act_enable_notifications).setVisible(true);
        menu.findItem(R.id.act_disable_notifications).setVisible(true);

        if (course.notifi == 1) {
            menu.findItem(R.id.act_enable_notifications).setVisible(false);
        }
        else {
            menu.findItem(R.id.act_disable_notifications).setVisible(false);
        }

        if (course.status == null) {
            course.status = CourseStatus.PLANNED;
            course.saveChanges(this);
        }

        switch (course.status.toString()) {
            case "PLANNED":
                menu.findItem(R.id.act_drop_course).setVisible(false);
                menu.findItem(R.id.act_start_course).setVisible(true);
                menu.findItem(R.id.act_mark_course_completed).setVisible(false);
                break;
            case "IN_PROGRESS":
                menu.findItem(R.id.act_drop_course).setVisible(true);
                menu.findItem(R.id.act_start_course).setVisible(false);
                menu.findItem(R.id.act_mark_course_completed).setVisible(true);
                break;
            case "COMPLETED":
                menu.findItem(R.id.act_drop_course).setVisible(false);
                menu.findItem(R.id.act_start_course).setVisible(false);
                menu.findItem(R.id.act_mark_course_completed).setVisible(false);
                break;
            case "DROPPED":
                menu.findItem(R.id.act_drop_course).setVisible(false);
                menu.findItem(R.id.act_start_course).setVisible(false);
                menu.findItem(R.id.act_mark_course_completed).setVisible(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.act_edit_course:
                return editCourse();
            case R.id.act_delete_course:
                return deleteCourse();
            case R.id.act_enable_notifications:
                return enableNotifi();
            case R.id.act_disable_notifications:
                return disableNotifi();
            case R.id.act_drop_course:
                return dropCourse();
            case R.id.act_start_course:
                return startCourse();
            case R.id.act_mark_course_completed:
                return markCourseCompleted();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean editCourse() {
        Intent intent = new Intent(this, CourseEdit.class);
        Uri uri = Uri.parse(DP.COURSES_URI + "/" + course.courseID);
        intent.putExtra(DP.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, COURSE_EDITOR_ACTIVITY_CODE);
        return true;
    }

    private boolean deleteCourse() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    DM.deleteCourse(CourseView.this, courseID);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(CourseView.this, getString(R.string.course_delete), Toast.LENGTH_SHORT).show();
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

    private boolean enableNotifi() {
        long now = DateCon.todayLong();

        if (now <= DateCon.getDateTimestamp(course.start)) {
            Notifications.scheduleCourseNotifi(getApplicationContext(), courseID, DateCon.getDateTimestamp(course.start),
                    "Course begins today.", course.name + " begins on " + course.start);
        }
        if (now <= DateCon.getDateTimestamp(course.start) - 3 * 24 * 60 * 60 * 1000) {
            Notifications.scheduleCourseNotifi(getApplicationContext(), courseID, DateCon.getDateTimestamp(course.start),
                    "Upcoming course in a week.", course.name + " begins on " + course.start);
        }
        if (now <= DateCon.getDateTimestamp(course.start) - 21 * 24 * 60 * 60 * 1000) {
            Notifications.scheduleCourseNotifi(getApplicationContext(), courseID, DateCon.getDateTimestamp(course.start),
                    "Upcoming course in 3 weeks.", course.name + " begins on " + course.start);
        }

        if (now <= DateCon.getDateTimestamp(course.end)) {
            Notifications.scheduleCourseNotifi(getApplicationContext(), courseID, DateCon.getDateTimestamp(course.end),
                    "Final day.", course.name + " ends on " + course.start);
        }
        if (now <= DateCon.getDateTimestamp(course.end) - 3 * 24 * 60 * 60 * 1000) {
            Notifications.scheduleCourseNotifi(getApplicationContext(), courseID, DateCon.getDateTimestamp(course.end),
                    "Three days left.", course.name + " ends on " + course.start);
        }
        if (now <= DateCon.getDateTimestamp(course.end) - 21 * 24 * 60 * 60 * 1000) {
            Notifications.scheduleCourseNotifi(getApplicationContext(), courseID, DateCon.getDateTimestamp(course.end),
                    "Three weeks left.", course.name + " ends on " + course.start);
        }
        course.notifi = 1;
        course.saveChanges(this);
        showAppMenuOptions();
        return true;
    }

    private boolean disableNotifi() {
        course.notifi = 0;
        course.saveChanges(this);
        showAppMenuOptions();
        return true;
    }

    private boolean dropCourse() {
        course.status = CourseStatus.DROPPED;
        course.saveChanges(this);
        setStatusLabel();
        showAppMenuOptions();
        return true;
    }

    private boolean startCourse() {
        course.status = CourseStatus.IN_PROGRESS;
        course.saveChanges(this);
        setStatusLabel();
        showAppMenuOptions();
        return true;
    }

    private boolean markCourseCompleted() {
        course.status = CourseStatus.COMPLETED;
        course.saveChanges(this);
        setStatusLabel();
        showAppMenuOptions();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            updateElements();
        }
    }
}