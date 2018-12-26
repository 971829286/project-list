package com.souche.bmgateway.core.util;

import java.math.BigDecimal;

/**
 * @author ccc
 * @date 2017/6/28
 */
public class MoneyUtil {
    private static final int POINT_POS = 2;

    /**
     * 单位分转到元
     */
    public static String fenToYuan(long money) {
        return new BigDecimal(money).movePointLeft(POINT_POS).toString();
    }


    /**
     * 单位分转到元，BigDecimal
     */
    public static BigDecimal fenToYuanBD(long money) {
        return new BigDecimal(money).movePointLeft(POINT_POS).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * double转为String,保留2位小数
     */
    public static String doubleToString(double money) {
        return new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 单位元转到分
     */
    public static long yuanToFen(String money) {
        return new BigDecimal(money).movePointRight(POINT_POS).longValue();
    }

    /**
     * 单位元转到分,返回为int
     */
    public static int yuanToIntFen(String money) {
        return new BigDecimal(money).movePointRight(POINT_POS).intValue();
    }

    /**
     * 单位元转到分,返回为int
     */
    public static int yuanToIntFen(BigDecimal money) {
        return money.movePointRight(POINT_POS).intValue();
    }

    /**
     * 四舍五入保留2位小数
     *
     * @param money
     * @return
     */
    public static BigDecimal save2Point(BigDecimal money) {
        return money.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 单位元转到分,并四舍五入保留2位小数
     *
     * @param money
     * @return
     */
    public static int yuanToFenAndSave2Point(BigDecimal money) {
        return yuanToIntFen(save2Point(money));
    }
}
