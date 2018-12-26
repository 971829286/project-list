package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 交易类型
 *
 * @author chenwj
 * @since 2018/07/16
 */
@Getter
@AllArgsConstructor
public enum TradeTypeEnum {

    /**
     * 正扫交易
     */
    Forward("01", "正扫交易"),

    /**
     * 反扫交易
     */
    Backward("02", "反扫交易"),

    /**
     * 退款交易
     */
    Refund("06", "退款交易"),

    /**
     * 动态订单扫码
     */
    SCAN("08", "动态订单扫码"),

    /**
     * APP/WAP支付
     */
    APPWAP("09", "APP/WAP支付");

    private String tradeCode;
    private String tradeDesc;

    public static String genTradeTypeList(List<TradeTypeEnum> list) {
        StringBuilder sb = new StringBuilder();

        for (TradeTypeEnum e : list) {
            sb.append(e.getTradeCode());
            sb.append(",");
        }

        String rst = sb.toString();
        return rst.length() > 0 ? rst.substring(0, rst.length()) : rst;
    }

}
