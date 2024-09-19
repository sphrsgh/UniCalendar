package com.sphr.unicalendar.helpers;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Calendar_helper {

    int gday = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(new Date()));
    int gmonth = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date()));
    int gyear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date()));
    String weekday = new SimpleDateFormat("EEE", Locale.ENGLISH).format(new Date());
    int[] sMonthDays = {31,31,31,31,31,31,30,30,30,30,30,29};
    String[] mon_per = {"فروردین","اردیبهشت","خرداد","تیر","مرداد","شهریور","مهر","آبان","آذر","دی","بهمن","اسفند"};
    String[] week_per = {"شنبه","یکشنبه","دوشنبه","سه شنبه","چهارشنبه","پنجشنبه","جمعه"};

    public boolean gLeapYear(int year) {
        return (year % 400 == 0) || (year % 4 == 0) && (year % 100 != 0);
    }

    public boolean sLeapYear(int year) {
        int[] arr = {1, 5, 9, 13, 17, 22, 26, 30};
        int b = year % 33;
        return Objects.equals(arr, b);
    }

    public String getShamsiDate() {

        return getDay() + " " + mon_per[getMonth()-1] + " " + getYear();
    }

    public int[] getShamsiDMY() {
        int[] _gl = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        int[] _g = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};

        int deydiffjan = 10;
        int gd, sd, sm, gmod, sy;

        if (gLeapYear(gyear - 1))
            deydiffjan = 11;
        if (gLeapYear(gyear))
            gd = _gl[gmonth - 1] + gday;
        else
            gd = _g[gmonth - 1] + gday;
        if (gd > 79) {
            sy = gyear - 621;
            gd = gd - 79;
            if (gd <= 186) {
                gmod = gd % 31;
                if (gmod == 0) {
                    sd = 31;
                    sm = gd / 31;
                } else {
                    sd = gmod;
                    sm = (gd / 31) + 1;
                }
            } else {
                gd = gd - 186;
                gmod = gd % 30;
                if (gmod == 0) {
                    sd = 30;
                    sm = (gd / 30) + 6;
                } else {
                    sd = gmod;
                    sm = (gd / 30) + 7;
                }
            }
        } else {
            sy = gyear - 622;
            gd = gd + deydiffjan;
            gmod = gd % 30;
            if (gmod == 0) {
                sd = 30;
                sm = (gd / 30) + 9;
            } else {
                sd = gmod;
                sm = (gd / 30) + 10;
            }
        }
        if (sLeapYear(sy))
            sMonthDays[11] = 30;
        return new int[]{sd, sm, sy};
    }

    public String getWeekDates(String day , int dayChange){
        int d, m, y;
        int dc = getWeekNum(getWeekday())*-1;
        String[] res = new String[7];

        for (int i = 0; i<7 ; i++) {
            d = dayChange + getShamsiDMY()[0];
            m = getShamsiDMY()[1];
            y = getShamsiDMY()[2];
            if(sLeapYear(y))
                sMonthDays[11]=30;

            d += dc;

            while (d>sMonthDays[m-1]) {
                if(sLeapYear(y+1))
                    sMonthDays[11]=30;
                d -= sMonthDays[m-1];
                m += 1;
                if (m > 12) {
                    m -= 12;
                    y += 1;
                }
            }
            while (d<1) {
                if(sLeapYear(y-1))
                    sMonthDays[11]=30;
                m -= 1;
                if (m < 1) {
                    m += 12;
                    y -= 1;
                }
                d += sMonthDays[m-1];
            }

            dc++;
            res[i] = d+" "+ mon_per[m-1]+" "+y;
            sMonthDays[11]=29;
        }
        int index = getWeekNum(day.trim());
        return res[index];
    }

    public int getDay(){
        return getShamsiDMY()[0];
    }

    public int getMonth(){
        return getShamsiDMY()[1];
    }

    public int getYear(){
        return getShamsiDMY()[2];
    }

    public String getWeekday(){
        return weekday.toLowerCase();
    }

    public int getWeekNum(String val){
        int res = 0;
        switch (val){
            case "sun":
                res = 1;
                break;
            case "mon":
                res = 2;
            break;
            case "tue":
                res = 3;
            break;
            case "wed":
                res = 4;
            break;
            case "thu":
                res = 5;
            break;
            case "fri":
                res = 6;
            break;
        }
        return res;
    }

    public String getWeekPer(){
        return week_per[getWeekNum(getWeekday())];
    }

    public boolean isEven(int dayChange){
        int theDay = getWeekNum(getWeekday())+1+dayChange;
        while (theDay % 7 != 0)
            theDay++;
        int sum = theDay;
        for (int i = 1 ; i<=getMonth() ; i++)
            sum += sMonthDays[i];
        sum+= getDay();
        return  ((sum / 7) % 2 != 0);
    }
}
