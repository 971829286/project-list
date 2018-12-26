package com.souche.bmgateway.model.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Deprecated
public class TransferAccountRequest extends CommonBaseRequest {

    //商户网站唯一 订单号
    private String outerTradeNo;

    //入款用户 Id
    private String fundinIdentityNo;
    //入款用户 Id 类 型
    private String fundinIdentityType;

    //入款账户类型
    private String fundinAccountType;

    //出款用户 Id
    private String fundoutIdentityNo;

    //出款用户 Id 类 型
    private String fundoutIdentityType;

    //出款账户类型
    private String fundoutAccountType;

    //转账金额
    private String transferAmount;

    //转账类型
    private String transferType;

    //服务器异步通 知页面路径
    private String notifyUrl;

    //扩展参数
    private String extension;
}
