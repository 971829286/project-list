package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zs.
 *         Created on 18/7/24.
 */
@Getter
@AllArgsConstructor
public enum PayAttrEnums {

    QPAY("qpay", "大快捷"), NORMAL("normal", "普通提现卡");

    /*** 代码 ***/
    private final String code;

    /*** 信息 ***/
    private final String message;

    /**
     * 通过代码获取枚举项
     *
     * @param code
     * @return
     */
    public static PayAttrEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (PayAttrEnums at : PayAttrEnums.values()) {
            if (at.getCode().equalsIgnoreCase(code)) {
                return at;
            }
        }
        return null;
    }
}
