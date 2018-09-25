package com.souche.niu.util;

import com.souche.optimus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author ZhangHui
 * @since 2018-07-10
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    public static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DAY = "yyyy-MM-dd";
    public static final String PATTERN_DAY_TWO = "yyyyMMdd";
    public static final String PATTERN_PUSH = "yyyy年MM月dd日 HH:mm";

    /**
     * 获得可以正常显示的时间
     *
     * @param time
     * @return
     */
    public static String normalizeTime(long time) {
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DEFAULT);
        Date date = new Date(time);
        return sm.format(date);
    }

    public static String normalizeTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DEFAULT);
        return sm.format(date);
    }

    /**
     * Date转换String
     *
     * @param date
     * @return
     */
    public static String parseDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sm = new SimpleDateFormat(format);
        return sm.format(date);
    }

    /**
     * timeMillis 转换String
     *
     * @param timeMillis
     * @param format
     * @return
     */
    public static String parseTimeMillis(Long timeMillis, String format) {
        if (timeMillis == null) {
            return "";
        }
        SimpleDateFormat sm = new SimpleDateFormat(format);
        return sm.format(timeMillis);
    }

    /**
     * 字符串转为Date
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sm = new SimpleDateFormat(format);
        try {
            return sm.parse(date);
        } catch (ParseException e) {
            logger.error("parseDate error", e);
            return null;
        }
    }

    /**
     * 取一天最大的时间
     *
     * @param date
     * @return
     */
    public static Date ceiling(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DAY);
        String day = sm.format(date) + " 23:59:59";
        return parseDate(day, PATTERN_DEFAULT);
    }

    /**
     * 取一天最小的时间
     *
     * @param date
     * @return
     */
    public static Date floor(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DAY);
        String day = sm.format(date) + " 00:00:00";
        return parseDate(day, PATTERN_DEFAULT);
    }

    /**
     * Date类型转换Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp toTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /**
     * 日期增加n天
     *
     * @param sDate
     * @param n
     * @return
     */
    public static Date addDays(Date sDate, int n) {
        if (sDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 日期减少n天
     *
     * @param sDate
     * @param n
     * @return
     */
    public static Date reduceDays(Date sDate, int n) {
        if (sDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.DATE, 0 - n);
        return cal.getTime();
    }

    /**
     * 求两日期之间相差几天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || "".equals(date1)) {
            return 0;
        }
        if (date2 == null || "".equals(date2)) {
            return 0;
        }
        SimpleDateFormat sf = new SimpleDateFormat(PATTERN_DAY);
        Date date = null;
        Date mydate = null;
        try {
            date = sf.parse(date1);
            mydate = sf.parse(date2);
        } catch (Exception e) {
            logger.error("getDays parseDate error", e);
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return Math.abs(day);
    }

    /**
     * 求两日期之间相差几天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long spacingDays(String date1, String date2) {
        if (date1 == null || "".equals(date1)) {
            return 0;
        }
        if (date2 == null || "".equals(date2)) {
            return 0;
        }
        SimpleDateFormat sf = new SimpleDateFormat(PATTERN_DAY);
        Date date = null;
        Date mydate = null;
        try {
            date = sf.parse(date1);
            mydate = sf.parse(date2);
        } catch (Exception e) {
            logger.error("spacingDays parseDate error", e);
        }
        return (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 月份转换
     *
     * @param date
     * @return
     */
    public static String formatMonth(String date) {
        if (StringUtil.isEmpty(date)) {
            return date;
        } else if (date.contains("-12") || date.contains("-11") || date.contains("-10") || date.contains("-0")) {
            return date;
        }
        return date.replace("-", "-0");
    }

    public static String ceilingStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DAY);
        String day = sm.format(date) + " 23:59:59";
        return day;
    }

    public static String floorStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DAY);
        String day = sm.format(date) + " 00:00:00";
        return day;
    }

    //计算当日剩余秒数
    public static long secondsLeftToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        Date nextDay = null;
        try {
            nextDay = dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long mill = nextDay.getTime() - System.currentTimeMillis();
        return mill / 1000;

    }

    /**
     * 今天yyyy-MM-dd
     */
    public static String today() {
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DAY);
        return sm.format(new Date());
    }

    /**
     * 今天Date
     */
    public static Date getTodayDate() {
        SimpleDateFormat sm = new SimpleDateFormat(PATTERN_DAY);
        String format = sm.format(new Date());
        Date parse = null;
        try {
            parse = sm.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    /**
     * 今天是周几
     *
     * @return
     */
    public static int weekDay() {
        Calendar now = Calendar.getInstance();
        //获取周几,从周日开始，所以-1
        return now.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 返回字符串类型的一天中的最大值
     * @param string
     * @return
     */
    public static String ceilingString (String string){
        return string + " 23:59:59";
    }
}
