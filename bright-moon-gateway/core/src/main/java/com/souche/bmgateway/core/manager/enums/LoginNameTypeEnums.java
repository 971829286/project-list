package com.souche.bmgateway.core.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 登录名类型
 * @author zs.
 *         Created on 18/8/31.
 */
@Getter
@ToString
@AllArgsConstructor
public enum LoginNameTypeEnums {

    EMAIL(1, "邮箱"),
    CELL_PHONE(2, "手机号"),
    COMMON_CHAR(3, "普通文字账号");

    /*** 登录名类型 ***/
    private Integer loginNameType;

    /*** 描述 ***/
    private String message;
}
