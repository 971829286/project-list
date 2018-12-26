package com.souche.bmgateway.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author zs.
 *         Created on 18/7/12.
 */
@Getter
@Setter
@ToString
@Deprecated
public class FundoutTradeRequest extends CommonBaseRequest {

    private static final long serialVersionUID = -4990405321139718723L;

    /*** 外部订单号 ***/
    @NotBlank(message = "outTradeNo不能为空")
    private String outTradeNo;

    /*** 会员id ***/
    private String memberId;

    /*** 产品编码 ***/
    private String productCode;

    /*** 储值账户类型 ***/
    private String accountType;

    /*** 储值账户号 ***/
    private String accountNo;

    /*** 绑卡id ***/
    @NotBlank(message = "bankCardId不能为空")
    private String bankCardId;

    /*** 卡号 ***/
    @NotBlank(message = "卡号不能为空")
    private String cardNo;

    /*** 户名 ***/
    private String bankAccountName;

    /*** 扩展信息 ***/
    private String extension;

    /*** 异步通知地址 ***/
    private String notifyUrl;

    /*** 同步返回地址 ***/
    private String returnUrl;

    /*** 请求时间 ***/
    private String requestTime;

    /** 金额 **/
    private String amount;

    /** 分支行编号 **/
    private String bankLineNo;

    /** 对公对私 **/
    private String companyOrPersonal;

    /** 交易凭证号 **/
    private String fundoutOrderNo;

    /** 支付凭证号 **/
    private String paymentOrderNo;

}
