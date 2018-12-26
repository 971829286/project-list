package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 照片上传
 *
 * @author chenwj
 * @since 2018/7/25
 */
@Setter
@Getter
@ToString
public class UploadPicRequest {

    /**
     * 会员号
     */
    private String memberId;

    /**
     * 身份证正面照片URL
     */
    private String idCardPic;

    /**
     * 身份证反面照片URL
     */
    private String idCardBackPic;

    /**
     * 营业执照照片URL
     */
    private String businessLicensePic;

    /**
     * 开户许可证照片URL
     */
    private String acctOpenLicensePic;

}
