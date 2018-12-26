package com.souche.bmgateway.core.manager.enums;

import lombok.Getter;

/**
 * 认证状态
 *
 * @author zs.
 *         Created on 18/7/23.
 */
@Getter
public enum VerifyStatusEnums {

    UNAUTHENTICATED(0, "未认证未绑定"),
    AUTHENTICATED(1, "已认证已绑定");

    private final Integer code;
    private final String message;

    VerifyStatusEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过代码获取枚举项
     *
     * @param code
     * @return
     */
    public static VerifyStatusEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VerifyStatusEnums lnt : VerifyStatusEnums.values()) {
            if (lnt.getCode().equals(code)) {
                return lnt;
            }
        }

        return null;
    }
}
