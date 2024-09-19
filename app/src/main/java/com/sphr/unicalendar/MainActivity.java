package com.sphr.unicalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.sphr.unicalendar.helpers.Calendar_helper;
import com.sphr.unicalendar.helpers.database_helper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Runnable {

    Intent intent;
    Toolbar ActionBar;
    Button btn_insert, btn_edit, btn_next, btn_prev, btn_change;
    ListView lvWeek, lvDay;
    TextView showOE;
    database_helper db;
    Calendar_helper ch;
    boolean vFlag = true;
    int dayChange = 0;
    String oddEven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        ch = new Calendar_helper();
        db = new database_helper(this);
        ActionBar = findViewById(R.id.ActionBar);
        ActionBar.setTitle("        تاریخ امروز: "+ch.getWeekPer()+" "+ch.getShamsiDate());
        btn_insert = findViewById(R.id.btn_insert);
        btn_edit = findViewById(R.id.btn_edit);
        lvWeek = findViewById(R.id.lvWeek);
        lvDay = findViewById(R.id.lvDay);
        btn_next = findViewById(R.id.btn_next);
        btn_prev = findViewById(R.id.btn_prev);
        btn_change = findViewById(R.id.btn_change);
        showOE = findViewById(R.id.showOE);

        if(vFlag) {
            lvDay.setVisibility(View.VISIBLE);
            lvWeek.setVisibility(View.GONE);
            btn_next.setText("روز بعد");
            btn_prev.setText("روز قبل");
        } else{
            lvDay.setVisibility(View.GONE);
            lvWeek.setVisibility(View.VISIBLE);
            btn_next.setText("هفته بعد");
            btn_prev.setText("هفته قبل");
        }
        btn_insert.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_change.setOnClickListener(this);

        changeView(vFlag);
        showListView(dayChange);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                intent = new Intent(getApplicationContext(), InsertActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_edit:
                intent = new Intent(getApplicationContext(), FindActivity.class);
                intent.putExtra("update" , false);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_next:
                if (vFlag) {
                    dayChange += 1;
                } else {
                    dayChange += 7;
                }
                showListView(dayChange);
                break;
            case R.id.btn_prev:
                if (vFlag) {
                    dayChange -= 1;
                } else {
                    dayChange -= 7;
                }
                showListView(dayChange);
                break;
            case R.id.btn_change:
                if (vFlag)
                    vFlag = false;
                else
                    vFlag = true;
                changeView(vFlag);
                dayChange = 0;
                showListView(dayChange);
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج دوباره دکمه بازگشت را بزنید.", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(this, 1500);
    }

    @Override
    public void run() {
        doubleBackToExitPressedOnce=false;
    }

    private void changeView(boolean flag){
        if(flag) {
            lvDay.setVisibility(View.VISIBLE);
            lvWeek.setVisibility(View.GONE);
            btn_next.setText("روز بعد");
            btn_prev.setText("روز قبل");
        } else{
            lvDay.setVisibility(View.GONE);
            lvWeek.setVisibility(View.VISIBLE);
            btn_next.setText("هفته بعد");
            btn_prev.setText("هفته قبل");
        }
    }

    public void showListView(int dayChange){
        ArrayAdapter<String> arrWeek = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, db.showWeek(dayChange));
        lvWeek.setAdapter(arrWeek);
        ArrayAdapter<String> arrDay = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, db.showDay(dayChange));
        lvDay.setAdapter(arrDay);
        if(ch.isEven(dayChange))
            oddEven = "هفته زوج";
        else
            oddEven = "هفته فرد";
        showOE.setText(oddEven);
    }
}