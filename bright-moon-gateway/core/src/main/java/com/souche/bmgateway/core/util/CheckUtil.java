package com.souche.bmgateway.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckUtil {

    private static Logger logger = LoggerFactory.getLogger(CheckUtil.class);

    private static Integer mouthNum = 12;

    private static Integer dayNum = 31;

    private static Integer Feb = 2;

    private static Integer leapDate = 29;
    private static Integer noLeapDate = 28;
    /**
     * 判断参数的格式是否为“yyyyMMdd”格式的合法日期字符串
     *
     */
    public static boolean isValidDate(String date) {
        try {
            if(date == null || StringUtils.isBlank(date)
                    || date.length() != 8) {
                return false;
            }

            // 闰年标志
            boolean isLeapYear = false;
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            int vYear = Integer.parseInt(year);
            // 判断是否为闰年
            if (vYear % 4 == 0 && vYear % 100 != 0 || vYear % 400 == 0) {
                isLeapYear = true;
            }
            // 判断月份
            // 1.判断月份

            int vmonth = Integer.parseInt(month);
            int vday = Integer.parseInt(day);
            if(vmonth > 0  && vmonth <= mouthNum) {
                if (vmonth == Feb) {
                    if (isLeapYear) {
                        if (vday > leapDate){
                            return false;
                        }
                    } else {
                        if (vday > noLeapDate) {
                            return false;
                        }
                    }
                }
            } else  {
                return false;
            }
            // 判断日期
            // 1.判断日期
            if(vday < 0 || vday > dayNum) {
                return false;
            }
            return true;

        } catch (Exception e) {
            logger.error("判断日期是否合法异常, e={}", e);
            return false;
        }
    }
}
