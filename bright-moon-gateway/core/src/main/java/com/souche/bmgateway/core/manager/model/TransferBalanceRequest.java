package com.souche.bmgateway.core.manager.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransferBalanceRequest  extends TradeCommonRequest{

    //商户网站唯一 订单号
    private String outer_trade_no;

    //入款用户 Id
    private String fundin_identity_no;
    //入款用户 Id 类 型
    private String fundin_identity_type;

    //入款账户类型
    private String fundin_account_type;

    //出款用户 Id
    private String fundout_identity_no;

    //出款用户 Id 类 型
    private String fundout_identity_type;

    //出款账户类型
    private String fundout_account_type;

    //转账金额
    private String transfer_amount;

    //转账类型
    private String transfer_type;

    //服务器异步通 知页面路径
    private String notify_url;

    //扩展参数
    private String extension;



}
