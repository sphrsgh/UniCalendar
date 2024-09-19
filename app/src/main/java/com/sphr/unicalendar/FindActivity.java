package com.sphr.unicalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sphr.unicalendar.helpers.database_helper;

public class FindActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    EditText et_search;
    Button btn_search;
    ListView lvEdit;
    database_helper db;
    Intent intent;
    String classname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        init();
    }


    private void init(){
        db = new database_helper(this);
        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);
        lvEdit = findViewById(R.id.lvEdit);
        btn_search.setOnClickListener(this);
        lvEdit.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                classname = et_search.getText().toString().trim();
                ArrayAdapter<String> array = new ArrayAdapter<String>(this,
                        android.R.layout.simple_expandable_list_item_1, db.showSearch(classname));
                lvEdit.setAdapter(array);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int[] IDs = db.getIDs(classname);
        String[] weeks = db.getClassWeeks(classname);
        intent = new Intent(getApplicationContext(),InsertActivity.class);
        intent.putExtra("update",true);
        intent.putExtra("id",IDs[position]);
        intent.putExtra("week", weeks[position]);
        startActivity(intent);
        finish();

    }
}