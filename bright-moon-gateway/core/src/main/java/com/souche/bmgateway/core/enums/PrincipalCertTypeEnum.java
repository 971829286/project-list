package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 证件类型
 *
 * @author chenwj
 * @since 2018/07/16
 */
@Getter
@AllArgsConstructor
public enum PrincipalCertTypeEnum {

    /**
     * 身份证
     */
    IdentityCard("01", "身份证");

    /**
     * code
     */
    private String certCode;

    /**
     * 描述
     */
    private String certDesc;

}
