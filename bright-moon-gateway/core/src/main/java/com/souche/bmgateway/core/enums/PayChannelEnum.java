package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 支付渠道
 *
 * @author chenwj
 * @since 2018/7/16
 */
@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    /**
     * 支付宝线上下
     */
    Ali("01", "支付宝线上下"),

    /**
     * 微信线下
     */
    WX("02", "微信线下"),

    /**
     * 手机QQ
     */
    QQ("03", "手机QQ"),

    /**
     * 京东钱包
     */
    JD("04", "京东钱包"),

    /**
     * 支付宝线上
     */
    AliOnline("05", "支付宝线上");

    private String chnCode;
    private String chnDesc;

    public static String genPayChannelList(List<PayChannelEnum> list) {
        StringBuilder sb = new StringBuilder();
        for (PayChannelEnum e : list) {
            sb.append(e.getChnCode());
            sb.append(",");
        }
        String rst = sb.toString();
        return rst.length() > 0 ? rst.substring(0, rst.length()) : rst;
    }

}
