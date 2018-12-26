package com.souche.bmgateway.core.service.merchant.builder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商户信息 业务对象
 *
 * @author chenwj
 * @since 2018/8/23
 */
@Setter
@Getter
@ToString
public class ShopInfoBO {

    /**
     * 身份证正面照图片链接
     */
    private String idCard;

    /**
     * 身份证反面照图片链接
     */
    private String idCardBack;

    /**
     * 营业执照图片链接
     */
    private String businessLicPic;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * 企业名称
     */
    private String licCompanyName;

    /**
     * 法人姓名
     */
    private String corporationName;

    /**
     * 证件号
     */
    private String identityCode;

    /**
     * 营业执照号码
     */
    private String creditCode;

    /**
     * 省码值
     */
    private String provinceCode;

    /**
     * 市码值
     */
    private String cityCode;

    /**
     * 区码值
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 企业别名
     */
    private String alias;

}
