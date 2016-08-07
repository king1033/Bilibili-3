package org.pqh.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 10295 on 2016/8/4.
 */
public class TimeUtil {
    /**
     * 毫秒换算几分钟几秒几毫秒
     * @param time 毫秒
     * @return
     */
    public static String longTimeFormatString(long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String timestr="";
        int ms=calendar.get(Calendar.MINUTE)>0?calendar.get(Calendar.MINUTE)*60*1000:0;
        if(ms>0){
            timestr+=calendar.get(Calendar.MINUTE)+"m";
        }
        ms+=calendar.get(Calendar.SECOND)>0?calendar.get(Calendar.SECOND)*1000:0;
        if(ms>0){
            timestr+=calendar.get(Calendar.SECOND)+"s";
        }
        return timestr+((time-ms)>0?(time-ms)+"ms":"");
    }
    /**
     * 日期转字符串
     * @param date 日期
     * @param format 格式
     * @return
     */
    public static String formatDateToString(Date date, String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format!=null?format:Constant.DATE);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转日期
     * @param date 日期字符串
     * @param format 格式
     * @return
     */
    public static Date formatStringToDate(String date,String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format!=null?format:Constant.DATE);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("无法按照\t\""+format+"\"\t格式解析日期");
        }
    }
}
