package com.souche.bmgateway.core.manager.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 平台类型
 * @author zs.
 *         Created on 18/8/31.
 */
@Getter
@ToString
@AllArgsConstructor
public enum PlatFormTypeEnums {

    // 默认登录类型LOGIN_NAME，兼容老的创建会员接口
    LOGIN_NAME("3", "登陆名平台类型"),
    COMPANY_ID("4", "外部用户对应的会员平台类型");

    /*** 平台类型 ***/
    private String loginNamePlatformType;
    
    /*** 描述 ***/
    private String message;
}
