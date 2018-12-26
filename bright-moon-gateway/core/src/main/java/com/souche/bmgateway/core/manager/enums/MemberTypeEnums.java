package com.souche.bmgateway.core.manager.enums;

import lombok.Getter;

/**
 * 会员类型枚举
 *
 * @author zs.
 *         Created on 18/7/19.
 */
@Getter
public enum MemberTypeEnums {
    PERSONAL("1", "个人", 101L), ENTERPRISE("2", "企业", 201L);

    /*** 代码 ***/
    private final String code;

    /*** 信息 ***/
    private final String message;

    /*** 基本户类型 ***/
    private final Long baseAccount;

    MemberTypeEnums(String code, String message, Long baseAccount) {
        this.code = code;
        this.message = message;
        this.baseAccount = baseAccount;
    }
}
