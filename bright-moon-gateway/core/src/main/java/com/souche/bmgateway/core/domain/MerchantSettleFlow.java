package com.souche.bmgateway.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 商户入驻流水
 *
 * @author chenwj
 * @since 2018/07/25
 */
@Setter
@Getter
@ToString
public class MerchantSettleFlow {

    private Integer id;

    private String shopCode;

    private String memberId;

    private String uid;

    private String outTradeNo;

    private String status;

    private String merchantName;

    private String outMerchantId;

    private String mybankOrderNo;

    private String returnMsg;

    private Date gtmCreate;

    private Date gtmModified;

    private String extension;

    private String reqMsgId;

    private String reqInfo;

    private String respInfo;

    private String callBackUrl;

    private String callBackStatus;

}