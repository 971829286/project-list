package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业信息相关枚举
 *
 * @author chenwj
 * @since 2018/8/2
 */
@Getter
@AllArgsConstructor
public enum ShopInfoEnums {

    /**
     * 企业认证信息JSON key值
     */
    SHOP_AUTH("SHOP_AUTH", "shopAuth"),

    /**
     * 查询企业信息接口返回
     */
    QUERY_INFO_SUCCESS("code", "200"),

    /**
     * 审核状态
     */
    REVIEW_STATUS("REVIEW_STATUS", "reviewStatus"),

    /**
     * 审核状态，0-待审核，1-审核通过, 2-审核不通过
     */
    REVIEW_STATUS_SUCCESS("REVIEW_STATUS_SUCCESS", "1"),

    /**
     * e签宝审核状态
     */
    E_STATUS("E_STATUS", "eStatus"),

    /**
     * e签宝审核状态，0-未认证，1-已通过, 2-不通过
     */
    E_STATUS_SUCCESS("E_STATUS_SUCCESS", "1"),

    /**
     * 认证状态
     */
    CERT_STATUS("CERT_STATUS", "certStatus"),

    /**
     * 认证状态，0-未认证，1-已认证
     */
    CERT_STATUS_SUCCESS("CERT_STATUS_SUCCESS", "1"),

    /**
     * 身份证正面
     */
    ID_CARD("ID_CARD", "identityCardCorrect"),

    /**
     * 身份证背面
     */
    ID_CARD_BACK("ID_CARD_BACK", "identityCardOpposite"),

    /**
     * 营业执照照片
     */
    BUSINESS_LIC("BUSINESS_LIC", "businessLicPic"),

    /**
     * 开户许可证
     */
    ACCT_OPEN_LIC("ACCT_OPEN_LIC", "acctOpenLicensePic"),

    /**
     * 手机号
     */
    PHONE("PHONE", "phone"),

    /**
     * 固定电话
     */
    ADDRESS_CALL("ADDRESS_CALL", "addresscall"),

    /**
     * 企业名称
     */
    LIC_COMPANY_NAME("LIC_COMPANY_NAME", "licenseCompanyName"),

    /**
     * 法人姓名
     */
    CORPORATION_NAME("CORPORATION_NAME", "corporationName"),

    /**
     * 证件号
     */
    IDENTITY_CODE("IDENTITY_CODE", "identityCode"),

    /**
     * 营业执照号
     */
    CREDIT_CODE("CREDIT_CODE", "creditCode"),
    BUSINESS_LIC_CODE("BUSINESS_LIC_CODE", "businessLicenseCode"),

    /**
     * 区域码
     */
    REGION_CODE("REGION_CODE", "regionCode"),

    /**
     * 详细地址
     */
    PLACE("PLACE", "place"),

    /**
     * 企业别名
     */
    ALIAS("ALIAS", "shortname");

    private String code;
    private String value;

}
