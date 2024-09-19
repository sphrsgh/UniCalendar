package com.sphr.unicalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sphr.unicalendar.helpers.database_helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements Runnable {

    Intent intent;
    database_helper program_db;
    String flag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(this,2000);
    }

    @Override
    public void run() {
        program_db = new database_helper(getApplicationContext());
        intent = new Intent(getApplicationContext(), StartActivity.class);
        if (program_db.getFlag())
            intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}