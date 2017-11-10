package org.qydata.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jonhn on 2017/3/8.
 */
public class CalendarTools {

    public static void main(String[] args) {
        System.out.println(getCurrentTime());
        System.out.println(getCurrentPreviousTime());
    }

    /**
     * 取得当前时间
     * @return
     */
    public static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        String previousDay = sdf.format(c.getTime());
        return previousDay;
    }

    /**
     * 取得当前时间前一天时间
     * @return
     */
    public static String getCurrentPreviousTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-1);
        String previousDay = sdf.format(c.getTime());
        return previousDay;
    }




}
