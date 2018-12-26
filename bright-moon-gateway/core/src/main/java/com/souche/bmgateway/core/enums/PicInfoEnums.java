package com.souche.bmgateway.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图片上传
 *
 * @author chenwj
 * @since 2018/7/23
 */
@Getter
@AllArgsConstructor
public enum PicInfoEnums {

    /**
     * 身份证正面
     */
    ID_CARD("ID_CARD", "身份证正面", "idCard.jpg", "01"),

    /**
     * 身份证反面
     */
    ID_CARD_BACK("ID_CARD_BACK", "身份证反面", "idCardBack.jpg", "02"),

    /**
     * 营业执照
     */
    BUSINESS_LICENSE("BUSINESS_LICENSE", "营业执照", "businessLicense.jpg", "03"),

    /**
     * 开户许可证
     */
    ACCT_OPEN_LICENSE("ACCT_OPEN_LICENSE", "开户许可证", "acctOpenLicense.jpg", "05")
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
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String photoType;

}
