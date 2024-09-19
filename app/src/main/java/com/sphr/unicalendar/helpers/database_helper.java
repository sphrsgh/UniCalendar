package com.sphr.unicalendar.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class database_helper extends SQLiteOpenHelper {

    public static final String DB_NAME = "program_db";
    public static final String[] week_tables = {"sat","sun","mon","tue","wed","thu","fri"};
    public static final String[] week_per = {"شنبه","یکشنبه","دوشنبه","سه شنبه","چهارشنبه","پنجشنبه","جمعه"};

    public static final String TBL_FLAG = "flag";
    Calendar_helper ch;

    public database_helper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String var : week_tables)
        {
            db.execSQL("CREATE TABLE " + var + "( id INTEGER PRIMARY KEY AUTOINCREMENT , " + "classname TEXT , " +
                    "timeF TEXT , " + "timeT TEXT , " + "odd INTEGER , " + "even INTEGER , " + "week TEXT );");
        }
        db.execSQL("CREATE TABLE " + TBL_FLAG + "(fs TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String var : week_tables){
            db.execSQL("DROP TABLE " + var + ";");
        }
        onCreate(db);
    }

    public boolean setFlag() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fs", "true");
        long result = db.insert("flag", null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean getFlag() {
        boolean flag = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM flag;";
        Cursor cursor;
        cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            if (cursor.getString(0) != "")
                flag = true;
        }
        return flag;
    }

    public Boolean InsertData(String tbl_name, String classname, String timeF, String timeT, int odd, int even) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("classname", classname);
        cv.put("timeF", timeF);
        cv.put("timeT", timeT);
        cv.put("odd", odd);
        cv.put("even", even);
        cv.put("week", tbl_name);
        long result = db.insert(tbl_name, null, cv);
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public Boolean DeleteData(String tbl_name, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tbl_name," id = '"+id+"' " , null);
        if (result == -1) {
            return false;
        } else
            return true;
    }

    public List<String> showWeek(int dayChange) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> post = new ArrayList<String>();
        ch = new Calendar_helper();
        Cursor cursor = null;
        for (int i = 0 ; i<7 ; i++){
            String query = "SELECT classname, timeF, timeT FROM " + week_tables[i];
            if(ch.isEven(dayChange))
                query += " WHERE even = 1 ";
            else
                query += " WHERE odd = 1 ";
            query+= "ORDER BY timeF ;";
            cursor = db.rawQuery(query, null);
            post.add("                          "+week_per[i]+
                    "\n                      "+ch.getWeekDates(week_tables[i],dayChange));
            if (cursor.moveToFirst()) {
                do {
                    post.add(cursor.getString(0) + " : "  + cursor.getString(1)
                            + " تا "  + cursor.getString(2));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return post;
    }

    public List<String> showDay(int dayChange) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> post = new ArrayList<String>();
        Cursor cursor;

        int theDay = ch.getWeekNum(ch.getWeekday())+dayChange;

        while (theDay<0)
            theDay += 7;
        while (theDay>6)
            theDay -= 7;

        String query = "SELECT classname, timeF, timeT FROM " + week_tables[theDay] ;
        if(ch.isEven(dayChange))
            query += " WHERE even = 1 ;";
        else
            query += " WHERE odd = 1 ;";
        query+= "ORDER BY timeF ;";
        cursor = db.rawQuery(query, null);
        post.add("                          "+week_per[theDay]+
                "\n                      "+ ch.getWeekDates(week_tables[ch.getWeekNum(ch.getWeekday())],dayChange));
            if (cursor.moveToFirst()) {
                do {
                    post.add(cursor.getString(0) + " : "  + cursor.getString(1)
                            + " تا "  + cursor.getString(2));
                } while (cursor.moveToNext());
            }
        cursor.close();
        db.close();
        return post;
    }

    public List<String> showSearch(String classname) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> post = new ArrayList<String>();
        String weekly;
        Cursor cursor = null;
        for (int i = 0 ; i<7 ; i++) {
            String query = "SELECT * FROM " + week_tables[i] + " WHERE classname LIKE '%" + classname + "%'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int odd = cursor.getInt(4);
                    int even = cursor.getInt(5);
                    if (odd == 1 && even == 1)
                        weekly = "هر هفته";
                    else if (odd == 1)
                        weekly = "فرد";
                    else
                        weekly = "زوج";
                    post.add(week_per[i] + "\n"+ cursor.getString(1) + " : " + cursor.getString(2)
                            + " تا " + cursor.getString(3) + " : "+weekly);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return post;
    }

    public int[] getIDs(String classname){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> post = new ArrayList<String>();
        ch = new Calendar_helper();
        Cursor cursor = null;
        for (int i = 0 ; i<7 ; i++){
            String query = "SELECT * FROM " + week_tables[i] + " WHERE classname LIKE '%" + classname + "%'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    post.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        db.close();

        int[] res = new int[post.toArray().length];
        for (int i = 0; i < post.toArray().length; i++) {
            res[i] = Integer.parseInt((String) post.toArray()[i]);
        }
        return res;
    }

    public String[] getClassWeeks(String classname){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> post = new ArrayList<String>();
        ch = new Calendar_helper();
        Cursor cursor = null;
        for (int i = 0 ; i<7 ; i++){
            String query = "SELECT week FROM " + week_tables[i] + " WHERE classname LIKE '%" + classname + "%'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    post.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        db.close();

        String[] res = new String[post.toArray().length];
        for (int i = 0; i < post.toArray().length; i++) {
            res[i] = (String) post.toArray()[i];
        }
        return res;
    }

    public String getData(int id, String week, String data){
        SQLiteDatabase db = this.getReadableDatabase();
        ch = new Calendar_helper();
        String res = "";
        Cursor cursor;
        String query = "SELECT "+data+" FROM " + week + " WHERE id = '"+id+"' ";
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                res = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return res;
    }

    public int getIntData(int id, String week, String data){
        SQLiteDatabase db = this.getReadableDatabase();
        ch = new Calendar_helper();
        int res = 0;
        Cursor cursor;
        String query = "SELECT "+data+" FROM " + week + " WHERE id = '"+id+"' ";
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                res = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return res;
    }
}


