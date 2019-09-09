package com.example.termtrackerandroid;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final int TERM_VIEWER_ACTIVITY_CODE = 11111;
    private static final int TERM_LIST_ACTIVITY_CODE = 22222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openThisTerm(View view) {
        Cursor c = getContentResolver().query(DP.TERMS_URI, null, DB.TERM_ACTIVE
                + " =1", null, null);
        while (c.moveToNext()) {
            Intent intent = new Intent(this, TermView.class);
            long id = c.getLong(c.getColumnIndex(DB.TERMS_TABLE_ID));
            Uri uri = Uri.parse(DP.TERMS_URI + "/" + id);
            intent.putExtra(DP.TERM_CONTENT_TYPE, uri);
            startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            return;
        }
        Toast.makeText(this, getString(R.string.no_active_term),
                Toast.LENGTH_SHORT).show();
    }

    public void openTermList(View view) {
        Intent intent = new Intent(this, TermList.class);
        startActivityForResult(intent, TERM_LIST_ACTIVITY_CODE);
    }
}