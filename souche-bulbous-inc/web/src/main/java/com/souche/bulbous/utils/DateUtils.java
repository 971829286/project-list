package com.souche.bulbous.utils;

import com.souche.optimus.common.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils {
    public static final String DATE_FORMAT_YYYSMMSDD = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYYYSMMSDD_HHMM = "yyyy/MM/dd HH:mm";

    public static final long SECOND_MSEC = 1000;
    public static final long MINUTE_MSEC = SECOND_MSEC * 60;
    public static final long HOUR_MSEC = MINUTE_MSEC * 60;
    public static final long DAY_MSEC = HOUR_MSEC * 24;

    public static String formatDate(Date date, String format) {
        if (null == date) {
            return "";
        }
        try {
            SimpleDateFormat f = new SimpleDateFormat(format);
            return f.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date parseDate(String str, String format) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDateByLong(Long value, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
            String formatDate = simpleDateFormat.format(new Date(value));
            return formatDate;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取一天的开始时间
     *
     * @param date
     * @return
     */
    public static Date dateToDayBegin(Date date) {
        if (null == date) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }


    /**
     * 获取一天的结束时间
     *
     * @param date
     * @return
     */
    public static Date dateToDayEnd(Date date) {
        if (null == date) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    /**
     * 计算日期
     *
     * @param date
     * @param field     the calendar field.
     * @param dayNumber
     * @return
     */
    public static Date dateCalculate(Date date, int field, int dayNumber) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(field, dayNumber);
        return instance.getTime();
    }

    /**
     * 删除时分秒
     *
     * @param date
     * @return
     */
    public static Date clearDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        return instance.getTime();

    }
    public static Date clearDate(Long date) {
        return clearDate(new Date(date));
    }

    /**
     * 删除时分秒
     *
     * @param date
     * @return
     */
    public static Long clearDateLong(Date date) {
        return clearDate(date).getTime();
    }

    public static Long clearDateLong(Long date) {
        return clearDate(new Date(date)).getTime();
    }

    /**
     * 计算时间差，转换成 几分钟钱 几小时钱 几天前 几月前 几年前
     *
     * @param createDate
     * @return
     */
    public static String calculateSubDateToShowLabel(long createDate) {
        long l = System.currentTimeMillis() - createDate;
        if (l < SECOND_MSEC) {
            return "刚刚";
        } else if (l < MINUTE_MSEC) {
            return l / SECOND_MSEC + "秒前";
        } else if (l < HOUR_MSEC) {
            return l / MINUTE_MSEC + "分钟前";
        } else if (l < DAY_MSEC) {
            return l / HOUR_MSEC + "小时前";
        } else {
            return l / DAY_MSEC + "天前";
        }
    }

    /**
     * 前面时间与后面时间的时间差
     * @param date
     * @param startDate
     * @return
     */
    public static Integer dateDiffDayCount(Date date, Date startDate) {
        Date date1 = clearDate(date);
        Date date2 = clearDate(startDate);
        return Math.toIntExact((date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000));
    }
}
