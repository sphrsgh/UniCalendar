package com.sphr.unicalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sphr.unicalendar.helpers.database_helper;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    Button btn_start;
    TextView tx_info;
    database_helper program_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();

    }

    private void init() {
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        tx_info = findViewById(R.id.tx_info);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                program_db = new database_helper(this);
                program_db.setFlag();
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}