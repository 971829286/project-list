package com.souche.bmgateway.core.manager.enums;

import lombok.Getter;

/**
 * 登录名类型类型枚举
 *
 * @author zs.
 *         Created on 18/7/23.
 */
@Getter
public enum VerifyTypeEnums {
    ID_CARD(1, "身份证"),
    CELL_PHONE(11, "手机号"),
    EMAIL(12, "邮箱");

    /*** 代码 ***/
    private final Integer code;

    /*** 信息 ***/
    private final String message;

    VerifyTypeEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过代码获取枚举项
     *
     * @param code
     * @return
     */
    public static VerifyTypeEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VerifyTypeEnums lnt : VerifyTypeEnums.values()) {
            if (lnt.getCode().equals(code)) {
                return lnt;
            }
        }
        return null;
    }
}
