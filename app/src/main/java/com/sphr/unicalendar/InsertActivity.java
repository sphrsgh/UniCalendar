package com.sphr.unicalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.sphr.unicalendar.helpers.Calendar_helper;
import com.sphr.unicalendar.helpers.database_helper;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner spinner_weekdays;
    String[] weekdayPer ={"شنبه","یکشنبه","دوشنبه","سه شنبه","چهارشنبه","پنجشنبه","جمعه"};
    String[] weekdays = {"sat","sun","mon","tue","wed","thu","fri"};
    String weekday = null;
    String weekEdit = null;
    Button btn_save, btn_edit, btn_delete;
    RadioGroup radioGP;
    RadioButton radioEvery, radioOdd, radioEven;
    database_helper db;
    Calendar_helper ch;
    EditText et_class, et_timeF, et_timeT;
    LinearLayout ll_btn;
    Intent intent;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        init();
    }

    private void init(){
        db = new database_helper(this);
        ch = new Calendar_helper();
        spinner_weekdays=findViewById(R.id.spinner_weekdays);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, weekdayPer);
        spinner_weekdays.setAdapter(arrayAdapter);
        spinner_weekdays.setOnItemSelectedListener(this);
        btn_save = findViewById(R.id.btn_save);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_save.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        et_class = findViewById(R.id.et_class);
        et_timeF = findViewById(R.id.et_timeF);
        et_timeT = findViewById(R.id.et_timeT);
        radioGP = findViewById(R.id.radioGp);
        radioEvery = findViewById(R.id.radioEvery);
        radioOdd = findViewById(R.id.radioOdd);
        radioEven = findViewById(R.id.radioEven);
        ll_btn = findViewById(R.id.ll_btn);

        intent = getIntent();
        if (intent.getBooleanExtra("update",false)) {
            btn_save.setVisibility(View.GONE);
            ll_btn.setVisibility(View.VISIBLE);
            id = intent.getIntExtra("id", 0);
            weekEdit = intent.getStringExtra("week");
            String classname = db.getData(id, weekEdit, "classname");
            String timeF = db.getData(id, weekEdit, "timeF");
            String timeT = db.getData(id, weekEdit, "timeT");
            int odd = db.getIntData(id, weekEdit, "odd");
            int even = db.getIntData(id, weekEdit, "even");
            et_class.setText(classname);
            et_timeF.setText(timeF);
            et_timeT.setText(timeT);
            spinner_weekdays.setSelection(ch.getWeekNum(weekEdit));
            if (odd == 1 && even == 1) {
                radioEvery.setChecked(true);
                radioOdd.setChecked(false);
                radioEven.setChecked(false);
            } else if (odd == 1) {
                radioEvery.setChecked(false);
                radioOdd.setChecked(true);
                radioEven.setChecked(false);
            } else {
                radioEvery.setChecked(false);
                radioOdd.setChecked(false);
                radioEven.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        db = new database_helper(this);
        String classname = et_class.getText().toString().trim();
        String timeF = et_timeF.getText().toString().trim();
        String timeT = et_timeT.getText().toString().trim();
        int even = 1 ,odd = 1;
        switch(radioGP.getCheckedRadioButtonId()){
            case R.id.radioOdd:
                even = 0;
                odd = 1;
                break;
            case R.id.radioEven:
                even = 1;
                odd = 0;
                break;
        }
        switch (v.getId()){
            case R.id.btn_save:

                boolean d = db.InsertData(weekday, classname, timeF, timeT, odd, even);
                if (d) {
                    Toast.makeText(this,"ثبت شد!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this,"ثبت نشد!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit:
                boolean d1 = db.DeleteData(weekEdit,String.valueOf(id));
                boolean d2 = db.InsertData(weekday, classname, timeF, timeT, odd, even);
                if (d1 && d2) {
                    Toast.makeText(this,"ویرایش شد!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this,"ویرایش نشد!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_delete:
                boolean d3 = db.DeleteData(weekEdit,String.valueOf(id));
                if (d3) {
                    Toast.makeText(this,"حذف شد!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this,"حذف نشد!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        weekday = weekdays[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}