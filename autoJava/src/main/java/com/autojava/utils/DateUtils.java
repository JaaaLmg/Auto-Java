package com.autojava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @FileName DateUtils
 * @Description 时间工具类
 * @Author LumingJia
 * @date 2026/3/14
 **/
public class DateUtils {
    public static final String date_time_format = "yyyy-MM-dd HH:mm:ss";
    public static final String date_format_1 = "yyyy-MM-dd";
    public static final String date_format_2 = "yyyy/MM/dd";
    public static final String date_format_3 = "yyyyMMdd";

    public static String format(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String date, String pattern){
        try {
            new SimpleDateFormat(pattern).parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }
}
