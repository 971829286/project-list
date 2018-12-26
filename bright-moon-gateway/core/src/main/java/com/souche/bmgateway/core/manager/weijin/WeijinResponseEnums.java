package com.souche.bmgateway.core.manager.weijin;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zs.
 *         Created on 18/8/2.
 */
@Getter
@AllArgsConstructor
public enum WeijinResponseEnums {

    SUCCESS("0", "处理成功"),
    QUERY_NO_RESULT("001", "无查询结果"),
    MEMBER_NOT_EXIST("103", "会员不存在"),
    MEMBER_IDENTITY_EXIST("104","会员标识已存在"),
    MEMBER_VERIFY_EXIST("455","会员认证信息重复");

    /*** 代码 ***/
    private final String code;
    /*** 信息 ***/
    private final String message;
}
