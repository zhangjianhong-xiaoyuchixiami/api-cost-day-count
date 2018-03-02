package org.qydata.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String tomorrow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar. getInstance ();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        Date time = cal.getTime();
        String tomorrow = sdf.format(time);
        return tomorrow;
    }

    public static String today(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar. getInstance ();
        Date time = cal.getTime();
        String today = sdf.format(time);
        return today;
    }

    public static String todayDawn(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cal = Calendar. getInstance ();
        Date time = cal.getTime();
        String todayDawn = sdf.format(time);
        return todayDawn;
    }


    public static String yesterdayDawn(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cal = Calendar. getInstance ();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        Date time = cal.getTime();
        String yesterDawn = sdf.format(time);
        return yesterDawn;
    }

    public static String afterYesterdayDawn(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cale = Calendar. getInstance ();
        cale.set(Calendar.DAY_OF_MONTH, cale.get(Calendar.DAY_OF_MONTH) - 2);
        Date time = cale.getTime();
        String afterYesterDawn = sdf.format(time);
        return afterYesterDawn;
    }

    public static void main(String[] args) {
        String s = afterYesterdayDawn();
        System.out.println(s);
    }
}
