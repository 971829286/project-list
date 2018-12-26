package com.souche.bmgateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式枚举
 *
 * @author zs.
 *         Created on 18/11/26.
 */
@AllArgsConstructor
@Getter
public enum PayMethodEnums {

    BALANCE("01"), ONLINE_BANK("02"), POS("03"), CASH("04"), QPAY("05");

    /*** channelCode, 其中余额和现金支付取此值来配置手续费，其它支付方式取bankCode来配置手续费 ***/
    private String code;

    /**
     * 获取支付方式类
     *
     * @param payMethod
     * @return
     */
    public static PayMethodEnums getPayMethodEnums(String payMethod) {
        for (PayMethodEnums payMethodEnums : PayMethodEnums.values()) {
            if (payMethodEnums.name().equalsIgnoreCase(payMethod)) {
                return payMethodEnums;
            }
        }
        return null;
    }
}
