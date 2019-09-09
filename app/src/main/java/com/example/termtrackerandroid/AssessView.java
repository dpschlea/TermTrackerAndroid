package com.example.termtrackerandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessView extends AppCompatActivity {

    private static final int ASSESS_EDITOR_ACTIVITY_CODE = 11111;
    private static final int ASSESS_NOTE_LIST_ACTIVITY_CODE = 22222;

    private long assessID;
    private Assessments assess;
    private TextView tvAssessTitle;
    private TextView tvAssessDesc;
    private TextView tvAssessDT;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_assess_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessView.this, AssessEdit.class);
                Uri uri = Uri.parse(DP.ASSESS_URI + "/" + assessID);
                intent.putExtra(DP.ASSESS_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESS_EDITOR_ACTIVITY_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadAssessment();
    }

    private void loadAssessment() {
        Uri assessUri = getIntent().getParcelableExtra(DP.ASSESS_CONTENT_TYPE);
        assessID = Long.parseLong(assessUri.getLastPathSegment());
        assess = DM.getAssess(this, assessID);
        tvAssessTitle = findViewById(R.id.tvAssessTitle);
        tvAssessDesc = findViewById(R.id.tvAssessDesc);
        tvAssessDT = findViewById(R.id.tvAssessDT);
        tvAssessTitle.setText(assess.code + ": " + assess.name);
        tvAssessDesc.setText(assess.desc);
        tvAssessDT.setText(assess.datetime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadAssessment();
        }
    }

    public void openAssessNotesList(View view) {
        Intent intent = new Intent(AssessView.this, AssessNoteList.class);
        Uri uri = Uri.parse(DP.ASSESS_URI + "/" + assessID);
        intent.putExtra(DP.ASSESS_CONTENT_TYPE, uri);
        startActivityForResult(intent, ASSESS_NOTE_LIST_ACTIVITY_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assess, menu);
        this.menu = menu;
        showAppropriateMenuOptions();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.act_delete_assess:
                return deleteAssessment();
            case R.id.act_enable_notifications:
                return enableNotifications();
            case R.id.act_disable_notifications:
                return disableNotifications();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAssessment() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    DM.deleteAssess(AssessView.this, assessID);
                    setResult(RESULT_OK);
                    finish();
                    Toast.makeText(AssessView.this, getString(R.string.item_delete), Toast.LENGTH_SHORT).show();
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

    private boolean enableNotifications() {
        long now = DateCon.todayLong();

        Notifications.scheduleAssessNotifi(getApplicationContext(), (int) assessID, System.currentTimeMillis()
                + 1000, "Assessment is today!", assess.name + " takes place on " + assess.datetime);
        if (now <= DateCon.getDateTimestamp(assess.datetime)) {
            Notifications.scheduleAssessNotifi(getApplicationContext(), (int) assessID, DateCon.getDateTimestamp(assess.datetime), "Assessment is today!", assess.name + " takes place on " + assess.datetime);
        }
        if (now <= DateCon.getDateTimestamp(assess.datetime) - 3 * 24 * 60 * 60 * 1000) {
            Notifications.scheduleAssessNotifi(getApplicationContext(), (int) assessID, DateCon.getDateTimestamp(assess.datetime) - 3 * 24 * 60 * 60 * 1000, "Assessment is in three days!", assess.name + " takes place on " + assess.datetime);
        }
        if (now <= DateCon.getDateTimestamp(assess.datetime) - 21 * 24 * 60 * 60 * 1000) {
            Notifications.scheduleAssessNotifi(getApplicationContext(), (int) assessID, DateCon.getDateTimestamp(assess.datetime) - 21 * 24 * 60 * 60 * 1000, "Assessment is in three weeks!", assess.name + " takes place on " + assess.datetime);
        }

        assess.notifi = 1;
        assess.saveChanges(this);
        showAppropriateMenuOptions();
        return true;
    }

    private boolean disableNotifications() {
        assess.notifi = 0;
        assess.saveChanges(this);
        showAppropriateMenuOptions();
        return true;
    }

    private void showAppropriateMenuOptions() {
        menu.findItem(R.id.act_enable_notifications).setVisible(true);
        menu.findItem(R.id.act_disable_notifications).setVisible(true);

        if (assess.notifi == 1) {
            menu.findItem(R.id.act_enable_notifications).setVisible(false);
        }
        else {
            menu.findItem(R.id.act_disable_notifications).setVisible(false);
        }
    }
}