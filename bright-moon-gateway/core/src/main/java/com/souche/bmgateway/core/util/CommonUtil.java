package com.souche.bmgateway.core.util;

import com.netfinworks.common.util.DateUtil;
import com.souche.optimus.common.util.DateTimeUtil;

import java.util.Date;
import java.util.List;

/**
 * 常用工具类
 *
 * @since 2018/07/12
 */
public class CommonUtil {

    /**
     * 获取当前日期，如20180707
     *
     * @return
     */
    public static String getToday(){
        return DateTimeUtil.getDateByFormat(new Date(), "yyyyMMdd");
    }

    /**
     * 获取离当天N天的日期(YYYYMMDD)
     */
    public static String getDate(Integer i) {
        Date yesterday = DateUtil.addDays(new Date(), i);
        return DateUtil.getDateString(yesterday);
    }

    public static String listToString(List list, char separator) {    StringBuilder sb = new StringBuilder();    for (int i = 0; i < list.size(); i++) {        sb.append(list.get(i)).append(separator);    }    return sb.toString().substring(0,sb.toString().length()-1);}

}
