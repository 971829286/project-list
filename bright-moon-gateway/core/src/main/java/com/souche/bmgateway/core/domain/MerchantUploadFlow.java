package com.souche.bmgateway.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 照片上传
 *
 * @author chenwj
 * @since 2018/7/25
 */
@Setter
@Getter
@ToString(exclude = "picture")
public class MerchantUploadFlow {

    private Integer id;

    private String outTradeNo;

    private String photoType;

    private String merchantId;

    private String respInfo;

    private String photoUrl;

    private String status;

    private Date gtmCreate;

    private Date gtmModified;

    private String extension;

    private String reqMsgId;

    private String picture;

}