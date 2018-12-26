package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 收单接口请求基本参数
 *
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
@ToString
@Deprecated
public class CreateInstantTradeRequest extends CommonBaseRequest {
    private static final long serialVersionUID = 257578682228129791L;

    /*** 商户网站请求号 ***/
    private String requestNo;

    /*** 交易列表 ***/
    private String tradeList;

    /*** 操作员Id ***/
    private String operatorId;

    /*** 买家ID 没有则传anymous ***/
    private String buyerId;

    /*** 买家ID类型 ***/
    private String buyerIdType;

    /*** 用户在商户平台下单时候的ip地址 ***/
    private String buyerIp;

    /*** 支付方式 ***/
    private String payMethod;

    /*** 是否转收银台标识 ***/
    private String goCashier;

    /*** 是否是WEB ***/
    private String isWebAccess;

    /*** 卖家账户类型 ***/
    private String sellerAccType;

    /*** app支付跳转url ***/
    private String returnUrl;

    /*** 客户端名称 ***/
    private String appName;

    /*** 扩展参数 ***/
    private String extension;
}
