package com.souche.bmgateway.core.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class InstantTradeRequest extends TradeCommonRequest implements Serializable {

    //商户网站请求号
    private String request_no;
    //交易列表
    private String trade_list;
    //操作员Id
    private String operator_id;
    //买家ID
    private String buyer_id;
    //买家ID类型
    private String buyer_id_type;
    //用户在商户平台下单时候的ip地址
    private String buyer_ip;
    //支付方式
    private String pay_method;
    //是否转收银台标识
    private String go_cashier;
    //是否是WEB
    private String is_web_access;
    //卖家账户类型
    private String seller_acc_type;
}
