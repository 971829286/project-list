package com.souche.bmgateway.model.response.trade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zs.
 *         Created on 18/11/16.
 */
@Getter
@Setter
@ToString
public class PayChannelResult implements Serializable {

    /*** 支付渠道 ***/
    private String payChannel;

    /*** 支付模式 ***/
    private String payMode;

    /***  交机构订单号 ***/
    private String instPayNo;

    /*** 扩展参数 ***/
    private String extension;

    /*** 支付完成时间 ***/
    private Date gmtPay;

    /*** 渠道扩展参数 ***/
    private String fundsExtension;

}

