package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zs.
 *         Created on 18/7/24.
 */
@Getter
@AllArgsConstructor
public enum CardTypeEnums {

    DEBIT_CARD("DEBIT", 1, "DC", "借记卡"),
    CREDIT_CARD("CREDIT", 2, "CC", "信用卡"),
    PASSBOOK("PASSBOOK", 3, "PB", "存折"),
    OTHER("OTHER", 4, "OC", "其它");

    /*** 代码 ***/
    private final String code;

    /*** 内部代码 ***/
    private final Integer insCode;

    /*** 代码缩写 ***/
    private final String shortCode;

    /*** 信息 ***/
    private final String message;

    /**
     * 通过代码获取枚举项
     *
     * @param code
     * @return
     */
    public static CardTypeEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CardTypeEnums at : CardTypeEnums.values()) {
            if (at.getCode().equals(code)) {
                return at;
            }
        }
        return null;
    }

    /**
     * 通过内部代码获取枚举项
     *
     * @param insCode
     * @return
     */
    public static CardTypeEnums getByInsCode(Integer insCode) {
        if (insCode == null) {
            return null;
        }
        for (CardTypeEnums at : CardTypeEnums.values()) {
            if (at.getInsCode().equals(insCode)) {
                return at;
            }
        }
        return null;
    }
}
