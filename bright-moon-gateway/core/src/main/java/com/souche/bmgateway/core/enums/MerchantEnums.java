package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户入驻相关枚举类
 *
 * @author: chenwj
 * @since: 2018/7/24
 */
@Getter
@AllArgsConstructor
public enum MerchantEnums {

    /**
     * 商户入驻结果查询，接口返回RegisterStatus字段
     */
    REGISTER_STATUS("REGISTER_STATUS", "", "RegisterStatus"),

    /**
     * 商户入驻结果查询_成功:1
     */
    REGISTER_STATUS_SUCCESS("REGISTER_STATUS_SUCCESS", "商户入驻结果查询_成功", "1"),

    /**
     * 商户入驻结果查询_失败:2
     */
    REGISTER_STATUS_FAIL("REGISTER_STATUS_FAIL", "商户入驻结果查询_失败", "2"),

    /**
     * 商户入驻结果查询_审核中:0
     */
    REGISTER_STATUS_PROCESS("REGISTER_STATUS_PROCESS", "商户入驻结果查询_审核中", "0"),

    /**
     * 商户入驻结果查询，接口返回ResultStatus字段
     */
    RESULT_STATUS("RESULT_STATUS", "", "ResultStatus"),

    /**
     * 商户入驻结果查询_查询成功:S
     */
    RESULT_STATUS_SUCCESS("RESULT_STATUS_SUCCESS", "商户入驻结果查询_查询成功", "S"),

    ;

    /**
     * code
     */
    private String code;

    /**
     * 描述
     */
    private String name;

    /**
     * 网商对应码值
     */
    private String mCode;

}
