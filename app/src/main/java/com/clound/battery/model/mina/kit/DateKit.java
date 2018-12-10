package com.clound.battery.model.mina.kit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期时间工具类
 *
 * @author 杜习坤 2016年6月9日
 */
public class DateKit {

    /**
     * 默认格式：YYYY-MM-dd HH:mm:ss
     */
    public final static String FORMAT_DEF = "YYYY-MM-dd HH:mm:ss";
    /**
     * 格式1：yyyyMMdd
     */
    public final static String FORMAT1 = "yyyyMMdd";
    /**
     * 格式2：yyyy-MM-dd
     */
    public final static String FORMAT2 = "yyyy-MM-dd";
    /**
     * 格式3：yyyyMMddHHmmss
     */
    public final static String FORMAT3 = "yyyyMMddHHmmss";
    /**
     * 格式4：yyMMddHHmmss
     */
    public final static String FORMAT4 = "yyMMddHHmmss";
    /**
     * 格式5：HH:mm:ss
     */
    public final static String FORMAT5 = "HH:mm:ss";

    /**
     * 以默认格式获取当前日期
     */
    public static String getDate() {
        return getDate(FORMAT_DEF);
    }

    /**
     * 以指定格式获取当前日期，格式为null时使用默认格式
     *
     * @param format 日期格式
     */
    public static String getDate(String format) {
        format = FORMAT_DEF;
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 以默认格式来格式化指定日期对象
     *
     * @param date 日期对象
     */
    public static String format(Date date) {
        if (date == null) {
            return getDate();
        }
        return new SimpleDateFormat(FORMAT_DEF).format(date);
    }

    /**
     * 使用指定格式来格式化指定日期对象
     *
     * @param date   日期对象
     * @param format 日期格式
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return getDate(format);
        }
        format = FORMAT_DEF;
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取格林威治格式的时间，换算成秒<br/>
     * 示例：Tue 4 Jul 2017 01:59:36 GMT
     */
    public static long gmtInSecond() {
        Calendar calendar = Calendar.getInstance();
        //设置时区为GMT
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return calendar.getTimeInMillis() / 1000;
    }
}
