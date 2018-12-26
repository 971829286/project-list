package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对公对私属性
 * @author zs.
 *         Created on 18/7/24.
 */
@Getter
@AllArgsConstructor
public enum CardAttrEnums {

    PERSONAL("C", 1, "对私"), ENTERPRISE("B", 0, "对公");

    /*** 代码 ***/
    private final String code;

    /*** 内部代码 ***/
    private final Integer insCode;

    /*** 信息 ***/
    private final String message;

    /**
     * 通过代码获取枚举项
     *
     * @param code
     * @return
     */
    public static CardAttrEnums getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (CardAttrEnums at : CardAttrEnums.values()) {
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
    public static CardAttrEnums getByInsCode(Integer insCode) {
        if (insCode == null) {
            return null;
        }
        for (CardAttrEnums at : CardAttrEnums.values()) {
            if (at.getInsCode().equals(insCode)) {
                return at;
            }
        }
        return null;
    }
}
